package com.example.tourismbooking.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="invalidated_tokens")
public class InvalidatedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique =true, length =500)
    private String token;
    @Column(name = "invalidated_at", nullable = false)
    private LocalDateTime invalidatedAt ;
    public InvalidatedToken(){
        this.invalidatedAt = LocalDateTime.now();
    }
    public InvalidatedToken(String token){
        this.token = token;
        this.invalidatedAt = LocalDateTime.now();
    }
    public Long getId(){
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getToken(){
    return token;
    }
    public void setToken(String token){
        this.token = token;
    }
    public LocalDateTime getInvalidatedAt(){
        return invalidatedAt;
    }
    public void setInvalidatedAt(LocalDateTime invalidatedAt){
        this.invalidatedAt = invalidatedAt;
    }
    @Override
    public String toString(){
        return "InvalidatedToken{" + "id=" + id +", token='" + token + '\'' + ", invalidatedAt = " + invalidatedAt + '}';
    }
}

