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

                // Set Client fields
                client = ClientBuilder.getNewClientBuilder()
                        .deRaisonSociale(request
                                        .getParameter("raisonSociale"))
                        .deTelephone(request.getParameter("telephone"))
                        .deMail(request.getParameter("adresseMail"))
                        .deCommentaires(request.getParameter("commentaires"))
                        .dAdresse(adresse)
                        .deChiffreAffaires(Double.parseDouble(request
                                        .getParameter("chiffreAffaires")))
                        .deNombreEmployes(Integer.parseInt(request.getParameter("nbEmployes")))
                        .build();

                Validator validator = Validation.buildDefaultValidatorFactory()
                                                .getValidator();
                Set<ConstraintViolation<Client>> violations =
                        validator.validate(client);

                if (!violations.isEmpty()) {
                    request.setAttribute("violations", violations);
                } else {
                    (new ClientJpaDAO()).save(client);

                    urlSuite = "redirect:?cmd=clients";
                }
            }
        }

        return urlSuite;
    }
}
