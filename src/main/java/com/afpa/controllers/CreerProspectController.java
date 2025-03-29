package com.afpa.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

public class CreerProspectController implements ICommand {
    private static final Logger LOGGER = Logger.getLogger(CreerProspectController.class.getName());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if ("POST".equals(request.getMethod())) {
            try {
                // TODO: Implémenter la logique de création du prospect
                // Prospect prospect = new Prospect();
                // prospect.setNom(request.getParameter("nom"));
                // prospectService.createProspect(prospect);
                return "listeProspects";
            } catch (Exception e) {
                LOGGER.severe("Erreur lors de la création du prospect: " + e.getMessage());
                throw e;
            }
        }
        return "/WEB-INF/views/prospects/creer.jsp";
    }
} 