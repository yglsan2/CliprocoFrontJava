package dao;

import exceptions.DatabaseException;
import models.User;
import java.util.List;

/**
 * Interface for User data access operations.
 */
public interface UserDAO {
    /**
     * Recherche un utilisateur par son email.
     *
     * @param email l'email de l'utilisateur
     * @return l'utilisateur trouvé, ou null si aucun utilisateur n'est trouvé
     * @throws DatabaseException si une erreur survient lors de l'accès à la base de données
     */
    User findByEmail(String email) throws DatabaseException;

    /**
     * Recherche un utilisateur par son ID.
     *
     * @param id l'ID de l'utilisateur
     * @return l'utilisateur trouvé, ou null si aucun utilisateur n'est trouvé
     * @throws DatabaseException si une erreur survient lors de l'accès à la base de données
     */
    User findById(Long id) throws DatabaseException;

    /**
     * Récupère tous les utilisateurs de la base de données.
     *
     * @return une liste de tous les utilisateurs
     * @throws DatabaseException si une erreur survient lors de l'accès à la base de données
     */
    List<User> findAll() throws DatabaseException;

    /**
     * Sauvegarde un utilisateur dans la base de données.
     *
     * @param user l'utilisateur à sauvegarder
     * @throws DatabaseException si une erreur survient lors de l'accès à la base de données
     */
    void save(User user) throws DatabaseException;

    /**
     * Met à jour un utilisateur existant.
     *
     * @param user l'utilisateur à mettre à jour
     * @throws DatabaseException si une erreur survient lors de l'accès à la base de données
     */
    void update(User user) throws DatabaseException;

    /**
     * Supprime un utilisateur.
     *
     * @param user l'utilisateur à supprimer
     * @throws DatabaseException si une erreur survient lors de l'accès à la base de données
     */
    void delete(User user) throws DatabaseException;

    /**
     * Recherche un utilisateur par son nom d'utilisateur.
     *
     * @param username le nom d'utilisateur à rechercher
     * @return l'utilisateur trouvé, ou null si non trouvé
     * @throws DatabaseException si une erreur survient lors de l'accès à la base de données
     */
    User findByUsername(String username) throws DatabaseException;

    /**
     * Recherche un utilisateur par son token.
     *
     * @param token le token à rechercher
     * @return l'utilisateur trouvé, ou null si non trouvé
     * @throws DatabaseException si une erreur survient lors de l'accès à la base de données
     */
    User findByToken(String token) throws DatabaseException;
} 