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

    @Column(name = "date_prospection", nullable = false)
    private String dateProspection;

    @Column(name = "prospect_interesse")
    private String prospectInteresse;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "statut")
    private String statut;

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

    public String getProspectInteresse() {
        return prospectInteresse;
    }

    public void setProspectInteresse(String prospectInteresse) {
        this.prospectInteresse = prospectInteresse;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
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
