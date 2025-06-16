package controllers.clients;

import controllers.ICommand;
import models.Client;
import models.Adresse;
import services.ClientService;
import utilities.Security;
import utilities.LogManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class CreationClientsController implements ICommand {
    private final ClientService clientService;

    public CreationClientsController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String execute(final @NotNull HttpServletRequest request,
                                   final HttpServletResponse response)
            throws Exception {

        String jsp = "clients/create.jsp";
        String urlSuite = Security.estConnecte(request, jsp);

        if (jsp.equals(urlSuite)) {
            request.setAttribute("titlePage", "Création");
            request.setAttribute("titleGroup", "Clients");

            if (request.getMethod().equals("POST")) {
                String raisonSociale = request.getParameter("raisonSociale");
                String adresse = request.getParameter("adresse");
                String telephone = request.getParameter("telephone");
                String email = request.getParameter("email");
                String commentaire = request.getParameter("commentaire");
                Double chiffreAffaires = Double.parseDouble(request.getParameter("chiffreAffaires"));
                Integer nombreEmployes = Integer.parseInt(request.getParameter("nombreEmployes"));

                Adresse adresseObj = new Adresse(adresse);
                Client client = new Client(raisonSociale, adresseObj, telephone, email, commentaire, chiffreAffaires, nombreEmployes);
                clientService.create(client);
                urlSuite = "clients/list.jsp";
            }
        }

        return urlSuite;
    }
}
