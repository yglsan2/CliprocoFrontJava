package dao.jpa;

import dao.IDAO;
import models.Societe;
import java.util.Optional;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;

/**
 * Classe abstraite fournissant une implémentation générique des opérations CRUD avec JPA pour les sociétés.
 * @param <T> Le type de société (Client ou Prospect)
 * @param <ID> Le type de l'identifiant
 */
public abstract class AbstractSocieteJpaDAO<T extends Societe, ID> extends AbstractJpaDAO<T, ID> implements IDAO<T, ID> {
    
    protected AbstractSocieteJpaDAO(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    public Optional<T> findById(ID id) throws ValidationException, DatabaseException {
        return super.findById(id);
    }

    @Override
    public T save(T entity) throws ValidationException, DatabaseException {
        return super.save(entity);
    }

    @Override
    public T update(T entity) throws ValidationException, ResourceNotFoundException, DatabaseException {
        return super.update(entity);
    }

    @Override
    public void delete(T entity) throws ValidationException, ResourceNotFoundException, DatabaseException {
        super.delete(entity);
    }
} 