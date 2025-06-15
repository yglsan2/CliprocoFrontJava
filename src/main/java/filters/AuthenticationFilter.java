package filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;
import utilities.LogManager;
import java.io.IOException;

/**
 * Filtre pour gérer l'authentification des utilisateurs.
 * Vérifie que l'utilisateur est connecté pour accéder aux ressources protégées.
 * 
 * @author Benja2
 * @version 1.0
 * @since 1.0
 */
@WebFilter(urlPatterns = {"/home/*", "/prospects/*", "/societes/*"})
public class AuthenticationFilter implements Filter {
    private static final String LOGIN_PAGE = "/login";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LogManager.info("Initialisation du filtre d'authentification");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);
        String loginURI = httpRequest.getContextPath() + LOGIN_PAGE;
        boolean isLoginRequest = httpRequest.getRequestURI().equals(loginURI);

        if (isLoggedIn || isLoginRequest) {
            chain.doFilter(request, response);
        } else {
            LogManager.warning("Tentative d'accès non autorisé à: " + httpRequest.getRequestURI());
            httpResponse.sendRedirect(loginURI);
        }
    }

    @Override
    public void destroy() {
        LogManager.info("Destruction du filtre d'authentification");
    }
} 