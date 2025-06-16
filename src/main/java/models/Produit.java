package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * Represents a product in an invoice with its details and price.
 */
@Entity
@Table(name = "produits")
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String nom;

    @NotNull
    @Column(nullable = false, length = 1000)
    private String description;

    @NotNull
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
} 