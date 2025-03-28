package com.cliproco.dao;

import com.cliproco.model.Contrat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ContratDAO {
    private static final Logger LOGGER = Logger.getLogger(ContratDAO.class.getName());
    private final DatabaseConnection dbConnection;

    public ContratDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public List<Contrat> findByIdClient(Integer clientId) {
        List<Contrat> contrats = new ArrayList<>();
        String sql = "SELECT * FROM contrat WHERE client_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, clientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    contrats.add(extractContratFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des contrats du client", e);
        }
        return contrats;
    }

    public Contrat find(Integer id) {
        String sql = "SELECT * FROM contrat WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractContratFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération du contrat", e);
        }
        return null;
    }

    public void save(Contrat contrat) {
        String sql = contrat.getId() == null ? 
            "INSERT INTO contrat (client_id, libelle, montant) VALUES (?, ?, ?)" :
            "UPDATE contrat SET client_id = ?, libelle = ?, montant = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, contrat.getClientId());
            stmt.setString(2, contrat.getLibelle());
            stmt.setDouble(3, contrat.getMontant());
            
            if (contrat.getId() != null) {
                stmt.setInt(4, contrat.getId());
            }
            
            stmt.executeUpdate();
            
            if (contrat.getId() == null) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        contrat.setId(rs.getInt(1));
                    }
                }
            }
            
            LOGGER.info("Contrat sauvegardé avec succès");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la sauvegarde du contrat", e);
        }
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM contrat WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
            LOGGER.info("Contrat supprimé avec succès");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression du contrat", e);
        }
    }

    private Contrat extractContratFromResultSet(ResultSet rs) throws SQLException {
        Contrat contrat = new Contrat();
        contrat.setId(rs.getInt("id"));
        contrat.setClientId(rs.getInt("client_id"));
        contrat.setLibelle(rs.getString("libelle"));
        contrat.setMontant(rs.getDouble("montant"));
        return contrat;
    }
} 