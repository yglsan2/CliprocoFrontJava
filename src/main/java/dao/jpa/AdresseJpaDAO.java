package dao.jpa;

import models.Adresse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dao.IDAO;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public class AdresseJpaDAO implements IDAO<Adresse, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(AdresseJpaDAO.class);
    private final EntityManager entityManager;

    public AdresseJpaDAO() {
        this.entityManager = DatabaseConnection.getEntityManager();
        logger.info("AdresseJpaDAO initialisé avec l'EntityManager par défaut");
    }

    public AdresseJpaDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
        logger.info("AdresseJpaDAO initialisé avec un EntityManager personnalisé");
    }

    @Override
    public Optional<Adresse> findById(Integer id) throws ValidationException, DatabaseException {
        logger.debug("Recherche de l'adresse avec l'ID: {}", id);
        try {
            Adresse adresse = entityManager.find(Adresse.class, id);
            return Optional.ofNullable(adresse);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de l'adresse avec l'ID: {}", id, e);
            throw new DatabaseException("Erreur lors de la recherche de l'adresse", e);
        }
    }

    @Override
    public List<Adresse> findAll() throws DatabaseException {
        logger.debug("Récupération de toutes les adresses");
        try {
            TypedQuery<Adresse> query = entityManager.createQuery("SELECT a FROM Adresse a", Adresse.class);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de toutes les adresses", e);
            throw new DatabaseException("Erreur lors de la récupération des adresses", e);
        }
    }

    public List<Adresse> findByVille(String ville) {
        logger.debug("Recherche des adresses dans la ville: {}", ville);
        try {
            TypedQuery<Adresse> query = entityManager.createQuery(
                "SELECT a FROM Adresse a WHERE a.ville = :ville", Adresse.class);
            query.setParameter("ville", ville);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche des adresses dans la ville: {}", ville, e);
            throw new RuntimeException("Erreur lors de la recherche des adresses par ville", e);
        }
    }

    @Override
    public Adresse save(Adresse adresse) throws ValidationException, DatabaseException {
        logger.debug("Sauvegarde d'une nouvelle adresse");
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(adresse);
            entityManager.getTransaction().commit();
            logger.info("Nouvelle adresse sauvegardée avec succès");
            return adresse;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            logger.error("Erreur lors de la sauvegarde de l'adresse", e);
            throw new DatabaseException("Erreur lors de la sauvegarde de l'adresse", e);
        }
    }

    @Override
    public Adresse update(Adresse adresse) throws ValidationException, ResourceNotFoundException, DatabaseException {
        logger.debug("Mise à jour de l'adresse avec l'ID: {}", adresse.getIdentifiant());
        try {
            if (!existsById(adresse.getIdentifiant())) {
                throw new ResourceNotFoundException("Adresse non trouvée avec l'ID: " + adresse.getIdentifiant());
            }
            entityManager.getTransaction().begin();
            Adresse updatedAdresse = entityManager.merge(adresse);
            entityManager.getTransaction().commit();
            logger.info("Adresse mise à jour avec succès");
            return updatedAdresse;
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            logger.error("Erreur lors de la mise à jour de l'adresse", e);
            throw new DatabaseException("Erreur lors de la mise à jour de l'adresse", e);
        }
    }

    @Override
    public void delete(Adresse adresse) throws ValidationException, ResourceNotFoundException, DatabaseException {
        logger.debug("Suppression de l'adresse avec l'ID: {}", adresse.getIdentifiant());
        try {
            if (!existsById(adresse.getIdentifiant())) {
                throw new ResourceNotFoundException("Adresse non trouvée avec l'ID: " + adresse.getIdentifiant());
            }
            entityManager.getTransaction().begin();
            entityManager.remove(entityManager.contains(adresse) ? adresse : entityManager.merge(adresse));
            entityManager.getTransaction().commit();
            logger.info("Adresse supprimée avec succès");
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            logger.error("Erreur lors de la suppression de l'adresse", e);
            throw new DatabaseException("Erreur lors de la suppression de l'adresse", e);
        }
    }

    @Override
    public boolean existsById(Integer id) {
        logger.debug("Vérification de l'existence de l'adresse avec l'ID: {}", id);
        try {
            TypedQuery<Integer> query = entityManager.createQuery(
                "SELECT COUNT(a) FROM Adresse a WHERE a.identifiant = :id", Integer.class);
            query.setParameter("id", id);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            logger.error("Erreur lors de la vérification de l'existence de l'adresse avec l'ID: {}", id, e);
            return false;
        }
    }

    @Override
    public void close() throws DatabaseException {
        try {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        } catch (Exception e) {
            throw new DatabaseException("Erreur lors de la fermeture de l'EntityManager", e);
        }
    }
} 