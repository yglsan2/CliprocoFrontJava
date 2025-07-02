package services;

import models.Prospect;
import dao.jpa.ProspectJpaDAO;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class ProspectService {
    private static final Logger logger = LoggerFactory.getLogger(ProspectService.class);
    private final ProspectJpaDAO prospectDao;

    public ProspectService() {
        this.prospectDao = new ProspectJpaDAO();
        logger.info("ProspectService initialisé avec le DAO par défaut");
    }

    public ProspectService(ProspectJpaDAO prospectDao) {
        this.prospectDao = prospectDao;
        logger.info("ProspectService initialisé avec un DAO personnalisé");
    }

    public Prospect findById(Integer id) throws DatabaseException, ValidationException, ResourceNotFoundException {
        logger.debug("Recherche du prospect avec l'ID: {}", id);
        Optional<Prospect> prospect = prospectDao.findById(id);
        if (prospect.isPresent()) {
            return prospect.get();
        }
        logger.warn("Aucun prospect trouvé avec l'ID: {}", id);
        throw new ResourceNotFoundException("Prospect non trouvé avec l'ID: " + id);
    }

    public List<Prospect> findAll() throws DatabaseException {
        logger.debug("Récupération de tous les prospects");
        return prospectDao.findAll();
    }

    public List<Prospect> findByRaisonSociale(String raisonSociale) throws DatabaseException, ValidationException {
        logger.debug("Recherche des prospects avec la raison sociale: {}", raisonSociale);
        return prospectDao.findByRaisonSociale(raisonSociale);
    }

    public void save(Prospect prospect) throws DatabaseException, ValidationException {
        logger.debug("Sauvegarde d'un nouveau prospect");
        prospectDao.save(prospect);
        logger.info("Nouveau prospect sauvegardé avec succès");
    }

    public void update(Prospect prospect) throws DatabaseException, ValidationException, ResourceNotFoundException {
        logger.debug("Mise à jour du prospect avec l'ID: {}", prospect.getIdentifiant());
        if (!prospectDao.existsById(prospect.getIdentifiant())) {
            logger.warn("Tentative de mise à jour d'un prospect inexistant avec l'ID: {}", prospect.getIdentifiant());
            throw new ResourceNotFoundException("Prospect non trouvé avec l'ID: " + prospect.getIdentifiant());
        }
        prospectDao.update(prospect);
        logger.info("Prospect mis à jour avec succès");
    }

    public void delete(Prospect prospect) throws DatabaseException, ValidationException, ResourceNotFoundException {
        logger.debug("Suppression du prospect avec l'ID: {}", prospect.getIdentifiant());
        if (!prospectDao.existsById(prospect.getIdentifiant())) {
            logger.warn("Tentative de suppression d'un prospect inexistant avec l'ID: {}", prospect.getIdentifiant());
            throw new ResourceNotFoundException("Prospect non trouvé avec l'ID: " + prospect.getIdentifiant());
        }
        prospectDao.delete(prospect);
        logger.info("Prospect supprimé avec succès");
    }

    public void create(Prospect prospect) throws DatabaseException, ValidationException {
        save(prospect);
    }
} 