package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.User;
import utilities.DatabaseConnection;
import exceptions.DatabaseException;
import logs.LogManager;
import java.util.ArrayList;
import java.util.List;
import utilities.Security;

/**
 * Implémentation MySQL du DAO pour la gestion des utilisateurs.
 * Cette classe gère toutes les opérations CRUD sur la table des utilisateurs.
 * Elle utilise une connexion à la base de données MySQL et gère proprement les ressources.
 *
 * @author Benja2
 * @version 1.0
 * @since 1.0
 */
public class UserMySqlDAO implements UserDAO {
    // Constantes pour les requêtes SQL
    private static final String FIND_BY_USERNAME = "SELECT * FROM users WHERE username = ?";
    private static final String FIND_BY_TOKEN = "SELECT * FROM users WHERE token = ?";
    private static final String INSERT_USER = "INSERT INTO users (username, password, token, expire) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_USER = "UPDATE users SET username = ?, password = ?, token = ?, expire = ? WHERE id = ?";
    private static final String DELETE_USER = "DELETE FROM users WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM users";

    /**
     * Récupère une connexion à la base de données.
     * Cette méthode gère les erreurs de connexion et les transforme en DatabaseException.
     *
     * @return une connexion à la base de données
     * @throws DatabaseException si une erreur survient lors de la connexion
     */
    private Connection getConnection() throws DatabaseException {
        try {
            return DatabaseConnection.getInstance().getConnection();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la connexion à la base de données", e);
            throw new DatabaseException("Erreur lors de la connexion à la base de données", e);
        }
    }

    /**
     * Convertit un ResultSet en objet User.
     * Cette méthode extrait les données d'une ligne de la base de données.
     *
     * @param rs le ResultSet contenant les données
     * @return un objet User avec les données extraites
     * @throws SQLException si une erreur survient lors de la lecture des données
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setToken(rs.getString("token"));
        user.setExpire(rs.getDate("expire").toLocalDate());
        return user;
    }

    /**
     * Recherche un utilisateur par son nom d'utilisateur.
     *
     * @param username le nom d'utilisateur à rechercher
     * @return l'utilisateur trouvé ou null si non trouvé
     * @throws DatabaseException si une erreur survient lors de la recherche
     */
    @Override
    public User findByUsername(String username) throws DatabaseException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_USERNAME)) {
            
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            LogManager.logException("Erreur lors de la recherche de l'utilisateur par nom", e);
            throw new DatabaseException("Erreur lors de la recherche de l'utilisateur", e);
        }
    }

    /**
     * Recherche un utilisateur par son token.
     *
     * @param token le token à rechercher
     * @return l'utilisateur trouvé ou null si non trouvé
     * @throws DatabaseException si une erreur survient lors de la recherche
     */
    @Override
    public User findByToken(String token) throws DatabaseException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_TOKEN)) {
            
            stmt.setString(1, token);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            LogManager.logException("Erreur lors de la recherche de l'utilisateur par token", e);
            throw new DatabaseException("Erreur lors de la recherche de l'utilisateur", e);
        }
    }

    /**
     * Sauvegarde un nouvel utilisateur dans la base de données.
     *
     * @param user l'utilisateur à sauvegarder
     * @throws DatabaseException si une erreur survient lors de la sauvegarde
     */
    @Override
    public void save(User user) throws DatabaseException {
        if (user == null) {
            throw new DatabaseException("L'utilisateur ne peut pas être null");
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_USER)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, Security.hashPassword(user.getPassword()));
            stmt.setString(3, user.getToken());
            stmt.setDate(4, java.sql.Date.valueOf(user.getExpire()));
            
            stmt.executeUpdate();
            LogManager.info("Nouvel utilisateur sauvegardé: " + user.getUsername());
        } catch (SQLException e) {
            LogManager.logException("Erreur lors de la sauvegarde de l'utilisateur", e);
            throw new DatabaseException("Erreur lors de la sauvegarde de l'utilisateur", e);
        }
    }

    /**
     * Met à jour un utilisateur existant dans la base de données.
     *
     * @param user l'utilisateur à mettre à jour
     * @throws DatabaseException si une erreur survient lors de la mise à jour
     */
    @Override
    public void update(User user) throws DatabaseException {
        if (user == null || user.getId() == null) {
            throw new DatabaseException("L'utilisateur et son ID ne peuvent pas être null");
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_USER)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, Security.hashPassword(user.getPassword()));
            stmt.setString(3, user.getToken());
            stmt.setDate(4, java.sql.Date.valueOf(user.getExpire()));
            stmt.setLong(5, user.getId());
            
            stmt.executeUpdate();
            LogManager.info("Utilisateur mis à jour: " + user.getUsername());
        } catch (SQLException e) {
            LogManager.logException("Erreur lors de la mise à jour de l'utilisateur", e);
            throw new DatabaseException("Erreur lors de la mise à jour de l'utilisateur", e);
        }
    }

    /**
     * Supprime un utilisateur de la base de données.
     *
     * @param user l'utilisateur à supprimer
     * @throws DatabaseException si une erreur survient lors de la suppression
     */
    @Override
    public void delete(User user) throws DatabaseException {
        if (user == null || user.getId() == null) {
            throw new DatabaseException("L'utilisateur et son ID ne peuvent pas être null");
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_USER)) {
            
            stmt.setLong(1, user.getId());
            stmt.executeUpdate();
            LogManager.info("Utilisateur supprimé: " + user.getUsername());
        } catch (SQLException e) {
            LogManager.logException("Erreur lors de la suppression de l'utilisateur", e);
            throw new DatabaseException("Erreur lors de la suppression de l'utilisateur", e);
        }
    }

    /**
     * Récupère tous les utilisateurs de la base de données.
     *
     * @return une liste de tous les utilisateurs
     * @throws DatabaseException si une erreur survient lors de la récupération
     */
    @Override
    public List<User> findAll() throws DatabaseException {
        List<User> users = new ArrayList<>();
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_ALL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            LogManager.info("Récupération de " + users.size() + " utilisateurs");

            return users;
        } catch (SQLException e) {
            LogManager.logException("Erreur lors de la récupération de tous les utilisateurs", e);
            throw new DatabaseException("Erreur lors de la récupération des utilisateurs", e);
        }
    }
} 