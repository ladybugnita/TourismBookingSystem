package com.example.tourismbooking.config;

import com.example.tourismbooking.security.JwtAuthenticationFilter;
import com.example.tourismbooking.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("SecurityConfig loaded...");

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/admin/packages").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/admin/packages/{id}").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/user/bookings").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/user/bookings").hasRole("USER")
                        .requestMatchers(HttpMethod.GET,"/api/user/bookings/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/user/bookings/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/user/bookings/{id}").hasRole("USER")

                                .requestMatchers(HttpMethod.POST, "/api/admin/packages").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/admin/packages/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/api/admin/packages/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/admin/bookings").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/admin/bookings/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET,"/api/admin/users").hasRole("ADMIN")

                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/user/**").hasRole("USER")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
