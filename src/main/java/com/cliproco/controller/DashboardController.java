package com.cliproco.controller;

import com.cliproco.dao.ClientDAO;
import com.cliproco.dao.ContratDAO;
import com.cliproco.dao.NotificationDAO;
import com.cliproco.dao.ProspectDAO;
import com.cliproco.model.DashboardStats;
import com.cliproco.model.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/dashboard/*")
public class DashboardController extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    private final ClientDAO clientDAO;
    private final ProspectDAO prospectDAO;
    private final ContratDAO contratDAO;
    private final NotificationDAO notificationDAO;

    public DashboardController() {
        this.clientDAO = new ClientDAO();
        this.prospectDAO = new ProspectDAO();
        this.contratDAO = new ContratDAO();
        this.notificationDAO = new NotificationDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // Récupération des statistiques
                DashboardStats stats = new DashboardStats();
                
                // Récupération des totaux
                stats.setTotalClients(clientDAO.findAll().size());
                stats.setTotalProspects(prospectDAO.findAll().size());
                stats.setTotalContrats(contratDAO.findAll().size());
                
                // Calcul du montant total des contrats
                double montantTotal = contratDAO.findAll().stream()
                        .mapToDouble(Contrat::getMontant)
                        .sum();
                stats.setMontantTotalContrats(montantTotal);
                
                // Récupération des derniers éléments
                stats.setDerniersClients(clientDAO.findAll().stream()
                        .limit(5)
                        .collect(Collectors.toList()));
                
                stats.setDerniersProspects(prospectDAO.findAll().stream()
                        .limit(5)
                        .collect(Collectors.toList()));
                
                stats.setDerniersContrats(contratDAO.findAll().stream()
                        .limit(5)
                        .collect(Collectors.toList()));
                
                // Statistiques des prospects par statut
                stats.setProspectsNouveaux((int) prospectDAO.findAll().stream()
                        .filter(p -> p.getStatut() == StatutProspect.NOUVEAU)
                        .count());
                
                stats.setProspectsEnCours((int) prospectDAO.findAll().stream()
                        .filter(p -> p.getStatut() == StatutProspect.EN_COURS)
                        .count());
                
                stats.setProspectsConvertis((int) prospectDAO.findAll().stream()
                        .filter(p -> p.getStatut() == StatutProspect.CONVERTI)
                        .count());
                
                // Récupération des notifications non lues
                List<Notification> notifications = notificationDAO.findNonLues();
                
                request.setAttribute("stats", stats);
                request.setAttribute("notifications", notifications);
                
                // Redirection vers la vue du tableau de bord
                request.getRequestDispatcher("/WEB-INF/JSP/dashboard.jsp").forward(request, response);
            } else if (pathInfo.equals("/api/stats")) {
                // Endpoint API pour les statistiques
                DashboardStats stats = new DashboardStats();
                // ... même logique que ci-dessus ...
                response.setContentType("application/json");
                response.getWriter().write(stats.toString());
            } else if (pathInfo.equals("/notification/marquer-lu")) {
                // Marquer une notification comme lue
                String idStr = request.getParameter("id");
                if (idStr != null && !idStr.isEmpty()) {
                    try {
                        Integer id = Integer.parseInt(idStr);
                        notificationDAO.marquerCommeLu(id);
                        response.setStatus(HttpServletResponse.SC_OK);
                    } catch (NumberFormatException e) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }
                }
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page non trouvée");
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des statistiques", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur serveur");
        }
    }
} 