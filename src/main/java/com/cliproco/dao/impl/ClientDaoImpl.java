package com.cliproco.dao.impl;

import com.cliproco.dao.ClientDao;
import com.cliproco.models.Client;
import com.cliproco.utils.TransactionUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ClientDaoImpl implements ClientDao {
    private static final String PERSISTENCE_UNIT_NAME = "crm";
    private EntityManagerFactory factory;
    private EntityManager em;

    public ClientDaoImpl() {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = factory.createEntityManager();
    }

    @Override
    public void create(Client client) {
        TransactionUtils.executeInTransaction(em, () -> {
            em.persist(client);
            return null;
        });
    }

    @Override
    public Client findById(Long id) {
        return TransactionUtils.executeInTransaction(em, () -> 
            em.find(Client.class, id)
        );
    }

    @Override
    public List<Client> findAll() {
        return TransactionUtils.executeInTransaction(em, () -> 
            em.createQuery("SELECT c FROM Client c", Client.class)
              .getResultList()
        );
    }

    @Override
    public List<Client> findByNom(String nom) {
        return TransactionUtils.executeInTransaction(em, () -> {
            TypedQuery<Client> query = em.createQuery(
                "SELECT c FROM Client c WHERE c.nom LIKE :nom",
                Client.class
            );
            query.setParameter("nom", "%" + nom + "%");
            return query.getResultList();
        });
    }

    @Override
    public List<Client> findByEntreprise(String entreprise) {
        return TransactionUtils.executeInTransaction(em, () -> {
            TypedQuery<Client> query = em.createQuery(
                "SELECT c FROM Client c WHERE c.entreprise LIKE :entreprise",
                Client.class
            );
            query.setParameter("entreprise", "%" + entreprise + "%");
            return query.getResultList();
        });
    }

    @Override
    public void update(Client client) {
        TransactionUtils.executeInTransaction(em, () -> {
            em.merge(client);
            return null;
        });
    }

    @Override
    public void delete(Long id) {
        TransactionUtils.executeInTransaction(em, () -> {
            Client client = findById(id);
            if (client != null) {
                em.remove(client);
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