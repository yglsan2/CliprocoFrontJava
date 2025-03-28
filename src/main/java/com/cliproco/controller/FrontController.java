package com.cliproco.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/*")
public class FrontController extends HttpServlet {
    private static final Logger logger = Logger.getLogger(FrontController.class.getName());
    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;
    private static final Map<String, String> ADMIN_PATHS = new HashMap<>();

    @Override
    public void init() throws ServletException {
        try {
            // Initialisation de l'EntityManagerFactory
            entityManagerFactory = Persistence.createEntityManagerFactory("cliproco");
            entityManager = entityManagerFactory.createEntityManager();

            // Configuration des chemins admin
            ADMIN_PATHS.put("/client/add", "POST");
            ADMIN_PATHS.put("/client/edit", "POST");
            ADMIN_PATHS.put("/client/delete", "POST");
            ADMIN_PATHS.put("/prospect/add", "POST");
            ADMIN_PATHS.put("/prospect/edit", "POST");
            ADMIN_PATHS.put("/prospect/delete", "POST");
            ADMIN_PATHS.put("/contrat/add", "POST");
            ADMIN_PATHS.put("/contrat/edit", "POST");
            ADMIN_PATHS.put("/contrat/delete", "POST");

            logger.info("FrontController initialisé avec succès");
        } catch (Exception e) {
            logger.severe("Erreur lors de l'initialisation du FrontController : " + e.getMessage());
            throw new ServletException("Erreur d'initialisation", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String path = request.getPathInfo();
        String method = request.getMethod();
        HttpSession session = request.getSession(false);

        // Vérification de l'authentification pour les chemins admin
        if (ADMIN_PATHS.containsKey(path) && ADMIN_PATHS.get(path).equals(method)) {
            if (session == null || session.getAttribute("user") == null) {
                session = request.getSession();
                session.setAttribute("redirectUrl", path);
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }
        }

        // Routage vers les contrôleurs appropriés
        try {
            if (path == null || path.equals("/")) {
                request.getRequestDispatcher("/WEB-INF/JSP/accueil.jsp").forward(request, response);
            } else if (path.startsWith("/client")) {
                request.getRequestDispatcher("/WEB-INF/JSP/client" + path.substring(7) + ".jsp").forward(request, response);
            } else if (path.startsWith("/prospect")) {
                request.getRequestDispatcher("/WEB-INF/JSP/prospect" + path.substring(9) + ".jsp").forward(request, response);
            } else if (path.startsWith("/contrat")) {
                request.getRequestDispatcher("/WEB-INF/JSP/contrat" + path.substring(8) + ".jsp").forward(request, response);
            } else if (path.equals("/login")) {
                request.getRequestDispatcher("/WEB-INF/JSP/login.jsp").forward(request, response);
            } else if (path.equals("/logout")) {
                if (session != null) {
                    session.invalidate();
                }
                response.sendRedirect(request.getContextPath() + "/login");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page non trouvée");
            }
        } catch (Exception e) {
            logger.severe("Erreur lors du traitement de la requête : " + e.getMessage());
            request.setAttribute("error", "Une erreur est survenue lors du traitement de votre requête.");
            request.getRequestDispatcher("/WEB-INF/JSP/erreur.jsp").forward(request, response);
        }
    }

    @Override
    public void destroy() {
        if (entityManager != null) {
            entityManager.close();
        }
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
        logger.info("FrontController détruit avec succès");
    }
} 