package controllers.prospects;

import controllers.ICommand;
import models.Prospect;
import services.ProspectService;
import utilities.Security;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class ListeProspectsController implements ICommand {
    private final ProspectService prospectService;

    public ListeProspectsController(ProspectService prospectService) {
        this.prospectService = prospectService;
    }

    @Override
    public @NotNull String execute(final HttpServletRequest request,
                                   final HttpServletResponse response)
            throws Exception {
        request.setAttribute("titlePage", "Liste");
        request.setAttribute("titleGroup", "Prospects");
        String jsp = "prospects/list.jsp";
        String urlSuite = Security.estConnecte(request, jsp);

        if (jsp.equals(urlSuite)) {
            List<Prospect> prospects = prospectService.findAll();
            request.setAttribute("prospects", prospects);
        }

        return urlSuite;
    }
}
