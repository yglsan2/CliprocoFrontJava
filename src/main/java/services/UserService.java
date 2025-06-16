package services;

import dao.IDAO;
import models.User;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des utilisateurs.
 */
public class UserService implements IDAO<User, Long> {
    private final IDAO<User, Long> userDAO;

    public UserService(IDAO<User, Long> userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userDAO.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Override
    public User save(User entity) {
        return userDAO.save(entity);
    }

    @Override
    public User update(User entity) {
        return userDAO.update(entity);
    }

    @Override
    public void delete(User entity) {
        userDAO.delete(entity);
    }

    public Optional<User> findByUsername(String username) {
        return findAll().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }
} 