package controllers.clients;

import controllers.ICommand;
import dao.jpa.ClientJpaDAO;
import models.Client;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utilities.Security;
import java.util.logging.Logger;

public final class DeleteClientsController implements ICommand {
    private static final Logger LOGGER = Logger.getLogger(DeleteClientsController.class.getName());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOGGER.info("Exécution de DeleteClientsController");

        // Vérification du token CSRF pour les requêtes POST
        if (request.getMethod().equals("POST")) {
            HttpSession session = request.getSession(false);
            String formToken = request.getParameter("csrfToken");
            String sessionToken = Security.getCSRFToken(session);
            
            if (!Security.verifyCSRFToken(sessionToken, formToken)) {
                LOGGER.warning("Token CSRF invalide lors de la suppression d'un client");
                request.setAttribute("error", "Erreur de sécurité : token CSRF invalide");
                return "redirect:?cmd=clients.liste";
            }
        }

        // Instanciation de la DAO
        ClientJpaDAO clientDAO = new ClientJpaDAO();

        // Récupération de l'identifiant du client
        String clientId = request.getParameter("id");
        LOGGER.info("ID du client à supprimer: " + clientId);

        if (clientId == null || clientId.trim().isEmpty()) {
            LOGGER.warning("ID client manquant ou vide");
            request.setAttribute("error", "Identifiant client manquant ou invalide.");
            return "redirect:?cmd=clients.liste";
        }

        try {
            // Récupération du client dans la base de données
            int identifiant = Integer.parseInt(clientId.trim());
            var clientOptional = clientDAO.findById(identifiant);

            if (clientOptional.isEmpty()) {
                LOGGER.severe("Tentative de suppression d'un client inexistant - ID: " + identifiant);
                request.setAttribute("error", "Client introuvable pour l'identifiant fourni.");
                return "redirect:?cmd=clients.liste";
            }

            Client client = clientOptional.get();
            LOGGER.info("Client trouvé, suppression en cours: " + client.getRaisonSociale());

            // Suppression du client
            clientDAO.delete(client);
            LOGGER.info("Client supprimé avec succès: " + client.getRaisonSociale());

        } catch (NumberFormatException e) {
            LOGGER.severe("Erreur de conversion ID client: " + clientId);
            request.setAttribute("error", "Identifiant client invalide.");
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la suppression du client: " + e.getMessage());
            request.setAttribute("error", "Erreur lors de la suppression du client.");
        }

        return "redirect:?cmd=clients.liste";
    }
}
