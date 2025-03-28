package com.cliproco.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class GDPRFilter implements Filter {
    private static final String CONSENT_COOKIE = "cookie_consent";
    private static final String CONSENT_PAGE = "/WEB-INF/views/consent.jsp";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialisation du filtre
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Vérifier si l'utilisateur a déjà donné son consentement
        if (!hasConsent(httpRequest) && !isConsentPage(httpRequest)) {
            // Rediriger vers la page de consentement
            httpRequest.getRequestDispatcher(CONSENT_PAGE).forward(httpRequest, httpResponse);
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean hasConsent(HttpServletRequest request) {
        return request.getCookies() != null &&
                java.util.Arrays.stream(request.getCookies())
                        .anyMatch(cookie -> CONSENT_COOKIE.equals(cookie.getName()));
    }

    private boolean isConsentPage(HttpServletRequest request) {
        return request.getRequestURI().contains("/consent");
    }

    @Override
    public void destroy() {
        // Nettoyage du filtre
    }
} 