package dao.jpa;

import dao.SocieteDatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import models.Prospect;
import utilities.JPAUtil;

import java.util.List;

public class ProspectJpaDAO extends GenericJpaDAO<Prospect> {
    
    public List<Prospect> findByRaisonSociale(String raisonSociale) throws SocieteDatabaseException {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Prospect> query = em.createQuery(
                "SELECT p FROM Prospect p WHERE p.raisonSociale LIKE :raisonSociale",
                Prospect.class
            );
            query.setParameter("raisonSociale", "%" + raisonSociale + "%");
            return query.getResultList();
        } catch (Exception e) {
            throw new SocieteDatabaseException("Erreur lors de la recherche par raison sociale", e);
        } finally {
            em.close();
        }
    }
    
    public boolean existsByRaisonSociale(String raisonSociale) throws SocieteDatabaseException {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(p) FROM Prospect p WHERE p.raisonSociale = :raisonSociale",
                Long.class
            );
            query.setParameter("raisonSociale", raisonSociale);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            throw new SocieteDatabaseException("Erreur lors de la vérification de l'existence", e);
        } finally {
            em.close();
        }
    }
} 