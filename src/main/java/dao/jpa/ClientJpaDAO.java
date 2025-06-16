package dao.jpa;

import dao.IDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import models.Client;
import utilities.JPAUtil;
import utilities.LogManager;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation JPA du DAO pour les clients.
 */
public class ClientJpaDAO extends AbstractJpaDAO<Client, Long> implements IDAO<Client, Long> {
    
    public ClientJpaDAO() {
        super(Client.class);
    }

    @Override
    public Optional<Client> findById(Long id) {
        return super.findById(id);
    }

    @Override
    public Client save(Client client) {
        return super.save(client);
    }

    @Override
    public Client update(Client client) {
        return super.update(client);
    }

    @Override
    public void delete(Client client) {
        super.delete(client);
    }

    public List<Client> findByRaisonSociale(String raisonSociale) {
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
            throw new RuntimeException("Erreur lors de la recherche par raison sociale", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public boolean existsByRaisonSociale(String raisonSociale) {
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
            throw new RuntimeException("Erreur lors de la vérification de l'existence", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
} 