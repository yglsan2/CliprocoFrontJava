package com.cliproco.model;

import java.util.List;

public class DashboardStats {
    private int totalClients;
    private int totalProspects;
    private int totalContrats;
    private double montantTotalContrats;
    private List<Contrat> derniersContrats;
    private List<Client> derniersClients;
    private List<Prospect> derniersProspects;
    private int prospectsNouveaux;
    private int prospectsEnCours;
    private int prospectsConvertis;

    // Constructeur
    public DashboardStats() {
    }

    // Getters et Setters
    public int getTotalClients() {
        return totalClients;
    }

    public void setTotalClients(int totalClients) {
        this.totalClients = totalClients;
    }

    public int getTotalProspects() {
        return totalProspects;
    }

    public void setTotalProspects(int totalProspects) {
        this.totalProspects = totalProspects;
    }

    public int getTotalContrats() {
        return totalContrats;
    }

    public void setTotalContrats(int totalContrats) {
        this.totalContrats = totalContrats;
    }

    public double getMontantTotalContrats() {
        return montantTotalContrats;
    }

    public void setMontantTotalContrats(double montantTotalContrats) {
        this.montantTotalContrats = montantTotalContrats;
    }

    public List<Contrat> getDerniersContrats() {
        return derniersContrats;
    }

    public void setDerniersContrats(List<Contrat> derniersContrats) {
        this.derniersContrats = derniersContrats;
    }

    public List<Client> getDerniersClients() {
        return derniersClients;
    }

    public void setDerniersClients(List<Client> derniersClients) {
        this.derniersClients = derniersClients;
    }

    public List<Prospect> getDerniersProspects() {
        return derniersProspects;
    }

    public void setDerniersProspects(List<Prospect> derniersProspects) {
        this.derniersProspects = derniersProspects;
    }

    public int getProspectsNouveaux() {
        return prospectsNouveaux;
    }

    public void setProspectsNouveaux(int prospectsNouveaux) {
        this.prospectsNouveaux = prospectsNouveaux;
    }

    public int getProspectsEnCours() {
        return prospectsEnCours;
    }

    public void setProspectsEnCours(int prospectsEnCours) {
        this.prospectsEnCours = prospectsEnCours;
    }

    public int getProspectsConvertis() {
        return prospectsConvertis;
    }

    public void setProspectsConvertis(int prospectsConvertis) {
        this.prospectsConvertis = prospectsConvertis;
    }
} 