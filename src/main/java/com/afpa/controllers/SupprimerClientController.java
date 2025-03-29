package com.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

public class SupprimerClientController implements ICommand {
    private static final Logger LOGGER = Logger.getLogger(SupprimerClientController.class.getName());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // TODO: Implémenter la logique de suppression du client
            // Long id = Long.parseLong(request.getParameter("id"));
            // clientService.deleteClient(id);
            return "listeClients";
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la suppression du client: " + e.getMessage());
            throw e;
        }
    }
} 