package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.User;
import utilities.DatabaseConnection;
import utilities.LogManager;
import java.util.ArrayList;
import java.util.List;
import utilities.Security;
import java.sql.DriverManager;
import java.sql.Statement;
import exceptions.DatabaseException;
import java.util.UUID;

/**
 * Implémentation MySQL du DAO pour la gestion des utilisateurs.
 * Cette classe gère toutes les opérations CRUD sur la table des utilisateurs.
 * Elle utilise une connexion à la base de données MySQL et gère proprement les ressources.
 *
 * @author Benja2
 * @version 1.0
 * @since 1.0
 */
public class UserMySqlDAO implements IDAO<User> {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cliprocodb";
    private static final String USER = "root";
    private static final String PASS = "";
    private final Connection connection;

    /**
     * Constructeur par défaut pour l'utilisation normale.
     */
    public UserMySqlDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Récupère une connexion à la base de données.
     * Cette méthode gère les erreurs de connexion et les transforme en DatabaseException.
     *
     * @return une connexion à la base de données
     * @throws DatabaseException si une erreur survient lors de la connexion
     */
    private Connection getConnection() throws DatabaseException {
        if (connection != null) {
            return connection;
        }
        try {
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            LogManager.logException("Erreur lors de la connexion à la base de données", e);
            throw new DatabaseException("Erreur de connexion à la base de données", e);
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
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setToken(rs.getString("token"));
        user.setExpire(rs.getDate("expire"));
        return user;
    }

    /**
     * Recherche un utilisateur par son nom d'utilisateur.
     *
     * @param username le nom d'utilisateur à rechercher
     * @return l'utilisateur trouvé ou null si non trouvé
     * @throws DatabaseException si une erreur survient lors de l'accès à la base de données
     */
    @Override
    public User findByUsername(String username) throws DatabaseException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            LogManager.logException("Erreur lors de la recherche de l'utilisateur", e);
            throw new DatabaseException("Erreur lors de la recherche de l'utilisateur", e);
        }
        return null;
    }

    /**
     * Recherche un utilisateur par son token.
     *
     * @param token le token à rechercher
     * @return l'utilisateur trouvé ou null si non trouvé
     * @throws DatabaseException si une erreur survient lors de l'accès à la base de données
     */
    @Override
    public User findByToken(String token) throws DatabaseException {
        String sql = "SELECT * FROM users WHERE token = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            LogManager.logException("Erreur lors de la recherche de l'utilisateur", e);
            throw new DatabaseException("Erreur lors de la recherche de l'utilisateur", e);
        }
        return null;
    }

    /**
     * Sauvegarde un nouvel utilisateur dans la base de données.
     *
     * @param user l'utilisateur à sauvegarder
     * @throws DatabaseException si une erreur survient lors de l'accès à la base de données
     */
    @Override
    public void save(User user) throws DatabaseException {
        if (user == null) {
            throw new DatabaseException("L'utilisateur ne peut pas être null");
        }

        String sql = "INSERT INTO users (username, password, email, token, expire) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, Security.hashPassword(user.getPassword()));
            stmt.setString(3, user.getEmail());
            stmt.setString(4, UUID.randomUUID().toString());
            stmt.setDate(5, new java.sql.Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            LogManager.logException("Erreur lors de la sauvegarde de l'utilisateur", e);
            throw new DatabaseException("Erreur lors de la sauvegarde de l'utilisateur", e);
        }
    }

    /**
     * Met à jour un utilisateur existant dans la base de données.
     *
     * @param user l'utilisateur à mettre à jour
     * @throws DatabaseException si une erreur survient lors de l'accès à la base de données
     */
    @Override
    public void update(User user) throws DatabaseException {
        if (user == null || user.getId() == null) {
            throw new DatabaseException("L'utilisateur et son ID ne peuvent pas être null");
        }

        String sql = "UPDATE users SET username = ?, password = ?, email = ?, token = ?, expire = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, Security.hashPassword(user.getPassword()));
            stmt.setString(3, user.getEmail());
            stmt.setString(4, UUID.randomUUID().toString());
            stmt.setDate(5, new java.sql.Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
            stmt.setLong(6, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LogManager.logException("Erreur lors de la mise à jour de l'utilisateur", e);
            throw new DatabaseException("Erreur lors de la mise à jour de l'utilisateur", e);
        }
    }

    /**
     * Supprime un utilisateur de la base de données.
     *
     * @param user l'utilisateur à supprimer
     * @throws DatabaseException si une erreur survient lors de l'accès à la base de données
     */
    @Override
    public void delete(User user) throws DatabaseException {
        if (user == null || user.getId() == null) {
            throw new DatabaseException("L'utilisateur et son ID ne peuvent pas être null");
        }

        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LogManager.logException("Erreur lors de la suppression de l'utilisateur", e);
            throw new DatabaseException("Erreur lors de la suppression de l'utilisateur", e);
        }
    }

    /**
     * Récupère tous les utilisateurs de la base de données.
     *
     * @return une liste de tous les utilisateurs
     * @throws DatabaseException si une erreur survient lors de l'accès à la base de données
     */
    @Override
    public List<User> findAll() throws DatabaseException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            LogManager.logException("Erreur lors de la récupération des utilisateurs", e);
            throw new DatabaseException("Erreur lors de la récupération des utilisateurs", e);
        }
        return users;
    }

    @Override
    public User findById(Long id) throws DatabaseException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            return null;
        } catch (SQLException e) {
            LogManager.logException("Erreur lors de la recherche de l'utilisateur par ID", e);
            throw new DatabaseException("Erreur lors de la recherche de l'utilisateur par ID", e);
        }
    }

    @Override
    public User save(User user) throws DatabaseException {
        String sql = "INSERT INTO users (email, password) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.executeUpdate();
            
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getLong(1));
            }
            return user;
        } catch (SQLException e) {
            LogManager.logException("Erreur lors de la sauvegarde de l'utilisateur", e);
            throw new DatabaseException("Erreur lors de la sauvegarde de l'utilisateur", e);
        }
    }

    @Override
    public User update(User user) throws DatabaseException {
        String sql = "UPDATE users SET email = ?, password = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.setLong(3, user.getId());
            stmt.executeUpdate();
            return user;
        } catch (SQLException e) {
            LogManager.logException("Erreur lors de la mise à jour de l'utilisateur", e);
            throw new DatabaseException("Erreur lors de la mise à jour de l'utilisateur", e);
        }
    }

    @Override
    public void delete(User user) throws DatabaseException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LogManager.logException("Erreur lors de la suppression de l'utilisateur", e);
            throw new DatabaseException("Erreur lors de la suppression de l'utilisateur", e);
        }
    }

    public User findByEmail(String email) throws DatabaseException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            return null;
        } catch (SQLException e) {
            LogManager.logException("Erreur lors de la recherche de l'utilisateur par email", e);
            throw new DatabaseException("Erreur lors de la recherche de l'utilisateur par email", e);
        }
    }
} 