package com.cliproco.filter;

import com.cliproco.dao.UtilisateurDao;
import com.cliproco.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebFilter("/*")
public class RememberMeFilter implements Filter {
    private static final String REMEMBER_ME_COOKIE = "rememberMe";
    private UtilisateurDao userDao;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        userDao = new ArrayListUserDaoImpl();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // Si l'utilisateur n'est pas connecté, vérifier le cookie remember-me
        if (session == null || session.getAttribute("user") == null) {
            Cookie[] cookies = httpRequest.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (REMEMBER_ME_COOKIE.equals(cookie.getName())) {
                        String token = cookie.getValue();
                        User user = userDao.findByRememberToken(token);
                        if (user != null) {
                            // Créer une nouvelle session et connecter l'utilisateur
                            session = httpRequest.getSession(true);
                            session.setAttribute("user", user);
                            session.setAttribute("username", user.getUser());
                            break;
                        }
                    }
                }
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Nettoyage des ressources si nécessaire
    }
} 