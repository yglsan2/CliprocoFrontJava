package models;

import jakarta.persistence.*;
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
    private Integer id;

    @Column(nullable = false, unique = true)
    private String numero;

    @Column(name = "date_emission", nullable = false)
    private LocalDate dateEmission;

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
    public void setCalcul(CalculFacture calculFacture) {
        this.calcul = calculFacture;
        if (calculFacture != null) {
            calculFacture.setFacture(this);
        }
    }

    /**
     * Gets the ID of this invoice.
     *
     * @return The invoice ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID of this invoice.
     *
     * @param id The invoice ID to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the invoice number.
     *
     * @return The invoice number
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Sets the invoice number.
     *
     * @param numero The invoice number to set
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * Gets the issue date.
     *
     * @return The issue date
     */
    public LocalDate getDateEmission() {
        return dateEmission;
    }

    /**
     * Sets the issue date.
     *
     * @param dateEmission The issue date to set
     */
    public void setDateEmission(LocalDate dateEmission) {
        this.dateEmission = dateEmission;
    }

    /**
     * Gets the due date.
     *
     * @return The due date
     */
    public LocalDate getDateEcheance() {
        return dateEcheance;
    }

    /**
     * Sets the due date.
     *
     * @param dateEcheance The due date to set
     */
    public void setDateEcheance(LocalDate dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    /**
     * Gets the associated client.
     *
     * @return The client
     */
    public Client getClient() {
        return client;
    }

    /**
     * Sets the associated client.
     *
     * @param client The client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Gets the list of products.
     *
     * @return The list of products
     */
    public List<Produit> getProduits() {
        return produits;
    }

    /**
     * Sets the list of products.
     *
     * @param produits The list of products to set
     */
    public void setProduits(List<Produit> produits) {
        this.produits = produits;
    }

    /**
     * Gets the calculation.
     *
     * @return The calculation
     */
    public CalculFacture getCalcul() {
        return calcul;
    }
} 