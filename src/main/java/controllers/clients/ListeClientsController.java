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

        // Récupération des clients depuis la base de données
        ClientJpaDAO clientDAO = new ClientJpaDAO();
        List<Client> clients = clientDAO.findAll();
        
        request.setAttribute("clients", clients);
        LOGGER.info("Clients récupérés depuis la base: " + clients.size());
        return "/WEB-INF/jsp/clients/liste.jsp";
    }
}
