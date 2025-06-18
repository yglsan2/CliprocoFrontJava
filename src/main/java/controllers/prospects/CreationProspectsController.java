package controllers.prospects;

import controllers.ICommand;
import models.Prospect;
import models.Adresse;
import services.ProspectService;
import builders.AdresseBuilder;
import builders.ProspectBuilder;
import utilities.Security;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

public final class CreationProspectsController implements ICommand {
    private final ProspectService prospectService;

    public CreationProspectsController(ProspectService prospectService) {
        this.prospectService = prospectService;
    }

    @Override
    public @NotNull String execute(final HttpServletRequest request,
                                   final HttpServletResponse response)
            throws Exception {
        request.setAttribute("titlePage", "Création");
        request.setAttribute("titleGroup", "Prospects");
        String jsp = "prospects/create.jsp";
        String urlSuite = Security.estConnecte(request, jsp);

        if (jsp.equals(urlSuite)) {
            // Build address
            Adresse adresse = AdresseBuilder.getNewAdresseBuilder()
                    .deNumeroRue(request.getParameter("numeroRue"))
                    .deNomRue(request.getParameter("nomRue"))
                    .deCodePostal(request.getParameter("codePostal"))
                    .deVille(request.getParameter("ville"))
                    .build();

            // Build prospect
            Prospect prospect = ProspectBuilder.getNewProspectBuilder()
                    .deRaisonSociale(request.getParameter("raisonSociale"))
                    .deTelephone(request.getParameter("telephone"))
                    .deMail(request.getParameter("mail"))
                    .deCommentaires(request.getParameter("commentaires"))
                    .dAdresse(adresse)
                    .deDateProspection(request.getParameter("dateProspection"))
                    .deProspectInteresse(request.getParameter("prospectInteresse"))
                    .build();

            prospectService.save(prospect);
            urlSuite = "/prospects";
        }

        return urlSuite;
    }
}
