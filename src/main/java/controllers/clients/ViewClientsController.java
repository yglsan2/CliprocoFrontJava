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

@WebServlet("/clients/view")
public class ViewClientsController extends HttpServlet {
    private ClientService clientService;

    @Override
    public void init() throws ServletException {
        clientService = new ClientService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Client client = clientService.findById(id);
            if (client != null) {
                request.setAttribute("client", client);
                request.getRequestDispatcher("/WEB-INF/views/clients/view.jsp")
                       .forward(request, response);
            } else {
                request.setAttribute("error", "Client non trouvé");
                request.getRequestDispatcher("/WEB-INF/views/error.jsp")
                       .forward(request, response);
            }
        } catch (DatabaseException e) {
            request.setAttribute("error", "Erreur lors de la récupération du client");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp")
                   .forward(request, response);
        }
    }

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
            try {
                Client client = new ClientJpaDAO().findById(Long.parseLong(clientId));
                request.setAttribute("client", client);
            } catch (DatabaseException e) {
                request.setAttribute("error", "Erreur lors de la recherche du client : " + e.getMessage());
            }
        }

        return urlSuite;
    }
}
