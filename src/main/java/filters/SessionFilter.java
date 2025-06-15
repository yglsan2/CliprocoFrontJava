package filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utilities.Configuration;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Filtre de gestion des sessions.
 * Vérifie l'authentification et gère le timeout des sessions.
 */
public class SessionFilter implements Filter {
    private static final Configuration config = Configuration.getInstance();
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/login",
        "/signin",
        "/css/",
        "/js/",
        "/images/",
        "/favicon.ico"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialisation non nécessaire
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String requestPath = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        // Vérifier si le chemin est public
        if (isPublicPath(requestPath)) {
            chain.doFilter(request, response);
            return;
        }

        // Vérifier la session
        if (session == null || session.getAttribute("user") == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }

        // Vérifier le timeout de la session
        long lastAccessTime = session.getLastAccessedTime();
        long currentTime = System.currentTimeMillis();
        long timeout = config.getLongProperty("security.session.timeout", 1800) * 1000;

        if (currentTime - lastAccessTime > timeout) {
            session.invalidate();
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }

        // Mettre à jour le dernier accès
        session.setAttribute("lastAccessTime", currentTime);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Nettoyage non nécessaire
    }

    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }
} 