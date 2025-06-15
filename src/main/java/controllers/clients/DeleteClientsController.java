package controllers.clients;

import controllers.ICommand;
import dao.jpa.ClientJpaDAO;
import models.Client;
import utilities.Security;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import exceptions.DatabaseException;
import utilities.LogManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import java.io.IOException;
import services.ClientService;

@WebServlet("/clients/delete")
public class DeleteClientsController extends HttpServlet {
    private ClientService clientService;

    @Override
    public void init() throws ServletException {
        clientService = new ClientService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Client client = clientService.findById(id);
            if (client != null) {
                clientService.delete(client);
                response.sendRedirect(request.getContextPath() + "/clients/liste");
            } else {
                request.setAttribute("error", "Client non trouvé");
                request.getRequestDispatcher("/WEB-INF/views/error.jsp")
                       .forward(request, response);
            }
        } catch (DatabaseException e) {
            request.setAttribute("error", "Erreur lors de la suppression du client");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp")
                   .forward(request, response);
        }
    }

    @Contract(pure = true)
    @Override
    public @NotNull String execute(final HttpServletRequest request,
                                   final HttpServletResponse response)
            throws Exception {

        String jsp = "clients/view.jsp";
        String urlSuite = Security.estConnecte(request, jsp);

        if (jsp.equals(urlSuite)) {
            request.setAttribute("titlePage", "Suppression");
            request.setAttribute("titleGroup", "Clients");

            String clientId = request.getParameter("clientId");
            Client client = null;
            try {
                client = new ClientJpaDAO().findById(Long.parseLong(clientId));
            } catch (DatabaseException e) {
                request.setAttribute("error", "Erreur lors de la recherche du client : " + e.getMessage());
                return jsp;
            }

            if (request.getParameterMap().containsKey("delete")) {
                try {
                    (new ClientJpaDAO()).delete(client);
                    urlSuite = "redirect:?cmd=clients";
                } catch (DatabaseException e) {
                    LogManager.logException("Erreur lors de la suppression du client", e);
                    request.setAttribute("error", "Erreur lors de la suppression du client");
                }
            }

            request.setAttribute("client", client);
        }

        return urlSuite;
    }
}
