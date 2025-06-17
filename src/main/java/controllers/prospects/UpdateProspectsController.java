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

import java.time.LocalDate;

public final class UpdateProspectsController implements ICommand {
    private final ProspectService prospectService;

    public UpdateProspectsController(ProspectService prospectService) {
        this.prospectService = prospectService;
    }

    @Override
    public @NotNull String execute(final HttpServletRequest request,
                                   final HttpServletResponse response)
            throws Exception {
        request.setAttribute("titlePage", "Modification");
        request.setAttribute("titleGroup", "Prospects");
        String jsp = "prospects/update.jsp";
        String urlSuite = Security.estConnecte(request, jsp);

        if (jsp.equals(urlSuite)) {
            Long id = Long.parseLong(request.getParameter("id"));
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
                        .deDateProspection(LocalDate.parse(request.getParameter("dateProspection")))
                        .deProspectInteresse(request.getParameter("prospectInteresse"))
                        .build();

                prospectService.update(updatedProspect);
                urlSuite = "/prospects";
            }
        }

        return urlSuite;
    }
}
