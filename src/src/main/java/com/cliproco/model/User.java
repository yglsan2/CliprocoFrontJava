package com.cliproco.model;

import java.time.LocalDateTime;

public class User {
    private Long id;
    private String username;
    private String password;
    private String role;
    private LocalDateTime lastLogin;
    private String rememberToken;
    private LocalDateTime rememberTokenExpiry;

    public User() {
    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getRememberToken() {
        return rememberToken;
    }

    public void setRememberToken(String rememberToken) {
        this.rememberToken = rememberToken;
    }

    public LocalDateTime getRememberTokenExpiry() {
        return rememberTokenExpiry;
    }

    public void setRememberTokenExpiry(LocalDateTime rememberTokenExpiry) {
        this.rememberTokenExpiry = rememberTokenExpiry;
    }

    public boolean isAdmin() {
        return "admin".equals(role);
    }
} 