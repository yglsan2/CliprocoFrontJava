package dao;

import exceptions.DatabaseException;
import models.User;
import utilities.DatabaseConnection;
import utilities.LogManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Interface pour l'accès aux données des utilisateurs.
 * 
 * @author Benja2
 * @version 1.0
 * @since 1.0
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
    Optional<User> findById(Long id) throws DatabaseException;

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
     * @return le utilisateur sauvegardé
     * @throws DatabaseException si une erreur survient lors de l'accès à la base de données
     */
    User save(User user) throws DatabaseException;

    /**
     * Update an existing user.
     *
     * @param user User to update
     * @return the updated user
     * @throws DatabaseException if database error occurs
     */
    User update(User user) throws DatabaseException;

    /**
     * Delete a user.
     *
     * @param id ID of the user to delete
     * @throws DatabaseException if database error occurs
     */
    void delete(Long id) throws DatabaseException;

    /**
     * Close the DAO.
     */
    void close();

    /**
     * Recherche un utilisateur par son nom d'utilisateur.
     *
     * @param username le nom d'utilisateur à rechercher
     * @return User l'utilisateur trouvé, ou null si non trouvé
     * @throws DatabaseException if database error occurs
     */
    User findByUsername(String username) throws DatabaseException;

    /**
     * Recherche un utilisateur par son token.
     *
     * @param token le token à rechercher
     * @return User l'utilisateur trouvé, ou null si non trouvé
     * @throws DatabaseException if database error occurs
     */
    User findByToken(String token) throws DatabaseException;
} 