package dao.jpa;

import dao.IDAO;
import models.Societe;
import java.util.Optional;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import utilities.LogManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Implémentation JPA du DAO pour les sociétés.
 */
public class SocieteJpaDAO extends AbstractJpaDAO<Societe, Integer> implements IDAO<Societe, Integer> {
    
    public SocieteJpaDAO() {
        super(Societe.class);
    }

    @Override
    public Optional<Societe> findById(Integer id) throws ValidationException, DatabaseException {
        return super.findById(id);
    }

    @Override
    public Societe save(Societe societe) throws ValidationException, DatabaseException {
        return super.save(societe);
    }

    @Override
    public Societe update(Societe societe) throws ValidationException, ResourceNotFoundException, DatabaseException {
        return super.update(societe);
    }

    @Override
    public void delete(Societe societe) throws ValidationException, ResourceNotFoundException, DatabaseException {
        super.delete(societe);
    }

    public Optional<Societe> findByRaisonSociale(String raisonSociale) throws DatabaseException, ValidationException {
        try {
            EntityManager em = getEntityManager();
            TypedQuery<Societe> query = em.createQuery(
                "SELECT s FROM Societe s WHERE s.raisonSociale = :raisonSociale", Societe.class);
            query.setParameter("raisonSociale", raisonSociale);
            List<Societe> results = query.getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche de la société par raison sociale", e);
            throw new DatabaseException("Erreur lors de la recherche de la société par raison sociale", e);
        }
    }

    public boolean existsByRaisonSociale(String raisonSociale) throws DatabaseException, ValidationException {
        try {
            EntityManager em = getEntityManager();
            TypedQuery<Integer> query = em.createQuery(
                "SELECT COUNT(s) FROM Societe s WHERE s.raisonSociale = :raisonSociale", Integer.class);
            query.setParameter("raisonSociale", raisonSociale);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la vérification de l'existence de la société par raison sociale", e);
            throw new DatabaseException("Erreur lors de la vérification de l'existence de la société par raison sociale", e);
        }
    }

    @Override
    public void close() throws DatabaseException {
        try {
            getEntityManager().close();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la fermeture des ressources", e);
            throw new DatabaseException("Erreur lors de la fermeture des ressources", e);
        }
    }
} 