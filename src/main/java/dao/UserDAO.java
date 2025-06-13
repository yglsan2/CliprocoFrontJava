package dao;

import models.User;
import exceptions.DatabaseException;
import java.util.List;

/**
 * Interface pour l'accès aux données des utilisateurs.
 * 
 * @author Benja2
 * @version 1.0
 * @since 1.0
 */
public interface UserDAO {
    /**
     * Recherche un utilisateur par son nom d'utilisateur.
     *
     * @param username le nom d'utilisateur
     * @return l'utilisateur trouvé, ou null si aucun utilisateur n'est trouvé
     * @throws DatabaseException si une erreur survient lors de l'accès à la base de données
     */
    User findByUsername(String username) throws DatabaseException;

    /**
     * Recherche un utilisateur par son token.
     *
     * @param token le token de l'utilisateur
     * @return l'utilisateur trouvé, ou null si aucun utilisateur n'est trouvé
     * @throws DatabaseException si une erreur survient lors de l'accès à la base de données
     */
    User findByToken(String token) throws DatabaseException;

    /**
     * Sauvegarde un utilisateur dans la base de données.
     *
     * @param user l'utilisateur à sauvegarder
     * @throws DatabaseException si une erreur survient lors de l'accès à la base de données
     */
    void save(User user) throws DatabaseException;

    /**
     * Update an existing user.
     *
     * @param user User to update
     * @throws DatabaseException if database error occurs
     */
    void update(User user) throws DatabaseException;

    /**
     * Delete a user.
     *
     * @param user User to delete
     * @throws DatabaseException if database error occurs
     */
    void delete(User user) throws DatabaseException;

    /**
     * Récupère tous les utilisateurs de la base de données.
     *
     * @return une liste de tous les utilisateurs
     * @throws DatabaseException si une erreur survient lors de l'accès à la base de données
     */
    List<User> findAll() throws DatabaseException;
} 