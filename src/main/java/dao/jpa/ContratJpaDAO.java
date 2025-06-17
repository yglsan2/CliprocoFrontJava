package dao.jpa;

import models.Contrat;
import utilities.LogManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;

public class ContratJpaDAO extends GenericJpaDAO<Contrat, Long> {
    public ContratJpaDAO() {
        super();
    }

    @Override
    public Optional<Contrat> findById(Long id) throws ValidationException, DatabaseException {
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