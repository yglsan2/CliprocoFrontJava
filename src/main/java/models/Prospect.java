package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    @Column(name = "date_prospection", nullable = false)
    private String dateProspection;

    /**
     * Constructs a Prospect avec identifiant.
     *
     * @param raisonSoc    Company name
     * @param adresse      Address
     * @param telephone    Contact number
     * @param mail         Email address
     * @param commentaires Additional comments
     * @param dateProsp    Prospection date (past)
     */
    public Prospect(
            final String raisonSoc,
            final Adresse adresse,
            final String telephone,
            final String mail,
            final String commentaires,
            final String dateProsp) {
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

    public String getDateProspection() {
        return dateProspection;
    }

    public void setDateProspection(String dateProspection) {
        this.dateProspection = dateProspection;
    }

    /**
     * {@inheritDoc}
     * Provides extended string representation with prospection details.
     */
    @Override
    public String toString() {
        return "Prospect{" +
                super.toString() +
                ", dateProspection='" + dateProspection + '\'' +
                '}';
    }
}
