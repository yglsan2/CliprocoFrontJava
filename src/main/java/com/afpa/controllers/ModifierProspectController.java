package com.afpa.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

public class ModifierProspectController implements ICommand {
    private static final Logger LOGGER = Logger.getLogger(ModifierProspectController.class.getName());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if ("POST".equals(request.getMethod())) {
            try {
                // TODO: Implémenter la logique de modification du prospect
                // Long id = Long.parseLong(request.getParameter("id"));
                // Prospect prospect = prospectService.getProspectById(id);
                // prospect.setNom(request.getParameter("nom"));
                // prospectService.updateProspect(prospect);
                return "listeProspects";
            } catch (Exception e) {
                LOGGER.severe("Erreur lors de la modification du prospect: " + e.getMessage());
                throw e;
            }
        }
        try {
            // TODO: Récupérer le prospect à modifier
            // Long id = Long.parseLong(request.getParameter("id"));
            // Prospect prospect = prospectService.getProspectById(id);
            // request.setAttribute("prospect", prospect);
            return "/WEB-INF/views/prospects/modifier.jsp";
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la récupération du prospect: " + e.getMessage());
            throw e;
        }
    }
} 