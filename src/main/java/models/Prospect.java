package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

/**
 * Représente un client potentiel étendant Societe avec les détails de prospection.
 */
@Entity
@Table(name = "prospect")
public class Prospect extends Societe {

    /**
     * Date de prospection (doit être dans le passé).
     */
    @NotNull
    @Past
    @Column(name = "date_prospection")
    private LocalDate dateProspection;

    /**
     * Indique le niveau d'intérêt du prospect (non vide).
     */
    @NotNull
    @NotBlank
    @Column(name = "interet")
    private String prospectInteresse;

    /**
     * Construit un Prospect sans identifiant.
     *
     * @param raisonSoc    Nom de la société
     * @param adr          Adresse
     * @param tel          Numéro de contact
     * @param email        Adresse email
     * @param comment      Commentaires additionnels
     * @param dateProsp    Date de prospection (passée)
     * @param interet      Niveau d'intérêt
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
     * Constructeur par défaut.
     */
    public Prospect() {
        super();
    }

    /**
     * Retourne la date de prospection.
     *
     * @return LocalDate date de prospection
     */
    public LocalDate getDateProspection() {
        return dateProspection;
    }

    /**
     * Définit la date de prospection.
     *
     * @param dateProsp Nouvelle date (doit être dans le passé)
     */
    public void setDateProspection(final LocalDate dateProsp) {
        this.dateProspection = dateProsp;
    }

    /**
     * Retourne le niveau d'intérêt.
     *
     * @return String niveau d'intérêt
     */
    public String getProspectInteresse() {
        return prospectInteresse;
    }

    /**
     * Définit le niveau d'intérêt.
     *
     * @param interet Nouveau niveau d'intérêt (non vide)
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
