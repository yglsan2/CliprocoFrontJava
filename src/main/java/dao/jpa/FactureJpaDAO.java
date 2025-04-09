package dao.jpa;

import models.Facture;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;
import dao.IDAO;
import dao.SocieteDatabaseException;

public class FactureJpaDAO extends GenericJpaDAO<Facture> implements IDAO<Facture> {
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public FactureJpaDAO() {
        this.emf = Persistence.createEntityManagerFactory("cliproco");
        this.em = emf.createEntityManager();
    }

    @Override
    public Facture findById(Long id) throws SocieteDatabaseException {
        try {
            return em.find(Facture.class, id);
        } catch (Exception e) {
            throw new SocieteDatabaseException("Erreur lors de la recherche de la facture par ID", e);
        }
    }

    @Override
    public List<Facture> findAll() throws SocieteDatabaseException {
        try {
            TypedQuery<Facture> query = em.createQuery("SELECT f FROM Facture f", Facture.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new SocieteDatabaseException("Erreur lors de la récupération de toutes les factures", e);
        }
    }

    @Override
    public void save(Facture facture) throws SocieteDatabaseException {
        try {
            em.getTransaction().begin();
            em.persist(facture);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SocieteDatabaseException("Erreur lors de la sauvegarde de la facture", e);
        }
    }

    @Override
    public void update(Facture facture) throws SocieteDatabaseException {
        try {
            em.getTransaction().begin();
            em.merge(facture);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SocieteDatabaseException("Erreur lors de la mise à jour de la facture", e);
        }
    }

    @Override
    public void delete(Facture facture) throws SocieteDatabaseException {
        try {
            em.getTransaction().begin();
            em.remove(facture);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SocieteDatabaseException("Erreur lors de la suppression de la facture", e);
        }
    }

    public List<Facture> findByClientId(Long clientId) throws SocieteDatabaseException {
        try {
            TypedQuery<Facture> query = em.createQuery(
                "SELECT f FROM Facture f WHERE f.client.id = :clientId", 
                Facture.class
            );
            query.setParameter("clientId", clientId);
            return query.getResultList();
        } catch (Exception e) {
            throw new SocieteDatabaseException("Erreur lors de la recherche des factures par ID client", e);
        }
    }

    public List<Facture> findUnpaid() throws SocieteDatabaseException {
        try {
            TypedQuery<Facture> query = em.createQuery(
                "SELECT f FROM Facture f WHERE f.payee = false", 
                Facture.class
            );
            return query.getResultList();
        } catch (Exception e) {
            throw new SocieteDatabaseException("Erreur lors de la recherche des factures non payées", e);
        }
    }

    public Facture findByNumeroFacture(String numeroFacture) throws SocieteDatabaseException {
        try {
            TypedQuery<Facture> query = em.createQuery(
                "SELECT f FROM Facture f WHERE f.numeroFacture = :numeroFacture", 
                Facture.class
            );
            query.setParameter("numeroFacture", numeroFacture);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new SocieteDatabaseException("Erreur lors de la recherche de la facture par numéro", e);
        }
    }

    public void close() {
        em.close();
        emf.close();
    }
} 