package com.cliproco.dao.impl;

import com.cliproco.dao.ProspectDAO;
import com.cliproco.model.Prospect;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@ApplicationScoped
public class ProspectDaoImpl implements ProspectDAO {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Prospect create(Prospect prospect) {
        entityManager.persist(prospect);
        return prospect;
    }

    @Override
    public Prospect findById(Long id) {
        return entityManager.find(Prospect.class, id);
    }

    @Override
    public List<Prospect> findAll() {
        TypedQuery<Prospect> query = entityManager.createQuery("SELECT p FROM Prospect p", Prospect.class);
        return query.getResultList();
    }

    @Override
    public Prospect update(Prospect prospect) {
        return entityManager.merge(prospect);
    }

    @Override
    public void delete(Long id) {
        Prospect prospect = findById(id);
        if (prospect != null) {
            entityManager.remove(prospect);
        }
    }

    @Override
    public List<Prospect> findByNom(String nom) {
        TypedQuery<Prospect> query = entityManager.createQuery(
            "SELECT p FROM Prospect p WHERE p.nom LIKE :nom", Prospect.class);
        query.setParameter("nom", "%" + nom + "%");
        return query.getResultList();
    }
} 