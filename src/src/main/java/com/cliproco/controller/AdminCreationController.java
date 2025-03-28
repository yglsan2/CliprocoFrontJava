package com.cliproco.controller;

import com.cliproco.dao.UtilisateurDao;
import com.cliproco.dao.impl.ArrayListUserDaoImpl;
import com.cliproco.model.User;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet("/admin/create")
public class AdminCreationController extends HttpServlet {
    private UtilisateurDao userDao;
    private Argon2 argon2;

    @Override
    public void init() throws ServletException {
        // Initialisation du DAO avec ArrayList
        userDao = new ArrayListUserDaoImpl();

        // Initialisation d'Argon2
        argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Récupération du sel depuis la variable d'environnement
        String appSecret = System.getenv("APP_SECRET");
        if (appSecret == null || appSecret.isEmpty()) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "APP_SECRET non configuré");
            return;
        }

        // Création de l'utilisateur admin
        User admin = new User();
        admin.setUsername("admin");
        
        // Hachage du mot de passe avec Argon2
        String password = "admin123"; // À changer en production
        String hashedPassword = argon2.hash(2, 65536, 1, password.toCharArray());
        admin.setPassword(hashedPassword);
        
        admin.setLastLogin(LocalDateTime.now());

        try {
            userDao.save(admin);
            response.getWriter().write("Administrateur créé avec succès");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur lors de la création de l'administrateur");
        }
    }
} 