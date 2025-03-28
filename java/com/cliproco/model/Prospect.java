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
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    @Column(name = "nom", length = 100, nullable = false)
    private String nom;

    @NotBlank(message = "L'adresse est obligatoire")
    @Size(max = 500, message = "L'adresse ne doit pas dépasser 500 caractères")
    @Column(name = "adresse", length = 500, nullable = false)
    private String adresse;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Pattern(regexp = "^[0-9+\\s-()]{10,20}$", message = "Le numéro de téléphone doit être valide")
    @Column(name = "telephone", length = 20, nullable = false)
    private String telephone;

    @NotBlank(message = "Le secteur d'activité est obligatoire")
    @Size(max = 50, message = "Le secteur d'activité ne doit pas dépasser 50 caractères")
    @Column(name = "secteur_activite", length = 50, nullable = false)
    private String secteurActivite;

    @NotNull(message = "La date de création est obligatoire")
    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @NotNull(message = "Le statut est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private Statut statut;

    @Size(max = 1000, message = "Les commentaires ne doivent pas dépasser 1000 caractères")
    @Column(name = "commentaires", length = 1000)
    private String commentaires;

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

    public Prospect(String nom, String adresse, String email, String telephone, String secteurActivite) {
        this.nom = nom;
        this.adresse = adresse;
        this.email = email;
        this.telephone = telephone;
        this.secteurActivite = secteurActivite;
        this.dateCreation = LocalDateTime.now();
        this.statut = Statut.NOUVEAU;
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
        this.dateModification = LocalDateTime.now();
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
        this.dateModification = LocalDateTime.now();
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

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
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
                ", dateModification=" + dateModification +
                ", statut=" + statut +
                ", commentaires='" + commentaires + '\'' +
                '}';
    }
} 