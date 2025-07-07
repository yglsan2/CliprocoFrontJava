package controllers.prospects;

import controllers.ICommand;
import dao.jpa.ProspectJpaDAO;
import dao.jpa.AdresseJpaDAO;
import models.Prospect;
import models.Adresse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utilities.Security;
import java.util.logging.Logger;

public final class CreationProspectsController implements ICommand {
    private static final Logger LOGGER = Logger.getLogger(CreationProspectsController.class.getName());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOGGER.info("Exécution de CreationProspectsController");

        // Instanciation des DAO
        ProspectJpaDAO prospectDAO = new ProspectJpaDAO();
        AdresseJpaDAO adresseDAO = new AdresseJpaDAO();

        // Si on reçoit un formulaire à traiter
        if (request.getMethod().equals("POST")) {
            LOGGER.info("Traitement du formulaire POST");

            // Vérification du token CSRF
            HttpSession session = request.getSession(false);
            String formToken = request.getParameter("csrfToken");
            String sessionToken = Security.getCSRFToken(session);
            
            if (!Security.verifyCSRFToken(sessionToken, formToken)) {
                LOGGER.warning("Token CSRF invalide lors de la création d'un prospect");
                request.setAttribute("errorGlobal", "Erreur de sécurité : token CSRF invalide");
                return "/WEB-INF/jsp/prospects/create.jsp";
            }

            try {
                // Vérification des paramètres obligatoires
                String raisonSociale = request.getParameter("raisonSociale");
                String nom = request.getParameter("nom");
                String prenom = request.getParameter("prenom");
                String telephone = request.getParameter("telephone");
                String mail = request.getParameter("mail");
                String numeroRue = request.getParameter("numeroRue");
                String nomRue = request.getParameter("nomRue");
                String codePostal = request.getParameter("codePostal");
                String ville = request.getParameter("ville");
                String dateProspectionStr = request.getParameter("dateProspection");
                String commentaires = request.getParameter("commentaires");
                String prospectInteresseStr = request.getParameter("prospectInteresse");
                
                // Traitement de la date de prospection
                java.sql.Date dateProspection;
                try {
                    dateProspection = java.sql.Date.valueOf(dateProspectionStr.trim());
                } catch (IllegalArgumentException e) {
                    request.setAttribute("errorFormat", "Format de date invalide. Utilisez le format YYYY-MM-DD");
                    return "/WEB-INF/jsp/prospects/create.jsp";
                }
                
                // Instanciation d'un prospect après réception du formulaire
                Prospect prospect = new Prospect(
                    raisonSociale.trim(),
                    nom.trim(),
                    prenom.trim(),
                    new Adresse(
                        numeroRue.trim(),
                        nomRue.trim(),
                        codePostal.trim(),
                        ville.trim()
                    ),
                    telephone.trim(),
                    mail.trim(),
                    commentaires != null ? commentaires.trim() : "",
                    dateProspection
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
        
        if (prospect.getNom() == null || prospect.getNom().trim().isEmpty()) {
            msg.append("- Le nom du contact est obligatoire<br>");
        }
        
        if (prospect.getPrenom() == null || prospect.getPrenom().trim().isEmpty()) {
            msg.append("- Le prénom du contact est obligatoire<br>");
        }
        
        if (prospect.getTelephone() == null || prospect.getTelephone().trim().isEmpty()) {
            msg.append("- Le numéro de téléphone est obligatoire<br>");
        }
        
        if (prospect.getMail() == null || prospect.getMail().trim().isEmpty()) {
            msg.append("- L'adresse email est obligatoire<br>");
        }
        
        if (prospect.getDateProspection() == null) {
            msg.append("- La date de prospection est obligatoire<br>");
        }
        
        if (prospect.getAdresse() == null) {
            msg.append("- L'adresse est obligatoire<br>");
        } else {
            if (prospect.getAdresse().getCodePostal() == null || prospect.getAdresse().getCodePostal().trim().isEmpty()) {
                msg.append("- Le code postal est obligatoire<br>");
            }
            
            if (prospect.getAdresse().getVille() == null || prospect.getAdresse().getVille().trim().isEmpty()) {
                msg.append("- La ville est obligatoire<br>");
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