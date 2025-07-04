package controllers.clients;

import controllers.ICommand;
import dao.jpa.ClientJpaDAO;
import models.Client;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

public final class ViewClientsController implements ICommand {
    private static final Logger LOGGER = Logger.getLogger(ViewClientsController.class.getName());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOGGER.info("Exécution de ViewClientsController");

        // Récupération de l'identifiant du client
        String clientId = request.getParameter("id");
        LOGGER.info("ID client reçu: " + clientId);

        if (clientId == null || clientId.trim().isEmpty()) {
            LOGGER.warning("ID client manquant ou vide");
            request.setAttribute("error", "Identifiant client manquant ou invalide.");
            return "/WEB-INF/jsp/clients/view.jsp";
        }

        try {
            // Instanciation de la DAO
            ClientJpaDAO clientDAO = new ClientJpaDAO();
            
            // Récupération du client dans la base de données
            int identifiant = Integer.parseInt(clientId.trim());
            var clientOptional = clientDAO.findById(identifiant);
            
            if (clientOptional.isEmpty()) {
                LOGGER.severe("Tentative de chargement d'un client inexistant - ID: " + identifiant);
                request.setAttribute("error", "Client introuvable pour l'identifiant fourni.");
                return "/WEB-INF/jsp/clients/view.jsp";
            }
            
            Client client = clientOptional.get();

            // Si le client obtenu est valide on l'affiche
            String validation = validationClient(client);
            if (validation.isEmpty()) {
                request.setAttribute("client", client);
                LOGGER.info("Client trouvé: " + client.getRaisonSociale());
            } else {
                // Si les données ne sont pas valides, il y a incohérence dans la base de données
                LOGGER.severe("Données de la base de données incohérentes pour le client ID: " + identifiant);
                request.setAttribute("error", "Données client incohérentes dans la base de données.");
            }

        } catch (NumberFormatException e) {
            LOGGER.severe("Erreur de conversion ID client: " + clientId);
            request.setAttribute("error", "Identifiant client invalide.");
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la récupération du client: " + e.getMessage());
            request.setAttribute("error", "Erreur lors du chargement du client.");
        }

        return "/WEB-INF/jsp/clients/view.jsp";
    }

    /**
     * Méthode vérifiant la validité des attributs d'une instance de client
     * et renvoyant une chaine de caractères contenant toutes les erreurs.
     * Si la chaine retournée est vide, le client est valide.
     *
     * @param client Le client à valider
     * @return String - Les erreurs de validations
     */
    private String validationClient(Client client) {
        StringBuilder msg = new StringBuilder();
        
        // Validation basique des champs obligatoires
        if (client.getRaisonSociale() == null || client.getRaisonSociale().trim().isEmpty()) {
            msg.append("- La raison sociale est obligatoire<br>");
        }
        
        if (client.getTelephone() == null || client.getTelephone().trim().isEmpty()) {
            msg.append("- Le numéro de téléphone est obligatoire<br>");
        }
        
        if (client.getMail() == null || client.getMail().trim().isEmpty()) {
            msg.append("- L'adresse email est obligatoire<br>");
        }
        
        if (client.getAdresse() == null) {
            msg.append("- L'adresse est obligatoire<br>");
        } else {
            if (client.getAdresse().getVille() == null || client.getAdresse().getVille().trim().isEmpty()) {
                msg.append("- La ville est obligatoire<br>");
            }
            if (client.getAdresse().getCodePostal() == null || client.getAdresse().getCodePostal().trim().isEmpty()) {
                msg.append("- Le code postal est obligatoire<br>");
            }
        }

        return msg.toString();
    }
}
