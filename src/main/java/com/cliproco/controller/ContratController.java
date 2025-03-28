package com.cliproco.controller;

import com.cliproco.dao.ContratDAO;
import com.cliproco.model.Contrat;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

public class ContratController implements ICommand {
    private final ContratDAO contratDAO;

    public ContratController() {
        this.contratDAO = new ContratDAO();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String action = request.getPathInfo();
        
        if (action == null || action.equals("/")) {
            return listContrats(request);
        } else if (action.equals("/create")) {
            return createContrat(request);
        } else if (action.equals("/edit")) {
            return editContrat(request);
        } else if (action.equals("/delete")) {
            return deleteContrat(request);
        } else {
            return "WEB-INF/JSP/erreur.jsp";
        }
    }

    private String listContrats(HttpServletRequest request) {
        Integer clientId = Integer.parseInt(request.getParameter("clientId"));
        List<Contrat> contrats = contratDAO.findByIdClient(clientId);
        request.setAttribute("contrats", contrats);
        return "WEB-INF/JSP/contrat_list.jsp";
    }

    private String createContrat(HttpServletRequest request) {
        if (request.getMethod().equals("POST")) {
            Contrat contrat = new Contrat();
            contrat.setClientId(Integer.parseInt(request.getParameter("clientId")));
            contrat.setLibelle(request.getParameter("libelle"));
            contrat.setMontant(Double.parseDouble(request.getParameter("montant")));
            
            // TODO: Implémenter la méthode save dans ContratDAO
            return "redirect:/client/view?id=" + contrat.getClientId();
        }
        return "WEB-INF/JSP/contrat_form.jsp";
    }

    private String editContrat(HttpServletRequest request) {
        if (request.getMethod().equals("POST")) {
            Contrat contrat = new Contrat();
            contrat.setId(Integer.parseInt(request.getParameter("id")));
            contrat.setClientId(Integer.parseInt(request.getParameter("clientId")));
            contrat.setLibelle(request.getParameter("libelle"));
            contrat.setMontant(Double.parseDouble(request.getParameter("montant")));
            
            // TODO: Implémenter la méthode save dans ContratDAO
            return "redirect:/client/view?id=" + contrat.getClientId();
        }
        
        Integer id = Integer.parseInt(request.getParameter("id"));
        // TODO: Implémenter la méthode find dans ContratDAO
        return "WEB-INF/JSP/contrat_form.jsp";
    }

    private String deleteContrat(HttpServletRequest request) {
        Integer id = Integer.parseInt(request.getParameter("id"));
        Integer clientId = Integer.parseInt(request.getParameter("clientId"));
        
        // TODO: Implémenter la méthode delete dans ContratDAO
        return "redirect:/client/view?id=" + clientId;
    }
} 