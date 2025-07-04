package controllers.prospects;

import controllers.ICommand;
import dao.jpa.ProspectJpaDAO;
import models.Prospect;
import models.Adresse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

public final class ViewProspectsController implements ICommand {
    private static final Logger LOGGER = Logger.getLogger(ViewProspectsController.class.getName());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOGGER.info("Exécution de ViewProspectsController");

        // Récupération de l'identifiant du prospect
        String prospectId = request.getParameter("id");
        LOGGER.info("ID prospect reçu: " + prospectId);

        if (prospectId == null || prospectId.trim().isEmpty()) {
            LOGGER.warning("ID prospect manquant ou vide");
            request.setAttribute("error", "Identifiant prospect manquant ou invalide.");
            return "/WEB-INF/jsp/prospects/view.jsp";
        }

        try {
            // Instanciation de la DAO
            ProspectJpaDAO prospectDAO = new ProspectJpaDAO();
            
            // Récupération du prospect dans la base de données
            int identifiant = Integer.parseInt(prospectId.trim());
            var prospectOptional = prospectDAO.findById(identifiant);
            
            if (prospectOptional.isEmpty()) {
                LOGGER.severe("Tentative de chargement d'un prospect inexistant - ID: " + identifiant);
                request.setAttribute("error", "Prospect introuvable pour l'identifiant fourni.");
                return "/WEB-INF/jsp/prospects/view.jsp";
            }
            
            Prospect prospect = prospectOptional.get();

            // Si le prospect obtenu est valide on l'affiche
            String validation = validationProspect(prospect);
            if (validation.isEmpty()) {
                request.setAttribute("prospect", prospect);
                LOGGER.info("Prospect trouvé: " + prospect.getRaisonSociale());
            } else {
                // Si les données ne sont pas valides, il y a incohérence dans la base de données
                LOGGER.severe("Données de la base de données incohérentes pour le prospect ID: " + identifiant);
                request.setAttribute("error", "Données prospect incohérentes dans la base de données.");
            }

        } catch (NumberFormatException e) {
            LOGGER.severe("Erreur de conversion ID prospect: " + prospectId);
            request.setAttribute("error", "Identifiant prospect invalide.");
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la récupération du prospect: " + e.getMessage());
            request.setAttribute("error", "Erreur lors du chargement du prospect.");
        }

        return "/WEB-INF/jsp/prospects/view.jsp";
    }

    /**
     * Méthode vérifiant la validité des attributs d'une instance de prospect
     * et renvoyant une chaine de caractères contenant toutes les erreurs.
     * Si la chaine retournée est vide, le prospect est valide.
     *
     * @param prospect Le prospect à valider
     * @return String - Les erreurs de validations
     */
    private String validationProspect(Prospect prospect) {
        StringBuilder msg = new StringBuilder();
        
        // Validation basique des champs obligatoires
        if (prospect.getRaisonSociale() == null || prospect.getRaisonSociale().trim().isEmpty()) {
            msg.append("- La raison sociale est obligatoire<br>");
        }
        
        if (prospect.getTelephone() == null || prospect.getTelephone().trim().isEmpty()) {
            msg.append("- Le numéro de téléphone est obligatoire<br>");
        }
        
        if (prospect.getMail() == null || prospect.getMail().trim().isEmpty()) {
            msg.append("- L'adresse email est obligatoire<br>");
        }
        
        if (prospect.getAdresse() == null) {
            msg.append("- L'adresse est obligatoire<br>");
        } else {
            if (prospect.getAdresse().getVille() == null || prospect.getAdresse().getVille().trim().isEmpty()) {
                msg.append("- La ville est obligatoire<br>");
            }
            if (prospect.getAdresse().getCodePostal() == null || prospect.getAdresse().getCodePostal().trim().isEmpty()) {
                msg.append("- Le code postal est obligatoire<br>");
            }
        }

        return msg.toString();
    }
}
