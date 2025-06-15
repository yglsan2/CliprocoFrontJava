package dao.jpa;

import models.Societe;
import exceptions.DatabaseException;
import dao.jpa.GenericJpaDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import utilities.LogManager;
import java.util.List;

public class SocieteJpaDAO extends GenericJpaDAO<Societe> {
    
    public SocieteJpaDAO() {
        super(Societe.class);
    }
    
    @Override
    public Societe findById(Long id) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(Societe.class, id);
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche de la société par ID", e);
            throw new DatabaseException("Erreur lors de la recherche de la société par ID", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    @Override
    public List<Societe> findAll() throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Societe> query = em.createQuery("SELECT s FROM Societe s", Societe.class);
            return query.getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la récupération de toutes les sociétés", e);
            throw new DatabaseException("Erreur lors de la récupération de toutes les sociétés", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    @Override
    public void save(Societe societe) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(societe);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la sauvegarde de la société", e);
            throw new DatabaseException("Erreur lors de la sauvegarde de la société", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    @Override
    public void update(Societe societe) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(societe);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la mise à jour de la société", e);
            throw new DatabaseException("Erreur lors de la mise à jour de la société", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    @Override
    public void delete(Societe societe) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.remove(em.merge(societe));
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la suppression de la société", e);
            throw new DatabaseException("Erreur lors de la suppression de la société", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
} 