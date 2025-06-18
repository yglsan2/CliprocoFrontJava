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
            try {
                LogManager.logInfo("Suppression du client avec l'ID : " + clientId);
                Optional<Client> client = clientService.findById(Integer.parseInt(clientId));
                if (client.isPresent()) {
                    LogManager.logInfo("Client trouvé, suppression en cours : " + client.get().getIdentifiant());
                    clientService.delete(client.get().getIdentifiant());
                    LogManager.logInfo("Client supprimé avec succès : " + client.get().getIdentifiant());
                } else {
                    LogManager.logWarning("Aucun client trouvé avec l'ID : " + clientId);
                }
            } catch (Exception e) {
                LogManager.logException("Erreur lors de la suppression du client avec l'ID : " + clientId, e);
                request.setAttribute("errorMessage", "Erreur lors de la suppression du client : " + e.getMessage());
            }
        }

        return urlSuite;
    }
}
