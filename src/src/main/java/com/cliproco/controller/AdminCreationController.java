package com.cliproco.controller;

import com.cliproco.model.User;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/createAdmin")
public class AdminCreationController extends HttpServlet {
    private Argon2 argon2;

    @Override
    public void init() throws ServletException {
        argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Récupérer le sel depuis la variable d'environnement
        String appSecret = System.getenv("APP_SECRET");
        if (appSecret == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Variable d'environnement APP_SECRET non définie");
            return;
        }

        // Créer l'utilisateur admin
        User admin = new User();
        admin.setUser("admin");
        
        // Hasher le mot de passe avec Argon2
        String password = "admin123"; // À changer en production
        String hashedPassword = argon2.hash(2, 65536, 1, password.toCharArray());
        admin.setPwd(hashedPassword);

        // Sauvegarder l'utilisateur dans la base de données
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("cliproco");
        EntityManager em = emf.createEntityManager();
        
        try {
            em.getTransaction().begin();
            em.persist(admin);
            em.getTransaction().commit();
            response.getWriter().write("Administrateur créé avec succès");
        } catch (Exception e) {
            em.getTransaction().rollback();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur lors de la création de l'administrateur");
        } finally {
            em.close();
            emf.close();
        }
    }

    @Override
    public void destroy() {
        if (argon2 != null) {
            argon2.wipeArray(new char[0]);
        }
    }
} 