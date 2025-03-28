package com.cliproco.dao;

import com.cliproco.model.User;
import java.util.List;

public interface UtilisateurDao {
    void save(User user);
    void update(User user);
    void delete(User user);
    User findById(Long id);
    List<User> findAll();
    User findByUsername(String username);
    void updateLastLogin(Long id);
    User findByRememberToken(String token);
} 