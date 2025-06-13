package dao;

import java.util.List;

public interface IDAO<T> {
    T findById(Long id) throws SocieteDatabaseException;
    List<T> findAll() throws SocieteDatabaseException;
    void save(T entity) throws SocieteDatabaseException;
    void update(T entity) throws SocieteDatabaseException;
    void delete(T entity) throws SocieteDatabaseException;
} 