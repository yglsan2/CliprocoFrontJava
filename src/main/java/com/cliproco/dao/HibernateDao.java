package com.cliproco.dao;

import java.util.List;

public interface HibernateDao<T> {
    T create(T entity);
    T update(T entity);
    void delete(T entity);
    T findById(Long id);
    List<T> findAll();
    void beginTransaction();
    void commitTransaction();
    void rollbackTransaction();
    void closeSession();
} 