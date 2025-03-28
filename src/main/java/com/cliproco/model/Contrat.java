package com.cliproco.model;

public class Contrat {
    private Integer id;
    private Integer clientId;
    private String libelle;
    private double montant;

    // Constructeurs
    public Contrat() {}

    public Contrat(Integer id, Integer clientId, String libelle, double montant) {
        this.id = id;
        this.clientId = clientId;
        this.libelle = libelle;
        this.montant = montant;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    @Override
    public String toString() {
        return "Contrat{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", libelle='" + libelle + '\'' +
                ", montant=" + montant +
                '}';
    }
} 