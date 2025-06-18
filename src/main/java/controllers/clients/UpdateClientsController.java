package controllers.clients;

import builders.AdresseBuilder;
import builders.ClientBuilder;
import controllers.ICommand;
import services.ClientService;
import models.Adresse;
import models.Client;
import utilities.Security;
import utilities.LogManager;
import exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import dao.IDAO;
import dao.jpa.ClientJpaDAO;
import dao.jpa.AdresseJpaDAO;

import java.util.Set;

public final class UpdateClientsController implements ICommand {
    private final ClientService clientService;

    public UpdateClientsController(ClientService clientService) {
        LogManager.logInfo("Initialisation de UpdateClientsController");
        this.clientService = clientService;
        LogManager.logInfo("ClientService injecté avec succès");
    }

    @Contract(pure = true)
    @Override
    public @NotNull String execute(final HttpServletRequest request,
                                   final HttpServletResponse response)
            throws Exception {
        LogManager.logInfo("Début de l'exécution de UpdateClientsController");

        String jsp = "clients/view.jsp";
        String urlSuite = Security.estConnecte(request, jsp);
        LogManager.logInfo("URL suite après vérification de connexion: " + urlSuite);

        if (jsp.equals(urlSuite)) {
            LogManager.logInfo("Utilisateur connecté, préparation de la mise à jour");
            request.setAttribute("titlePage", "Mise à jour");
            request.setAttribute("titleGroup", "Clients");

            String clientId = request.getParameter("clientId");
            LogManager.logInfo("ID du client à mettre à jour: " + clientId);

            try {
                Client client = clientService.findById(Integer.parseInt(clientId))
                    .orElseThrow(() -> new ResourceNotFoundException("Client non trouvé avec l'ID: " + clientId));
                LogManager.logInfo("Client trouvé: " + client);

                if (request.getParameterMap().containsKey("raisonSociale")) {
                    LogManager.logInfo("Données de mise à jour reçues, construction du client");
                    Adresse adresse;

                    try {
                        // Set Adresse fields from request parameters
                        LogManager.logInfo("Construction de l'adresse");
                        adresse = AdresseBuilder.getNewAdresseBuilder()
                                .deNumeroRue(request.getParameter("numeroRue"))
                                .deNomRue(request.getParameter("nomRue"))
                                .deCodePostal(request.getParameter("codePostal"))
                                .deVille(request.getParameter("ville"))
                                .build();
                        LogManager.logInfo("Adresse construite avec succès: " + adresse);

                        // Set Client fields
                        LogManager.logInfo("Construction du client");
                        Client updatedClient = ClientBuilder.getNewClientBuilder()
                                .dIdentifiant(Integer.parseInt(request.getParameter("identifiant")))
                                .deRaisonSociale(request.getParameter("raisonSociale"))
                                .deTelephone(request.getParameter("telephone"))
                                .deMail(request.getParameter("adresseMail"))
                                .deCommentaires(request.getParameter("commentaires"))
                                .dAdresse(adresse)
                                .deChiffreAffaires(Double.parseDouble(request.getParameter("chiffreAffaires")))
                                .deNombreEmployes(Integer.parseInt(request.getParameter("nbEmployes")))
                                .build();
                        LogManager.logInfo("Client construit avec succès: " + updatedClient);

                        LogManager.logInfo("Validation du client");
                        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
                        Set<ConstraintViolation<Client>> violations = validator.validate(updatedClient);

                        if (!violations.isEmpty()) {
                            LogManager.logWarning("Violations de contraintes détectées: " + violations.size());
                            request.setAttribute("violations", violations);
                        } else {
                            LogManager.logInfo("Aucune violation de contraintes, mise à jour du client");
                            clientService.update(updatedClient);
                            urlSuite = "redirect:?cmd=clients";
                            LogManager.logInfo("Redirection vers: " + urlSuite);
                        }
                    } catch (Exception e) {
                        LogManager.logException("Erreur lors de la construction ou de la mise à jour du client", e);
                        throw e;
                    }
                }

                request.setAttribute("client", client);
            } catch (Exception e) {
                LogManager.logException("Erreur lors du traitement de la mise à jour", e);
                throw e;
            }
        }

        LogManager.logInfo("Fin de l'exécution de UpdateClientsController, URL suite: " + urlSuite);
        return urlSuite;
    }
}
