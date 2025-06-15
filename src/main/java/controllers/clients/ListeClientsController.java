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

import java.util.ArrayList;

public final class ListeClientsController implements ICommand {

    @Contract(pure = true)
    @Override
    public @NotNull String execute(final HttpServletRequest request,
                                   final HttpServletResponse response)
            throws Exception {

        request.setAttribute("titlePage", "Liste");
        request.setAttribute("titleGroup", "Clients");
        String jsp = "clients/list.jsp";

        String urlSuite = Security.estConnecte(request, jsp);

        if (jsp.equals(urlSuite)) {
            try {
                ClientJpaDAO clientJpaDAO = new ClientJpaDAO();
                ArrayList<Client> clients = new ArrayList<>(clientJpaDAO.findAll());
                request.setAttribute("clients", clients);
            } catch (DatabaseException e) {
                LogManager.logException("Erreur lors de la récupération des clients", e);
                request.setAttribute("error", "Erreur lors de la récupération des clients");
            }
        }

        return urlSuite;
    }
}
