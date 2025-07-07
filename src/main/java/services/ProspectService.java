package services;

import models.Prospect;
import dao.jpa.ProspectJpaDAO;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    /**
     * Recherche des prospects par raison sociale (recherche partielle)
     */
    public List<Prospect> searchByRaisonSociale(String searchTerm) throws DatabaseException {
        logger.debug("Recherche des prospects avec le terme: {}", searchTerm);
        try {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                logger.warn("Terme de recherche vide");
                return List.of();
            }
            
            String lowerSearchTerm = searchTerm.toLowerCase();
            List<Prospect> prospects = prospectDao.findAll().stream()
                .filter(prospect -> prospect.getRaisonSociale() != null && 
                                  prospect.getRaisonSociale().toLowerCase().contains(lowerSearchTerm))
                .collect(Collectors.toList());
            logger.info("Nombre de prospects trouvés: {}", prospects.size());
            return prospects;
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche des prospects", e);
            throw new DatabaseException("Erreur lors de la recherche des prospects", e);
        }
    }

    /**
     * Trouve les prospects intéressés
     */
    public List<Prospect> findInterestedProspects() throws DatabaseException {
        logger.debug("Recherche des prospects intéressés");
        try {
            List<Prospect> interestedProspects = prospectDao.findAll().stream()
                .filter(Prospect::getProspectInteresse)
                .collect(Collectors.toList());
            logger.info("Nombre de prospects intéressés trouvés: {}", interestedProspects.size());
            return interestedProspects;
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche des prospects intéressés", e);
            throw new DatabaseException("Erreur lors de la recherche des prospects intéressés", e);
        }
    }

    /**
     * Calcule le taux de conversion des prospects
     */
    public double calculateConversionRate() throws DatabaseException {
        logger.debug("Calcul du taux de conversion des prospects");
        try {
            List<Prospect> allProspects = prospectDao.findAll();
            if (allProspects.isEmpty()) {
                return 0.0;
            }
            
            long interestedCount = allProspects.stream()
                .filter(Prospect::getProspectInteresse)
                .count();
            
            double conversionRate = (double) interestedCount / allProspects.size() * 100;
            logger.info("Taux de conversion calculé: {}%", conversionRate);
            return conversionRate;
        } catch (Exception e) {
            logger.error("Erreur lors du calcul du taux de conversion", e);
            throw new DatabaseException("Erreur lors du calcul du taux de conversion", e);
        }
    }

    /**
     * Trouve les prospects récents (dans les X derniers jours)
     */
    public List<Prospect> findRecentProspects(int days) throws DatabaseException {
        logger.debug("Recherche des prospects des {} derniers jours", days);
        try {
            LocalDate cutoffDate = LocalDate.now().minusDays(days);
            List<Prospect> recentProspects = prospectDao.findAll().stream()
                .filter(prospect -> prospect.getDateProspection() != null &&
                                  prospect.getDateProspection().toLocalDate().isAfter(cutoffDate))
                .collect(Collectors.toList());
            logger.info("Nombre de prospects récents trouvés: {}", recentProspects.size());
            return recentProspects;
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche des prospects récents", e);
            throw new DatabaseException("Erreur lors de la recherche des prospects récents", e);
        }
    }

    /**
     * Trouve les prospects triés par date de prospection (plus récent en premier)
     */
    public List<Prospect> findProspectsSortedByDate() throws DatabaseException {
        logger.debug("Récupération des prospects triés par date");
        try {
            List<Prospect> sortedProspects = prospectDao.findAll().stream()
                .filter(prospect -> prospect.getDateProspection() != null)
                .sorted((p1, p2) -> p2.getDateProspection().compareTo(p1.getDateProspection()))
                .collect(Collectors.toList());
            logger.info("Nombre de prospects triés: {}", sortedProspects.size());
            return sortedProspects;
        } catch (Exception e) {
            logger.error("Erreur lors du tri des prospects", e);
            throw new DatabaseException("Erreur lors du tri des prospects", e);
        }
    }

    /**
     * Valide les données métier d'un prospect
     */
    private void validateProspect(Prospect prospect) throws ValidationException {
        if (prospect == null) {
            throw new ValidationException("Le prospect ne peut pas être null");
        }
        
        if (prospect.getDateProspection() != null && 
            prospect.getDateProspection().toLocalDate().isAfter(LocalDate.now())) {
            throw new ValidationException("La date de prospection ne peut pas être dans le futur");
        }
        
        if (prospect.getMail() != null && !utilities.ValidationManager.isValidEmail(prospect.getMail())) {
            throw new ValidationException("Format d'email invalide");
        }
        
        if (prospect.getTelephone() != null && !utilities.ValidationManager.isValidPhone(prospect.getTelephone())) {
            throw new ValidationException("Format de téléphone invalide. Formats acceptés : 0612345678, +33612345678, 0033612345678");
        }
        
        if (prospect.getRaisonSociale() != null && !utilities.ValidationManager.isValidCompanyName(prospect.getRaisonSociale())) {
            throw new ValidationException("Format de raison sociale invalide");
        }
        
        if (prospect.getProspectInteresse() == null) {
            throw new ValidationException("L'intérêt du prospect doit être défini");
        }
    }

    /**
     * Création avec validation métier
     */
    public void createWithValidation(Prospect prospect) throws ValidationException, DatabaseException {
        logger.debug("Création d'un nouveau prospect avec validation: {}", prospect);
        try {
            validateProspect(prospect);
            create(prospect);
        } catch (ValidationException e) {
            logger.warn("Erreur de validation lors de la création du prospect: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erreur lors de la création du prospect", e);
            throw new DatabaseException("Erreur lors de la création du prospect", e);
        }
    }
} 