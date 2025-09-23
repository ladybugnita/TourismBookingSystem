package com.example.tourismbooking.payload;

import com.example.tourismbooking.entity.Role;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignupRequest {

    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(unique = true, nullable = false)
    private String email;
    @NotBlank(message = "password is required")
    @Size(min=6,message = "password must be at least 6 characters")
    private String password;
    private Role role;
    private Boolean admin;
    private String adminSecret;


    public String getUsername(){
       return username;
    }
    public void setUsername(String username){
    this.username = username;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public Role getRole(){
        return role;
    }
    public void setRole(Role role){
        this.role = role;
    }
    public Boolean isAdmin(){
        return admin;
    }
    public void setAdmin(Boolean admin){
        this.admin = admin;
    }
    public String getAdminSecret(){
        return adminSecret;
    }
    public void setAdminSecret(String adminSecret){
        this.adminSecret = adminSecret;
    }
}
