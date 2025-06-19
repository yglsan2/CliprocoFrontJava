package models;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Represents a product in an invoice with its details and price.
 */
@Entity
@Table(name = "produits")
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false, length = 1000)
    private String description;

    @Positive
    @Column(name = "prix_unitaire", nullable = false)
    private BigDecimal prixUnitaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facture_id", nullable = false)
    private Facture facture;

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
    public Produit(String nom, String description, BigDecimal prixUnitaire) {
        this.nom = nom;
        this.description = description;
        this.prixUnitaire = prixUnitaire;
    }

    /**
     * Sets the invoice this product belongs to.
     * Used by Facture.addProduit().
     *
     * @param facture The invoice to set
     */
    void setFacture(Facture facture) {
        this.facture = facture;
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
        return id;
    }

    /**
     * Setter pour l'identifiant du produit.
     */
    public void setId(Integer id) {
        this.id = id;
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
    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    /**
     * Setter pour le prix unitaire du produit.
     */
    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    /**
     * Getter pour la facture associée au produit.
     */
    public Facture getFacture() {
        return facture;
    }
} 