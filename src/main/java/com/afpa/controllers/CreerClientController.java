package com.afpa.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

public class CreerClientController implements ICommand {
    private static final Logger LOGGER = Logger.getLogger(CreerClientController.class.getName());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if ("POST".equals(request.getMethod())) {
            try {
                // TODO: Implémenter la logique de création du client
                // Client client = new Client();
                // client.setNom(request.getParameter("nom"));
                // clientService.createClient(client);
                return "listeClients";
            } catch (Exception e) {
                LOGGER.severe("Erreur lors de la création du client: " + e.getMessage());
                throw e;
            }
        }
        return "/WEB-INF/views/clients/creer.jsp";
    }
} 