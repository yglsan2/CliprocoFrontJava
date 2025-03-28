package com.cliproco.dao;

import com.cliproco.model.User;
import java.util.List;

public class UserDAO extends HibernateDao<User> {
    
    public UserDAO() {
        super(User.class);
    }

    public User findByUsername(String username) {
        return em.createQuery(
            "SELECT u FROM User u WHERE u.username = :username", User.class)
            .setParameter("username", username)
            .getResultList()
            .stream()
            .findFirst()
            .orElse(null);
    }

    public User findByEmail(String email) {
        return em.createQuery(
            "SELECT u FROM User u WHERE u.email = :email", User.class)
            .setParameter("email", email)
            .getResultList()
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public void create(User user) {
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    @Override
    public void update(User user) {
        try {
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }
} 