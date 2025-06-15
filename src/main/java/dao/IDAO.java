package dao;

import exceptions.DatabaseException;
import java.util.List;

public interface IDAO<T> {
    T findById(Long id) throws DatabaseException;
    List<T> findAll() throws DatabaseException;
    void save(T entity) throws DatabaseException;
    void update(T entity) throws DatabaseException;
    void delete(T entity) throws DatabaseException;
} 