package com.cliproco.bean;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProspectBean {
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email n'est pas valide")
    private String email;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Size(min = 10, max = 20, message = "Le numéro de téléphone n'est pas valide")
    private String telephone;

    @NotBlank(message = "Le secteur d'activité est obligatoire")
    private String secteurActivite;

    @NotNull(message = "Le statut est obligatoire")
    private String statut;

    private String commentaires;

    // Constructeurs
    public ProspectBean() {
    }

    public ProspectBean(Long id, String nom, String email, String telephone, 
                       String secteurActivite, String statut, String commentaires) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
        this.secteurActivite = secteurActivite;
        this.statut = statut;
        this.commentaires = commentaires;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSecteurActivite() {
        return secteurActivite;
    }

    public void setSecteurActivite(String secteurActivite) {
        this.secteurActivite = secteurActivite;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    @Override
    public String toString() {
        return "ProspectBean{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", secteurActivite='" + secteurActivite + '\'' +
                ", statut='" + statut + '\'' +
                ", commentaires='" + commentaires + '\'' +
                '}';
    }
} 