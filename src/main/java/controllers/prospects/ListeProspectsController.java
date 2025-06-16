package controllers.prospects;

import controllers.ICommand;
import dao.jpa.ProspectJpaDAO;
import models.Prospect;
import utilities.Security;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public final class ListeProspectsController implements ICommand {

    @Override
    public @NotNull String execute(final HttpServletRequest request,
                                   final HttpServletResponse response)
            throws Exception {
        request.setAttribute("titlePage", "Liste");
        request.setAttribute("titleGroup", "Prospects");
        String jsp = "prospects/list.jsp";
        String urlSuite = Security.estConnecte(request, jsp);

        if (jsp.equals(urlSuite)) {
            request.setAttribute("prospects", new ArrayList<>(new ProspectJpaDAO().findAll()));
        }

        return urlSuite;
    }
}
