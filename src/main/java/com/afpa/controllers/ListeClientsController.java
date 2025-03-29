package com.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

public class ListeClientsController implements ICommand {
    private static final Logger LOGGER = Logger.getLogger(ListeClientsController.class.getName());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // TODO: Implémenter la logique de récupération des clients
            // request.setAttribute("clients", clientService.getAllClients());
            return "/WEB-INF/views/clients/liste.jsp";
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la récupération de la liste des clients: " + e.getMessage());
            throw e;
        }
    }
} 