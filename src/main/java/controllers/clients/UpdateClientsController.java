package controllers.clients;

import controllers.ICommand;
import dao.jpa.ClientJpaDAO;
import models.Client;
import models.Adresse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

public final class UpdateClientsController implements ICommand {
    private static final Logger LOGGER = Logger.getLogger(UpdateClientsController.class.getName());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LOGGER.info("Exécution de UpdateClientsController");

        // Récupération de l'identifiant du client
        String clientId = request.getParameter("id");
        LOGGER.info("ID du client à mettre à jour: " + clientId);

        try {
            // Instanciation de la DAO
            ClientJpaDAO clientDAO = new ClientJpaDAO();

            if (clientId == null || clientId.isEmpty()) {
                LOGGER.warning("ID client manquant ou vide");
                request.setAttribute("error", "Identifiant client manquant ou invalide.");
                return "/WEB-INF/jsp/clients/update.jsp";
            }

            // Récupération du client dans la base de données
            int identifiant = Integer.parseInt(clientId);
            var clientOptional = clientDAO.findById(identifiant);

            if (clientOptional.isEmpty()) {
                LOGGER.severe("Tentative de modification d'un client inexistant - ID: " + identifiant);
                request.setAttribute("error", "Client introuvable pour l'identifiant fourni.");
                return "/WEB-INF/jsp/clients/update.jsp";
            }

            Client client = clientOptional.get();
            LOGGER.info("Client trouvé: " + client.getRaisonSociale());

            // Si on reçoit un formulaire à traiter
            if (request.getMethod().equals("POST")) {
                LOGGER.info("Traitement du formulaire POST de mise à jour");

                try {
                    // Mise à jour du client
                    client.setRaisonSociale(request.getParameter("raisonSociale") != null ? request.getParameter("raisonSociale").trim() : "");
                    client.setTelephone(request.getParameter("telephone") != null ? request.getParameter("telephone").trim() : "");
                    client.setMail(request.getParameter("mail") != null ? request.getParameter("mail").trim() : "");
                    client.setCommentaires(request.getParameter("commentaires") != null ? request.getParameter("commentaires").trim() : "");
                    
                    String chiffreAffairesStr = request.getParameter("chiffreAffaires");
                    String nbEmployesStr = request.getParameter("nbEmployes");
                    
                    if (chiffreAffairesStr != null && !chiffreAffairesStr.trim().isEmpty()) {
                        client.setChiffreAffaires(Double.parseDouble(chiffreAffairesStr.trim()));
                    }
                    if (nbEmployesStr != null && !nbEmployesStr.trim().isEmpty()) {
                        client.setNbEmployes(Integer.parseInt(nbEmployesStr.trim()));
                    }

                    // Mise à jour de l'adresse
                    Adresse adresse = client.getAdresse();
                    if (adresse == null) {
                        adresse = new Adresse();
                        client.setAdresse(adresse);
                    }
                    
                    adresse.setNumeroRue(request.getParameter("numeroRue") != null ? request.getParameter("numeroRue").trim() : "");
                    adresse.setNomRue(request.getParameter("nomRue") != null ? request.getParameter("nomRue").trim() : "");
                    adresse.setCodePostal(request.getParameter("codePostal") != null ? request.getParameter("codePostal").trim() : "");
                    adresse.setVille(request.getParameter("ville") != null ? request.getParameter("ville").trim() : "");

                    // Vérification des données saisies
                    String validation = validationClient(client);
                    if (validation.isEmpty()) {
                        // Si la saisie ne contient aucune erreur, elle est enregistrée dans la base de données
                        clientDAO.update(client);
                        LOGGER.info("Client mis à jour avec succès: " + client.getRaisonSociale());
                        return "redirect:?cmd=clients.liste";
                    } else {
                        // Si les saisies ne sont pas valides, on affiche les corrections à effectuer
                        request.setAttribute("errorValidation", validation);
                    }

                } catch (NumberFormatException e) {
                    request.setAttribute("errorFormat", "Le chiffre d'affaires et le nombre d'employés doivent être des nombres valides");
                } catch (Exception e) {
                    LOGGER.severe("Erreur lors de la mise à jour du client: " + e.getMessage());
                    request.setAttribute("errorGlobal", "Erreur lors de la mise à jour du client: " + e.getMessage());
                }
            }

            // Affichage du client pour modification
            request.setAttribute("client", client);

        } catch (NumberFormatException e) {
            LOGGER.severe("Erreur de conversion ID client: " + clientId);
            request.setAttribute("error", "Identifiant client invalide.");
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la récupération du client: " + e.getMessage());
            request.setAttribute("error", "Erreur lors du chargement du client.");
        }

        return "/WEB-INF/jsp/clients/update.jsp";
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
