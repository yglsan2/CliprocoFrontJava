package controllers.clients;

import controllers.ICommand;
import models.Client;
import services.ClientService;
import utilities.Security;
import utilities.LogManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;

public final class DeleteClientsController implements ICommand {
    private final ClientService clientService;

    public DeleteClientsController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
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
                    request.setAttribute("errorNotFound", "Aucun client trouvé avec l'ID : " + clientId);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorFormat", "Format d'ID invalide : " + e.getMessage());
            } catch (Exception e) {
                LogManager.logWarning("Erreur inattendue lors de la suppression du client avec l'ID : " + clientId + " : " + e.getMessage());
                request.setAttribute("errorGlobal", "Une erreur inattendue est survenue lors de la suppression. Merci de réessayer.");
            } finally {
                LogManager.logInfo("Fin de la tentative de suppression de client.");
            }
        }

        return urlSuite;
    }
}
