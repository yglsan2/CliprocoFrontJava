package com.cliproco.dao.impl;

import com.cliproco.dao.ClientDao;
import com.cliproco.model.Client;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClientDaoImpl extends HibernateDaoImpl<Client> implements ClientDao {

    public ClientDaoImpl() {
        super(Client.class);
    }

    @Override
    public List<Client> findBySecteurActivite(String secteurActivite) {
        try {
            beginTransaction();
            return session.createQuery("from Client c where c.secteurActivite = :secteur", Client.class)
                    .setParameter("secteur", secteurActivite)
                    .list();
        } finally {
            closeSession();
        }
    }

    @Override
    public Client findByEmail(String email) {
        try {
            beginTransaction();
            return session.createQuery("from Client c where c.email = :email", Client.class)
                    .setParameter("email", email)
                    .uniqueResult();
        } finally {
            closeSession();
        }
    }

    @Override
    public List<Client> findByChiffreAffairesMin(double chiffreAffaires) {
        try {
            beginTransaction();
            return session.createQuery("from Client c where c.chiffreAffaires >= :chiffre", Client.class)
                    .setParameter("chiffre", chiffreAffaires)
                    .list();
        } finally {
            closeSession();
        }
    }
} 