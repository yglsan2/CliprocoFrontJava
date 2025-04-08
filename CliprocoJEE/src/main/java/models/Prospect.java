package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

/**
 * Represents a potential customer extending Societe with prospection details.
 */
@Entity
@Table(name = "prospect")
public class Prospect extends Societe {

    /**
     * Date of prospection (must be in the past).
     */
    @NotNull
    @Past
    @Column(name = "date_prospection")
    private LocalDate dateProspection;

    /**
     * Indicates the prospect's interest level (non-blank).
     */
    @NotNull
    @NotBlank
    @Column(name = "interet")
    private String prospectInteresse;

    /**
     * Constructs a Prospect without identifier.
     *
     * @param raisonSoc    Company name
     * @param adr          Address
     * @param tel          Contact number
     * @param email        Email address
     * @param comment      Additional comments
     * @param dateProsp    Prospection date (past)
     * @param interet      Interest level
     */
    public Prospect(
            final String raisonSoc,
            final Adresse adr,
            final String tel,
            final String email,
            final String comment,
            final LocalDate dateProsp,
            final String interet) {
        super(raisonSoc, adr, tel, email, comment);
        this.setDateProspection(dateProsp);
        this.setProspectInteresse(interet);
    }

    /**
     * Default constructor.
     */
    public Prospect() {
        super();
    }

    /**
     * Returns the prospection date.
     *
     * @return LocalDate prospection date
     */
    public LocalDate getDateProspection() {
        return dateProspection;
    }

    /**
     * Sets the prospection date.
     *
     * @param dateProsp New date (must be in the past)
     */
    public void setDateProspection(final LocalDate dateProsp) {
        this.dateProspection = dateProsp;
    }

    /**
     * Returns the interest level.
     *
     * @return String interest level
     */
    public String getProspectInteresse() {
        return prospectInteresse;
    }

    /**
     * Sets the interest level.
     *
     * @param interet New interest level (non-blank)
     */
    public void setProspectInteresse(final String interet) {
        this.prospectInteresse = interet;
    }

    /**
     * {@inheritDoc}
     * Provides extended string representation with prospection details.
     */
    @Override
    public String toString() {
        return super.toString()
                + " dateProspection=" + getDateProspection()
                + ", interet='" + getProspectInteresse() + '\'';
    }
}
