package com.cliproco.controller;

import com.cliproco.dao.UtilisateurDao;
import com.cliproco.dao.impl.ArrayListUserDaoImpl;
import com.cliproco.model.User;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@WebServlet("/login")
public class LoginController extends HttpServlet {
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
        // Vérification du cookie remember me
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("rememberToken".equals(cookie.getName())) {
                    User user = userDao.findByRememberToken(cookie.getValue());
                    if (user != null) {
                        HttpSession session = request.getSession();
                        session.setAttribute("user", user);
                        String redirectUrl = request.getParameter("redirectUrl");
                        if (redirectUrl != null && !redirectUrl.isEmpty()) {
                            response.sendRedirect(redirectUrl);
                        } else {
                            response.sendRedirect(request.getContextPath() + "/");
                        }
                        return;
                    }
                }
            }
        }

        // Si pas de cookie valide, afficher la page de connexion
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean rememberMe = "on".equals(request.getParameter("rememberMe"));

        User user = userDao.findByUsername(username);
        if (user != null && argon2.verify(user.getPassword(), password.toCharArray())) {
            // Mise à jour du dernier login
            user.setLastLogin(LocalDateTime.now());
            userDao.updateLastLogin(user.getId());

            // Création de la session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // Gestion du remember me
            if (rememberMe) {
                String rememberToken = UUID.randomUUID().toString();
                user.setRememberToken(rememberToken);
                userDao.update(user);

                Cookie cookie = new Cookie("rememberToken", rememberToken);
                cookie.setMaxAge(30 * 24 * 60 * 60); // 30 jours
                cookie.setHttpOnly(true);
                cookie.setSecure(true);
                response.addCookie(cookie);
            }

            // Redirection vers la page demandée ou la page d'accueil
            String redirectUrl = request.getParameter("redirectUrl");
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                response.sendRedirect(redirectUrl);
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }
        } else {
            request.setAttribute("error", "Identifiants invalides");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
} 