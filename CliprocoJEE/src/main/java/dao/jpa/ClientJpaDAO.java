package dao.jpa;

import dao.IDAO;
import exceptions.DatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import models.Client;
import utilities.JPAUtil;
import utilities.LogManager;

import java.util.List;

public class ClientJpaDAO extends GenericJpaDAO<Client> implements IDAO<Client> {
    
    public List<Client> findByRaisonSociale(String raisonSociale) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Client> query = em.createQuery(
                "SELECT c FROM Client c WHERE c.raisonSociale LIKE :raisonSociale",
                Client.class
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
                "SELECT COUNT(c) FROM Client c WHERE c.raisonSociale = :raisonSociale",
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