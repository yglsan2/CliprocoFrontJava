package com.cliproco.filter;

import com.cliproco.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebFilter("/*")
public class RememberMeFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // Si l'utilisateur n'est pas connecté, vérifier le cookie remember me
        if (session == null || session.getAttribute("user") == null) {
            Cookie[] cookies = httpRequest.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("remember_token".equals(cookie.getName())) {
                        String token = cookie.getValue();
                        
                        // Vérifier le token dans la base de données
                        EntityManagerFactory emf = Persistence.createEntityManagerFactory("cliproco");
                        EntityManager em = emf.createEntityManager();

                        try {
                            List<User> users = em.createQuery("SELECT u FROM User u WHERE u.rememberToken = :token", User.class)
                                    .setParameter("token", token)
                                    .getResultList();

                            if (!users.isEmpty()) {
                                // Créer une nouvelle session
                                session = httpRequest.getSession();
                                session.setAttribute("user", users.get(0).getUser());
                            }
                        } finally {
                            em.close();
                            emf.close();
                        }
                        break;
                    }
                }
            }
        }

        chain.doFilter(request, response);
    }
} 