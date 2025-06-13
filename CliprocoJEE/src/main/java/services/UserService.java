package services;

import dao.jpa.UserJpaDAO;
import models.User;
import utilities.Security;
import java.util.List;

public class UserService {
    private final UserJpaDAO userDAO;

    public UserService() {
        this.userDAO = new UserJpaDAO();
    }

    public User authenticate(String username, String password) throws Exception {
        List<User> users = userDAO.findAll();
        for (User user : users) {
            if (user.getUsername().equals(username) && 
                Security.verifyPassword(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }
} 