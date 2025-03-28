package com.cliproco.dao.impl;

import com.cliproco.dao.UtilisateurDao;
import com.cliproco.model.User;
import java.time.LocalDateTime;
import java.util.List;

public class ArrayListUserDaoImpl extends ArrayListDaoImpl<User> implements UtilisateurDao {
    @Override
    public void save(User user) {
        if (user.getId() == null) {
            user.setId(generateId());
        }
        super.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return findAll().stream()
                .filter(user -> username.equals(user.getUsername()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void updateLastLogin(Long id) {
        User user = findById(id);
        if (user != null) {
            user.setLastLogin(LocalDateTime.now());
            update(user);
        }
    }

    @Override
    public User findByRememberToken(String token) {
        return findAll().stream()
                .filter(user -> token.equals(user.getRememberToken()))
                .findFirst()
                .orElse(null);
    }
} 