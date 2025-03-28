package com.cliproco.dao;

import com.cliproco.model.Notification;
import com.cliproco.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {
    private static final Logger logger = LoggerFactory.getLogger(NotificationDAO.class);

    public List<Notification> findAll() {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notifications ORDER BY date_creation DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                notifications.add(extractNotificationFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la récupération des notifications", e);
        }

        return notifications;
    }

    public List<Notification> findNonLues() {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE lu = false ORDER BY date_creation DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                notifications.add(extractNotificationFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Erreur lors de la récupération des notifications non lues", e);
        }

        return notifications;
    }

    public void save(Notification notification) {
        String sql = notification.getId() == null ?
                "INSERT INTO notifications (message, type, date_creation, lu) VALUES (?, ?, ?, ?)" :
                "UPDATE notifications SET message = ?, type = ?, lu = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (notification.getId() == null) {
                pstmt.setString(1, notification.getMessage());
                pstmt.setString(2, notification.getType());
                pstmt.setTimestamp(3, Timestamp.valueOf(notification.getDateCreation()));
                pstmt.setBoolean(4, notification.isLu());
            } else {
                pstmt.setString(1, notification.getMessage());
                pstmt.setString(2, notification.getType());
                pstmt.setBoolean(3, notification.isLu());
                pstmt.setInt(4, notification.getId());
            }

            pstmt.executeUpdate();
            logger.info("Notification sauvegardée avec succès");
        } catch (SQLException e) {
            logger.error("Erreur lors de la sauvegarde de la notification", e);
        }
    }

    public void marquerCommeLu(Integer id) {
        String sql = "UPDATE notifications SET lu = true WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            logger.info("Notification marquée comme lue");
        } catch (SQLException e) {
            logger.error("Erreur lors du marquage de la notification comme lue", e);
        }
    }

    private Notification extractNotificationFromResultSet(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setId(rs.getInt("id"));
        notification.setMessage(rs.getString("message"));
        notification.setType(rs.getString("type"));
        notification.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
        notification.setLu(rs.getBoolean("lu"));
        return notification;
    }
} 