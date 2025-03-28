package com.cliproco.dao.impl;

import com.cliproco.dao.ArrayListDao;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ArrayListDaoImpl<T> implements ArrayListDao<T> {
    private final List<T> entities;
    private final AtomicLong idGenerator;

    public ArrayListDaoImpl() {
        this.entities = new ArrayList<>();
        this.idGenerator = new AtomicLong(1);
    }

    @Override
    public void save(T entity) {
        entities.add(entity);
    }

    @Override
    public void update(T entity) {
        int index = entities.indexOf(entity);
        if (index != -1) {
            entities.set(index, entity);
        }
    }

    @Override
    public void delete(T entity) {
        entities.remove(entity);
    }

    @Override
    public T findById(Long id) {
        return entities.stream()
                .filter(entity -> {
                    try {
                        return id.equals(entity.getClass().getMethod("getId").invoke(entity));
                    } catch (Exception e) {
                        return false;
                    }
                })
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(entities);
    }

    protected Long generateId() {
        return idGenerator.getAndIncrement();
    }
} 