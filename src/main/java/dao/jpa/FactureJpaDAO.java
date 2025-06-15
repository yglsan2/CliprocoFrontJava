package dao.jpa;

import models.Facture;
import exceptions.DatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;
import dao.IDAO;
import utilities.LogManager;

public class FactureJpaDAO extends GenericJpaDAO<Facture> implements IDAO<Facture> {
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public FactureJpaDAO() {
        this.emf = Persistence.createEntityManagerFactory("cliproco");
        this.em = emf.createEntityManager();
    }

    @Override
    public Facture findById(Long id) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(Facture.class, id);
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche de la facture par ID", e);
            throw new DatabaseException("Erreur lors de la recherche de la facture par ID", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Facture> findAll() throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Facture> query = em.createQuery("SELECT f FROM Facture f", Facture.class);
            return query.getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la récupération de toutes les factures", e);
            throw new DatabaseException("Erreur lors de la récupération de toutes les factures", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void save(Facture facture) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(facture);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la sauvegarde de la facture", e);
            throw new DatabaseException("Erreur lors de la sauvegarde de la facture", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void update(Facture facture) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(facture);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la mise à jour de la facture", e);
            throw new DatabaseException("Erreur lors de la mise à jour de la facture", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void delete(Facture facture) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.remove(em.merge(facture));
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la suppression de la facture", e);
            throw new DatabaseException("Erreur lors de la suppression de la facture", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public List<Facture> findByClientId(Long clientId) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Facture> query = em.createQuery(
                "SELECT f FROM Facture f WHERE f.client.id = :clientId", 
                Facture.class
            );
            query.setParameter("clientId", clientId);
            return query.getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche des factures par ID client", e);
            throw new DatabaseException("Erreur lors de la recherche des factures par ID client", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public List<Facture> findUnpaid() throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Facture> query = em.createQuery(
                "SELECT f FROM Facture f WHERE f.paye = false", 
                Facture.class
            );
            return query.getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche des factures non payées", e);
            throw new DatabaseException("Erreur lors de la recherche des factures non payées", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public Facture findByNumeroFacture(String numeroFacture) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Facture> query = em.createQuery(
                "SELECT f FROM Facture f WHERE f.numeroFacture = :numeroFacture", 
                Facture.class
            );
            query.setParameter("numeroFacture", numeroFacture);
            return query.getSingleResult();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche de la facture par numéro", e);
            throw new DatabaseException("Erreur lors de la recherche de la facture par numéro", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void close() {
        em.close();
        emf.close();
    }
} 