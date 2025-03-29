package com.cliproco.dao;

import com.cliproco.model.User;
import java.util.Optional;

public interface UtilisateurDao {
    void save(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findByRememberToken(String token);
    void updateLastLogin(User user);
    void updateRememberToken(User user, String token, java.time.LocalDateTime expiry);
} 