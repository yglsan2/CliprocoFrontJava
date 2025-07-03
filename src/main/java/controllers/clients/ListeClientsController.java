package controllers.clients;

import controllers.ICommand;
import services.ClientService;
import models.Client;
import utilities.Security;
import utilities.LogManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public final class ListeClientsController implements ICommand {
    private final ClientService clientService;

    public ListeClientsController(ClientService clientService) {
        this.clientService = clientService;
        LogManager.logInfo("ListeClientsController initialisé avec succès");
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogManager.logInfo("Exécution de ListeClientsController");

        request.setAttribute("titlePage", "Liste");
        request.setAttribute("titleGroup", "Clients");
        String jsp = "/WEB-INF/jsp/clients/liste.jsp";
        LogManager.logInfo("Page JSP cible: " + jsp);

        String urlSuite = Security.estConnecte(request, jsp);
        LogManager.logInfo("URL de redirection après vérification de sécurité: " + urlSuite);

        if (jsp.equals(urlSuite)) {
            LogManager.logInfo("Récupération des clients depuis la base de données");
            try {
                List<Client> clients = clientService.findAll();
                request.setAttribute("clients", clients);
                LogManager.logInfo("Clients récupérés avec succès: " + clients.size() + " clients");
            } catch (Exception e) {
                LogManager.logError("Erreur lors de la récupération des clients: " + e.getMessage());
                request.setAttribute("error", "Erreur lors du chargement des clients");
            }
        } else {
            LogManager.logWarning("Accès non autorisé à la liste des clients");
        }

        return urlSuite;
    }
}
