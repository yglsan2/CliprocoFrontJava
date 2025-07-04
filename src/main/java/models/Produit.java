package models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe métier pour un produit
 */
@Entity
@Table(name = "produits")
@Access(AccessType.FIELD)
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer identifiant;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "prix", nullable = false)
    private Double prix;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "categorie")
    private String categorie;

    @Column(name = "reference")
    private String reference;

    /**
     * Default constructor for JPA.
     */
    protected Produit() {
    }

    /**
     * Creates a new product with the specified details.
     *
     * @param nom          Product name
     * @param description  Product description
     * @param prixUnitaire Unit price
     */
    public Produit(String nom, String description, Double prixUnitaire) {
        this.nom = nom;
        this.description = description;
        this.prix = prixUnitaire;
    }

    /**
     * Getter pour le nom du produit.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Setter pour le nom du produit.
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Getter pour l'identifiant du produit.
     */
    public Integer getId() {
        return identifiant;
    }

    /**
     * Setter pour l'identifiant du produit.
     */
    public void setId(Integer id) {
        this.identifiant = id;
    }

    /**
     * Getter pour la description du produit.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter pour la description du produit.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter pour le prix unitaire du produit.
     */
    public Double getPrixUnitaire() {
        return prix;
    }

    /**
     * Setter pour le prix unitaire du produit.
     */
    public void setPrixUnitaire(Double prixUnitaire) {
        this.prix = prixUnitaire;
    }

    /**
     * Getter pour le stock du produit.
     */
    public Integer getStock() {
        return stock;
    }

    /**
     * Setter pour le stock du produit.
     */
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    /**
     * Getter pour la catégorie du produit.
     */
    public String getCategorie() {
        return categorie;
    }

    /**
     * Setter pour la catégorie du produit.
     */
    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    /**
     * Getter pour la référence du produit.
     */
    public String getReference() {
        return reference;
    }

    /**
     * Setter pour la référence du produit.
     */
    public void setReference(String reference) {
        this.reference = reference;
    }
} 