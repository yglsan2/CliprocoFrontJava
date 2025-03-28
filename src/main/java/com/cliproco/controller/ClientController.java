package com.cliproco.controller;

import com.cliproco.dao.ClientDAO;
import com.cliproco.dao.ContratDAO;
import com.cliproco.model.Client;
import com.cliproco.model.Contrat;
import com.cliproco.service.ClientService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.logging.Level;

@Named("clientController")
@RequestScoped
public class ClientController implements ICommand {
    private static final Logger LOGGER = Logger.getLogger(ClientController.class.getName());
    private final ClientDAO clientDAO;
    private final ContratDAO contratDAO;
    private final ClientService clientService;
    private final ValidatorFactory factory;
    private final Validator validator;

    public ClientController() {
        this.clientDAO = new ClientDAO();
        this.contratDAO = new ContratDAO();
        this.clientService = new ClientService();
        this.factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String action = request.getPathInfo();
        
        // Log des paramètres de la requête
        Map<String, String[]> parameterMap = request.getParameterMap();
        LOGGER.info("Paramètres de la requête :");
        parameterMap.forEach((key, value) -> 
            LOGGER.info(key + " = " + String.join(", ", value))
        );
        
        if (action == null || action.equals("/")) {
            return listClients(request);
        } else if (action.equals("/create")) {
            return createClient(request);
        } else if (action.equals("/edit")) {
            return editClient(request);
        } else if (action.equals("/delete")) {
            return deleteClient(request);
        } else if (action.equals("/view")) {
            return viewClient(request);
        } else {
            return "WEB-INF/JSP/erreur.jsp";
        }
    }

    private String listClients(HttpServletRequest request) {
        List<Client> clients = clientDAO.findAll();
        request.setAttribute("clients", clients);
        request.setAttribute("message", "Liste des clients");
        return "WEB-INF/JSP/client_list.jsp";
    }

    private String createClient(HttpServletRequest request) {
        if (request.getMethod().equals("POST")) {
            // Récupération des paramètres du formulaire
            String nom = request.getParameter("nom");
            String email = request.getParameter("email");
            String telephone = request.getParameter("telephone");
            String secteurActivite = request.getParameter("secteurActivite");
            
            // Vérification de la présence des paramètres obligatoires
            if (nom == null || email == null || telephone == null || secteurActivite == null) {
                request.setAttribute("error", "Tous les champs sont obligatoires");
                return "WEB-INF/JSP/client_form.jsp";
            }
            
            Client client = new Client();
            client.setNom(nom);
            client.setEmail(email);
            client.setTelephone(telephone);
            client.setSecteurActivite(secteurActivite);
            
            // Validation du client
            Set<ConstraintViolation<Client>> violations = validator.validate(client);
            if (!violations.isEmpty()) {
                LOGGER.log(Level.WARNING, "Violations de validation : {0}", violations);
                request.setAttribute("errors", violations);
                request.setAttribute("client", client);
                return "WEB-INF/JSP/client_form.jsp";
            }
            
            clientDAO.save(client);
            request.setAttribute("message", "Client créé avec succès");
            return "redirect:/client/list";
        }
        return "WEB-INF/JSP/client_form.jsp";
    }

    private String editClient(HttpServletRequest request) {
        if (request.getMethod().equals("POST")) {
            // Récupération des paramètres du formulaire
            String idStr = request.getParameter("id");
            if (idStr == null || idStr.isEmpty()) {
                request.setAttribute("error", "ID du client manquant");
                return "WEB-INF/JSP/erreur.jsp";
            }
            
            Client client = new Client();
            client.setId(Integer.parseInt(idStr));
            client.setNom(request.getParameter("nom"));
            client.setEmail(request.getParameter("email"));
            client.setTelephone(request.getParameter("telephone"));
            client.setSecteurActivite(request.getParameter("secteurActivite"));
            
            // Validation du client
            Set<ConstraintViolation<Client>> violations = validator.validate(client);
            if (!violations.isEmpty()) {
                LOGGER.log(Level.WARNING, "Violations de validation : {0}", violations);
                request.setAttribute("errors", violations);
                request.setAttribute("client", client);
                return "WEB-INF/JSP/client_form.jsp";
            }
            
            clientDAO.save(client);
            request.setAttribute("message", "Client modifié avec succès");
            return "redirect:/client/list";
        }
        
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            request.setAttribute("error", "ID du client manquant");
            return "WEB-INF/JSP/erreur.jsp";
        }
        
        Client client = clientDAO.find(Integer.parseInt(idStr));
        if (client == null) {
            request.setAttribute("error", "Client non trouvé");
            return "WEB-INF/JSP/erreur.jsp";
        }
        
        request.setAttribute("client", client);
        return "WEB-INF/JSP/client_form.jsp";
    }

    private String deleteClient(HttpServletRequest request) {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            request.setAttribute("error", "ID du client manquant");
            return "WEB-INF/JSP/erreur.jsp";
        }
        
        clientDAO.delete(Integer.parseInt(idStr));
        request.setAttribute("message", "Client supprimé avec succès");
        return "redirect:/client/list";
    }

    private String viewClient(HttpServletRequest request) {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            request.setAttribute("error", "ID du client manquant");
            return "WEB-INF/JSP/erreur.jsp";
        }
        
        Client client = clientDAO.find(Integer.parseInt(idStr));
        if (client == null) {
            request.setAttribute("error", "Client non trouvé");
            return "WEB-INF/JSP/erreur.jsp";
        }
        
        List<Contrat> contrats = contratDAO.findByIdClient(client.getId());
        
        request.setAttribute("client", client);
        request.setAttribute("contrats", contrats);
        return "WEB-INF/JSP/client_view.jsp";
    }
} 