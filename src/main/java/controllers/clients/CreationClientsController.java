package controllers.clients;

import controllers.ICommand;
import dao.jpa.ClientJpaDAO;
import dao.jpa.AdresseJpaDAO;
import models.Client;
import models.Adresse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utilities.Security;
import java.util.logging.Logger;

public final class CreationClientsController implements ICommand {
    private static final Logger LOGGER = Logger.getLogger(CreationClientsController.class.getName());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOGGER.info("Exécution de CreationClientsController");

        // Instanciation des DAO
        ClientJpaDAO clientDAO = new ClientJpaDAO();
        AdresseJpaDAO adresseDAO = new AdresseJpaDAO();

        // Si on reçoit un formulaire à traiter
        if (request.getMethod().equals("POST")) {
            LOGGER.info("Traitement du formulaire POST");

            // Vérification du token CSRF
            HttpSession session = request.getSession(false);
            String formToken = request.getParameter("csrfToken");
            String sessionToken = Security.getCSRFToken(session);
            
            if (!Security.verifyCSRFToken(sessionToken, formToken)) {
                LOGGER.warning("Token CSRF invalide lors de la création d'un client");
                request.setAttribute("errorGlobal", "Erreur de sécurité : token CSRF invalide");
                return "/WEB-INF/jsp/clients/create.jsp";
            }

            try {
                // Vérification des paramètres obligatoires
                String raisonSociale = request.getParameter("raisonSociale");
                String telephone = request.getParameter("telephone");
                String mail = request.getParameter("mail");
                String numeroRue = request.getParameter("numeroRue");
                String nomRue = request.getParameter("nomRue");
                String codePostal = request.getParameter("codePostal");
                String ville = request.getParameter("ville");
                String chiffreAffairesStr = request.getParameter("chiffreAffaires");
                String nbEmployesStr = request.getParameter("nbEmployes");
                String commentaires = request.getParameter("commentaires");
                
                // Validation des paramètres obligatoires
                if (raisonSociale == null || raisonSociale.trim().isEmpty() ||
                    telephone == null || telephone.trim().isEmpty() ||
                    mail == null || mail.trim().isEmpty() ||
                    numeroRue == null || numeroRue.trim().isEmpty() ||
                    nomRue == null || nomRue.trim().isEmpty() ||
                    codePostal == null || codePostal.trim().isEmpty() ||
                    ville == null || ville.trim().isEmpty() ||
                    chiffreAffairesStr == null || chiffreAffairesStr.trim().isEmpty() ||
                    nbEmployesStr == null || nbEmployesStr.trim().isEmpty()) {
                    request.setAttribute("errorValidation", "Tous les champs obligatoires doivent être remplis");
                    return "/WEB-INF/jsp/clients/create.jsp";
                }
                
                // Instanciation d'un client après réception du formulaire
                Client client = new Client(
                    new Adresse(
                        numeroRue.trim(),
                        nomRue.trim(),
                        codePostal.trim(),
                        ville.trim()
                    ),
                    mail.trim(),
                    commentaires != null ? commentaires.trim() : "",
                    raisonSociale.trim(),
                    telephone.trim(),
                    Double.parseDouble(chiffreAffairesStr.trim()),
                    Integer.parseInt(nbEmployesStr.trim())
                );

                // Vérification des données saisies
                String validation = validationClient(client);
                if (validation.isEmpty()) {
                    // Si la saisie ne contient aucune erreur, elle est enregistrée dans la base de données
                    clientDAO.save(client);
                    LOGGER.info("Client créé avec succès: " + client.getRaisonSociale());
                    return "redirect:?cmd=clients.liste";
                } else {
                    // Si les saisies ne sont pas valides, on affiche les corrections à effectuer
                    request.setAttribute("errorValidation", validation);
                }

            } catch (NumberFormatException e) {
                request.setAttribute("errorFormat", "Le chiffre d'affaires et le nombre d'employés doivent être des nombres valides");
            } catch (Exception e) {
                LOGGER.severe("Erreur lors de la création du client: " + e.getMessage());
                request.setAttribute("errorGlobal", "Erreur lors de la création du client: " + e.getMessage());
            }
        }

        return "/WEB-INF/jsp/clients/create.jsp";
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
            if (client.getAdresse().getNumeroRue() == null || client.getAdresse().getNumeroRue().trim().isEmpty()) {
                msg.append("- Le numéro de rue est obligatoire<br>");
            }
            if (client.getAdresse().getNomRue() == null || client.getAdresse().getNomRue().trim().isEmpty()) {
                msg.append("- Le nom de rue est obligatoire<br>");
            }
        }

        return msg.toString();
    }
}
