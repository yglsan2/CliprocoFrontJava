package controllers.prospects;

import controllers.ICommand;
import dao.jpa.ProspectJpaDAO;
import models.Prospect;
import models.Adresse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

public final class UpdateProspectsController implements ICommand {
    private static final Logger LOGGER = Logger.getLogger(UpdateProspectsController.class.getName());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOGGER.info("Exécution de UpdateProspectsController");

        // Instanciation de la DAO
        ProspectJpaDAO prospectDAO = new ProspectJpaDAO();

        // Récupération de l'identifiant du prospect
        String prospectId = request.getParameter("id");
        LOGGER.info("ID du prospect à mettre à jour: " + prospectId);

        if (prospectId == null || prospectId.trim().isEmpty()) {
            LOGGER.warning("ID prospect manquant ou vide");
            request.setAttribute("error", "Identifiant prospect manquant ou invalide.");
            return "/WEB-INF/jsp/prospects/update.jsp";
        }

        try {
            // Récupération du prospect dans la base de données
            int identifiant = Integer.parseInt(prospectId.trim());
            var prospectOptional = prospectDAO.findById(identifiant);

            if (prospectOptional.isEmpty()) {
                LOGGER.severe("Tentative de modification d'un prospect inexistant - ID: " + identifiant);
                request.setAttribute("error", "Prospect introuvable pour l'identifiant fourni.");
                return "/WEB-INF/jsp/prospects/update.jsp";
            }

            Prospect prospect = prospectOptional.get();

            // Si on reçoit un formulaire à traiter
            if (request.getMethod().equals("POST")) {
                LOGGER.info("Traitement du formulaire POST de mise à jour");

                try {
                    // Mise à jour du prospect
                    prospect.setRaisonSociale(request.getParameter("raisonSociale") != null ? request.getParameter("raisonSociale").trim() : "");
                    prospect.setTelephone(request.getParameter("telephone") != null ? request.getParameter("telephone").trim() : "");
                    prospect.setMail(request.getParameter("mail") != null ? request.getParameter("mail").trim() : "");
                    prospect.setCommentaires(request.getParameter("commentaires") != null ? request.getParameter("commentaires").trim() : "");
                    
                    String dateProspectionStr = request.getParameter("dateProspection");
                    if (dateProspectionStr != null && !dateProspectionStr.trim().isEmpty()) {
                        prospect.setDateProspection(java.sql.Date.valueOf(dateProspectionStr.trim()));
                    }

                    String prospectInteresseStr = request.getParameter("prospectInteresse");
                    if (prospectInteresseStr != null) {
                        prospect.setProspectInteresse(Boolean.parseBoolean(prospectInteresseStr));
                    }

                    // Mise à jour de l'adresse
                    Adresse adresse = prospect.getAdresse();
                    if (adresse == null) {
                        adresse = new Adresse();
                        prospect.setAdresse(adresse);
                    }
                    
                    adresse.setNumeroRue(request.getParameter("numeroRue") != null ? request.getParameter("numeroRue").trim() : "");
                    adresse.setNomRue(request.getParameter("nomRue") != null ? request.getParameter("nomRue").trim() : "");
                    adresse.setCodePostal(request.getParameter("codePostal") != null ? request.getParameter("codePostal").trim() : "");
                    adresse.setVille(request.getParameter("ville") != null ? request.getParameter("ville").trim() : "");

                    // Vérification des données saisies
                    String validation = validationProspect(prospect);
                    if (validation.isEmpty()) {
                        // Si la saisie ne contient aucune erreur, elle est enregistrée dans la base de données
                        prospectDAO.update(prospect);
                        LOGGER.info("Prospect mis à jour avec succès: " + prospect.getRaisonSociale());
                        return "redirect:?cmd=prospects.liste";
                    } else {
                        // Si les saisies ne sont pas valides, on affiche les corrections à effectuer
                        request.setAttribute("errorValidation", validation);
                    }

                } catch (Exception e) {
                    LOGGER.severe("Erreur lors de la mise à jour du prospect: " + e.getMessage());
                    request.setAttribute("errorGlobal", "Erreur lors de la mise à jour du prospect: " + e.getMessage());
                }
            }

            // Affichage du prospect pour modification
            request.setAttribute("prospect", prospect);

        } catch (NumberFormatException e) {
            LOGGER.severe("Erreur de conversion ID prospect: " + prospectId);
            request.setAttribute("error", "Identifiant prospect invalide.");
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la récupération du prospect: " + e.getMessage());
            request.setAttribute("error", "Erreur lors du chargement du prospect.");
        }

        return "/WEB-INF/jsp/prospects/update.jsp";
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
            if (prospect.getAdresse().getNumeroRue() == null || prospect.getAdresse().getNumeroRue().trim().isEmpty()) {
                msg.append("- Le numéro de rue est obligatoire<br>");
            }
            if (prospect.getAdresse().getNomRue() == null || prospect.getAdresse().getNomRue().trim().isEmpty()) {
                msg.append("- Le nom de rue est obligatoire<br>");
            }
        }

        return msg.toString();
    }
}
