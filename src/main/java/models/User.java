package models;

import java.io.Serializable;
import java.time.LocalDate;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String email;
    private String password;
    private String role;
    private String token;
    private LocalDate expire;

    public User() {
    }

    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDate getExpire() {
        return expire;
    }

    public void setExpire(LocalDate expire) {
        this.expire = expire;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
