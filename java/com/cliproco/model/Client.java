package com.cliproco.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "clients")
public class Client {
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

    @NotNull(message = "Le chiffre d'affaires est obligatoire")
    @DecimalMin(value = "0.0", message = "Le chiffre d'affaires doit être positif")
    @DecimalMax(value = "999999999.99", message = "Le chiffre d'affaires ne doit pas dépasser 999 999 999,99")
    @Column(name = "chiffre_affaires", precision = 10, scale = 2, nullable = false)
    private Double chiffreAffaires;

    // Constructeurs
    public Client() {
        this.dateCreation = LocalDateTime.now();
    }

    public Client(String nom, String adresse, String email, String telephone, String secteurActivite, Double chiffreAffaires) {
        this.nom = nom;
        this.adresse = adresse;
        this.email = email;
        this.telephone = telephone;
        this.secteurActivite = secteurActivite;
        this.chiffreAffaires = chiffreAffaires;
        this.dateCreation = LocalDateTime.now();
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

    public Double getChiffreAffaires() {
        return chiffreAffaires;
    }

    public void setChiffreAffaires(Double chiffreAffaires) {
        this.chiffreAffaires = chiffreAffaires;
        this.dateModification = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", secteurActivite='" + secteurActivite + '\'' +
                ", dateCreation=" + dateCreation +
                ", dateModification=" + dateModification +
                ", chiffreAffaires=" + chiffreAffaires +
                '}';
    }
} 