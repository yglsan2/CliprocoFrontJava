package dao.jpa;

import models.Prospect;
import dao.IDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import routers.FrontController;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Implémentation JPA du DAO pour les prospects.
 */
public class ProspectJpaDAO implements IDAO<Prospect, Integer> {
    private static final Logger logger = Logger.getLogger(ProspectJpaDAO.class.getName());
    private final EntityManager entityManager;

    public ProspectJpaDAO() {
        this.entityManager = FrontController.getEntityManager();
        logger.info("ProspectJpaDAO initialisé");
    }

    public ProspectJpaDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
        logger.info("ProspectJpaDAO initialisé avec un EntityManager personnalisé");
    }

    @Override
    public Optional<Prospect> findById(Integer id) throws ValidationException, DatabaseException {
        logger.info("Recherche du prospect avec l'ID: " + id);
        try {
            if (id == null) {
                throw new ValidationException("L'ID ne peut pas être null");
            }
            Prospect prospect = entityManager.find(Prospect.class, id);
            return Optional.ofNullable(prospect);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            logger.severe("Erreur lors de la recherche du prospect avec l'ID: " + id + " - " + e.getMessage());
            throw new DatabaseException("Erreur lors de la recherche du prospect", e);
        }
    }

    @Override
    public List<Prospect> findAll() throws DatabaseException {
        logger.info("Récupération de tous les prospects");
        try {
            TypedQuery<Prospect> query = entityManager.createQuery(
                "SELECT p FROM Prospect p", 
                Prospect.class
            );
            List<Prospect> result = query.getResultList();
            logger.info("Résultat obtenu: " + result.size() + " prospects");
            return result;
        } catch (Exception e) {
            logger.severe("Erreur lors de la récupération de tous les prospects: " + e.getMessage());
            throw new DatabaseException("Erreur lors de la récupération des prospects", e);
        }
    }

    @Override
    public Prospect save(Prospect prospect) throws ValidationException, DatabaseException {
        logger.info("Sauvegarde d'un nouveau prospect");
        try {
            if (prospect == null) {
                throw new ValidationException("Le prospect ne peut pas être null");
            }
            
            // Validation des champs
            validateProspect(prospect);
            
            entityManager.getTransaction().begin();
            
            // Persister l'adresse en premier si elle n'a pas d'ID
            if (prospect.getAdresse() != null && prospect.getAdresse().getIdentifiant() == null) {
                entityManager.persist(prospect.getAdresse());
                logger.info("Adresse persistée avec l'ID: " + prospect.getAdresse().getIdentifiant());
            }
            
            // Persister le prospect
            entityManager.persist(prospect);
            entityManager.getTransaction().commit();
            logger.info("Nouveau prospect sauvegardé avec succès");
            return prospect;
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            logger.severe("Erreur lors de la sauvegarde du prospect: " + e.getMessage());
            throw new DatabaseException("Erreur lors de la sauvegarde du prospect", e);
        }
    }

    @Override
    public Prospect update(Prospect prospect) throws ValidationException, ResourceNotFoundException, DatabaseException {
        try {
            if (prospect == null) {
                throw new ValidationException("Le prospect ne peut pas être null");
            }
            logger.info("Mise à jour du prospect avec l'ID: " + prospect.getIdentifiant());
            
            // Validation des champs
            validateProspect(prospect);
            
            entityManager.getTransaction().begin();
            Prospect existingProspect = entityManager.find(Prospect.class, prospect.getIdentifiant());
            if (existingProspect == null) {
                throw new ResourceNotFoundException("Prospect non trouvé avec l'ID: " + prospect.getIdentifiant());
            }
            Prospect updatedProspect = entityManager.merge(prospect);
            entityManager.getTransaction().commit();
            logger.info("Prospect mis à jour avec succès");
            return updatedProspect;
        } catch (ValidationException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            logger.severe("Erreur lors de la mise à jour du prospect: " + e.getMessage());
            throw new DatabaseException("Erreur lors de la mise à jour du prospect", e);
        }
    }

    @Override
    public void delete(Prospect prospect) throws ValidationException, ResourceNotFoundException, DatabaseException {
        try {
            if (prospect == null) {
                throw new ValidationException("Le prospect ne peut pas être null");
            }
            logger.info("Suppression du prospect avec l'ID: " + prospect.getIdentifiant());
            
            entityManager.getTransaction().begin();
            Prospect existingProspect = entityManager.find(Prospect.class, prospect.getIdentifiant());
            if (existingProspect == null) {
                throw new ResourceNotFoundException("Prospect non trouvé avec l'ID: " + prospect.getIdentifiant());
            }
            entityManager.remove(existingProspect);
            entityManager.getTransaction().commit();
            logger.info("Prospect supprimé avec succès");
        } catch (ValidationException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            logger.severe("Erreur lors de la suppression du prospect: " + e.getMessage());
            throw new DatabaseException("Erreur lors de la suppression du prospect", e);
        }
    }

    @Override
    public boolean existsById(Integer id) throws ValidationException, DatabaseException {
        logger.info("Vérification de l'existence du prospect avec l'ID: " + id);
        try {
            if (id == null) {
                throw new ValidationException("L'ID ne peut pas être null");
            }
            Prospect prospect = entityManager.find(Prospect.class, id);
            return prospect != null;
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            logger.severe("Erreur lors de la vérification de l'existence du prospect: " + e.getMessage());
            throw new DatabaseException("Erreur lors de la vérification de l'existence du prospect", e);
        }
    }

    @Override
    public void close() throws DatabaseException {
        try {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        } catch (Exception e) {
            logger.severe("Erreur lors de la fermeture des ressources: " + e.getMessage());
            throw new DatabaseException("Erreur lors de la fermeture des ressources", e);
        }
    }

    public List<Prospect> findByRaisonSociale(String raisonSociale) throws ValidationException, DatabaseException {
        logger.info("Recherche des prospects avec la raison sociale: " + raisonSociale);
        try {
            if (raisonSociale == null) {
                throw new ValidationException("La raison sociale ne peut pas être null");
            }
            TypedQuery<Prospect> query = entityManager.createQuery(
                "SELECT p FROM Prospect p WHERE p.raisonSociale LIKE :raisonSociale",
                Prospect.class
            );
            query.setParameter("raisonSociale", "%" + raisonSociale + "%");
            return query.getResultList();
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            logger.severe("Erreur lors de la recherche des prospects par raison sociale: " + e.getMessage());
            throw new DatabaseException("Erreur lors de la recherche des prospects par raison sociale", e);
        }
    }

    /**
     * Valide les champs d'un prospect.
     * @param prospect Le prospect à valider
     * @throws ValidationException Si la validation échoue
     */
    private void validateProspect(Prospect prospect) throws ValidationException {
        if (prospect.getRaisonSociale() == null || prospect.getRaisonSociale().trim().isEmpty()) {
            throw new ValidationException("La raison sociale ne peut pas être vide");
        }
        if (prospect.getRaisonSociale().length() > 255) {
            throw new ValidationException("La raison sociale ne peut pas dépasser 255 caractères");
        }
        if (prospect.getMail() != null && !prospect.getMail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new ValidationException("L'adresse email n'est pas valide");
        }
        if (prospect.getTelephone() != null && !prospect.getTelephone().matches("^[0-9]{10}$")) {
            throw new ValidationException("Le numéro de téléphone doit contenir 10 chiffres");
        }
        if (prospect.getAdresse() == null) {
            throw new ValidationException("L'adresse ne peut pas être null");
        }
        if (prospect.getCommentaires() != null && prospect.getCommentaires().length() > 1000) {
            throw new ValidationException("Les commentaires ne peuvent pas dépasser 1000 caractères");
        }
    }
} 