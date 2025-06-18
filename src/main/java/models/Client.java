package models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Range;

/**
 * Classe métier pour un client
 */
@Entity
@Table(name = "clients")
@Access(AccessType.FIELD)
public class Client extends Societe {

    /**
     * Identifiant du client
     */
    private Integer identifiantClient = null;

    /**
     * chiffre d'affaire du client
     */
    @NotNull
    @Max(2000000)
    private Integer chiffreAffaire;

    /**
     * Nombre d'employés du client
     */
    @NotNull
    @Max(2000000)
    private Integer nbrEmploye;

    /**
     * Liste de contrats du client
     */
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contrat> contrats = new ArrayList<>();

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
     * @param chiffreAffaire    Chiffre d'affaires du client.
     * @param nbrEmploye        Nombre d'employés du client.
     * @param gestionnaire      L'utilisateur gérant le client
     */
    public Client(final Integer identifiantClient, final Adresse adresse,
                  final String adresseMail, final String commentaire,
                  final String raisonSociale, final String telephone,
                  final Integer chiffreAffaire, final Integer nbrEmploye,
                  final Integer gestionnaire) {
        super(raisonSociale, adresse, telephone, adresseMail, commentaire);
        setIdentifiantClient(identifiantClient);
        setChiffreAffaire(chiffreAffaire);
        setNbrEmploye(nbrEmploye);
    }

    /**
     * Constructeur pour créer un client
     * @param adresse
     * @param adresseMail
     * @param commentaire
     * @param raisonSociale
     * @param telephone
     * @param chiffreAffaire
     * @param nbrEmploye
     * @param gestionnaire
     */
    public Client(final Adresse adresse,
                  final String adresseMail, final String commentaire,
                  final String raisonSociale, final String telephone,
                  final Integer chiffreAffaire, final Integer nbrEmploye,
                  final Integer gestionnaire) {
        super(raisonSociale, adresse, telephone, adresseMail, commentaire);
        setChiffreAffaire(chiffreAffaire);
        setNbrEmploye(nbrEmploye);
    }

    /**
     * Définit l'identifiant spécifique du client.
     *
     * @param identifiantClient Identifiant du client.
     */
    public void setIdentifiantClient(final Integer identifiantClient) {
        this.identifiantClient = identifiantClient;
    }

    /**
     * Retourne l'identifiant spécifique du client.
     *
     * @return Identifiant du client.
     */
    public Integer getIdentifiantClient() {
        return identifiantClient;
    }

    /**
     * Retourne le chiffre d'affaires du client.
     *
     * @return Chiffre d'affaires.
     */
    public Integer getChiffreAffaire() {
        return chiffreAffaire;
    }

    /**
     * Définit le chiffre d'affaires du client après validation.
     *
     * @param chiffreAffaire Chiffre d'affaires à définir.
     */
    public void setChiffreAffaire(final Integer chiffreAffaire)  {
        this.chiffreAffaire = chiffreAffaire;
    }

    /**
     * Retourne le nombre d'employés du client.
     *
     * @return Nombre d'employés.
     */
    public Integer getNbrEmploye() {
        return nbrEmploye;
    }

    /**
     * Définit le nombre d'employés du client après validation.
     *
     * @param nbrEmploye Nombre d'employés à définir.
     */
    public void setNbrEmploye(final Integer nbrEmploye) {
        this.nbrEmploye = nbrEmploye;
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
                "idClient="
                + getIdentifiantClient()
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
                ", chiffreAffaire="
                + getChiffreAffaire()
                +
                ", nbrEmploye="
                + getNbrEmploye()
                +
                ", adresse="
                + (getAdresse() != null ? getAdresse().toString() : "null")
                +
                ", SocieteID="
                + (getIdentifiant())
                +
                '}';
    }

    /**
     * Récupère la liste des contrats
     * @return une liste de contrats
     */
    public List<Contrat> getContrats() {
        return contrats;
    }

    /**
     * Ajoute un contrat au client
     * @param contrat Un objet Contrat
     */
    public void addContrat(final Contrat contrat) {
        this.contrats.add(contrat);
    }
}
