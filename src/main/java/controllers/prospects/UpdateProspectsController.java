package controllers.prospects;

import builders.AdresseBuilder;
import controllers.ICommand;
import dao.jpa.ProspectJpaDAO;
import models.Adresse;
import models.Prospect;
import utilities.Security;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Set;

public final class UpdateProspectsController implements ICommand {

    @Contract(pure = true)
    @Override
    public @NotNull String execute(final HttpServletRequest request,
                                   final HttpServletResponse response)
            throws Exception {

        String jsp = "prospects/view.jsp";
        String urlSuite = Security.estConnecte(request, jsp);

        if (jsp.equals(urlSuite)) {
            request.setAttribute("titlePage", "Mise à jour");
            request.setAttribute("titleGroup", "Prospects");

            String prospectId = request.getParameter("prospectId");
            Prospect prospect = new ProspectJpaDAO()
                    .findById(Long.parseLong(prospectId));

            if (request.getParameterMap().containsKey("raisonSociale")) {
                Adresse adresse;

                // Build address from parameters
                adresse = AdresseBuilder.getNewAdresseBuilder()
                        .deNumeroRue(request.getParameter("numeroRue"))
                        .deNomRue(request.getParameter("nomRue"))
                        .deCodePostal(request.getParameter("codePostal"))
                        .deVille(request.getParameter("ville"))
                        .build();

                // Update prospect fields
                prospect.setRaisonSociale(request
                        .getParameter("raisonSociale"));
                prospect.setTelephone(request.getParameter("telephone"));
                prospect.setMail(request.getParameter("adresseMail"));
                prospect.setCommentaires(request.getParameter("commentaires"));
                prospect.setAdresse(adresse);
                prospect.setDateProspection(
                        LocalDate.parse(
                                request.getParameter("dateProspection")));
                prospect.setProspectInteresse(request.getParameter(
                        "prospectInteresse") != null ? "oui" : "non");

                // Validate prospect
                Validator validator = Validation.buildDefaultValidatorFactory()
                        .getValidator();
                Set<ConstraintViolation<Prospect>> violations =
                        validator.validate(prospect);

                if (!violations.isEmpty()) {
                    request.setAttribute("violations", violations);
                } else {
                    (new ProspectJpaDAO()).save(prospect);
                    urlSuite = "redirect:?cmd=prospects";
                }
            }

            request.setAttribute("prospect", prospect);
        }

        return urlSuite;
    }
}
