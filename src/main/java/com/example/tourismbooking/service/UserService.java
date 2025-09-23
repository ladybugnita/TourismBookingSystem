package com.example.tourismbooking.service;

import com.example.tourismbooking.entity.Role;
import com.example.tourismbooking.entity.User;
import com.example.tourismbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id:" + userId));
    }
    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
    public User registerUser(User user) {
        // Hardcoded admin emails
        List<String> adminEmails = List.of("admin1@gmail.com", "admin2@gmail.com", "admin3@gmail.com");

        if(adminEmails.contains(user.getEmail())) {
            user.setRole(Role.ADMIN); // assign ADMIN role
        } else {
            user.setRole(Role.USER);
        }

        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save user
        return userRepository.save(user);
    }
}
