package dao.jpa;

import dao.IDAO;
import exceptions.DatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import models.Prospect;
import utilities.JPAUtil;
import utilities.LogManager;

import java.util.List;

public class ProspectJpaDAO extends GenericJpaDAO<Prospect> implements IDAO<Prospect> {
    
    public List<Prospect> findByRaisonSociale(String raisonSociale) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Prospect> query = em.createQuery(
                "SELECT p FROM Prospect p WHERE p.raisonSociale LIKE :raisonSociale",
                Prospect.class
            );
            query.setParameter("raisonSociale", "%" + raisonSociale + "%");
            return query.getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche par raison sociale", e);
            throw new DatabaseException("Erreur lors de la recherche par raison sociale", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public boolean existsByRaisonSociale(String raisonSociale) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(p) FROM Prospect p WHERE p.raisonSociale = :raisonSociale",
                Long.class
            );
            query.setParameter("raisonSociale", raisonSociale);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la vérification de l'existence", e);
            throw new DatabaseException("Erreur lors de la vérification de l'existence", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
} 