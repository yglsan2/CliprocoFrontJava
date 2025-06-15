opackage dao.jpa;

import dao.IDAO;
import exceptions.DatabaseException;
import models.Prospect;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import utilities.LogManager;

/**
 * Implémentation JPA du DAO Prospect.
 */
public class ProspectJpaDAO extends GenericJpaDAO<Prospect> implements IDAO<Prospect> {

    public ProspectJpaDAO() {
        super();
    }

    @Override
    public List<Prospect> findAll() throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Prospect> query = em.createQuery("SELECT p FROM Prospect p", Prospect.class);
            return query.getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la récupération de tous les prospects", e);
            throw new DatabaseException("Erreur lors de la récupération de tous les prospects", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public List<Prospect> findBySocieteId(Long societeId) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Prospect> query = em.createQuery(
                "SELECT p FROM Prospect p WHERE p.societe.id = :societeId", 
                Prospect.class
            );
            query.setParameter("societeId", societeId);
            return query.getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la récupération des prospects par société", e);
            throw new DatabaseException("Erreur lors de la récupération des prospects par société", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

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

    public void save(Prospect prospect) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(prospect);
            em.getTransaction().commit();
            LogManager.info("Prospect sauvegardé avec succès: " + prospect.getIdentifiant());
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la sauvegarde du prospect", e);
            throw new DatabaseException("Erreur lors de la sauvegarde du prospect", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
} 