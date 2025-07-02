package controllers.clients;

import builders.AdresseBuilder;
import builders.ClientBuilder;
import controllers.ICommand;
import models.Client;
import models.Adresse;
import services.ClientService;
import utilities.Security;
import utilities.LogManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import exceptions.ValidationException;

public final class CreationClientsController implements ICommand {
    private final ClientService clientService;

    public CreationClientsController(ClientService clientService) {
        this.clientService = clientService;
        LogManager.logInfo("Initialisation de CreationClientsController avec clientService");
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogManager.logInfo("Début de l'exécution de CreationClientsController");

        String jsp = "/WEB-INF/jsp/clients/create.jsp";
        String urlSuite = Security.estConnecte(request, jsp);
        LogManager.logInfo("URL suite après vérification de connexion: " + urlSuite);

        if (jsp.equals(urlSuite)) {
            LogManager.logInfo("Utilisateur connecté, préparation de la création");
            request.setAttribute("titlePage", "Création");
            request.setAttribute("titleGroup", "Clients");

            if (request.getMethod().equals("POST")) {
                LogManager.logInfo("Méthode POST détectée, traitement des données");
                try {
                    // Validation et construction de l'adresse
                    Adresse adresse = AdresseBuilder.getNewAdresseBuilder()
                            .deNumeroRue(request.getParameter("numeroRue"))
                            .deNomRue(request.getParameter("nomRue"))
                            .deCodePostal(request.getParameter("codePostal"))
                            .deVille(request.getParameter("ville"))
                            .build();

                    // Validation et construction du client
                    Client client = ClientBuilder.getNewClientBuilder()
                            .deRaisonSociale(request.getParameter("raisonSociale"))
                            .deTelephone(request.getParameter("telephone"))
                            .deMail(request.getParameter("adresseMail"))
                            .deCommentaires(request.getParameter("commentaires"))
                            .dAdresse(adresse)
                            .deChiffreAffaires(Double.parseDouble(request.getParameter("chiffreAffaires")))
                            .deNombreEmployes(Integer.parseInt(request.getParameter("nbEmployes")))
                            .build();

                    // Ici, on peut ajouter des validations supplémentaires si besoin
                    // ValidationManager.isValidPhone(client.getTelephone());
                    // ValidationManager.isValidPostalCode(adresse.getCodePostal());
                    // ValidationManager.isValidEmail(client.getMail());

                    clientService.create(client);
                    urlSuite = "redirect:?cmd=clients";
                } catch (ValidationException e) {
                    request.setAttribute("errorValidation", e.getMessage());
                } catch (NumberFormatException e) {
                    request.setAttribute("errorFormat", "Format numérique invalide : " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    request.setAttribute("errorArgument", "Erreur de saisie : " + e.getMessage());
                } catch (Exception e) {
                    LogManager.logWarning("Erreur inattendue lors de la création du client : " + e.getMessage());
                    request.setAttribute("errorGlobal", "Une erreur inattendue est survenue. Merci de réessayer.");
                } finally {
                    LogManager.logInfo("Fin de la tentative de création de client.");
                }
            }
        }

        LogManager.logInfo("Fin de l'exécution de CreationClientsController, URL suite: " + urlSuite);
        return urlSuite;
    }
}
