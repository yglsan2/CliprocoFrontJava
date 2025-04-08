package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an invoice with its details, products, and calculations.
 */
@Entity
@Table(name = "factures")
public class Facture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, unique = true)
    private String numero;

    @NotNull
    @Column(name = "date_emission", nullable = false)
    private LocalDate dateEmission;

    @NotNull
    @Column(name = "date_echeance", nullable = false)
    private LocalDate dateEcheance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Produit> produits = new ArrayList<>();

    @OneToOne(mappedBy = "facture", cascade = CascadeType.ALL, orphanRemoval = true)
    private CalculFacture calcul;

    /**
     * Default constructor for JPA.
     */
    protected Facture() {
    }

    /**
     * Creates a new invoice with the specified details.
     *
     * @param numero        Invoice number
     * @param dateEmission  Issue date
     * @param dateEcheance Due date
     * @param client       Associated client
     */
    public Facture(String numero, LocalDate dateEmission, LocalDate dateEcheance, Client client) {
        this.numero = numero;
        this.dateEmission = dateEmission;
        this.dateEcheance = dateEcheance;
        this.client = client;
    }

    /**
     * Adds a product to this invoice.
     *
     * @param produit The product to add
     */
    public void addProduit(Produit produit) {
        produits.add(produit);
        produit.setFacture(this);
    }

    /**
     * Removes a product from this invoice.
     *
     * @param produit The product to remove
     */
    public void removeProduit(Produit produit) {
        produits.remove(produit);
        produit.setFacture(null);
    }

    /**
     * Sets the calculation for this invoice.
     *
     * @param calculFacture The calculation to set
     */
    void setCalcul(CalculFacture calculFacture) {
        this.calcul = calculFacture;
        if (calculFacture != null) {
            calculFacture.setFacture(this);
        }
    }
} 