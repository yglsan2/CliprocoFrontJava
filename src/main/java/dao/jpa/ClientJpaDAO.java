package dao.jpa;

import dao.SocieteDatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import models.Client;
import utilities.JPAUtil;

import java.util.List;

public class ClientJpaDAO extends GenericJpaDAO<Client> {
    
    public List<Client> findByRaisonSociale(String raisonSociale) throws SocieteDatabaseException {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            TypedQuery<Client> query = em.createQuery(
                "SELECT c FROM Client c WHERE c.raisonSociale LIKE :raisonSociale",
                Client.class
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
                "SELECT COUNT(c) FROM Client c WHERE c.raisonSociale = :raisonSociale",
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