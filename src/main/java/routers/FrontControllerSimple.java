package routers;

import controllers.IndexController;
import controllers.ICommand;
import controllers.clients.CreationClientsController;
import controllers.clients.DeleteClientsController;
import controllers.clients.ListeClientsController;
import controllers.clients.UpdateClientsController;
import controllers.clients.ViewClientsController;
import controllers.prospects.CreationProspectsController;
import controllers.prospects.DeleteProspectsController;
import controllers.prospects.ListeProspectsController;
import controllers.prospects.UpdateProspectsController;
import controllers.prospects.ViewProspectsController;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur frontal ultra-simplifié pour tester Tomcat 11
 * Basé sur l'architecture du projet de référence GestionClientProspectMaven
 */
public class FrontControllerSimple extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Map<String, ICommand> commands;

    @Override
    public void init() throws ServletException {
        System.out.println("Initialisation du FrontControllerSimple");
        commands = new HashMap<>();
        
        // Commande pour la page d'accueil
        commands.put("/", new IndexController());
        commands.put("/index", new IndexController());
        commands.put("index", new IndexController());
        
        // Commandes pour les clients
        commands.put("/clients", new ListeClientsController());
        commands.put("/clients.liste", new ListeClientsController());
        commands.put("/clients.update", new UpdateClientsController());
        commands.put("/clients.view", new ViewClientsController());
        commands.put("/clients.create", new CreationClientsController());
        commands.put("/clients.delete", new DeleteClientsController());
        commands.put("/clients/update", new UpdateClientsController());
        commands.put("/clients/view", new ViewClientsController());
        commands.put("/clients/create", new CreationClientsController());
        commands.put("/clients/delete", new DeleteClientsController());
        
        // Commandes pour les prospects
        commands.put("/prospects", new ListeProspectsController());
        commands.put("/prospects.liste", new ListeProspectsController());
        commands.put("/prospects.create", new CreationProspectsController());
        commands.put("/prospects.update", new UpdateProspectsController());
        commands.put("/prospects.delete", new DeleteProspectsController());
        commands.put("/prospects.view", new ViewProspectsController());
        commands.put("/prospects/create", new CreationProspectsController());
        commands.put("/prospects/update", new UpdateProspectsController());
        commands.put("/prospects/delete", new DeleteProspectsController());
        commands.put("/prospects/view", new ViewProspectsController());
        
        System.out.println("FrontControllerSimple initialisé avec succès");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        String cmd = request.getParameter("cmd");
        
        System.out.println("Path: " + path + ", Cmd: " + cmd);
        
        // Si un paramètre cmd est présent, l'utiliser comme chemin
        if (cmd != null && !cmd.isEmpty()) {
            path = "/" + cmd;
        } else if (path == null) {
            path = "/";
        }

        System.out.println("Chemin final: " + path);
        
        ICommand command = commands.get(path);
        if (command == null) {
            System.out.println("Commande non trouvée pour: " + path);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            System.out.println("Exécution de la commande: " + command.getClass().getSimpleName());
            String result = command.execute(request, response);
            System.out.println("Résultat: " + result);
            
            if (result.startsWith("redirect:")) {
                response.sendRedirect(request.getContextPath() + result.substring(9));
            } else {
                request.getRequestDispatcher(result).forward(request, response);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'exécution de la commande: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Erreur lors de l'exécution de la commande", e);
        }
    }
} 