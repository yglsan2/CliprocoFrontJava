package services;

import dao.IDAO;
import models.User;
import utilities.LogManager;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des utilisateurs.
 */
public class UserService {
    private final IDAO<User, Integer> userDAO;

    public UserService(IDAO<User, Integer> userDAO) {
        this.userDAO = userDAO;
        LogManager.logInfo("Initialisation du UserService avec le DAO fourni");
    }

    public Optional<User> findById(Integer id) throws DatabaseException, ValidationException {
        LogManager.logInfo("Recherche de l'utilisateur avec l'ID: " + id);
        try {
            if (id == null) {
                LogManager.logWarning("Tentative de recherche avec un ID null");
                throw new ValidationException("L'ID ne peut pas être null");
            }
            Optional<User> result = userDAO.findById(id);
            LogManager.logInfo("Résultat de la recherche: " + (result.isPresent() ? "Utilisateur trouvé" : "Utilisateur non trouvé"));
            return result;
        } catch (ValidationException e) {
            LogManager.logWarning("Erreur de validation lors de la recherche de l'utilisateur: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche de l'utilisateur", e);
            throw new DatabaseException("Erreur lors de la recherche de l'utilisateur", e);
        }
    }

    public List<User> findAll() throws DatabaseException {
        LogManager.logInfo("Récupération de tous les utilisateurs");
        try {
            List<User> users = userDAO.findAll();
            LogManager.logInfo("Nombre d'utilisateurs trouvés: " + users.size());
            return users;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la récupération des utilisateurs", e);
            throw new DatabaseException("Erreur lors de la récupération des utilisateurs", e);
        }
    }

    public User save(User user) throws DatabaseException, ValidationException {
        LogManager.logInfo("Sauvegarde de l'utilisateur: " + user);
        try {
            if (user == null) {
                LogManager.logWarning("Tentative de sauvegarde d'un utilisateur null");
                throw new ValidationException("L'utilisateur ne peut pas être null");
            }
            User savedUser = userDAO.save(user);
            LogManager.logInfo("Utilisateur sauvegardé avec succès: " + savedUser.getId());
            return savedUser;
        } catch (ValidationException e) {
            LogManager.logWarning("Erreur de validation lors de la sauvegarde de l'utilisateur: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la sauvegarde de l'utilisateur", e);
            throw new DatabaseException("Erreur lors de la sauvegarde de l'utilisateur", e);
        }
    }

    public User update(User user) throws DatabaseException, ValidationException, ResourceNotFoundException {
        LogManager.logInfo("Mise à jour de l'utilisateur: " + user);
        try {
            if (user == null) {
                LogManager.logWarning("Tentative de mise à jour d'un utilisateur null");
                throw new ValidationException("L'utilisateur ne peut pas être null");
            }
            if (user.getId() == null) {
                LogManager.logWarning("Tentative de mise à jour d'un utilisateur sans ID");
                throw new ValidationException("L'ID de l'utilisateur ne peut pas être null");
            }
            
            LogManager.logInfo("Recherche de l'utilisateur existant avec l'ID: " + user.getId());
            User existingUser = userDAO.findById(user.getId())
                .orElseThrow(() -> {
                    LogManager.logWarning("Utilisateur non trouvé avec l'ID: " + user.getId());
                    return new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + user.getId());
                });
            
            LogManager.logInfo("Mise à jour de l'utilisateur dans la base de données");
            User updatedUser = userDAO.update(user);
            LogManager.logInfo("Utilisateur mis à jour avec succès: " + updatedUser.getId());
            return updatedUser;
        } catch (ValidationException | ResourceNotFoundException e) {
            LogManager.logWarning("Erreur lors de la mise à jour de l'utilisateur: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la mise à jour de l'utilisateur", e);
            throw new DatabaseException("Erreur lors de la mise à jour de l'utilisateur", e);
        }
    }

    public void delete(User user) throws DatabaseException, ValidationException, ResourceNotFoundException {
        LogManager.logInfo("Suppression de l'utilisateur: " + user);
        try {
            if (user == null) {
                LogManager.logWarning("Tentative de suppression d'un utilisateur null");
                throw new ValidationException("L'utilisateur ne peut pas être null");
            }
            if (user.getId() == null) {
                LogManager.logWarning("Tentative de suppression d'un utilisateur sans ID");
                throw new ValidationException("L'ID de l'utilisateur ne peut pas être null");
            }
            
            LogManager.logInfo("Recherche de l'utilisateur à supprimer");
            User existingUser = userDAO.findById(user.getId())
                .orElseThrow(() -> {
                    LogManager.logWarning("Utilisateur non trouvé avec l'ID: " + user.getId());
                    return new ResourceNotFoundException("Utilisateur non trouvé avec l'ID: " + user.getId());
                });
            
            LogManager.logInfo("Suppression de l'utilisateur de la base de données");
            userDAO.delete(user);
            LogManager.logInfo("Utilisateur supprimé avec succès: " + user.getId());
        } catch (ValidationException | ResourceNotFoundException e) {
            LogManager.logWarning("Erreur lors de la suppression de l'utilisateur: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la suppression de l'utilisateur", e);
            throw new DatabaseException("Erreur lors de la suppression de l'utilisateur", e);
        }
    }

    public Optional<User> findByUsername(String username) throws DatabaseException, ValidationException {
        LogManager.logInfo("Recherche de l'utilisateur avec le nom d'utilisateur: " + username);
        try {
            if (username == null || username.trim().isEmpty()) {
                LogManager.logWarning("Tentative de recherche avec un nom d'utilisateur vide");
                throw new ValidationException("Le nom d'utilisateur ne peut pas être vide");
            }
            Optional<User> result = findAll().stream()
                .filter(user -> username.equals(user.getUsername()))
                .findFirst();
            LogManager.logInfo("Résultat de la recherche par nom d'utilisateur: " + (result.isPresent() ? "Utilisateur trouvé" : "Utilisateur non trouvé"));
            return result;
        } catch (ValidationException e) {
            LogManager.logWarning("Erreur de validation lors de la recherche par nom d'utilisateur: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche par nom d'utilisateur", e);
            throw new DatabaseException("Erreur lors de la recherche par nom d'utilisateur", e);
        }
    }
} 