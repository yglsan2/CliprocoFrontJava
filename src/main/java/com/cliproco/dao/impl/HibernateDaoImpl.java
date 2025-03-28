package com.cliproco.dao.impl;

import com.cliproco.dao.HibernateDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HibernateDaoImpl<T> implements HibernateDao<T> {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    private Session session;
    private Transaction transaction;
    private final Class<T> entityClass;

    public HibernateDaoImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public void beginTransaction() {
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
    }

    @Override
    public void commitTransaction() {
        if (transaction != null) {
            transaction.commit();
        }
    }

    @Override
    public void rollbackTransaction() {
        if (transaction != null) {
            transaction.rollback();
        }
    }

    @Override
    public void closeSession() {
        if (session != null) {
            session.close();
        }
    }

    @Override
    public T create(T entity) {
        try {
            beginTransaction();
            session.persist(entity);
            commitTransaction();
            return entity;
        } catch (Exception e) {
            rollbackTransaction();
            throw e;
        } finally {
            closeSession();
        }
    }

    @Override
    public T update(T entity) {
        try {
            beginTransaction();
            session.merge(entity);
            commitTransaction();
            return entity;
        } catch (Exception e) {
            rollbackTransaction();
            throw e;
        } finally {
            closeSession();
        }
    }

    @Override
    public void delete(T entity) {
        try {
            beginTransaction();
            session.delete(entity);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            throw e;
        } finally {
            closeSession();
        }
    }

    @Override
    public T findById(Long id) {
        try {
            beginTransaction();
            return session.get(entityClass, id);
        } finally {
            closeSession();
        }
    }

    @Override
    public List<T> findAll() {
        try {
            beginTransaction();
            return session.createQuery("from " + entityClass.getName(), entityClass).list();
        } finally {
            closeSession();
        }
    }
} 