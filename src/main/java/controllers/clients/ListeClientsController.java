package controllers.clients;

import controllers.ICommand;
import services.ClientService;
import models.Client;
import utilities.Security;
import utilities.LogManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.ArrayList;

public final class ListeClientsController implements ICommand {
    private final ClientService clientService;

    public ListeClientsController(ClientService clientService) {
        this.clientService = clientService;
        LogManager.logInfo("ListeClientsController initialisé avec succès");
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogManager.logInfo("Exécution de ListeClientsController");

        request.setAttribute("titlePage", "Liste");
        request.setAttribute("titleGroup", "Clients");
        String jsp = "/WEB-INF/jsp/clients/liste.jsp";
        LogManager.logInfo("Page JSP cible: " + jsp);

        String urlSuite = Security.estConnecte(request, jsp);
        LogManager.logInfo("URL de redirection après vérification de sécurité: " + urlSuite);

        if (jsp.equals(urlSuite)) {
            LogManager.logInfo("Création de données de test en dur");
            // Créer des données de test en dur pour éviter les problèmes de base
            List<Client> clientsTest = new ArrayList<>();
            
            // Créer 5 clients avec des noms rigolos lorrains
            String[] raisonsSociales = {
                "Quiche Lorraine Express", "Mirabelle & Co", "Bretzel Brothers", 
                "Choucroute Royale", "Schnaps & Schnitzel"
            };
            
            for (int i = 0; i < 5; i++) {
                Client client = new Client();
                client.setIdentifiantClient(i + 1);
                client.setRaisonSociale(raisonsSociales[i]);
                client.setTelephone("0383" + String.format("%06d", (i + 1) * 100000));
                client.setMail("contact@" + raisonsSociales[i].toLowerCase().replace(" ", "").replace("&", "") + ".fr");
                client.setChiffreAffaire(100000 * (i + 1));
                client.setNbrEmploye(10 * (i + 1));
                clientsTest.add(client);
            }
            
            request.setAttribute("clients", clientsTest);
            LogManager.logInfo("Données de test créées: " + clientsTest.size() + " clients");
        } else {
            LogManager.logWarning("Accès non autorisé à la liste des clients");
        }

        return urlSuite;
    }
}
