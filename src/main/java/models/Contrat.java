package models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a contract with an identifier, client ID, label,
 * and monetary amount.
 */
@Entity
@Table(name = "contrats")
public class Contrat {

    /**
     * Unique identifier for the contract (must be ≥1).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Label/name of the contract (non-blank).
     */
    @Column(name = "libelle", nullable = false)
    private String libelle;

    /**
     * Monetary value of the contract (must be ≥1).
     */
    @Positive
    @Column(name = "montant", nullable = false)
    private BigDecimal montant;

    /**
     * The associated client.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "prospect_id")
    private Prospect prospect;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    /**
     * Constructeur par défaut.
     */
    public Contrat() {
    }

    /**
     * Constructs a Contrat with specified details.
     *
     * @param clientParam   The associated client
     * @param libelleParam  Label/name of the contract
     * @param montantParam  Monetary value of the contract
     */
    public Contrat(
            final Client clientParam,
            final String libelleParam,
            final BigDecimal montantParam) {
        this.client = clientParam;
        this.libelle = libelleParam;
        this.montant = montantParam;
    }

    /**
     * @return The contract's identifier.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the associated client. Used by Client.addContrat().
     *
     * @param client New client
     */

    /**
     * @return Libellé du contrat.
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Sets the contract's label.
     *
     * @param libelleParam New label
     */
    public void setLibelle(final String libelleParam) {
        this.libelle = libelleParam;
    }

    /**
     * @return Montant du contrat.
     */
    public BigDecimal getMontant() {
        return montant;
    }

    /**
     * Sets the monetary amount.
     *
     * @param montantParam New amount
     */
    public void setMontant(final BigDecimal montantParam) {
        this.montant = montantParam;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Prospect getProspect() {
        return prospect;
    }

    public void setProspect(Prospect prospect) {
        this.prospect = prospect;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * @return Représentation de l'instance d'objet sous forme d'une chaine
     * de caractères.
     */
    @Override
    public String toString() {
        return "Contrat n°" + id
                + " pour le client " + (client != null ? client.getRaisonSociale() : "non défini")
                + " nommé '" + libelle + '\''
                + ", de valeur " + montant + "€";
    }
}
