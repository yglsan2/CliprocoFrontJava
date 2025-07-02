package controllers;

import models.Client;
import models.Prospect;
import models.Adresse;
import services.ClientService;
import services.ProspectService;
import utilities.ValidationManager;
import utilities.LogManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.mindrot.jbcrypt.BCrypt;
import exceptions.ValidationException;

public class RegisterController extends HttpServlet {
    private ClientService clientService = new ClientService();
    private ProspectService prospectService = new ProspectService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userType = req.getParameter("userType");
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String nom = req.getParameter("nom");
        String prenom = req.getParameter("prenom");
        String telephone = req.getParameter("telephone");
        String adresseStr = req.getParameter("adresse");
        String codePostal = req.getParameter("codePostal");
        String ville = req.getParameter("ville");

        try {
            // Validation de base (unicité, format, etc.)
            ValidationManager.isValidEmail(email);
            ValidationManager.isValidPhone(telephone);
            ValidationManager.isValidPostalCode(codePostal);
            // TODO: vérifier unicité email/username dans la base

            // Hash du mot de passe
            String hash = BCrypt.hashpw(password, BCrypt.gensalt());

            Adresse adresse = new Adresse();
            adresse.setNomRue(adresseStr);
            adresse.setCodePostal(codePostal);
            adresse.setVille(ville);

            if ("client".equals(userType)) {
                String raisonSociale = req.getParameter("raisonSociale");
                String chiffreAffaireStr = req.getParameter("chiffreAffaire");
                String nbrEmployeStr = req.getParameter("nbrEmploye");
                Integer chiffreAffaire = chiffreAffaireStr != null && !chiffreAffaireStr.isEmpty() ? Integer.parseInt(chiffreAffaireStr) : null;
                Integer nbrEmploye = nbrEmployeStr != null && !nbrEmployeStr.isEmpty() ? Integer.parseInt(nbrEmployeStr) : null;

                Client client = new Client();
                client.setNom(nom);
                client.setPrenom(prenom);
                client.setRaisonSociale(raisonSociale);
                client.setMail(email);
                client.setTelephone(telephone);
                client.setAdresse(adresse);
                client.setChiffreAffaire(chiffreAffaire);
                client.setNbrEmploye(nbrEmploye);
                client.setStatut("actif");
                client.setCommentaires("");
                // Stocke le hash du mot de passe dans un champ dédié si besoin
                // client.setPasswordHash(hash);
                clientService.create(client);
            } else {
                String dateProspection = req.getParameter("dateProspection");
                String prospectInteresse = req.getParameter("prospectInteresse");

                Prospect prospect = new Prospect();
                prospect.setNom(nom);
                prospect.setPrenom(prenom);
                prospect.setRaisonSociale("");
                prospect.setMail(email);
                prospect.setTelephone(telephone);
                prospect.setAdresse(adresse);
                prospect.setDateProspection(dateProspection);
                prospect.setProspectInteresse(prospectInteresse);
                prospect.setStatut("actif");
                prospect.setCommentaires("");
                // prospect.setPasswordHash(hash);
                prospectService.create(prospect);
            }
            req.setAttribute("success", "Compte créé avec succès. Vous pouvez vous connecter.");
            req.getRequestDispatcher("/WEB-INF/jsp/connexion.jsp").forward(req, resp);
        } catch (ValidationException e) {
            req.setAttribute("errorValidation", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/signin.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            req.setAttribute("errorFormat", "Format numérique invalide : " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/signin.jsp").forward(req, resp);
        } catch (IllegalArgumentException e) {
            req.setAttribute("errorArgument", "Erreur de saisie : " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/signin.jsp").forward(req, resp);
        } catch (Exception e) {
            LogManager.logWarning("Erreur inattendue lors de l'inscription : " + e.getMessage());
            req.setAttribute("errorGlobal", "Une erreur inattendue est survenue. Merci de réessayer.");
            req.getRequestDispatcher("/WEB-INF/jsp/signin.jsp").forward(req, resp);
        }
    }
} 