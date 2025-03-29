package com.cliproco.controller;

import com.cliproco.form.UserForm;
import com.cliproco.model.User;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    private Argon2 argon2;

    @Override
    public void init() throws ServletException {
        argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserForm form = new UserForm(request);

        if (!form.validate()) {
            response.sendRedirect("login.jsp?error=true");
            return;
        }

        // Vérifier les identifiants dans la base de données
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("cliproco");
        EntityManager em = emf.createEntityManager();

        try {
            List<User> users = em.createQuery("SELECT u FROM User u WHERE u.user = :user", User.class)
                    .setParameter("user", form.getUser())
                    .getResultList();

            if (users.isEmpty() || !argon2.verify(users.get(0).getPwd(), form.getPwd().toCharArray())) {
                response.sendRedirect("login.jsp?error=true");
                return;
            }

            // Créer la session
            HttpSession session = request.getSession();
            form.setSession(session);

            // Gérer "Se souvenir de moi"
            if (form.isRemember()) {
                String token = java.util.UUID.randomUUID().toString();
                Cookie cookie = new Cookie("remember_token", token);
                cookie.setMaxAge(30 * 24 * 60 * 60); // 30 jours
                response.addCookie(cookie);
            }

            // Rediriger vers la page demandée ou la page d'accueil
            String redirectUrl = (String) session.getAttribute("redirectUrl");
            if (redirectUrl != null) {
                session.removeAttribute("redirectUrl");
                response.sendRedirect(redirectUrl);
            } else {
                response.sendRedirect("index.jsp");
            }

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