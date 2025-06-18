package services;

import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import dao.jpa.ContratJpaDAO;
import models.Contrat;
import models.Client;
import utilities.LogManager;

import java.util.List;
import java.util.Optional;

public class ContratService {
    private final ContratJpaDAO contratDAO;
    
    public ContratService() {
        this.contratDAO = new ContratJpaDAO();
        LogManager.logInfo("Initialisation du ContratService avec le DAO par défaut");
    }
    
    public ContratService(ContratJpaDAO contratDAO) {
        this.contratDAO = contratDAO;
        LogManager.logInfo("Initialisation du ContratService avec le DAO fourni");
    }
    
    public Contrat findById(Integer id) throws DatabaseException, ValidationException, ResourceNotFoundException {
        LogManager.logInfo("Recherche du contrat avec l'ID: " + id);
        Optional<Contrat> contrat = contratDAO.findById(id);
        if (contrat.isEmpty()) {
            LogManager.logWarning("Contrat non trouvé avec l'ID: " + id);
            throw new ResourceNotFoundException("Contrat non trouvé avec l'ID: " + id);
        }
        return contrat.get();
    }
    
    public List<Contrat> findAll() throws DatabaseException {
        LogManager.logInfo("Récupération de tous les contrats");
        return contratDAO.findAll();
    }
    
    public List<Contrat> findByClient(Client client) throws DatabaseException, ValidationException {
        LogManager.logInfo("Recherche des contrats pour le client: " + client.getRaisonSociale());
        return contratDAO.findByClient(client);
    }
    
    public void save(Contrat contrat) throws DatabaseException, ValidationException {
        LogManager.logInfo("Sauvegarde du contrat: " + contrat);
        contratDAO.save(contrat);
        LogManager.logInfo("Contrat sauvegardé avec succès");
    }
    
    public void update(Contrat contrat) throws DatabaseException, ValidationException, ResourceNotFoundException {
        LogManager.logInfo("Mise à jour du contrat: " + contrat);
        contratDAO.update(contrat);
        LogManager.logInfo("Contrat mis à jour avec succès");
    }
    
    public void delete(Contrat contrat) throws DatabaseException, ValidationException, ResourceNotFoundException {
        LogManager.logInfo("Suppression du contrat: " + contrat);
        contratDAO.delete(contrat);
        LogManager.logInfo("Contrat supprimé avec succès");
    }
} 