package controllers.prospects;

import controllers.ICommand;
import models.Prospect;
import services.ProspectService;
import utilities.Security;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public final class DeleteProspectsController implements ICommand {
    private final ProspectService prospectService;

    public DeleteProspectsController(ProspectService prospectService) {
        this.prospectService = prospectService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String jsp = "prospects/delete.jsp";
        String urlSuite = Security.estConnecte(request, jsp);

        if (jsp.equals(urlSuite)) {
            Integer id = Integer.parseInt(request.getParameter("id"));
            Prospect prospect = prospectService.findById(id);
            
            if (prospect != null) {
                prospectService.delete(prospect);
                urlSuite = "/prospects";
            }
        }

        return urlSuite;
    }
}
