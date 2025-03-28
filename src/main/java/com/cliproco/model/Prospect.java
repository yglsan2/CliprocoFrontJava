package com.cliproco.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.io.Serializable;

@Entity
@Table(name = "prospects")
public class Prospect implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    @Column(name = "nom", length = 100, nullable = false)
    private String nom;

    @NotBlank(message = "L'adresse est obligatoire")
    @Size(max = 500, message = "L'adresse ne doit pas dépasser 500 caractères")
    private String adresse;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email n'est pas valide")
    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Size(min = 10, max = 20, message = "Le numéro de téléphone n'est pas valide")
    @Column(name = "telephone", length = 20, nullable = false)
    private String telephone;

    @NotBlank(message = "Le secteur d'activité est obligatoire")
    @Column(name = "secteur_activite", length = 100, nullable = false)
    private String secteurActivite;

    @NotNull(message = "La date de création est obligatoire")
    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @NotNull(message = "Le statut est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", length = 50, nullable = false)
    private Statut statut;

    @Column(name = "commentaires", columnDefinition = "TEXT")
    private String commentaires;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    public enum Statut {
        NOUVEAU,
        EN_COURS,
        CONVERTI,
        PERDU
    }

    // Constructeurs
    public Prospect() {
        this.dateCreation = LocalDateTime.now();
        this.statut = Statut.NOUVEAU;
    }

    public Prospect(String nom, String email, String telephone, String secteurActivite, Statut statut, String commentaires) {
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
        this.secteurActivite = secteurActivite;
        this.statut = statut;
        this.commentaires = commentaires;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
        this.dateModification = LocalDateTime.now();
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        this.dateModification = LocalDateTime.now();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
        this.dateModification = LocalDateTime.now();
    }

    public String getSecteurActivite() {
        return secteurActivite;
    }

    public void setSecteurActivite(String secteurActivite) {
        this.secteurActivite = secteurActivite;
        this.dateModification = LocalDateTime.now();
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
        this.dateModification = LocalDateTime.now();
    }

    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
        this.dateModification = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Prospect{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", secteurActivite='" + secteurActivite + '\'' +
                ", dateCreation=" + dateCreation +
                ", statut=" + statut +
                ", commentaires='" + commentaires + '\'' +
                '}';
    }
} 