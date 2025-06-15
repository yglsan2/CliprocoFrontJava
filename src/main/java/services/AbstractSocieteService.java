package services;

import dao.IDAO;
import exceptions.DatabaseException;
import java.util.List;

public abstract class AbstractSocieteService<T> {
    protected final IDAO<T> dao;

    protected AbstractSocieteService(IDAO<T> dao) {
        this.dao = dao;
    }

    public T create(T entity) throws DatabaseException {
        dao.save(entity);
        return entity;
    }

    public T update(T entity) throws DatabaseException {
        dao.update(entity);
        return entity;
    }

    public void delete(T entity) throws DatabaseException {
        dao.delete(entity);
    }

    public T findById(Long id) throws DatabaseException {
        return dao.findById(id);
    }

    public List<T> findAll() throws DatabaseException {
        return dao.findAll();
    }

    public List<T> findBySiret(String siret) throws DatabaseException {
        return dao.findBySiret(siret);
    }

    public List<T> findByNom(String nom) throws DatabaseException {
        return dao.findByNom(nom);
    }

    public List<T> findByVille(String ville) throws DatabaseException {
        return dao.findByVille(ville);
    }

    public List<T> findByCodePostal(String codePostal) throws DatabaseException {
        return dao.findByCodePostal(codePostal);
    }
} 