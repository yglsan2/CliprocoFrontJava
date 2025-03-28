package com.cliproco.dao;

import com.cliproco.model.Utilisateur;
import java.util.List;

public interface UtilisateurDao extends HibernateDao<Utilisateur> {
    Utilisateur findByEmail(String email);
    List<Utilisateur> findByRole(Utilisateur.Role role);
    void updateDernierLogin(Long id);
} 