package com.cliproco.controller;

import java.io.IOException;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Si l'utilisateur est déjà connecté, rediriger vers la page d'accueil
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/accueil");
            return;
        }

        // Sinon, afficher la page de connexion
        request.getRequestDispatcher("/WEB-INF/JSP/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");

        // Vérifier le token CSRF
        String csrfToken = request.getParameter("csrfToken");
        String sessionToken = (String) request.getSession().getAttribute("csrfToken");
        
        if (csrfToken == null || !csrfToken.equals(sessionToken)) {
            logger.warning("Tentative de connexion avec un token CSRF invalide");
            request.setAttribute("error", "Session expirée. Veuillez réessayer.");
            request.getRequestDispatcher("/WEB-INF/JSP/login.jsp").forward(request, response);
            return;
        }

        // TODO: Implémenter la vérification des identifiants avec la base de données
        // Pour l'instant, on accepte n'importe quel identifiant
        if (username != null && password != null && !username.isEmpty() && !password.isEmpty()) {
            HttpSession session = request.getSession();
            session.setAttribute("user", username);
            session.setMaxInactiveInterval(30 * 60); // 30 minutes

            // Si "Se souvenir de moi" est coché, créer un cookie
            if ("on".equals(rememberMe)) {
                Cookie userCookie = new Cookie("user", username);
                userCookie.setMaxAge(7 * 24 * 60 * 60); // 7 jours
                userCookie.setPath("/");
                userCookie.setHttpOnly(true);
                userCookie.setSecure(true);
                response.addCookie(userCookie);
            }

            logger.info("Utilisateur " + username + " connecté avec succès");
            response.sendRedirect(request.getContextPath() + "/accueil");
        } else {
            logger.warning("Tentative de connexion avec des identifiants vides");
            request.setAttribute("error", "Veuillez entrer vos identifiants.");
            request.getRequestDispatcher("/WEB-INF/JSP/login.jsp").forward(request, response);
        }
    }
} 