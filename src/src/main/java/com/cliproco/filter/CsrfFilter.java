package com.cliproco.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class CsrfFilter implements Filter {
    private static final String CSRF_TOKEN_ATTRIBUTE = "csrfToken";
    private static final String CSRF_HEADER = "X-CSRF-Token";
    private static final String CSRF_PARAM = "_csrf";

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

        // Vérifier si c'est une requête POST
        if ("POST".equalsIgnoreCase(httpRequest.getMethod())) {
            if (session == null) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Session invalide");
                return;
            }

            String sessionToken = (String) session.getAttribute(CSRF_TOKEN_ATTRIBUTE);
            String requestToken = httpRequest.getHeader(CSRF_HEADER);
            
            if (requestToken == null) {
                requestToken = httpRequest.getParameter(CSRF_PARAM);
            }

            if (sessionToken == null || !sessionToken.equals(requestToken)) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Token CSRF invalide");
                return;
            }
        }

        // Générer un nouveau token si nécessaire
        if (session != null && session.getAttribute(CSRF_TOKEN_ATTRIBUTE) == null) {
            String token = generateToken();
            session.setAttribute(CSRF_TOKEN_ATTRIBUTE, token);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Nettoyage du filtre
    }

    private String generateToken() {
        return java.util.UUID.randomUUID().toString();
    }
} 