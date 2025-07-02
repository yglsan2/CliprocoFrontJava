package controllers.prospects;

import controllers.ICommand;
import models.Prospect;
import models.Adresse;
import services.ProspectService;
import builders.AdresseBuilder;
import builders.ProspectBuilder;
import utilities.Security;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import exceptions.ValidationException;

public final class CreationProspectsController implements ICommand {
    private final ProspectService prospectService;

    public CreationProspectsController(ProspectService prospectService) {
        this.prospectService = prospectService;
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("titlePage", "Création");
        request.setAttribute("titleGroup", "Prospects");
        String jsp = "prospects/create.jsp";
        String urlSuite = Security.estConnecte(request, jsp);

        if (jsp.equals(urlSuite)) {
            try {
                Adresse adresse = AdresseBuilder.getNewAdresseBuilder()
                        .deNumeroRue(request.getParameter("numeroRue"))
                        .deNomRue(request.getParameter("nomRue"))
                        .deCodePostal(request.getParameter("codePostal"))
                        .deVille(request.getParameter("ville"))
                        .build();

                Prospect prospect = ProspectBuilder.getNewProspectBuilder()
                        .deRaisonSociale(request.getParameter("raisonSociale"))
                        .deTelephone(request.getParameter("telephone"))
                        .deMail(request.getParameter("mail"))
                        .deCommentaires(request.getParameter("commentaires"))
                        .dAdresse(adresse)
                        .deDateProspection(request.getParameter("dateProspection"))
                        .deProspectInteresse(request.getParameter("prospectInteresse"))
                        .build();

                // Ici, on peut ajouter des validations supplémentaires si besoin
                // ValidationManager.isValidPhone(prospect.getTelephone());
                // ValidationManager.isValidPostalCode(adresse.getCodePostal());
                // ValidationManager.isValidEmail(prospect.getMail());

                prospectService.save(prospect);
                urlSuite = "/prospects";
            } catch (ValidationException e) {
                request.setAttribute("errorValidation", e.getMessage());
            } catch (NumberFormatException e) {
                request.setAttribute("errorFormat", "Format numérique invalide : " + e.getMessage());
            } catch (IllegalArgumentException e) {
                request.setAttribute("errorArgument", "Erreur de saisie : " + e.getMessage());
            } catch (Exception e) {
                request.setAttribute("errorGlobal", "Une erreur inattendue est survenue. Merci de réessayer.");
            } finally {
                // Log ou nettoyage éventuel
            }
        }

        return urlSuite;
    }
}
