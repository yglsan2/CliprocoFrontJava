package controllers.prospects;

import controllers.ICommand;
import dao.jpa.ProspectJpaDAO;
import models.Prospect;
import models.Adresse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

public final class CreationProspectsController implements ICommand {
    private static final Logger LOGGER = Logger.getLogger(CreationProspectsController.class.getName());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOGGER.info("Exécution de CreationProspectsController");

        // Instanciation de la DAO
        ProspectJpaDAO prospectDAO = new ProspectJpaDAO();

        // Si on reçoit un formulaire à traiter
        if (request.getMethod().equals("POST")) {
            LOGGER.info("Traitement du formulaire POST");

            try {
                // Vérification des paramètres obligatoires
                String raisonSociale = request.getParameter("raisonSociale");
                String telephone = request.getParameter("telephone");
                String mail = request.getParameter("mail");
                String numeroRue = request.getParameter("numeroRue");
                String nomRue = request.getParameter("nomRue");
                String codePostal = request.getParameter("codePostal");
                String ville = request.getParameter("ville");
                String dateProspectionStr = request.getParameter("dateProspection");
                String commentaires = request.getParameter("commentaires");
                String prospectInteresseStr = request.getParameter("prospectInteresse");
                
                // Validation des paramètres obligatoires
                if (raisonSociale == null || raisonSociale.trim().isEmpty() ||
                    telephone == null || telephone.trim().isEmpty() ||
                    mail == null || mail.trim().isEmpty() ||
                    numeroRue == null || numeroRue.trim().isEmpty() ||
                    nomRue == null || nomRue.trim().isEmpty() ||
                    codePostal == null || codePostal.trim().isEmpty() ||
                    ville == null || ville.trim().isEmpty() ||
                    dateProspectionStr == null || dateProspectionStr.trim().isEmpty()) {
                    request.setAttribute("errorValidation", "Tous les champs obligatoires doivent être remplis");
                    return "/WEB-INF/jsp/prospects/create.jsp";
                }
                
                // Instanciation d'un prospect après réception du formulaire
                Prospect prospect = new Prospect(
                    raisonSociale.trim(),
                    new Adresse(
                        numeroRue.trim(),
                        nomRue.trim(),
                        codePostal.trim(),
                        ville.trim()
                    ),
                    telephone.trim(),
                    mail.trim(),
                    commentaires != null ? commentaires.trim() : "",
                    java.sql.Date.valueOf(dateProspectionStr.trim())
                );

                // Gestion du champ prospectInteresse
                if (prospectInteresseStr != null) {
                    prospect.setProspectInteresse(Boolean.parseBoolean(prospectInteresseStr));
                }

                // Vérification des données saisies
                String validation = validationProspect(prospect);
                if (validation.isEmpty()) {
                    // Si la saisie ne contient aucune erreur, elle est enregistrée dans la base de données
                    prospectDAO.save(prospect);
                    LOGGER.info("Prospect créé avec succès: " + prospect.getRaisonSociale());
                    return "redirect:?cmd=prospects.liste";
                } else {
                    // Si les saisies ne sont pas valides, on affiche les corrections à effectuer
                    request.setAttribute("errorValidation", validation);
                }

            } catch (Exception e) {
                LOGGER.severe("Erreur lors de la création du prospect: " + e.getMessage());
                request.setAttribute("errorGlobal", "Erreur lors de la création du prospect: " + e.getMessage());
            }
        }

        return "/WEB-INF/jsp/prospects/create.jsp";
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
