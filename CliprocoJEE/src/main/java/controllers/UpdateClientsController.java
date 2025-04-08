package controllers;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import models.Client;
import models.Adresse;
import services.ClientService;

@Controller
@RequestMapping("/clients")
public class UpdateClientsController {

    @Autowired
    private ClientService clientService;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long id = Long.parseLong(request.getParameter("id"));
        String raisonSociale = request.getParameter("raisonSociale");
        String telephone = request.getParameter("telephone");
        String email = request.getParameter("email");
        String commentaires = request.getParameter("commentaires");
        Double chiffreAffaires = Double.parseDouble(request.getParameter("chiffreAffaires"));
        Integer nombreEmployes = Integer.parseInt(request.getParameter("nombreEmployes"));

        Adresse adresse = new Adresse();
        adresse.setRue(request.getParameter("rue"));
        adresse.setCodePostal(request.getParameter("codePostal"));
        adresse.setVille(request.getParameter("ville"));
        adresse.setPays(request.getParameter("pays"));

        Client client = new Client(raisonSociale, adresse, telephone, email, commentaires, chiffreAffaires, nombreEmployes, new ArrayList<>());
        client.setId(id);

        clientService.updateClient(client);
        return "clients";
    }
} 