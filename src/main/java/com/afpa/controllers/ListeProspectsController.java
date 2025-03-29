package com.afpa.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

public class ListeProspectsController implements ICommand {
    private static final Logger LOGGER = Logger.getLogger(ListeProspectsController.class.getName());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // TODO: Implémenter la logique de récupération des prospects
            // request.setAttribute("prospects", prospectService.getAllProspects());
            return "/WEB-INF/views/prospects/liste.jsp";
        } catch (Exception e) {
            LOGGER.severe("Erreur lors de la récupération de la liste des prospects: " + e.getMessage());
            throw e;
        }
    }
} 