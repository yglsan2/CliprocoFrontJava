package com.cliproco.dao.impl;

import com.cliproco.dao.ProspectDao;
import com.cliproco.model.Prospect;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProspectDaoImpl extends HibernateDaoImpl<Prospect> implements ProspectDao {

    public ProspectDaoImpl() {
        super(Prospect.class);
    }

    @Override
    public List<Prospect> findByStatut(Prospect.Statut statut) {
        try {
            beginTransaction();
            return session.createQuery("from Prospect p where p.statut = :statut", Prospect.class)
                    .setParameter("statut", statut)
                    .list();
        } finally {
            closeSession();
        }
    }

    @Override
    public Prospect findByEmail(String email) {
        try {
            beginTransaction();
            return session.createQuery("from Prospect p where p.email = :email", Prospect.class)
                    .setParameter("email", email)
                    .uniqueResult();
        } finally {
            closeSession();
        }
    }

    @Override
    public List<Prospect> findBySecteurActivite(String secteurActivite) {
        try {
            beginTransaction();
            return session.createQuery("from Prospect p where p.secteurActivite = :secteur", Prospect.class)
                    .setParameter("secteur", secteurActivite)
                    .list();
        } finally {
            closeSession();
        }
    }
} 