package dao.jpa;

import models.CalculFacture;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;
import dao.IDAO;
import dao.SocieteDatabaseException;

public class CalculFactureJpaDAO extends GenericJpaDAO<CalculFacture> implements IDAO<CalculFacture> {
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public CalculFactureJpaDAO() {
        this.emf = Persistence.createEntityManagerFactory("cliproco");
        this.em = emf.createEntityManager();
    }

    @Override
    public CalculFacture findById(Long id) throws SocieteDatabaseException {
        try {
            return em.find(CalculFacture.class, id);
        } catch (Exception e) {
            throw new SocieteDatabaseException("Erreur lors de la recherche du calcul de facture par ID", e);
        }
    }

    @Override
    public List<CalculFacture> findAll() throws SocieteDatabaseException {
        try {
            TypedQuery<CalculFacture> query = em.createQuery("SELECT cf FROM CalculFacture cf", CalculFacture.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new SocieteDatabaseException("Erreur lors de la récupération de tous les calculs de facture", e);
        }
    }

    @Override
    public void save(CalculFacture calculFacture) throws SocieteDatabaseException {
        try {
            em.getTransaction().begin();
            em.persist(calculFacture);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SocieteDatabaseException("Erreur lors de la sauvegarde du calcul de facture", e);
        }
    }

    @Override
    public void update(CalculFacture calculFacture) throws SocieteDatabaseException {
        try {
            em.getTransaction().begin();
            em.merge(calculFacture);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SocieteDatabaseException("Erreur lors de la mise à jour du calcul de facture", e);
        }
    }

    @Override
    public void delete(CalculFacture calculFacture) throws SocieteDatabaseException {
        try {
            em.getTransaction().begin();
            em.remove(calculFacture);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SocieteDatabaseException("Erreur lors de la suppression du calcul de facture", e);
        }
    }

    public List<CalculFacture> findByFactureId(Long factureId) throws SocieteDatabaseException {
        try {
            TypedQuery<CalculFacture> query = em.createQuery(
                "SELECT cf FROM CalculFacture cf WHERE cf.facture.id = :factureId", 
                CalculFacture.class
            );
            query.setParameter("factureId", factureId);
            return query.getResultList();
        } catch (Exception e) {
            throw new SocieteDatabaseException("Erreur lors de la recherche des calculs de facture par ID facture", e);
        }
    }

    public void close() {
        em.close();
        emf.close();
    }
} 