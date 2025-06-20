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

public final class CreationClientsController implements ICommand {
    private final ClientService clientService;

    public CreationClientsController(ClientService clientService) {
        this.clientService = clientService;
        LogManager.logInfo("Initialisation de CreationClientsController avec clientService");
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogManager.logInfo("Début de l'exécution de CreationClientsController");

        String jsp = "clients/create.jsp";
        String urlSuite = Security.estConnecte(request, jsp);
        LogManager.logInfo("URL suite après vérification de connexion: " + urlSuite);

        if (jsp.equals(urlSuite)) {
            LogManager.logInfo("Utilisateur connecté, préparation de la création");
            request.setAttribute("titlePage", "Création");
            request.setAttribute("titleGroup", "Clients");

            if (request.getMethod().equals("POST")) {
                LogManager.logInfo("Méthode POST détectée, traitement des données");
                try {
                    // Construction de l'adresse
                    LogManager.logInfo("Construction de l'adresse");
                    Adresse adresse = AdresseBuilder.getNewAdresseBuilder()
                            .deNumeroRue(request.getParameter("numeroRue"))
                            .deNomRue(request.getParameter("nomRue"))
                            .deCodePostal(request.getParameter("codePostal"))
                            .deVille(request.getParameter("ville"))
                            .build();
                    LogManager.logInfo("Adresse construite avec succès: " + adresse);

                    // Construction du client
                    LogManager.logInfo("Construction du client");
                    Client client = ClientBuilder.getNewClientBuilder()
                            .deRaisonSociale(request.getParameter("raisonSociale"))
                            .deTelephone(request.getParameter("telephone"))
                            .deMail(request.getParameter("adresseMail"))
                            .deCommentaires(request.getParameter("commentaires"))
                            .dAdresse(adresse)
                            .deChiffreAffaires(Double.parseDouble(request.getParameter("chiffreAffaires")))
                            .deNombreEmployes(Integer.parseInt(request.getParameter("nbEmployes")))
                            .build();
                    LogManager.logInfo("Client construit avec succès: " + client);

                    // Création du client
                    LogManager.logInfo("Création du client");
                    clientService.create(client);
                    urlSuite = "redirect:?cmd=clients";
                    LogManager.logInfo("Redirection vers: " + urlSuite);
                } catch (Exception e) {
                    LogManager.logException("Erreur lors de la création du client", e);
                    throw e;
                }
            }
        }

        LogManager.logInfo("Fin de l'exécution de CreationClientsController, URL suite: " + urlSuite);
        return urlSuite;
    }
}
