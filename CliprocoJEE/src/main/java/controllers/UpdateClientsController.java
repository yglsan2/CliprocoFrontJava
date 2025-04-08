package controllers;

import java.util.ArrayList;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import models.Client;
import models.Adresse;
import services.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/clients")
public class UpdateClientsController {

    private static final Logger logger = LoggerFactory.getLogger(UpdateClientsController.class);

    @Autowired
    private ClientService clientService;

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String execute(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            // Validation des paramètres obligatoires
            String idStr = request.getParameter("id");
            String raisonSociale = request.getParameter("raisonSociale");
            String telephone = request.getParameter("telephone");
            String email = request.getParameter("email");

            if (idStr == null || raisonSociale == null || raisonSociale.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "L'identifiant et la raison sociale sont obligatoires");
                return "redirect:/clients";
            }

            Long id = Long.parseLong(idStr);

            // Création de l'adresse avec validation
            Adresse adresse = new Adresse();
            String numeroRue = request.getParameter("numeroRue");
            String nomRue = request.getParameter("nomRue");
            String codePostal = request.getParameter("codePostal");
            String ville = request.getParameter("ville");
            String pays = request.getParameter("pays");

            if (numeroRue != null) adresse.setNumeroRue(numeroRue);
            if (nomRue != null) adresse.setNomRue(nomRue);
            if (codePostal != null) adresse.setCodePostal(codePostal);
            if (ville != null) adresse.setVille(ville);
            if (pays != null) adresse.setPays(pays);

            // Création du client avec gestion des valeurs optionnelles
            Client client = new Client(
                raisonSociale,
                adresse,
                telephone,
                email,
                request.getParameter("commentaires"),
                parseDoubleSafely(request.getParameter("chiffreAffaires")),
                parseIntSafely(request.getParameter("nombreEmployes"))
            );
            client.setIdentifiant(id);

            // Mise à jour du client
            clientService.update(client);
            logger.info("Client {} mis à jour avec succès", id);
            redirectAttributes.addFlashAttribute("success", "Client mis à jour avec succès");

        } catch (NumberFormatException e) {
            logger.error("Erreur de format de nombre", e);
            redirectAttributes.addFlashAttribute("error", "Format de nombre invalide");
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du client", e);
            redirectAttributes.addFlashAttribute("error", "Une erreur est survenue lors de la mise à jour");
        }

        return "redirect:/clients";
    }

    private Double parseDoubleSafely(String value) {
        try {
            return value != null && !value.trim().isEmpty() ? Double.parseDouble(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer parseIntSafely(String value) {
        try {
            return value != null && !value.trim().isEmpty() ? Integer.parseInt(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
} 