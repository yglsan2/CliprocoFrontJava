package filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtre pour gérer l'encodage des caractères.
 * Assure que toutes les requêtes et réponses utilisent l'encodage UTF-8.
 * 
 * @author Benja2
 * @version 1.0
 * @since 1.0
 */
public class CharacterEncodingFilter implements Filter {
    private String encoding;
    private boolean forceEncoding;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = filterConfig.getInitParameter("encoding");
        if (encoding == null) {
            encoding = "UTF-8";
        }
        forceEncoding = Boolean.parseBoolean(filterConfig.getInitParameter("forceEncoding"));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (forceEncoding || request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(encoding);
        }
        if (forceEncoding || response.getCharacterEncoding() == null) {
            response.setCharacterEncoding(encoding);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Rien à nettoyer
    }
} 