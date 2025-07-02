package routers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Contrôleur frontal ultra-simplifié pour tester Tomcat 11
 * Basé sur l'architecture du projet de référence GestionClientProspectMaven
 */
@WebServlet(name = "front", value = "/front")
public class FrontControllerSimple extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(FrontControllerSimple.class.getName());

    @Override
    public void init() throws ServletException {
        LOGGER.info("FrontControllerSimple initialisé avec succès");
    }

    protected void processRequest(final HttpServletRequest request,
                                 final HttpServletResponse response) throws ServletException, IOException {
        try {
            LOGGER.info("Requête reçue: " + request.getRequestURI());
            
            // Réponse simple pour tester
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("<!DOCTYPE html>");
            response.getWriter().println("<html>");
            response.getWriter().println("<head>");
            response.getWriter().println("<title>CliprocoJEE - Test</title>");
            response.getWriter().println("</head>");
            response.getWriter().println("<body>");
            response.getWriter().println("<h1>CliprocoJEE fonctionne !</h1>");
            response.getWriter().println("<p>Le FrontController répond correctement.</p>");
            response.getWriter().println("<p>Date: " + new java.util.Date() + "</p>");
            response.getWriter().println("</body>");
            response.getWriter().println("</html>");
            
        } catch (Exception e) {
            LOGGER.severe("Erreur dans processRequest : " + e.getMessage());
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur interne");
            } catch (IOException ex) {
                LOGGER.severe("Erreur lors de l'envoi de l'erreur : " + ex.getMessage());
            }
        }
    }

    @Override
    protected void doGet(final HttpServletRequest request,
                         final HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
} 