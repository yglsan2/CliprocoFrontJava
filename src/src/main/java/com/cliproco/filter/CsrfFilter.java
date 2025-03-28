package com.cliproco.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.UUID;

@WebFilter("/*")
public class CsrfFilter implements Filter {
    private static final String CSRF_TOKEN_ATTRIBUTE = "csrfToken";
    private static final String CSRF_TOKEN_HEADER = "X-CSRF-Token";
    private static final String CSRF_TOKEN_PARAM = "_csrf";

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

        // Vérification si c'est une requête POST
        if ("POST".equalsIgnoreCase(httpRequest.getMethod())) {
            if (session == null) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Session invalide");
                return;
            }

            String sessionToken = (String) session.getAttribute(CSRF_TOKEN_ATTRIBUTE);
            String requestToken = httpRequest.getHeader(CSRF_TOKEN_HEADER);
            
            // Si pas de token dans le header, vérifier dans les paramètres
            if (requestToken == null) {
                requestToken = httpRequest.getParameter(CSRF_TOKEN_PARAM);
            }

            if (sessionToken == null || !sessionToken.equals(requestToken)) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Token CSRF invalide");
                return;
            }
        }

        // Génération d'un nouveau token si nécessaire
        if (session != null && session.getAttribute(CSRF_TOKEN_ATTRIBUTE) == null) {
            String token = UUID.randomUUID().toString();
            session.setAttribute(CSRF_TOKEN_ATTRIBUTE, token);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Nettoyage du filtre
    }
} 