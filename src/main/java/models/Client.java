package models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * Represents a client company with specific business metrics and contracts.
 */
@Entity
@Table(name = "clients")
public class Client extends Societe {
    /**
     *
     */
    private static final long MIN_CHIFFRE_AFFAIRES = 250L;

    /**
     *
     */
    @NotNull
    @Min(MIN_CHIFFRE_AFFAIRES)
    @Column(name = "chiffre_affaires", nullable = false)
    private Double chiffreAffaires;

    /**
     *
     */
    @NotNull
    @Min(1)
    @Column(name = "nombre_employes", nullable = false)
    private Integer nombreEmployes;

    /**
     * List of associated contracts.
     */
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contrat> contrats = new ArrayList<>();

    @Email
    @Column(nullable = false)
    private String mail;

    @Size(max = 1000)
    @Column(length = 1000)
    private String commentaires;

    /**
     * Default constructor for JPA.
     */
    public Client() {
        super();
    }

    /**
     * Constructs a Client with business details.
     *
     * @param raisonSociale  company name
     * @param adr            address
     * @param tel            contact number
     * @param mail           email address
     * @param commentaires   additional comments
     * @param caValue        annual turnover (≥250)
     * @param employesCount  employee count (≥1)
     */
    public Client(final String raisonSociale, final Adresse adr,
                  final String tel, final String mail, final String commentaires,
                  final Double caValue, final Integer employesCount) {
        super(raisonSociale, adr, tel, mail, commentaires);
        this.mail = mail;
        this.commentaires = commentaires;
        this.chiffreAffaires = caValue;
        this.nombreEmployes = employesCount;
    }

    /**
     *
     * @return Le chiffre d'affaires
     */
    public Double getChiffreAffaires() {
        return chiffreAffaires;
    }

    /**
     * Sets annual turnover.
     *
     * @param caValue the turnover to set (must be ≥250)
     */
    public void setChiffreAffaires(final Double caValue) {
        this.chiffreAffaires = caValue;
    }

    /**
     *
     * @return Le nombre d'employés
     */
    public Integer getNombreEmployes() {
        return nombreEmployes;
    }

    /**
     * Sets employee count.
     *
     * @param employesCount the count to set (must be ≥1)
     */
    public void setNombreEmployes(final Integer employesCount) {
        this.nombreEmployes = employesCount;
    }

    /**
     *
     * @return Les contrats du client.
     */
    public List<Contrat> getContrats() {
        return contrats;
    }

    /**
     * Sets contracts list.
     *
     * @param contratsList the list to set
     */
    public void setContrats(final List<Contrat> contratsList) {
        this.contrats = contratsList;
    }

    /**
     * Adds a contract to this client.
     *
     * @param contrat the contract to add
     */
    public void addContrat(Contrat contrat) {
        contrats.add(contrat);
        contrat.setClient(this);
    }

    /**
     * Removes a contract from this client.
     *
     * @param contrat the contract to remove
     */
    public void removeContrat(Contrat contrat) {
        contrats.remove(contrat);
        contrat.setClient(null);
    }

    /**
     * {@inheritDoc}
     * Provides string representation with business metrics.
     */
    @Override
    public String toString() {
        return super.toString()
                + " chiffreAffaires=" + chiffreAffaires
                + ", nombreEmployes=" + nombreEmployes;
    }

    /**
     * Getter email.
     *
     * @return L'adresse email.
     */
    public String getMail() {
        return mail;
    }

    /**
     * Setter email.
     *
     * @param email Nouvelle adresse email.
     */
    public void setMail(String email) {
        this.mail = email;
    }

    /**
     * Getter commentaire.
     *
     * @return Le commentaire.
     */
    public String getCommentaires() {
        return commentaires;
    }

    /**
     * Setter commentaire.
     *
     * @param commentaire Nouveau commentaire.
     */
    public void setCommentaires(String commentaire) {
        this.commentaires = commentaire;
    }
}
