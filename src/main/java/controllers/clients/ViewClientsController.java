package controllers.clients;

import controllers.ICommand;
import dao.jpa.ClientJpaDAO;
import models.Client;
import utilities.Security;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ViewClientsController implements ICommand {

    @Contract(pure = true)
    @Override
    public @NotNull String execute(final @NotNull HttpServletRequest request,
                                   final HttpServletResponse response)
            throws Exception {

        String jsp = "clients/view.jsp";
        String urlSuite = Security.estConnecte(request, jsp);

        if (jsp.equals(urlSuite)) {
            request.setAttribute("titlePage", "Consultation");
            request.setAttribute("titleGroup", "Clients");
            String clientId = request.getParameter("clientId");
            Client client = new ClientJpaDAO()
                    .findById(Long.parseLong(clientId));

            request.setAttribute("client", client);
        }

        return urlSuite;
    }
}
