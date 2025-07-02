package services;

import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import dao.jpa.SocieteJpaDAO;
import models.Societe;
import utilities.LogManager;

import java.util.List;
import java.util.Optional;

public class SocieteService {
    private final SocieteJpaDAO societeDAO;
    
    public SocieteService() {
        this.societeDAO = new SocieteJpaDAO();
        LogManager.logInfo("Initialisation du SocieteService avec le DAO par défaut");
    }
    
    public SocieteService(SocieteJpaDAO societeDAO) {
        this.societeDAO = societeDAO;
        LogManager.logInfo("Initialisation du SocieteService avec le DAO fourni");
    }
    
    public Societe findById(Integer id) throws DatabaseException, ValidationException, ResourceNotFoundException {
        LogManager.logInfo("Recherche de la société avec l'ID: " + id);
        Optional<Societe> societe = societeDAO.findById(id);
        if (societe.isEmpty()) {
            LogManager.logWarning("Société non trouvée avec l'ID: " + id);
            throw new ResourceNotFoundException("Société non trouvée avec l'ID: " + id);
        }
        return societe.get();
    }
    
    public List<Societe> findAll() throws DatabaseException {
        LogManager.logInfo("Récupération de toutes les sociétés");
        return societeDAO.findAll();
    }
    
    public Optional<Societe> findByRaisonSociale(String raisonSociale) throws DatabaseException, ValidationException {
        LogManager.logInfo("Recherche de la société avec la raison sociale: " + raisonSociale);
        return societeDAO.findByRaisonSociale(raisonSociale);
    }
    
    public void save(Societe societe) throws DatabaseException, ValidationException {
        LogManager.logInfo("Sauvegarde de la société: " + societe);
        if (societeDAO.existsByRaisonSociale(societe.getRaisonSociale())) {
            LogManager.logWarning("Une société avec cette raison sociale existe déjà: " + societe.getRaisonSociale());
            throw new DatabaseException("Une société avec cette raison sociale existe déjà");
        }
        societeDAO.save(societe);
        LogManager.logInfo("Société sauvegardée avec succès");
    }
    
    public void update(Societe societe) throws DatabaseException, ValidationException, ResourceNotFoundException {
        LogManager.logInfo("Mise à jour de la société: " + societe);
        societeDAO.update(societe);
        LogManager.logInfo("Société mise à jour avec succès");
    }
    
    public void delete(Societe societe) throws DatabaseException, ValidationException, ResourceNotFoundException {
        LogManager.logInfo("Suppression de la société: " + societe);
        societeDAO.delete(societe);
        LogManager.logInfo("Société supprimée avec succès");
    }
} 