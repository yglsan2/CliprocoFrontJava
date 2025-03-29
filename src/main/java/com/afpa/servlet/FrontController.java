package com.afpa.servlet;

import com.afpa.controllers.ICommand;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@WebServlet(name = "FrontController", urlPatterns = {"/accueil"})
public class FrontController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(FrontController.class.getName());
    private Map<String, ICommand> commands = new HashMap<>();

    @Override
    public void init() throws ServletException {
        // Enregistrement des commandes
        commands.put(null, new PageAccueilController());
        commands.put("listeClients", new ListeClientsController());
        commands.put("creerClient", new CreerClientController());
        commands.put("modifierClient", new ModifierClientController());
        commands.put("supprimerClient", new SupprimerClientController());
        commands.put("listeProspects", new ListeProspectsController());
        commands.put("creerProspect", new CreerProspectController());
        commands.put("modifierProspect", new ModifierProspectController());
        commands.put("supprimerProspect", new SupprimerProspectController());
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException {
        String urlSuite = "erreur.jsp";
        try {
            String cmd = request.getParameter("cmd");
            ICommand command = commands.get(cmd);
            urlSuite = command.execute(request, response);
        } catch (Exception e) {
            LOGGER.severe("Erreur dans le FrontController: " + e.getMessage());
        } finally {
            try {
                request.getRequestDispatcher(urlSuite).forward(request, response);
            } catch (Exception e) {
                LOGGER.severe("Erreur lors du forward: " + e.getMessage());
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException {
        processRequest(request, response);
    }
} 