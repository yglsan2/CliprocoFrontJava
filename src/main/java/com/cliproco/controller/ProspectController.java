package com.cliproco.controller;

import com.cliproco.dao.ProspectDAO;
import com.cliproco.model.Prospect;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.io.IOException;
import java.util.List;

@WebServlet("/prospect/*")
public class ProspectController extends HttpServlet {
    private ProspectDAO prospectDAO;
    private EntityManager entityManager;

    @Override
    public void init() throws ServletException {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("cliproco");
        entityManager = emf.createEntityManager();
        prospectDAO = new ProspectDAO(entityManager);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String path = request.getPathInfo();
        
        if (path == null || path.equals("/")) {
            // Liste des prospects
            List<Prospect> prospects = prospectDAO.findAll();
            request.setAttribute("prospects", prospects);
            request.getRequestDispatcher("/WEB-INF/JSP/prospect/list.jsp").forward(request, response);
        } else if (path.equals("/view")) {
            // Vue détaillée d'un prospect
            Integer id = Integer.parseInt(request.getParameter("id"));
            Prospect prospect = prospectDAO.find(id);
            if (prospect != null) {
                request.setAttribute("prospect", prospect);
                request.getRequestDispatcher("/WEB-INF/JSP/prospect/view.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Prospect non trouvé");
            }
        } else if (path.equals("/edit")) {
            // Formulaire de modification
            Integer id = Integer.parseInt(request.getParameter("id"));
            Prospect prospect = prospectDAO.find(id);
            if (prospect != null) {
                request.setAttribute("prospect", prospect);
                request.getRequestDispatcher("/WEB-INF/JSP/prospect/form.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Prospect non trouvé");
            }
        } else if (path.equals("/create")) {
            // Formulaire de création
            request.getRequestDispatcher("/WEB-INF/JSP/prospect/form.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page non trouvée");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String path = request.getPathInfo();
        
        if (path.equals("/save")) {
            // Sauvegarde d'un prospect
            Prospect prospect = new Prospect();
            if (request.getParameter("id") != null && !request.getParameter("id").isEmpty()) {
                prospect.setId(Integer.parseInt(request.getParameter("id")));
                Prospect existingProspect = prospectDAO.find(prospect.getId());
                if (existingProspect != null) {
                    prospect = existingProspect;
                }
            }
            
            prospect.setNom(request.getParameter("nom"));
            prospect.setEmail(request.getParameter("email"));
            prospect.setTelephone(request.getParameter("telephone"));
            prospect.setSecteurActivite(request.getParameter("secteur"));
            prospect.setStatut(Prospect.Statut.valueOf(request.getParameter("statut")));
            prospect.setCommentaires(request.getParameter("commentaires"));
            
            prospectDAO.save(prospect);
            response.sendRedirect(request.getContextPath() + "/prospect");
        } else if (path.equals("/delete")) {
            // Suppression d'un prospect
            Integer id = Integer.parseInt(request.getParameter("id"));
            Prospect prospect = prospectDAO.find(id);
            if (prospect != null) {
                prospectDAO.delete(prospect);
            }
            response.sendRedirect(request.getContextPath() + "/prospect");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Action non trouvée");
        }
    }

    @Override
    public void destroy() {
        if (entityManager != null) {
            entityManager.close();
        }
    }
} 