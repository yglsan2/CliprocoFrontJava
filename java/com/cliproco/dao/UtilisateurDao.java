package com.cliproco.dao;

import com.cliproco.model.Utilisateur;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class UtilisateurDao extends HibernateDao<Utilisateur> {
    public UtilisateurDao() {
        super(Utilisateur.class);
    }

    public Utilisateur findByEmail(String email) {
        try {
            TypedQuery<Utilisateur> query = em.createQuery(
                "SELECT u FROM Utilisateur u WHERE u.email = :email",
                Utilisateur.class
            );
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Utilisateur> findByRole(String role) {
        TypedQuery<Utilisateur> query = em.createQuery(
            "SELECT u FROM Utilisateur u WHERE u.role = :role",
            Utilisateur.class
        );
        query.setParameter("role", role);
        return query.getResultList();
    }
} 