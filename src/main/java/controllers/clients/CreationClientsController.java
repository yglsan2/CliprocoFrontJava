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
import org.jetbrains.annotations.NotNull;
import exceptions.DatabaseException;
import utilities.LogManager;

import java.util.Set;

public final class CreationClientsController implements ICommand {

    @Override
    public @NotNull String execute(final HttpServletRequest request,
                                   final HttpServletResponse response)
            throws Exception {

        request.setAttribute("titlePage", "Création");
        request.setAttribute("titleGroup", "Clients");
        String jsp = "clients/view.jsp";
        String urlSuite = Security.estConnecte(request, jsp);

        if (jsp.equals(urlSuite)) {
            if (request.getParameterMap().containsKey("raisonSociale")) {
                Client client;
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
                        LogManager.logException("Erreur lors de la création du client", e);
                        request.setAttribute("error", "Erreur lors de la création du client");
                    }
                }
            }
        }

        return urlSuite;
    }
}
