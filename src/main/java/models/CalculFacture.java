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
    private Long id;

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
     * @param montantHT  Amount without tax
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
    void setFacture(Facture facture) {
        this.facture = facture;
    }
}