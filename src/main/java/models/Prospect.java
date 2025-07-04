package models;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a potential customer extending Societe with prospection details.
 */
@Entity
@Table(name = "prospects")
@Access(AccessType.FIELD)
public class Prospect extends Societe {
    private static final Logger logger = LoggerFactory.getLogger(Prospect.class);

    @Column(name = "dateProspection", nullable = false)
    private java.sql.Date dateProspection;

    @Column(name = "prospectInteresse")
    private Boolean prospectInteresse;

    /**
     * Constructs a Prospect avec identifiant.
     *
     * @param raisonSoc    Company name
     * @param adresse      Address
     * @param telephone    Contact number
     * @param mail         Email address
     * @param commentaires Additional comments
     * @param dateProsp    Prospection date
     */
    public Prospect(
            final String raisonSoc,
            final Adresse adresse,
            final String telephone,
            final String mail,
            final String commentaires,
            final java.sql.Date dateProsp) {
        super(raisonSoc, adresse, telephone, mail, commentaires);
        this.dateProspection = dateProsp;
    }

    /**
     * Default constructor.
     */
    public Prospect() {
        super();
        logger.debug("Création d'un nouveau prospect");
    }

    public java.sql.Date getDateProspection() {
        return dateProspection;
    }

    public void setDateProspection(java.sql.Date dateProspection) {
        this.dateProspection = dateProspection;
    }

    public Boolean getProspectInteresse() {
        return prospectInteresse;
    }

    public void setProspectInteresse(Boolean prospectInteresse) {
        this.prospectInteresse = prospectInteresse;
    }

    /**
     * {@inheritDoc}
     * Provides extended string representation with prospection details.
     */
    @Override
    public String toString() {
        return "Prospect{" +
                "identifiant=" + getIdentifiant() +
                ", raisonSociale='" + getRaisonSociale() + '\'' +
                ", adresse=" + getAdresse() +
                ", telephone='" + getTelephone() + '\'' +
                ", mail='" + getMail() + '\'' +
                ", commentaires='" + getCommentaires() + '\'' +
                ", dateProspection='" + dateProspection + '\'' +
                ", prospectInteresse=" + prospectInteresse +
                '}';
    }
}
