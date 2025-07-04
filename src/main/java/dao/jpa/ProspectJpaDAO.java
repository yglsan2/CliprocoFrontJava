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
            entityManager.getTransaction().begin();
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
        logger.info("Mise à jour du prospect avec l'ID: " + prospect.getIdentifiant());
        try {
            if (prospect == null) {
                throw new ValidationException("Le prospect ne peut pas être null");
            }
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
        logger.info("Suppression du prospect avec l'ID: " + prospect.getIdentifiant());
        try {
            if (prospect == null) {
                throw new ValidationException("Le prospect ne peut pas être null");
            }
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
} 