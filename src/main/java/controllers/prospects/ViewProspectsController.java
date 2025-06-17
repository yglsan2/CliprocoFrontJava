package controllers.prospects;

import controllers.ICommand;
import models.Prospect;
import services.ProspectService;
import utilities.Security;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

public final class ViewProspectsController implements ICommand {
    private final ProspectService prospectService;

    public ViewProspectsController(ProspectService prospectService) {
        this.prospectService = prospectService;
    }

    @Override
    public @NotNull String execute(final HttpServletRequest request,
                                   final HttpServletResponse response)
            throws Exception {
        request.setAttribute("titlePage", "Détails");
        request.setAttribute("titleGroup", "Prospects");
        String jsp = "prospects/view.jsp";
        String urlSuite = Security.estConnecte(request, jsp);

        if (jsp.equals(urlSuite)) {
            Long id = Long.parseLong(request.getParameter("id"));
            Prospect prospect = prospectService.findById(id);
            
            if (prospect != null) {
                request.setAttribute("prospect", prospect);
            }
        }

        return urlSuite;
    }
}
