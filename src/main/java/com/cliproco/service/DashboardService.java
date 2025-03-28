package com.cliproco.service;

import com.cliproco.dao.ClientDAO;
import com.cliproco.dao.ContratDAO;
import com.cliproco.dao.ProspectDAO;
import com.cliproco.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class DashboardService {
    private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);
    private final ClientDAO clientDAO;
    private final ProspectDAO prospectDAO;
    private final ContratDAO contratDAO;

    public DashboardService() {
        this.clientDAO = new ClientDAO();
        this.prospectDAO = new ProspectDAO();
        this.contratDAO = new ContratDAO();
    }

    public DashboardStats getDashboardStats() {
        try {
            DashboardStats stats = new DashboardStats();
            
            // Récupération des totaux
            List<Client> clients = clientDAO.findAll();
            List<Prospect> prospects = prospectDAO.findAll();
            List<Contrat> contrats = contratDAO.findAll();
            
            stats.setTotalClients(clients.size());
            stats.setTotalProspects(prospects.size());
            stats.setTotalContrats(contrats.size());
            
            // Calcul du montant total des contrats
            double montantTotal = contrats.stream()
                    .mapToDouble(Contrat::getMontant)
                    .sum();
            stats.setMontantTotalContrats(montantTotal);
            
            // Récupération des derniers éléments (limités à 5)
            stats.setDerniersClients(clients.stream()
                    .limit(5)
                    .collect(Collectors.toList()));
            
            stats.setDerniersProspects(prospects.stream()
                    .limit(5)
                    .collect(Collectors.toList()));
            
            stats.setDerniersContrats(contrats.stream()
                    .limit(5)
                    .collect(Collectors.toList()));
            
            // Statistiques des prospects par statut
            stats.setProspectsNouveaux((int) prospects.stream()
                    .filter(p -> p.getStatut() == StatutProspect.NOUVEAU)
                    .count());
            
            stats.setProspectsEnCours((int) prospects.stream()
                    .filter(p -> p.getStatut() == StatutProspect.EN_COURS)
                    .count());
            
            stats.setProspectsConvertis((int) prospects.stream()
                    .filter(p -> p.getStatut() == StatutProspect.CONVERTI)
                    .count());
            
            return stats;
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des statistiques du tableau de bord", e);
            throw new RuntimeException("Erreur lors de la récupération des statistiques du tableau de bord", e);
        }
    }
} 