package controllers.prospects;

import controllers.ICommand;
import dao.jpa.ProspectJpaDAO;
import models.Prospect;
import utilities.Security;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ViewProspectsController implements ICommand {

    @Contract(pure = true)
    @Override
    public @NotNull String execute(final @NotNull HttpServletRequest request,
                                   final HttpServletResponse response)
            throws Exception {

        String jsp = "prospects/view.jsp";
        String urlSuite = Security.estConnecte(request, jsp);

        if (jsp.equals(urlSuite)) {
            request.setAttribute("titlePage", "Consultation");
            request.setAttribute("titleGroup", "Prospects");
            String prospectId = request.getParameter("prospectId");
            Prospect prospect = new ProspectJpaDAO()
                            .findById(Long.parseLong(prospectId));

            request.setAttribute("prospect", prospect);
        }

        return urlSuite;
    }
}
