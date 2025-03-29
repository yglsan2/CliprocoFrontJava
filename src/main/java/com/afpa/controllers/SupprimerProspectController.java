package com.afpa.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

public class SupprimerProspectController implements ICommand {
    private static final Logger LOGGER = Logger.getLogger(SupprimerProspectController.class.getName());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // TODO: Implémenter la logique de suppression du prospect
            // Long id = Long.parseLong(request.getParameter("id"));
            // prospectService.deleteProspect(id);
            return "listeProspects";
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la suppression du prospect: " + e.getMessage());
            throw e;
        }
    }
} 