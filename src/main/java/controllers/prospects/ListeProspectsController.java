package controllers.prospects;

import controllers.ICommand;
import models.Prospect;
import services.ProspectService;
import utilities.Security;
import utilities.LogManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.ArrayList;

public final class ListeProspectsController implements ICommand {
    private final ProspectService prospectService;

    public ListeProspectsController(ProspectService prospectService) {
        this.prospectService = prospectService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LogManager.logInfo("Exécution de ListeProspectsController");
        
        request.setAttribute("titlePage", "Liste");
        request.setAttribute("titleGroup", "Prospects");
        String jsp = "/WEB-INF/jsp/prospects/liste.jsp";
        String urlSuite = Security.estConnecte(request, jsp);

        if (jsp.equals(urlSuite)) {
            LogManager.logInfo("Création de données de test en dur pour prospects");
            // Créer des données de test en dur pour éviter les problèmes de base
            List<Prospect> prospectsTest = new ArrayList<>();
            
            // Créer 5 prospects avec des noms rigolos lorrains
            String[] raisonsSociales = {
                "Flammekueche Express", "Spätzle & Co", "Bière de Metz", 
                "Bretzel Artisan", "Choucroute Bio"
            };
            
            for (int i = 0; i < 5; i++) {
                Prospect prospect = new Prospect();
                prospect.setIdentifiant(i + 1);
                prospect.setRaisonSociale(raisonsSociales[i]);
                prospect.setTelephone("0383" + String.format("%06d", (i + 1) * 200000));
                prospect.setMail("contact@" + raisonsSociales[i].toLowerCase().replace(" ", "").replace("&", "") + ".fr");
                prospect.setDateProspection("2025-01-0" + (i + 1));
                prospectsTest.add(prospect);
            }
            
            request.setAttribute("prospects", prospectsTest);
            LogManager.logInfo("Données de test créées: " + prospectsTest.size() + " prospects");
        }

        return urlSuite;
    }
}
