package controllers.clients;

import builders.AdresseBuilder;
import builders.ClientBuilder;
import controllers.ICommand;
import dao.jpa.ClientJpaDAO;
import models.Adresse;
import models.Client;
import utilities.Security;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import exceptions.DatabaseException;
import services.ClientService;
import utilities.LogManager;

import java.util.Set;

public final class UpdateClientsController implements ICommand {

    @Contract(pure = true)
    @Override
    public @NotNull String execute(final HttpServletRequest request,
                                   final HttpServletResponse response)
            throws Exception {

        String jsp = "clients/view.jsp";
        String urlSuite = Security.estConnecte(request, jsp);

        if (jsp.equals(urlSuite)) {
            request.setAttribute("titlePage", "Mise à jour");
            request.setAttribute("titleGroup", "Clients");

            String clientId = request.getParameter("clientId");
            Client client = null;
            try {
                client = new ClientJpaDAO().findById(Long.parseLong(clientId));
            } catch (DatabaseException e) {
                request.setAttribute("error", "Erreur lors de la recherche du client : " + e.getMessage());
                return jsp;
            }

            if (request.getParameterMap().containsKey("raisonSociale")) {
                Adresse adresse;

                // Set Adresse fields from request parameters
                adresse = AdresseBuilder.getNewAdresseBuilder()
                        .deNumeroRue(request.getParameter("numeroRue"))
                        .deNomRue(request.getParameter("nomRue"))
                        .deCodePostal(request.getParameter("codePostal"))
                        .deVille(request.getParameter("ville"))
                        .build();

                // Build client with specific fields
                ClientBuilder builder = ClientBuilder.getNewClientBuilder()
                        .dIdentifiant(Long.parseLong(request.getParameter("id")))
                        .deRaisonSociale(request.getParameter("raisonSociale"))
                        .deTelephone(request.getParameter("telephone"))
                        .dEmail(request.getParameter("adresseMail"))
                        .deCommentaire(request.getParameter("commentaires"))
                        .dAdresse(adresse)
                        .avecChiffreAffaires(Double.parseDouble(
                                request.getParameter("chiffreAffaires")))
                        .avecNombreEmployes(Integer.parseInt(
                                request.getParameter("nombreEmployes")));
                client = builder.build();

                Validator validator = Validation.buildDefaultValidatorFactory()
                        .getValidator();
                Set<ConstraintViolation<Client>> violations =
                        validator.validate(client);

                if (!violations.isEmpty()) {
                    request.setAttribute("violations", violations);
                } else {
                    try {
                        (new ClientJpaDAO()).save(client);
                        urlSuite = "redirect:?cmd=clients";
                    } catch (DatabaseException e) {
                        request.setAttribute("error", "Erreur lors de la mise à jour du client : " + e.getMessage());
                    }
                }
            }

            request.setAttribute("client", client);
        }

        return urlSuite;
    }
}
