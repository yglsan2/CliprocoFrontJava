package dao.jpa;

import models.CalculFacture;
import exceptions.DatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;
import dao.IDAO;
import utilities.LogManager;

public class CalculFactureJpaDAO extends GenericJpaDAO<CalculFacture> implements IDAO<CalculFacture> {
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public CalculFactureJpaDAO() {
        super(CalculFacture.class);
        this.emf = Persistence.createEntityManagerFactory("cliproco");
        this.em = emf.createEntityManager();
    }

    @Override
    public CalculFacture findById(Long id) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(CalculFacture.class, id);
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche du calcul de facture par ID", e);
            throw new DatabaseException("Erreur lors de la recherche du calcul de facture par ID", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<CalculFacture> findAll() throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<CalculFacture> query = em.createQuery("SELECT cf FROM CalculFacture cf", CalculFacture.class);
            return query.getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la récupération de tous les calculs de facture", e);
            throw new DatabaseException("Erreur lors de la récupération de tous les calculs de facture", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void save(CalculFacture calculFacture) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(calculFacture);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la sauvegarde du calcul de facture", e);
            throw new DatabaseException("Erreur lors de la sauvegarde du calcul de facture", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void update(CalculFacture calculFacture) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(calculFacture);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la mise à jour du calcul de facture", e);
            throw new DatabaseException("Erreur lors de la mise à jour du calcul de facture", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void delete(CalculFacture calculFacture) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.remove(em.merge(calculFacture));
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la suppression du calcul de facture", e);
            throw new DatabaseException("Erreur lors de la suppression du calcul de facture", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public List<CalculFacture> findByFactureId(Long factureId) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<CalculFacture> query = em.createQuery(
                "SELECT cf FROM CalculFacture cf WHERE cf.facture.id = :factureId", 
                CalculFacture.class
            );
            query.setParameter("factureId", factureId);
            return query.getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche des calculs de facture par ID facture", e);
            throw new DatabaseException("Erreur lors de la recherche des calculs de facture par ID facture", e);
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