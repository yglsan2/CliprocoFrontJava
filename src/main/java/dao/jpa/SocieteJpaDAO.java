package dao.jpa;

import dao.IDAO;
import models.Societe;
import java.util.Optional;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import utilities.LogManager;

/**
 * Implémentation JPA du DAO pour les sociétés.
 */
public class SocieteJpaDAO extends AbstractJpaDAO<Societe, Long> implements IDAO<Societe, Long> {
    
    public SocieteJpaDAO() {
        super(Societe.class);
    }

    @Override
    public Optional<Societe> findById(Long id) throws ValidationException, DatabaseException {
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