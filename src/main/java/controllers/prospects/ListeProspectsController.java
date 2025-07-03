package controllers.prospects;

import controllers.ICommand;
import models.Prospect;
import services.ProspectService;
import utilities.Security;
import utilities.LogManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

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
            LogManager.logInfo("Récupération des prospects depuis la base de données");
            try {
                List<Prospect> prospects = prospectService.findAll();
                request.setAttribute("prospects", prospects);
                LogManager.logInfo("Prospects récupérés avec succès: " + prospects.size() + " prospects");
            } catch (Exception e) {
                LogManager.logError("Erreur lors de la récupération des prospects: " + e.getMessage());
                request.setAttribute("error", "Erreur lors du chargement des prospects");
            }
        }

        return urlSuite;
    }
}
