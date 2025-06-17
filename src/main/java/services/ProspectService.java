package services;

import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import dao.jpa.ProspectJpaDAO;
import models.Prospect;
import utilities.LogManager;

import java.util.List;
import java.util.Optional;

public class ProspectService {
    private final ProspectJpaDAO prospectDAO;
    
    public ProspectService() {
        this.prospectDAO = new ProspectJpaDAO();
        LogManager.logInfo("Initialisation du ProspectService avec le DAO par défaut");
    }
    
    public ProspectService(ProspectJpaDAO prospectDAO) {
        this.prospectDAO = prospectDAO;
        LogManager.logInfo("Initialisation du ProspectService avec le DAO fourni");
    }
    
    public Prospect findById(Long id) throws DatabaseException, ValidationException, ResourceNotFoundException {
        LogManager.logInfo("Recherche du prospect avec l'ID: " + id);
        Optional<Prospect> prospect = prospectDAO.findById(id);
        if (prospect.isEmpty()) {
            LogManager.logWarning("Prospect non trouvé avec l'ID: " + id);
            throw new ResourceNotFoundException("Prospect non trouvé avec l'ID: " + id);
        }
        return prospect.get();
    }
    
    public List<Prospect> findAll() throws DatabaseException {
        LogManager.logInfo("Récupération de tous les prospects");
        return prospectDAO.findAll();
    }
    
    public List<Prospect> findByRaisonSociale(String raisonSociale) throws DatabaseException, ValidationException {
        LogManager.logInfo("Recherche des prospects avec la raison sociale: " + raisonSociale);
        return prospectDAO.findByRaisonSociale(raisonSociale);
    }
    
    public void save(Prospect prospect) throws DatabaseException, ValidationException {
        LogManager.logInfo("Sauvegarde du prospect: " + prospect);
        if (prospectDAO.existsByRaisonSociale(prospect.getRaisonSociale())) {
            LogManager.logWarning("Un prospect avec cette raison sociale existe déjà: " + prospect.getRaisonSociale());
            throw new DatabaseException("Un prospect avec cette raison sociale existe déjà");
        }
        prospectDAO.save(prospect);
        LogManager.logInfo("Prospect sauvegardé avec succès");
    }
    
    public void update(Prospect prospect) throws DatabaseException, ValidationException, ResourceNotFoundException {
        LogManager.logInfo("Mise à jour du prospect: " + prospect);
        prospectDAO.update(prospect);
        LogManager.logInfo("Prospect mis à jour avec succès");
    }
    
    public void delete(Prospect prospect) throws DatabaseException, ValidationException, ResourceNotFoundException {
        LogManager.logInfo("Suppression du prospect: " + prospect);
        prospectDAO.delete(prospect);
        LogManager.logInfo("Prospect supprimé avec succès");
    }
} 