package com.example.tourismbooking.entity;

import jakarta.persistence.*;
import java.time.Instant;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    private Instant createdAt = Instant.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role ;

    @OneToMany(mappedBy ="user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<com.example.tourismbooking.entity.Booking> bookings;


    public User(){}
        public User(String username, String password, String email, Role role){
            this.username = username;
            this.password = password;
            this.email = email;
            this.role =role == null ? Role.USER : role;
        }
        public Long getId(){
            return id;
        }
        public void setId(Long id){
            this.id = id;
        }
        public String getUsername(){
            return username;
        }
        public void setUsername(String username){
            this.username = username;
        }
        public String getPassword(){
            return password;
        }
        public void setPassword(String password){
            this.password = password;
        }
        public String getEmail(){
            return email;
        }
        public void setEmail(String email){
            this.email =email;
        }
        public Instant getCreatedAt(){
        return createdAt;
        }
        public void setCreatedAt(Instant createdAt){
        this.createdAt = createdAt;
        }
        public Role getRole(){
        return role;
        }
        public void setRole(Role role){
        this.role = role;
        }

}
