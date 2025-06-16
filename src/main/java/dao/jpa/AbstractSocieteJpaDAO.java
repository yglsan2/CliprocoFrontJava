package dao.jpa;

import dao.IDAO;
import models.Societe;
import java.util.Optional;

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
    public Optional<T> findById(ID id) {
        return super.findById(id);
    }

    @Override
    public T save(T entity) {
        return super.save(entity);
    }

    @Override
    public T update(T entity) {
        return super.update(entity);
    }

    @Override
    public void delete(T entity) {
        super.delete(entity);
    }
} 