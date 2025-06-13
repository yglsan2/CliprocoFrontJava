package services;

import exceptions.ApplicationException;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import logs.LogManager;
import models.User;
import dao.UserDAO;
import dao.UserMySqlDAO;
import utilities.Security;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Service gérant les opérations liées aux utilisateurs.
 * Cette classe fournit les fonctionnalités d'authentification et de gestion des utilisateurs.
 */
public class UserService {
    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserMySqlDAO();
    }

    /**
     * Authentifie un utilisateur avec son nom d'utilisateur et son mot de passe.
     *
     * @param username le nom d'utilisateur
     * @param password le mot de passe
     * @return l'utilisateur authentifié, ou null si l'authentification échoue
     * @throws ApplicationException si une erreur survient lors de l'authentification
     */
    public User authenticate(String username, String password) throws ApplicationException {
        try {
            if (username == null || password == null) {
                throw new ValidationException("Nom d'utilisateur et mot de passe requis");
            }

            User user = userDAO.findByUsername(username);
            if (user != null && Security.verifyPassword(password, user.getPassword())) {
                // Génération d'un nouveau token
                String token = UUID.randomUUID().toString();
                user.setToken(token);
                user.setExpire(LocalDate.now().plusDays(7));
                userDAO.update(user);
                
                LogManager.info("Authentification réussie pour l'utilisateur: " + username);
                return user;
            }
            
            LogManager.warning("Tentative d'authentification échouée pour l'utilisateur: " + username);
            return null;
        } catch (DatabaseException e) {
            LogManager.logException("Erreur de base de données lors de l'authentification", e);
            throw new ApplicationException("Erreur lors de l'authentification", e);
        } catch (Exception e) {
            LogManager.logException("Erreur inattendue lors de l'authentification", e);
            throw new ApplicationException("Erreur lors de l'authentification", e);
        }
    }

    /**
     * Vérifie si un token est valide.
     *
     * @param token le token à vérifier
     * @return l'utilisateur associé au token, ou null si le token est invalide
     * @throws ApplicationException si une erreur survient lors de la vérification
     */
    public User validateToken(String token) throws ApplicationException {
        try {
            if (token == null) {
                throw new ValidationException("Token requis");
            }

            User user = userDAO.findByToken(token);
            if (user != null && user.getExpire().isAfter(LocalDate.now())) {
                LogManager.info("Token valide pour l'utilisateur: " + user.getUsername());
                return user;
            }
            
            LogManager.warning("Token invalide ou expiré");
            return null;
        } catch (DatabaseException e) {
            LogManager.logException("Erreur de base de données lors de la validation du token", e);
            throw new ApplicationException("Erreur lors de la validation du token", e);
        } catch (Exception e) {
            LogManager.logException("Erreur inattendue lors de la validation du token", e);
            throw new ApplicationException("Erreur lors de la validation du token", e);
        }
    }
} 