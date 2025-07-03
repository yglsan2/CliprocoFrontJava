package controllers.clients;

import controllers.ICommand;
import models.Client;
import services.ClientService;
import utilities.Security;
import utilities.LogManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;

public final class ViewClientsController implements ICommand {
    private final ClientService clientService;

    public ViewClientsController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String jsp = "/WEB-INF/jsp/clients/view.jsp";
        String urlSuite = Security.estConnecte(request, jsp);

        if (jsp.equals(urlSuite)) {
            request.setAttribute("titlePage", "Consultation");
            request.setAttribute("titleGroup", "Clients");
            String clientId = request.getParameter("id");
            Optional<Client> client = clientService.findById(Integer.parseInt(clientId));
            client.ifPresent(c -> request.setAttribute("client", c));
        }

        return urlSuite;
    }
}
