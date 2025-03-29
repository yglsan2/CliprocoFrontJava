package com.cliproco.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/admin/*")
public class AuthFilter implements Filter {
    private static final String USER_SESSION_ATTRIBUTE = "user";
    private static final String LOGIN_PAGE = "/login.jsp";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialisation du filtre
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // Vérifier si l'utilisateur est authentifié
        if (session == null || session.getAttribute(USER_SESSION_ATTRIBUTE) == null) {
            // Rediriger vers la page de connexion
            httpResponse.sendRedirect(httpRequest.getContextPath() + LOGIN_PAGE);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Nettoyage du filtre
    }
} 