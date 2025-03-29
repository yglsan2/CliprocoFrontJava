package com.cliproco.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

@WebFilter("/*")
public class CsrfFilter implements Filter {
    private static final String CSRF_TOKEN_ATTR = "_csrf";
    private static final String CSRF_TOKEN_PARAM = "_csrf";
    private static final String CSRF_TOKEN_HEADER = "X-CSRF-Token";
    private static final int TOKEN_LENGTH = 32;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(true);

        // Générer un nouveau token si nécessaire
        if (session.getAttribute(CSRF_TOKEN_ATTR) == null) {
            String token = generateToken();
            session.setAttribute(CSRF_TOKEN_ATTR, token);
        }

        // Vérifier le token pour les requêtes POST
        if ("POST".equalsIgnoreCase(httpRequest.getMethod())) {
            String sessionToken = (String) session.getAttribute(CSRF_TOKEN_ATTR);
            String requestToken = httpRequest.getParameter(CSRF_TOKEN_PARAM);
            
            // Vérifier aussi le header pour les requêtes AJAX
            if (requestToken == null) {
                requestToken = httpRequest.getHeader(CSRF_TOKEN_HEADER);
            }

            if (sessionToken == null || !sessionToken.equals(requestToken)) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Token CSRF invalide");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[TOKEN_LENGTH];
        secureRandom.nextBytes(token);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialisation si nécessaire
    }

    @Override
    public void destroy() {
        // Nettoyage si nécessaire
    }
} 