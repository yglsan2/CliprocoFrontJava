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
 * 
 * <p>Cette classe fournit une couche de service pour gérer les opérations
 * métier liées aux utilisateurs. Elle encapsule la logique de validation,
 * la gestion des erreurs et le logging des opérations.</p>
 * 
 * <p>Le service utilise un DAO générique pour accéder aux données et
 * ajoute une couche de validation et de gestion d'erreurs avant de
 * déléguer les opérations au DAO.</p>
 * 
 * @author CliprocoJEE
 * @version 1.0
 * @since 1.0
 */
public class UserService {
    /**
     * DAO pour l'accès aux données des utilisateurs.
     * Utilise l'interface générique IDAO pour permettre différentes implémentations.
     */
    private final IDAO<User, Integer> userDAO;

    /**
     * Constructeur du service utilisateur.
     * 
     * <p>Initialise le service avec le DAO fourni et enregistre
     * l'initialisation dans les logs.</p>
     * 
     * @param userDAO Le DAO à utiliser pour l'accès aux données des utilisateurs
     */
    public UserService(IDAO<User, Integer> userDAO) {
        this.userDAO = userDAO;
        LogManager.logInfo("Initialisation du UserService avec le DAO fourni");
    }

    /**
     * Recherche un utilisateur par son identifiant unique.
     * 
     * <p>Cette méthode valide l'identifiant fourni avant de déléguer
     * la recherche au DAO. Elle gère les erreurs de validation et
     * de base de données avec un logging approprié.</p>
     * 
     * @param id L'identifiant unique de l'utilisateur à rechercher
     * @return Un Optional contenant l'utilisateur s'il est trouvé
     * @throws DatabaseException Si une erreur survient lors de l'accès à la base de données
     * @throws ValidationException Si l'identifiant fourni est invalide (null)
     */
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

    /**
     * Récupère tous les utilisateurs du système.
     * 
     * <p>Cette méthode récupère la liste complète des utilisateurs
     * enregistrés. Elle gère les erreurs de base de données avec
     * un logging approprié.</p>
     * 
     * @return La liste de tous les utilisateurs
     * @throws DatabaseException Si une erreur survient lors de l'accès à la base de données
     */
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

    /**
     * Sauvegarde un nouvel utilisateur.
     * 
     * <p>Cette méthode valide l'utilisateur fourni avant de le sauvegarder.
     * Elle vérifie que l'utilisateur n'est pas null et gère les erreurs
     * de validation et de base de données.</p>
     * 
     * @param user L'utilisateur à sauvegarder
     * @return L'utilisateur sauvegardé avec son identifiant généré
     * @throws DatabaseException Si une erreur survient lors de l'accès à la base de données
     * @throws ValidationException Si l'utilisateur fourni est invalide (null)
     */
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

    /**
     * Met à jour un utilisateur existant.
     * 
     * <p>Cette méthode valide l'utilisateur et vérifie son existence
     * avant de procéder à la mise à jour. Elle gère les erreurs de
     * validation, de ressource non trouvée et de base de données.</p>
     * 
     * @param user L'utilisateur à mettre à jour
     * @return L'utilisateur mis à jour
     * @throws DatabaseException Si une erreur survient lors de l'accès à la base de données
     * @throws ValidationException Si l'utilisateur fourni est invalide
     * @throws ResourceNotFoundException Si l'utilisateur n'existe pas
     */
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

    /**
     * Supprime un utilisateur du système.
     * 
     * <p>Cette méthode valide l'utilisateur et vérifie son existence
     * avant de procéder à la suppression. Elle gère les erreurs de
     * validation, de ressource non trouvée et de base de données.</p>
     * 
     * @param user L'utilisateur à supprimer
     * @throws DatabaseException Si une erreur survient lors de l'accès à la base de données
     * @throws ValidationException Si l'utilisateur fourni est invalide
     * @throws ResourceNotFoundException Si l'utilisateur n'existe pas
     */
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

    /**
     * Recherche un utilisateur par son nom d'utilisateur.
     * 
     * <p>Cette méthode recherche un utilisateur en filtrant la liste
     * complète des utilisateurs par nom d'utilisateur. Elle valide
     * le nom d'utilisateur fourni avant la recherche.</p>
     * 
     * @param username Le nom d'utilisateur à rechercher
     * @return Un Optional contenant l'utilisateur s'il est trouvé
     * @throws DatabaseException Si une erreur survient lors de l'accès à la base de données
     * @throws ValidationException Si le nom d'utilisateur fourni est invalide (null ou vide)
     */
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