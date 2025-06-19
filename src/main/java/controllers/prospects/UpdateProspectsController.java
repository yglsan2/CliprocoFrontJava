package controllers.prospects;

import controllers.ICommand;
import models.Prospect;
import models.Adresse;
import services.ProspectService;
import builders.AdresseBuilder;
import builders.ProspectBuilder;
import utilities.Security;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class UpdateProspectsController implements ICommand {
    private final ProspectService prospectService;

    public UpdateProspectsController(ProspectService prospectService) {
        this.prospectService = prospectService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("titlePage", "Modification");
        request.setAttribute("titleGroup", "Prospects");
        String jsp = "prospects/update.jsp";
        String urlSuite = Security.estConnecte(request, jsp);

        if (jsp.equals(urlSuite)) {
            Integer id = Integer.parseInt(request.getParameter("id"));
            Prospect prospect = prospectService.findById(id);

            if (prospect != null) {
                // Build address
                Adresse adresse = AdresseBuilder.getNewAdresseBuilder()
                        .deNumeroRue(request.getParameter("numeroRue"))
                        .deNomRue(request.getParameter("nomRue"))
                        .deCodePostal(request.getParameter("codePostal"))
                        .deVille(request.getParameter("ville"))
                        .build();

                // Build prospect
                Prospect updatedProspect = ProspectBuilder.getNewProspectBuilder()
                        .dIdentifiant(id)
                        .deRaisonSociale(request.getParameter("raisonSociale"))
                        .deTelephone(request.getParameter("telephone"))
                        .deMail(request.getParameter("mail"))
                        .deCommentaires(request.getParameter("commentaires"))
                        .dAdresse(adresse)
                        .deDateProspection(request.getParameter("dateProspection"))
                        .build();

                prospectService.update(updatedProspect);
                urlSuite = "/prospects";
            }
        }

        return urlSuite;
    }
}
