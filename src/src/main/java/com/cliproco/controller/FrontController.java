package com.cliproco.controller;

import com.cliproco.dao.DaoClient;
import com.cliproco.model.Client;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/")
public class FrontController extends HttpServlet {
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private DaoClient daoClient;

    @Override
    public void init() throws ServletException {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("cliprocoPU");
            entityManager = entityManagerFactory.createEntityManager();
            daoClient = new DaoClient(entityManager);
        } catch (Exception e) {
            throw new ServletException("Erreur lors de l'initialisation de JPA", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String cmd = request.getParameter("cmd");
        
        try {
            switch (cmd) {
                case "clients":
                    List<Client> clients = daoClient.findAll();
                    request.setAttribute("clients", clients);
                    request.getRequestDispatcher("/WEB-INF/views/clients/list.jsp").forward(request, response);
                    break;
                    
                case "createClient":
                    request.getRequestDispatcher("/WEB-INF/views/clients/create.jsp").forward(request, response);
                    break;
                    
                case "editClient":
                    Long id = Long.parseLong(request.getParameter("id"));
                    Client client = daoClient.findById(id);
                    request.setAttribute("client", client);
                    request.getRequestDispatcher("/WEB-INF/views/clients/edit.jsp").forward(request, response);
                    break;
                    
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Commande non trouvée");
            }
        } catch (Exception e) {
            throw new ServletException("Erreur lors du traitement de la requête", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String cmd = request.getParameter("cmd");
        
        try {
            switch (cmd) {
                case "createClient":
                case "editClient":
                    Client client = new Client();
                    if (cmd.equals("editClient")) {
                        client.setId(Long.parseLong(request.getParameter("id")));
                    }
                    client.setNom(request.getParameter("nom"));
                    client.setPrenom(request.getParameter("prenom"));
                    client.setEmail(request.getParameter("email"));
                    client.setTelephone(request.getParameter("telephone"));
                    client.setEntreprise(request.getParameter("entreprise"));
                    client.setAdresse(request.getParameter("adresse"));
                    
                    daoClient.save(client);
                    response.sendRedirect(request.getContextPath() + "/?cmd=clients");
                    break;
                    
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Commande non trouvée");
            }
        } catch (Exception e) {
            throw new ServletException("Erreur lors du traitement de la requête", e);
        }
    }

    @Override
    public void destroy() {
        if (entityManager != null) {
            entityManager.close();
        }
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
} 