package com.cliproco.dao.impl;

import com.cliproco.dao.ClientDAO;
import com.cliproco.model.Client;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@ApplicationScoped
public class ClientDaoImpl implements ClientDAO {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Client create(Client client) {
        entityManager.persist(client);
        return client;
    }

    @Override
    public Client findById(Long id) {
        return entityManager.find(Client.class, id);
    }

    @Override
    public List<Client> findAll() {
        TypedQuery<Client> query = entityManager.createQuery("SELECT c FROM Client c", Client.class);
        return query.getResultList();
    }

    @Override
    public Client update(Client client) {
        return entityManager.merge(client);
    }

    @Override
    public void delete(Long id) {
        Client client = findById(id);
        if (client != null) {
            entityManager.remove(client);
        }
    }

    @Override
    public List<Client> findByNom(String nom) {
        TypedQuery<Client> query = entityManager.createQuery(
            "SELECT c FROM Client c WHERE c.nom LIKE :nom", Client.class);
        query.setParameter("nom", "%" + nom + "%");
        return query.getResultList();
    }
} 