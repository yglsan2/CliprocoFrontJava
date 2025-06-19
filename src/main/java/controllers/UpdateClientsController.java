package controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.Client;
import models.Adresse;
import services.ClientService;
import utilities.Security;
import dao.IDAO;
import dao.jpa.ClientJpaDAO;
import dao.jpa.AdresseJpaDAO;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/update-client")
public class UpdateClientsController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(UpdateClientsController.class.getName());
    private final ClientService clientService;

    public UpdateClientsController() {
        IDAO<Client, Integer> clientDAO = new ClientJpaDAO();
        IDAO<Adresse, Integer> adresseDAO = new AdresseJpaDAO();
        this.clientService = new ClientService(clientDAO, adresseDAO);
        LOGGER.info("UpdateClientsController initialisé avec succès");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if ("index.jsp".equals(Security.estConnecte(request, "clients.jsp"))) {
            response.sendRedirect(request.getContextPath() + "/signin");
            return;
        }

        try {
            String clientId = request.getParameter("clientId");
            String raisonSociale = request.getParameter("raisonSociale");
            String telephone = request.getParameter("telephone");
            String email = request.getParameter("email");
            String commentaires = request.getParameter("commentaires");
            String chiffreAffairesStr = request.getParameter("chiffreAffaires");
            String nombreEmployesStr = request.getParameter("nombreEmployes");

            // Création de l'adresse
            Adresse adresse = new Adresse();
            adresse.setNumeroRue(request.getParameter("numeroRue"));
            adresse.setNomRue(request.getParameter("nomRue"));
            adresse.setCodePostal(request.getParameter("codePostal"));
            adresse.setVille(request.getParameter("ville"));
            adresse.setPays(request.getParameter("pays"));

            // Création du client
Client client = new Client();
            client.setIdentifiant(Integer.parseInt(clientId));
            client.setRaisonSociale(raisonSociale);
            client.setAdresse(adresse);
            client.setTelephone(telephone);
            client.setMail(email);
            client.setCommentaires(commentaires);
            client.setChiffreAffaire(Integer.parseInt(chiffreAffairesStr));
            client.setNbrEmploye(Integer.parseInt(nombreEmployesStr));

            clientService.update(client);
            request.getSession().setAttribute("successMessage", "Client mis à jour avec succès");
            response.sendRedirect(request.getContextPath() + "/clients");
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la mise à jour du client : " + e.getMessage());
            request.getSession().setAttribute("errorMessage", "Erreur lors de la mise à jour du client");
            response.sendRedirect(request.getContextPath() + "/clients");
        }
    }
} 