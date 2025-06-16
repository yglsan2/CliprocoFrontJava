package models;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Represents a company entity in the system.
 * This abstract class serves as a base for specific types of companies.
 */
@Entity
@Table(name = "societes")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Societe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "raison_sociale", nullable = false, unique = true)
    private String raisonSociale;

    @NotNull
    @Valid
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "adresse_id")
    private Adresse adresse;

    @NotNull
    @Pattern(regexp = "^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}")
    @Column(name = "telephone")
    private String telephone;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @Column(name = "email")
    private String mail;

    @Column(name = "commentaire", columnDefinition = "TEXT")
    private String commentaires;

    /**
     * Constructs a Societe without identifier.
     *
     * @param raisonSoc Legal name
     * @param adr      Physical address
     * @param tel      French phone number
     * @param email    Valid email
     * @param comment  Additional comments
     */
    public Societe(
            final String raisonSoc,
            final Adresse adr,
            final String tel,
            final String email,
            final String comment) {
        this.raisonSociale = raisonSoc;
        this.adresse = adr;
        this.telephone = tel;
        this.mail = email;
        this.commentaires = comment;
    }

    /**
     * Default constructor.
     */
    public Societe() {
    }

    /**
     *
     * @return Les commentaires sur la société.
     */
    public String getCommentaires() {
        return commentaires;
    }

    /**
     * Sets additional comments.
     *
     * @param comment New comments
     */
    public void setCommentaires(final String comment) {
        this.commentaires = comment;
    }

    /**
     *
     * @return Le mail de la société.
     */
    public String getMail() {
        return mail;
    }

    /**
     * Sets email address.
     *
     * @param email New email
     */
    public void setMail(final String email) {
        this.mail = email;
    }

    /**
     *
     * @return Le téléphone de la société.
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Sets phone number.
     *
     * @param tel New phone number
     */
    public void setTelephone(final String tel) {
        this.telephone = tel;
    }

    /**
     *
     * @return L'adresse de la société.
     */
    public Adresse getAdresse() {
        return adresse;
    }

    /**
     * Sets physical address.
     *
     * @param adr New address
     */
    public void setAdresse(final Adresse adr) {
        this.adresse = adr;
    }

    /**
     *
     * @return La raison sociale de la société.
     */
    public String getRaisonSociale() {
        return raisonSociale;
    }

    /**
     * Sets legal name.
     *
     * @param raisonSoc New legal name
     */
    public void setRaisonSociale(final String raisonSoc) {
        this.raisonSociale = raisonSoc;
    }

    /**
     *
     * @return Identifiant de la société.
     */
    public Long getIdentifiant() {
        return id;
    }

    /**
     * Sets unique identifier.
     *
     * @param id New identifier
     */
    public void setIdentifiant(final Long id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     * Provides a string representation of the company.
     * Subclasses should use {@code super.toString()} and
     * append additional fields.
     */
    @Override
    public String toString() {
        return "identifiant=" + getIdentifiant()
                + ", raisonSociale='" + getRaisonSociale() + '\''
                + ", adresse=" + getAdresse()
                + ", telephone='" + getTelephone() + '\''
                + ", mail='" + getMail() + '\''
                + ", commentaires='" + getCommentaires() + '\'';
    }
}
