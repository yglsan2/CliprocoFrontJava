package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * Represents the calculation details of an invoice, including VAT and totals.
 */
@Entity
@Table(name = "calculs_facture")
public class CalculFacture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Positive
    @Column(name = "montant_ht", nullable = false)
    private BigDecimal montantHT;

    @NotNull
    @Positive
    @Column(name = "taux_tva", nullable = false)
    private BigDecimal tauxTVA;

    @NotNull
    @Positive
    @Column(name = "montant_tva", nullable = false)
    private BigDecimal montantTVA;

    @NotNull
    @Positive
    @Column(name = "montant_ttc", nullable = false)
    private BigDecimal montantTTC;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facture_id", nullable = false)
    private Facture facture;

    /**
     * Default constructor for JPA.
     */
    protected CalculFacture() {
    }

    /**
     * Creates a new invoice calculation with the specified amounts.
     *
     * @param montantHT  Montant sans taxe
     * @param tauxTVA   VAT rate
     * @param montantTVA VAT amount
     * @param montantTTC Total amount with tax
     */
    public CalculFacture(BigDecimal montantHT, BigDecimal tauxTVA, 
                        BigDecimal montantTVA, BigDecimal montantTTC) {
        this.montantHT = montantHT;
        this.tauxTVA = tauxTVA;
        this.montantTVA = montantTVA;
        this.montantTTC = montantTTC;
    }

    /**
     * Sets the invoice this calculation belongs to.
     * Used by Facture.setCalcul().
     *
     * @param facture The invoice to set
     */
    public void setFacture(Facture facture) {
        this.facture = facture;
    }

    /**
     * Gets the ID of this calculation.
     *
     * @return The calculation ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the ID of this calculation.
     *
     * @param id The calculation ID to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the amount sans taxe.
     *
     * @return The amount sans taxe
     */
    public BigDecimal getMontantHT() {
        return montantHT;
    }

    /**
     * Sets the amount sans taxe.
     *
     * @param montantHT The amount sans taxe to set
     */
    public void setMontantHT(BigDecimal montantHT) {
        this.montantHT = montantHT;
    }

    /**
     * Gets the VAT rate.
     *
     * @return The VAT rate
     */
    public BigDecimal getTauxTVA() {
        return tauxTVA;
    }

    /**
     * Sets the VAT rate.
     *
     * @param tauxTVA The VAT rate to set
     */
    public void setTauxTVA(BigDecimal tauxTVA) {
        this.tauxTVA = tauxTVA;
    }

    /**
     * Gets the VAT amount.
     *
     * @return The VAT amount
     */
    public BigDecimal getMontantTVA() {
        return montantTVA;
    }

    /**
     * Sets the VAT amount.
     *
     * @param montantTVA The VAT amount to set
     */
    public void setMontantTVA(BigDecimal montantTVA) {
        this.montantTVA = montantTVA;
    }

    /**
     * Gets the total amount with tax.
     *
     * @return The total amount with tax
     */
    public BigDecimal getMontantTTC() {
        return montantTTC;
    }

    /**
     * Sets the total amount with tax.
     *
     * @param montantTTC The total amount with tax to set
     */
    public void setMontantTTC(BigDecimal montantTTC) {
        this.montantTTC = montantTTC;
    }

    /**
     * Gets the invoice this calculation belongs to.
     *
     * @return The invoice
     */
    public Facture getFacture() {
        return facture;
    }
}