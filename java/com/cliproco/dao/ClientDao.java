package com.cliproco.dao;

import com.cliproco.model.Client;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ClientDao extends HibernateDao<Client> {
    public ClientDao() {
        super(Client.class);
    }

    public List<Client> findBySecteurActivite(String secteurActivite) {
        TypedQuery<Client> query = em.createQuery(
            "SELECT c FROM Client c WHERE c.secteurActivite = :secteurActivite",
            Client.class
        );
        query.setParameter("secteurActivite", secteurActivite);
        return query.getResultList();
    }

    public List<Client> findByChiffreAffairesMinimal(double chiffreAffaires) {
        TypedQuery<Client> query = em.createQuery(
            "SELECT c FROM Client c WHERE c.chiffreAffaires >= :chiffreAffaires",
            Client.class
        );
        query.setParameter("chiffreAffaires", chiffreAffaires);
        return query.getResultList();
    }
} 