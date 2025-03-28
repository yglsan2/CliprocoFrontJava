package com.cliproco.dao.impl;

import com.cliproco.dao.UtilisateurDao;
import com.cliproco.model.Utilisateur;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import java.util.List;

public class UtilisateurDaoImpl implements UtilisateurDao {
    private final SessionFactory sessionFactory;

    public UtilisateurDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Utilisateur findByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<Utilisateur> query = session.createQuery(
                "FROM Utilisateur WHERE email = :email", Utilisateur.class);
            query.setParameter("email", email);
            return query.uniqueResult();
        }
    }

    @Override
    public List<Utilisateur> findByRole(Utilisateur.Role role) {
        try (Session session = sessionFactory.openSession()) {
            Query<Utilisateur> query = session.createQuery(
                "FROM Utilisateur WHERE role = :role", Utilisateur.class);
            query.setParameter("role", role);
            return query.list();
        }
    }

    @Override
    public void updateDernierLogin(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query = session.createQuery(
                "UPDATE Utilisateur SET dernierLogin = CURRENT_TIMESTAMP WHERE id = :id");
            query.setParameter("id", id);
            query.executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public void save(Utilisateur entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Utilisateur entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Utilisateur entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(entity);
            session.getTransaction().commit();
        }
    }

    @Override
    public Utilisateur findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Utilisateur.class, id);
        }
    }

    @Override
    public List<Utilisateur> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Utilisateur> query = session.createQuery(
                "FROM Utilisateur", Utilisateur.class);
            return query.list();
        }
    }
} 