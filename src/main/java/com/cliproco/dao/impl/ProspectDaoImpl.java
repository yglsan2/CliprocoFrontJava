package com.cliproco.dao.impl;

import com.cliproco.dao.ProspectDao;
import com.cliproco.models.Prospect;
import com.cliproco.utils.TransactionUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ProspectDaoImpl implements ProspectDao {
    private static final String PERSISTENCE_UNIT_NAME = "crm";
    private EntityManagerFactory factory;
    private EntityManager em;

    public ProspectDaoImpl() {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = factory.createEntityManager();
    }

    @Override
    public void create(Prospect prospect) {
        TransactionUtils.executeInTransaction(em, () -> {
            em.persist(prospect);
            return null;
        });
    }

    @Override
    public Prospect findById(Long id) {
        return TransactionUtils.executeInTransaction(em, () -> 
            em.find(Prospect.class, id)
        );
    }

    @Override
    public List<Prospect> findAll() {
        return TransactionUtils.executeInTransaction(em, () -> 
            em.createQuery("SELECT p FROM Prospect p", Prospect.class)
              .getResultList()
        );
    }

    @Override
    public List<Prospect> findByNom(String nom) {
        return TransactionUtils.executeInTransaction(em, () -> {
            TypedQuery<Prospect> query = em.createQuery(
                "SELECT p FROM Prospect p WHERE p.nom LIKE :nom",
                Prospect.class
            );
            query.setParameter("nom", "%" + nom + "%");
            return query.getResultList();
        });
    }

    @Override
    public List<Prospect> findByEntreprise(String entreprise) {
        return TransactionUtils.executeInTransaction(em, () -> {
            TypedQuery<Prospect> query = em.createQuery(
                "SELECT p FROM Prospect p WHERE p.entreprise LIKE :entreprise",
                Prospect.class
            );
            query.setParameter("entreprise", "%" + entreprise + "%");
            return query.getResultList();
        });
    }

    @Override
    public void update(Prospect prospect) {
        TransactionUtils.executeInTransaction(em, () -> {
            em.merge(prospect);
            return null;
        });
    }

    @Override
    public void delete(Long id) {
        TransactionUtils.executeInTransaction(em, () -> {
            Prospect prospect = findById(id);
            if (prospect != null) {
                em.remove(prospect);
            }
            return null;
        });
    }

    @Override
    public void close() {
        if (em != null) {
            em.close();
        }
        if (factory != null) {
            factory.close();
        }
    }
} 