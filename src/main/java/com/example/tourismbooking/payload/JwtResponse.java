package com.example.tourismbooking.payload;
import com.example.tourismbooking.entity.Role;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String email;
    private Long id;
    private Role role;

    public JwtResponse(String token, Long id, String email, Role role) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Role getRole(){
        return role;
    }
    public void setRole(Role role){
        this.role = role;
    }
}