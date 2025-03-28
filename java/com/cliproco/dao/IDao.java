package com.cliproco.dao;

import java.util.List;

public interface IDao<T> {
    T findById(Long id);
    List<T> findAll();
    void create(T entity);
    void update(T entity);
    void delete(Long id);
} 