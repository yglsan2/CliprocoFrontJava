package controllers.clients;

import controllers.ICommand;
import dao.jpa.ClientJpaDAO;
import models.Client;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

public final class ListeClientsController implements ICommand {
    private static final Logger LOGGER = Logger.getLogger(ListeClientsController.class.getName());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOGGER.info("Exécution de ListeClientsController");

        try {
            // Récupération des clients depuis la base de données
            ClientJpaDAO clientDAO = new ClientJpaDAO();
            List<Client> clients = clientDAO.findAll();
            
            LOGGER.info("Nombre de clients trouvés: " + clients.size());
            
            request.setAttribute("clients", clients);
            LOGGER.info("Clients ajoutés aux attributs de la requête");
            
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la récupération des clients: " + e.getMessage());
            request.setAttribute("error", "Erreur lors du chargement des clients.");
        }
        
        return "/WEB-INF/jsp/clients/liste.jsp";
    }
}
