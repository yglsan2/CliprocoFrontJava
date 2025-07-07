package filters;

import utilities.LogManager;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class AuthenticationFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialisation du filtre
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        // Logique de filtrage d'authentification
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // Nettoyage du filtre
    }
} 