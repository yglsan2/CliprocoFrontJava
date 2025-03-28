package com.cliproco.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@WebFilter("/*")
public class AdminAuthFilter implements Filter {
    private static final Set<String> ADMIN_PATHS = new HashSet<>();

    static {
        ADMIN_PATHS.add("/clients/add");
        ADMIN_PATHS.add("/clients/edit");
        ADMIN_PATHS.add("/clients/delete");
        ADMIN_PATHS.add("/admin/");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialisation du filtre
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        // Vérification si le chemin est une page admin
        boolean isAdminPath = ADMIN_PATHS.stream().anyMatch(path::startsWith);
        if (isAdminPath) {
            HttpSession session = httpRequest.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                // Stockage de l'URL demandée pour redirection après connexion
                String redirectUrl = httpRequest.getRequestURI();
                if (httpRequest.getQueryString() != null) {
                    redirectUrl += "?" + httpRequest.getQueryString();
                }
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login?redirectUrl=" + redirectUrl);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Nettoyage du filtre
    }
} 