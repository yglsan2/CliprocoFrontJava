package com.cliproco.controller;

import com.cliproco.dao.ClientDao;
import com.cliproco.dao.ContratDao;
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
    private final ClientDao clientDao;
    private final ContratDao contratDao;
    private final ClientService clientService;
    private final ValidatorFactory factory;
    private final Validator validator;

    public ClientController() {
        this.clientDao = new ClientDao();
        this.contratDao = new ContratDao();
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
        
        try {
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
                request.setAttribute("error", "Action non reconnue : " + action);
                return "/WEB-INF/views/error/404.jsp";
            }
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Erreur de validation : " + e.getMessage());
            request.setAttribute("error", e.getMessage());
            return "/WEB-INF/views/error/400.jsp";
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur inattendue : " + e.getMessage(), e);
            request.setAttribute("error", "Une erreur inattendue s'est produite");
            return "/WEB-INF/views/error/500.jsp";
        }
    }

    private String listClients(HttpServletRequest request) {
        try {
            List<Client> clients = clientService.getAllClients();
        request.setAttribute("clients", clients);
            return "/WEB-INF/views/client/list.jsp";
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des clients", e);
            request.setAttribute("error", "Impossible de récupérer la liste des clients");
            return "/WEB-INF/views/error/500.jsp";
        }
    }

    private String createClient(HttpServletRequest request) {
        try {
        if (request.getMethod().equals("POST")) {
            Client client = new Client();
                // Remplir les champs du client à partir des paramètres de la requête
                client.setNomEntreprise(request.getParameter("nomEntreprise"));
                client.setSecteurActivite(request.getParameter("secteurActivite"));
                client.setChiffreAffaires(Double.parseDouble(request.getParameter("chiffreAffaires")));
                
                clientService.createClient(client);
                request.setAttribute("message", "Client créé avec succès");
                return listClients(request);
            }
            return "/WEB-INF/views/client/form.jsp";
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("client", request.getParameterMap());
            return "/WEB-INF/views/client/form.jsp";
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la création du client", e);
            request.setAttribute("error", "Impossible de créer le client");
            return "/WEB-INF/views/error/500.jsp";
        }
    }

    private String editClient(HttpServletRequest request) {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Client client = clientService.getClient(id);
            
            if (request.getMethod().equals("POST")) {
                // Mettre à jour les champs du client
                client.setNomEntreprise(request.getParameter("nomEntreprise"));
            client.setSecteurActivite(request.getParameter("secteurActivite"));
                client.setChiffreAffaires(Double.parseDouble(request.getParameter("chiffreAffaires")));
                
                clientService.updateClient(client);
                request.setAttribute("message", "Client mis à jour avec succès");
                return listClients(request);
            }
            
            request.setAttribute("client", client);
            return "/WEB-INF/views/client/form.jsp";
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            return "/WEB-INF/views/error/400.jsp";
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour du client", e);
            request.setAttribute("error", "Impossible de mettre à jour le client");
            return "/WEB-INF/views/error/500.jsp";
        }
    }

    private String deleteClient(HttpServletRequest request) {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            clientService.deleteClient(id);
            request.setAttribute("message", "Client supprimé avec succès");
            return listClients(request);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            return "/WEB-INF/views/error/400.jsp";
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression du client", e);
            request.setAttribute("error", "Impossible de supprimer le client");
            return "/WEB-INF/views/error/500.jsp";
        }
    }

    private String viewClient(HttpServletRequest request) {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Client client = clientService.getClient(id);
            List<Contrat> contrats = contratDao.findByClientId(id);
        
        request.setAttribute("client", client);
        request.setAttribute("contrats", contrats);
            return "/WEB-INF/views/client/view.jsp";
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            return "/WEB-INF/views/error/400.jsp";
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des détails du client", e);
            request.setAttribute("error", "Impossible de récupérer les détails du client");
            return "/WEB-INF/views/error/500.jsp";
        }
    }
} 