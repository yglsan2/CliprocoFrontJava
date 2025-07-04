package controllers.prospects;

import controllers.ICommand;
import dao.jpa.ProspectJpaDAO;
import models.Prospect;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utilities.Security;
import java.util.logging.Logger;

public final class DeleteProspectsController implements ICommand {
    private static final Logger LOGGER = Logger.getLogger(DeleteProspectsController.class.getName());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOGGER.info("Exécution de DeleteProspectsController");

        // Vérification du token CSRF pour les requêtes POST
        if (request.getMethod().equals("POST")) {
            HttpSession session = request.getSession(false);
            String formToken = request.getParameter("csrfToken");
            String sessionToken = Security.getCSRFToken(session);
            
            if (!Security.verifyCSRFToken(sessionToken, formToken)) {
                LOGGER.warning("Token CSRF invalide lors de la suppression d'un prospect");
                request.setAttribute("error", "Erreur de sécurité : token CSRF invalide");
                return "redirect:?cmd=prospects.liste";
            }
        }

        // Instanciation de la DAO
        ProspectJpaDAO prospectDAO = new ProspectJpaDAO();

        // Récupération de l'identifiant du prospect
        String prospectId = request.getParameter("id");
        LOGGER.info("ID du prospect à supprimer: " + prospectId);

        if (prospectId == null || prospectId.trim().isEmpty()) {
            LOGGER.warning("ID prospect manquant ou vide");
            request.setAttribute("error", "Identifiant prospect manquant ou invalide.");
            return "redirect:?cmd=prospects.liste";
        }

        try {
            // Récupération du prospect dans la base de données
            int identifiant = Integer.parseInt(prospectId.trim());
            var prospectOptional = prospectDAO.findById(identifiant);

            if (prospectOptional.isEmpty()) {
                LOGGER.severe("Tentative de suppression d'un prospect inexistant - ID: " + identifiant);
                request.setAttribute("error", "Prospect introuvable pour l'identifiant fourni.");
                return "redirect:?cmd=prospects.liste";
            }

            Prospect prospect = prospectOptional.get();
            LOGGER.info("Prospect trouvé, suppression en cours: " + prospect.getRaisonSociale());

            // Suppression du prospect
            prospectDAO.delete(prospect);
            LOGGER.info("Prospect supprimé avec succès: " + prospect.getRaisonSociale());

        } catch (NumberFormatException e) {
            LOGGER.severe("Erreur de conversion ID prospect: " + prospectId);
            request.setAttribute("error", "Identifiant prospect invalide.");
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la suppression du prospect: " + e.getMessage());
            request.setAttribute("error", "Erreur lors de la suppression du prospect.");
        }

        return "redirect:?cmd=prospects.liste";
    }
}
