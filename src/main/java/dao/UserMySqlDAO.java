package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.User;
import utilities.DatabaseConnection;

public class UserMySqlDAO implements UserDAO {

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setToken(rs.getString("token"));
        return user;
    }

    @Override
    public User findByUsername(String username) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de l'utilisateur", e);
        }
        return null;
    }

    @Override
    public User findByToken(String token) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE token = ?")) {
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de l'utilisateur par token", e);
        }
        return null;
    }

    @Override
    public void save(User user) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO users (username, password, token) VALUES (?, ?, ?)",
                 PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getToken());
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde de l'utilisateur", e);
        }
    }

    @Override
    public void update(User user) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "UPDATE users SET username = ?, password = ?, token = ? WHERE id = ?")) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getToken());
            ps.setLong(4, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'utilisateur", e);
        }
    }

    @Override
    public void delete(User user) {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {
            ps.setLong(1, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'utilisateur", e);
        }
    }
} 