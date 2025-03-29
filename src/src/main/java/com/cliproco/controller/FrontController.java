package com.cliproco.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/*")
public class FrontController extends HttpServlet {
    private Map<String, String> urlMappings;
    private Map<String, Boolean> adminUrls;

    @Override
    public void init() throws ServletException {
        urlMappings = new HashMap<>();
        urlMappings.put("home", "/WEB-INF/views/home.jsp");
        urlMappings.put("login", "/WEB-INF/views/login.jsp");
        urlMappings.put("logout", "/logout");
        urlMappings.put("admin", "/admin");
        urlMappings.put("createAdmin", "/createAdmin"); // À supprimer après la création de l'admin
        urlMappings.put("clients", "/WEB-INF/views/clients/list.jsp");
        urlMappings.put("createClient", "/WEB-INF/views/clients/create.jsp");
        urlMappings.put("editClient", "/WEB-INF/views/clients/edit.jsp");
        urlMappings.put("deleteClient", "/clients/delete");

        // URLs qui nécessitent une authentification
        adminUrls = new HashMap<>();
        adminUrls.put("admin", true);
        adminUrls.put("clients", true);
        adminUrls.put("createClient", true);
        adminUrls.put("editClient", true);
        adminUrls.put("deleteClient", true);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String cmd = request.getParameter("cmd");
        if (cmd == null) {
            cmd = "home"; // Page par défaut
        }

        // Vérifier si l'URL nécessite une authentification
        if (adminUrls.containsKey(cmd) && adminUrls.get(cmd)) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                // Stocker l'URL demandée pour rediriger après la connexion
                session = request.getSession(true);
                session.setAttribute("redirectUrl", request.getRequestURI() + "?" + request.getQueryString());
                response.sendRedirect(request.getContextPath() + "/?cmd=login");
                return;
            }
        }

        if (urlMappings.containsKey(cmd)) {
            request.getRequestDispatcher(urlMappings.get(cmd)).forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Commande non trouvée");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
} 