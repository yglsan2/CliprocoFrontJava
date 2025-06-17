package dao.jpa;

import models.Adresse;
import utilities.LogManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import exceptions.BusinessException;
import java.util.List;
import java.util.Optional;

public class AdresseJpaDAO extends GenericJpaDAO<Adresse, Long> {
    
    public AdresseJpaDAO() {
        super();
    }

    @Override
    public Optional<Adresse> findById(Long id) throws ValidationException, DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Adresse adresse = em.find(Adresse.class, id);
            return Optional.ofNullable(adresse);
        } catch (IllegalArgumentException e) {
            LogManager.logWarning("ID invalide pour la recherche d'adresse: " + id);
            throw new ValidationException("ID invalide", e);
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche de l'adresse par ID", e);
            throw new DatabaseException("Erreur lors de la recherche de l'adresse", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Adresse> findAll() throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Adresse> query = em.createQuery("SELECT a FROM Adresse a", Adresse.class);
            return query.getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la récupération des adresses", e);
            throw new DatabaseException("Erreur lors de la récupération des adresses", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Adresse save(Adresse adresse) throws ValidationException, DatabaseException {
        EntityManager em = null;
        try {
            if (adresse == null) {
                throw new ValidationException("L'adresse ne peut pas être null");
            }
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(adresse);
            em.getTransaction().commit();
            return adresse;
        } catch (ValidationException e) {
            LogManager.logWarning("Erreur de validation lors de la sauvegarde de l'adresse: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la sauvegarde de l'adresse", e);
            throw new DatabaseException("Erreur lors de la sauvegarde de l'adresse", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Adresse update(Adresse adresse) throws ValidationException, ResourceNotFoundException, DatabaseException {
        EntityManager em = null;
        try {
            if (adresse == null) {
                throw new ValidationException("L'adresse ne peut pas être null");
            }
            em = getEntityManager();
            em.getTransaction().begin();
            Adresse existingAdresse = em.find(Adresse.class, adresse.getIdentifiant());
            if (existingAdresse == null) {
                throw new ResourceNotFoundException("Adresse non trouvée avec l'ID: " + adresse.getIdentifiant());
            }
            Adresse updatedAdresse = em.merge(adresse);
            em.getTransaction().commit();
            return updatedAdresse;
        } catch (ValidationException | ResourceNotFoundException e) {
            LogManager.logWarning("Erreur lors de la mise à jour de l'adresse: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la mise à jour de l'adresse", e);
            throw new DatabaseException("Erreur lors de la mise à jour de l'adresse", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void delete(Adresse adresse) throws ValidationException, ResourceNotFoundException, DatabaseException {
        EntityManager em = null;
        try {
            if (adresse == null) {
                throw new ValidationException("L'adresse ne peut pas être null");
            }
            em = getEntityManager();
            em.getTransaction().begin();
            Adresse existingAdresse = em.find(Adresse.class, adresse.getIdentifiant());
            if (existingAdresse == null) {
                throw new ResourceNotFoundException("Adresse non trouvée avec l'ID: " + adresse.getIdentifiant());
            }
            em.remove(existingAdresse);
            em.getTransaction().commit();
        } catch (ValidationException | ResourceNotFoundException e) {
            LogManager.logWarning("Erreur lors de la suppression de l'adresse: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la suppression de l'adresse", e);
            throw new DatabaseException("Erreur lors de la suppression de l'adresse", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void close() throws DatabaseException {
        try {
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la fermeture des ressources", e);
            throw new DatabaseException("Erreur lors de la fermeture des ressources", e);
        }
    }
} 