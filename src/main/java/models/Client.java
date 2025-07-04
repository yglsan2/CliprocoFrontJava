package models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

/**
 * Classe métier pour un client
 */
@Entity
@Table(name = "clients")
@Access(AccessType.FIELD)
public class Client extends Societe {

    /**
     * chiffre d'affaire du client
     */
    @Column(name = "chiffreAffaires")
    private Double chiffreAffaires;

    /**
     * Nombre d'employés du client
     */
    @Column(name = "nbEmployes")
    private Integer nbEmployes;

    /**
     * Default constructor for JPA.
     */
    public Client() {
        super();
    }

    /**
     * Constructeur pour modifier / supprimer un client avec les informations spécifiées.
     *
     * @param identifiantClient Identifiant spécifique au client.
     * @param adresse           Adresse du client.
     * @param adresseMail       Adresse e-mail du client.
     * @param commentaire       Commentaire sur le client.
     * @param raisonSociale     Raison sociale du client.
     * @param telephone         Numéro de téléphone du client.
     * @param chiffreAffaires   Chiffre d'affaires du client.
     * @param nbEmployes        Nombre d'employés du client.
     */
    public Client(final Integer identifiantClient, final Adresse adresse,
                  final String adresseMail, final String commentaire,
                  final String raisonSociale, final String telephone,
                  final Double chiffreAffaires, final Integer nbEmployes) {
        super(raisonSociale, adresse, telephone, adresseMail, commentaire);
        setIdentifiant(identifiantClient);
        setChiffreAffaires(chiffreAffaires);
        setNbEmployes(nbEmployes);
    }

    /**
     * Constructeur pour créer un client
     * @param adresse
     * @param adresseMail
     * @param commentaire
     * @param raisonSociale
     * @param telephone
     * @param chiffreAffaires
     * @param nbEmployes
     */
    public Client(final Adresse adresse,
                  final String adresseMail, final String commentaire,
                  final String raisonSociale, final String telephone,
                  final Double chiffreAffaires, final Integer nbEmployes) {
        super(raisonSociale, adresse, telephone, adresseMail, commentaire);
        setChiffreAffaires(chiffreAffaires);
        setNbEmployes(nbEmployes);
    }

    /**
     * Retourne le chiffre d'affaires du client.
     *
     * @return Chiffre d'affaires.
     */
    public Double getChiffreAffaires() {
        return chiffreAffaires;
    }

    /**
     * Définit le chiffre d'affaires du client après validation.
     *
     * @param chiffreAffaires Chiffre d'affaires à définir.
     */
    public void setChiffreAffaires(final Double chiffreAffaires)  {
        this.chiffreAffaires = chiffreAffaires;
    }

    /**
     * Retourne le nombre d'employés du client.
     *
     * @return Nombre d'employés.
     */
    public Integer getNbEmployes() {
        return nbEmployes;
    }

    /**
     * Définit le nombre d'employés du client après validation.
     *
     * @param nbEmployes Nombre d'employés à définir.
     */
    public void setNbEmployes(final Integer nbEmployes) {
        this.nbEmployes = nbEmployes;
    }

    /**
     * Méthode toString pour récupérer l'ensemble
     * des infos de l'objet
     * @return Les infos complétes de l'objet
     */
    @Override
    public String toString() {
        return "Client{"
                +
                "identifiant="
                + getIdentifiant()
                +
                ", raisonSociale='"
                + getRaisonSociale()
                + '\''
                +
                ", telephone='"
                + getTelephone()
                + '\''
                +
                ", email='"
                + getMail()
                + '\''
                +
                ", chiffreAffaires="
                + getChiffreAffaires()
                +
                ", nbEmployes="
                + getNbEmployes()
                +
                ", adresse="
                + (getAdresse() != null ? getAdresse().toString() : "null")
                +
                '}';
    }
}
