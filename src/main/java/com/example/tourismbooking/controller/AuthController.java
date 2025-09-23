package com.example.tourismbooking.controller;

import com.example.tourismbooking.entity.Role;
import com.example.tourismbooking.entity.User;
import com.example.tourismbooking.payload.LoginRequest;
import com.example.tourismbooking.payload.SignupRequest;
import com.example.tourismbooking.payload.JwtResponse;
import com.example.tourismbooking.repository.InvalidatedTokenRepository;
import com.example.tourismbooking.repository.UserRepository;
import com.example.tourismbooking.security.JwtUtil;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.tourismbooking.entity.InvalidatedToken;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController{
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private InvalidatedTokenRepository invalidatedTokenRepository;
    @Value("${app.admin.secret:MY_SECURE_ADMIN_KEY}")
    private String adminSecretKey;

    private final List<String> allowedAdminEmails = List.of(
            "admin1@gmail.com",
            "admin2@gmail.com",
            "admin3@gmail.com"
    );

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            List<String> errors =bindingResult.getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        if(userRepository.existsByEmail(signupRequest.getEmail())){
            return ResponseEntity
                    .badRequest()
                    .body("Error: Email is already in use");
        }

        Role assignedRole = Role.USER;

        log.info("Signup request - Email: {}, isAdmin: {}, AdminSecret provided: {}",
                signupRequest.getEmail(),
                signupRequest.isAdmin(),
                signupRequest.getAdminSecret() != null ? "YES" : "NO");

        if(signupRequest.isAdmin() != null && signupRequest.isAdmin()){
            log.info("Checking admin credentials - Expected Secret: {}, Provided Secret: {}, Allowed Email: {}",
                    adminSecretKey, signupRequest.getAdminSecret(), allowedAdminEmails.contains(signupRequest.getEmail()));

            if(adminSecretKey.equals(signupRequest.getAdminSecret()) &&
                    allowedAdminEmails.contains(signupRequest.getEmail())) {
                assignedRole = Role.ADMIN;
                log.info("Admin registration approved for email: {}", signupRequest.getEmail());
            } else {
                log.warn("Admin registration rejected for email: {}", signupRequest.getEmail());
                return ResponseEntity.status(403).body("Error: Not allowed to register as admin");
            }
        }

        User user = new User(signupRequest.getUsername(),
                passwordEncoder.encode(signupRequest.getPassword()),signupRequest.getEmail(), assignedRole);
        userRepository.save(user);

        log.info("User registered successfully with role: {}", assignedRole);
        return ResponseEntity.ok("User registered successfully with role: " + assignedRole);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails);
            User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
            return ResponseEntity.ok(new JwtResponse(jwt,user.getId(), user.getEmail(), user.getRole()));
        }catch(BadCredentialsException e){
            return ResponseEntity.status(401).body("Error:Invalid email or password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            InvalidatedToken invalidated = new InvalidatedToken();
            invalidated.setToken(token);
            invalidatedTokenRepository.save(invalidated);
            return ResponseEntity.ok("User logged out successfully");
        }
        return ResponseEntity.badRequest().body("No token found");
    }
}