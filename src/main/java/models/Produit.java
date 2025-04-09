package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * Représente un produit dans une facture avec ses détails et son prix.
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
     * Constructeur par défaut pour JPA.
     */
    protected Produit() {
    }

    /**
     * Crée un nouveau produit avec les détails spécifiés.
     *
     * @param nom          Nom du produit
     * @param description  Description du produit
     * @param prixUnitaire Prix unitaire
     */
    public Produit(String nom, String description, BigDecimal prixUnitaire) {
        this.nom = nom;
        this.description = description;
        this.prixUnitaire = prixUnitaire;
    }

    /**
     * Définit la facture à laquelle ce produit appartient.
     * Utilisé par Facture.addProduit().
     *
     * @param facture La facture à définir
     */
    void setFacture(Facture facture) {
        this.facture = facture;
    }
} 