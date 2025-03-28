package com.cliproco.dao;

import com.cliproco.model.Prospect;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ProspectDao extends HibernateDao<Prospect> {
    public ProspectDao() {
        super(Prospect.class);
    }

    public List<Prospect> findByStatut(String statut) {
        TypedQuery<Prospect> query = em.createQuery(
            "SELECT p FROM Prospect p WHERE p.statut = :statut",
            Prospect.class
        );
        query.setParameter("statut", statut);
        return query.getResultList();
    }

    public List<Prospect> findBySecteurActivite(String secteurActivite) {
        TypedQuery<Prospect> query = em.createQuery(
            "SELECT p FROM Prospect p WHERE p.secteurActivite = :secteurActivite",
            Prospect.class
        );
        query.setParameter("secteurActivite", secteurActivite);
        return query.getResultList();
    }
} 