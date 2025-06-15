package filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utilities.Configuration;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Filtre de protection CSRF.
 * Génère et vérifie les tokens CSRF pour toutes les requêtes POST, PUT, DELETE.
 */
public class CsrfFilter implements Filter {
    private static final Configuration config = Configuration.getInstance();
    private static final String CSRF_TOKEN_NAME = "csrf_token";
    private static final String CSRF_HEADER_NAME = "X-CSRF-TOKEN";
    private static final SecureRandom secureRandom = new SecureRandom();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialisation non nécessaire
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        if (!config.getBooleanProperty("security.csrf.enabled", true)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        if (session == null) {
            session = httpRequest.getSession(true);
            generateToken(session);
        }

        String method = httpRequest.getMethod();
        if (isModifyingMethod(method)) {
            String token = httpRequest.getHeader(CSRF_HEADER_NAME);
            String sessionToken = (String) session.getAttribute(CSRF_TOKEN_NAME);

            if (token == null || !token.equals(sessionToken)) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Token CSRF invalide");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Nettoyage non nécessaire
    }

    private boolean isModifyingMethod(String method) {
        return "POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method);
    }

    private void generateToken(HttpSession session) {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String token = Base64.getEncoder().encodeToString(randomBytes);
        session.setAttribute(CSRF_TOKEN_NAME, token);
    }
} 