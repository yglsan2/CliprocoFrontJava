package services;

import dao.IDAO;
import dao.UserDAO;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import models.User;
import utilities.LogManager;
import utilities.Security;

import java.util.List;
import java.util.Optional;

/**
 * Service gérant les opérations liées aux utilisateurs.
 * Cette classe fournit les fonctionnalités d'authentification et de gestion des utilisateurs.
 * 
 * @author Benja2
 * @version 1.0
 * @since 1.0
 */
public class UserService {
    private final IDAO<User> userDAO;

    /**
     * Constructeur par défaut.
     * Initialise le DAO pour la gestion des utilisateurs.
     */
    public UserService() {
        this.userDAO = new UserDAO();
    }

    public User findByEmail(String email) throws DatabaseException {
        return ((UserDAO) userDAO).findByEmail(email);
    }

    public User createUser(User user) throws ValidationException, DatabaseException {
        try {
            validateUser(user);
            user.setPassword(Security.hashPassword(user.getPassword()));
            LogManager.info("Création d'un nouvel utilisateur : " + user.getEmail());
            return userDAO.save(user);
        } catch (ValidationException e) {
            LogManager.warning("Erreur de validation lors de la création de l'utilisateur : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la création de l'utilisateur", e);
            throw new DatabaseException("Erreur lors de la création de l'utilisateur", e);
        }
    }

    public User updateUser(User user) throws ValidationException, DatabaseException {
        try {
            validateUser(user);
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                user.setPassword(Security.hashPassword(user.getPassword()));
            }
            LogManager.info("Mise à jour de l'utilisateur : " + user.getEmail());
            return userDAO.update(user);
        } catch (ValidationException e) {
            LogManager.warning("Erreur de validation lors de la mise à jour de l'utilisateur : " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la mise à jour de l'utilisateur", e);
            throw new DatabaseException("Erreur lors de la mise à jour de l'utilisateur", e);
        }
    }

    public void deleteUser(Long id) throws DatabaseException {
        try {
            LogManager.info("Suppression de l'utilisateur avec l'ID : " + id);
            userDAO.delete(id);
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la suppression de l'utilisateur", e);
            throw new DatabaseException("Erreur lors de la suppression de l'utilisateur", e);
        }
    }

    public List<User> findAll() throws DatabaseException {
        try {
            LogManager.info("Récupération de tous les utilisateurs");
            return userDAO.findAll();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la récupération des utilisateurs", e);
            throw new DatabaseException("Erreur lors de la récupération des utilisateurs", e);
        }
    }

    public Optional<User> findById(Long id) throws DatabaseException {
        try {
            LogManager.info("Recherche de l'utilisateur avec l'ID : " + id);
            return userDAO.findById(id);
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche de l'utilisateur par ID", e);
            throw new DatabaseException("Erreur lors de la recherche de l'utilisateur", e);
        }
    }

    private void validateUser(User user) throws ValidationException {
        if (user == null) {
            throw new ValidationException("L'utilisateur ne peut pas être null");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new ValidationException("L'email est obligatoire");
        }
        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("Format d'email invalide");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new ValidationException("Le mot de passe est obligatoire");
        }
        if (user.getPassword().length() < 8) {
            throw new ValidationException("Le mot de passe doit contenir au moins 8 caractères");
        }
    }

    /**
     * Ferme les ressources du service.
     * Cette méthode doit être appelée lorsque le service n'est plus utilisé.
     */
    public void close() {
        try {
            userDAO.close();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la fermeture du DAO utilisateur", e);
        }
    }
} 