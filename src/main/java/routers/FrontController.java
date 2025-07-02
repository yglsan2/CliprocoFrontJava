package routers;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FrontController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String servletPath = request.getServletPath();
        String cmd = request.getParameter("cmd");
        
        // Gestion des routes directes
        if ("/clients".equals(servletPath)) {
            request.getRequestDispatcher("/WEB-INF/jsp/clients.jsp").forward(request, response);
            return;
        }
        
        if ("/prospects".equals(servletPath)) {
            request.getRequestDispatcher("/WEB-INF/jsp/prospects.jsp").forward(request, response);
            return;
        }
        
        // Gestion des routes avec paramètre cmd
        if (cmd == null || cmd.isEmpty()) {
            cmd = "index";
        }
        
        switch (cmd) {
            case "index":
                request.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(request, response);
                break;
            case "connexion":
                request.getRequestDispatcher("/WEB-INF/jsp/connexion.jsp").forward(request, response);
                break;
            case "signin":
                request.getRequestDispatcher("/WEB-INF/jsp/signin.jsp").forward(request, response);
                break;
            case "clients":
                request.getRequestDispatcher("/WEB-INF/jsp/clients.jsp").forward(request, response);
                break;
            case "prospects":
                request.getRequestDispatcher("/WEB-INF/jsp/prospects.jsp").forward(request, response);
                break;
            case "contact":
                request.getRequestDispatcher("/WEB-INF/jsp/contact.jsp").forward(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String cmd = request.getParameter("cmd");
        
        if (cmd == null || cmd.isEmpty()) {
            cmd = "index";
        }
        
        switch (cmd) {
            case "connexion":
                // Traitement de la connexion
                request.getRequestDispatcher("/WEB-INF/jsp/connexion.jsp").forward(request, response);
                break;
            case "signin":
                // Traitement de l'inscription
                request.getRequestDispatcher("/WEB-INF/jsp/connexion.jsp").forward(request, response);
                break;
            default:
        doGet(request, response);
                break;
        }
    }
}
