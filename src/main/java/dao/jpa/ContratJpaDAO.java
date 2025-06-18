package dao.jpa;

import models.Contrat;
import models.Client;
import utilities.LogManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;

public class ContratJpaDAO extends GenericJpaDAO<Contrat, Integer> {
    public ContratJpaDAO() {
        super();
    }

    @Override
    public Optional<Contrat> findById(Integer id) throws ValidationException, DatabaseException {
        return super.findById(id);
    }

    @Override
    public Contrat save(Contrat entity) throws ValidationException, DatabaseException {
        return super.save(entity);
    }

    @Override
    public Contrat update(Contrat entity) throws ValidationException, ResourceNotFoundException, DatabaseException {
        return super.update(entity);
    }

    @Override
    public void delete(Contrat entity) throws ValidationException, ResourceNotFoundException, DatabaseException {
        super.delete(entity);
    }

    public List<Contrat> findByClient(Client client) throws DatabaseException, ValidationException {
        try {
            EntityManager em = getEntityManager();
            TypedQuery<Contrat> query = em.createQuery(
                "SELECT c FROM Contrat c WHERE c.client = :client", Contrat.class);
            query.setParameter("client", client);
            return query.getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche des contrats par client", e);
            throw new DatabaseException("Erreur lors de la recherche des contrats par client", e);
        }
    }

    @Override
    public boolean existsById(Integer id) throws ValidationException, DatabaseException {
        try {
            if (id == null) {
                throw new ValidationException("L'ID ne peut pas être null");
            }
            EntityManager em = getEntityManager();
            TypedQuery<Integer> query = em.createQuery(
                "SELECT COUNT(c) FROM Contrat c WHERE c.id = :id",
                Integer.class
            );
            query.setParameter("id", id);
            return query.getSingleResult() > 0;
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la vérification de l'existence du contrat", e);
            throw new DatabaseException("Erreur lors de la vérification de l'existence du contrat", e);
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