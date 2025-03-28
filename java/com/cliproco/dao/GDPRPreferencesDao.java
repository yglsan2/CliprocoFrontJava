package com.cliproco.dao;

import com.cliproco.model.GDPRPreferences;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Optional;

public class GDPRPreferencesDao extends HibernateDao<GDPRPreferences> {
    
    public GDPRPreferencesDao() {
        super(GDPRPreferences.class);
    }

    public Optional<GDPRPreferences> findByUserId(String userId) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<GDPRPreferences> query = em.createQuery(
                "SELECT g FROM GDPRPreferences g WHERE g.userId = :userId",
                GDPRPreferences.class
            );
            query.setParameter("userId", userId);
            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void deleteByUserId(String userId) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            TypedQuery<GDPRPreferences> query = em.createQuery(
                "DELETE FROM GDPRPreferences g WHERE g.userId = :userId",
                GDPRPreferences.class
            );
            query.setParameter("userId", userId);
            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    public void deleteExpiredConsents() {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            TypedQuery<GDPRPreferences> query = em.createQuery(
                "DELETE FROM GDPRPreferences g WHERE g.lastConsentDate + g.dataRetentionPeriod < CURRENT_TIMESTAMP",
                GDPRPreferences.class
            );
            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }
} 