package com.cliproco.filter;

import java.io.IOException;
import java.util.UUID;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter("/*")
public class AuthFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);
    private static final String CSRF_TOKEN_COOKIE = "XSRF-TOKEN";
    private static final String CSRF_TOKEN_HEADER = "X-XSRF-TOKEN";
    private static final String[] PUBLIC_PATHS = {
        "/login",
        "/register",
        "/css/",
        "/js/",
        "/images/",
        "/favicon.ico"
    };

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("Initialisation du filtre d'authentification");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        // Vérifier si le chemin est public
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Vérifier si l'utilisateur est authentifié
        if (session == null || session.getAttribute("user") == null) {
            logger.warn("Tentative d'accès non autorisé à {}", path);
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }

        // Gérer le token CSRF pour les requêtes POST
        if ("POST".equalsIgnoreCase(httpRequest.getMethod())) {
            String csrfToken = httpRequest.getHeader(CSRF_TOKEN_HEADER);
            Cookie[] cookies = httpRequest.getCookies();
            String cookieToken = null;

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (CSRF_TOKEN_COOKIE.equals(cookie.getName())) {
                        cookieToken = cookie.getValue();
                        break;
                    }
                }
            }

            if (csrfToken == null || !csrfToken.equals(cookieToken)) {
                logger.error("Token CSRF invalide pour la requête {}", path);
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Token CSRF invalide");
                return;
            }
        }

        // Générer un nouveau token CSRF si nécessaire
        if (session.getAttribute("csrfToken") == null) {
            String token = UUID.randomUUID().toString();
            session.setAttribute("csrfToken", token);
            Cookie cookie = new Cookie(CSRF_TOKEN_COOKIE, token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            httpResponse.addCookie(cookie);
        }

        chain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        for (String publicPath : PUBLIC_PATHS) {
            if (path.startsWith(publicPath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        logger.info("Destruction du filtre d'authentification");
    }
} 