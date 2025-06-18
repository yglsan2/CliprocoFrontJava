package dao.mysql;

import models.User;
import dao.IDAO;
import exceptions.ValidationException;
import exceptions.DatabaseException;
import java.util.Optional;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Implémentation MySQL du DAO pour les utilisateurs.
 */
public class UserMySqlDAO implements IDAO<User, Integer> {
    
    private Connection connection;

    public UserMySqlDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<User> findById(Integer id) throws ValidationException, DatabaseException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la recherche de l'utilisateur", e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() throws DatabaseException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la récupération des utilisateurs", e);
        }
        return users;
    }

    @Override
    public User save(User user) throws ValidationException, DatabaseException {
        if (user == null) {
            throw new ValidationException("L'utilisateur ne peut pas être null");
        }
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la sauvegarde de l'utilisateur", e);
        }
        return user;
    }

    @Override
    public User update(User user) throws ValidationException, DatabaseException {
        if (user == null) {
            throw new ValidationException("L'utilisateur ne peut pas être null");
        }
        String sql = "UPDATE users SET username = ?, password = ?, email = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setInt(4, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la mise à jour de l'utilisateur", e);
        }
        return user;
    }

    @Override
    public void delete(User user) throws ValidationException, DatabaseException {
        if (user == null) {
            throw new ValidationException("L'utilisateur ne peut pas être null");
        }
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la suppression de l'utilisateur", e);
        }
    }

    @Override
    public boolean existsById(Integer id) throws ValidationException, DatabaseException {
        if (id == null) {
            throw new ValidationException("L'ID ne peut pas être null");
        }
        String sql = "SELECT COUNT(*) FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la vérification de l'existence de l'utilisateur", e);
        }
        return false;
    }

    @Override
    public void close() throws DatabaseException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la fermeture de la connexion", e);
        }
    }
} 