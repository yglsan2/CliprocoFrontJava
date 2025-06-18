package dao.jpa;

import dao.IDAO;
import models.Prospect;
import utilities.LogManager;
import java.util.List;
import java.util.Optional;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProspectJpaDAO extends AbstractJpaDAO<Prospect, Integer> implements IDAO<Prospect, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(ProspectJpaDAO.class);
    private final EntityManager entityManager;

    public ProspectJpaDAO() {
        super(Prospect.class);
        this.entityManager = DatabaseConnection.getEntityManager();
        logger.info("ProspectJpaDAO initialisé avec l'EntityManager par défaut");
    }

    public ProspectJpaDAO(EntityManager entityManager) {
        super(Prospect.class);
        this.entityManager = entityManager;
        logger.info("ProspectJpaDAO initialisé avec un EntityManager personnalisé");
    }

    @Override
    public Optional<Prospect> findById(Integer id) throws ValidationException, DatabaseException {
        logger.debug("Recherche du prospect avec l'ID: {}", id);
        try {
            Prospect prospect = entityManager.find(Prospect.class, id);
            return Optional.ofNullable(prospect);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche du prospect avec l'ID: {}", id, e);
            throw new RuntimeException("Erreur lors de la recherche du prospect", e);
        }
    }

    @Override
    public Prospect save(Prospect entity) throws ValidationException, DatabaseException {
        logger.debug("Sauvegarde d'un nouveau prospect");
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
            logger.info("Nouveau prospect sauvegardé avec succès");
            return entity;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            logger.error("Erreur lors de la sauvegarde du prospect", e);
            throw new RuntimeException("Erreur lors de la sauvegarde du prospect", e);
        }
    }

    @Override
    public Prospect update(Prospect entity) throws ValidationException, ResourceNotFoundException, DatabaseException {
        logger.debug("Mise à jour du prospect avec l'ID: {}", entity.getIdentifiant());
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
            logger.info("Prospect mis à jour avec succès");
            return entity;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            logger.error("Erreur lors de la mise à jour du prospect", e);
            throw new RuntimeException("Erreur lors de la mise à jour du prospect", e);
        }
    }

    @Override
    public void delete(Prospect entity) throws ValidationException, ResourceNotFoundException, DatabaseException {
        logger.debug("Suppression du prospect avec l'ID: {}", entity.getIdentifiant());
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
            entityManager.getTransaction().commit();
            logger.info("Prospect supprimé avec succès");
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            logger.error("Erreur lors de la suppression du prospect", e);
            throw new RuntimeException("Erreur lors de la suppression du prospect", e);
        }
    }

    public List<Prospect> findByRaisonSociale(String raisonSociale) throws ValidationException, DatabaseException {
        logger.debug("Recherche des prospects avec la raison sociale: {}", raisonSociale);
        try {
            if (raisonSociale == null) {
                throw new ValidationException("La raison sociale ne peut pas être null");
            }
            TypedQuery<Prospect> query = entityManager.createQuery(
                "SELECT p FROM Prospect p WHERE p.raisonSociale = :raisonSociale", Prospect.class);
            query.setParameter("raisonSociale", raisonSociale);
            return query.getResultList();
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche des prospects par raison sociale: {}", raisonSociale, e);
            throw new DatabaseException("Erreur lors de la recherche des prospects par raison sociale", e);
        }
    }

    public boolean existsByRaisonSociale(String raisonSociale) throws ValidationException, DatabaseException {
        logger.debug("Vérification de l'existence des prospects avec la raison sociale: {}", raisonSociale);
        try {
            if (raisonSociale == null) {
                throw new ValidationException("La raison sociale ne peut pas être null");
            }
            TypedQuery<Integer> query = entityManager.createQuery(
                "SELECT COUNT(p) FROM Prospect p WHERE p.raisonSociale = :raisonSociale", Integer.class);
            query.setParameter("raisonSociale", raisonSociale);
            return query.getSingleResult() > 0;
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Erreur lors de la vérification de l'existence des prospects avec la raison sociale: {}", raisonSociale, e);
            throw new DatabaseException("Erreur lors de la vérification de l'existence des prospects", e);
        }
    }

    @Override
    public void close() throws DatabaseException {
        try {
            entityManager.close();
        } catch (Exception e) {
            logger.error("Erreur lors de la fermeture des ressources", e);
            throw new DatabaseException("Erreur lors de la fermeture des ressources", e);
        }
    }
} 