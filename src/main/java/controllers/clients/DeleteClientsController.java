package controllers.clients;

import controllers.ICommand;
import models.Client;
import services.ClientService;
import utilities.Security;
import utilities.LogManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.util.Optional;

public final class DeleteClientsController implements ICommand {
    private final ClientService clientService;

    public DeleteClientsController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String execute(final @NotNull HttpServletRequest request,
                                   final HttpServletResponse response)
            throws Exception {

        String jsp = "clients/list.jsp";
        String urlSuite = Security.estConnecte(request, jsp);

        if (jsp.equals(urlSuite)) {
            String clientId = request.getParameter("clientId");
            Optional<Client> client = clientService.findById(Long.parseLong(clientId));
            client.ifPresent(c -> clientService.delete(c.getIdentifiant()));
        }

        return urlSuite;
    }
}
