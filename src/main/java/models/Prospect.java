package models;

import jakarta.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Représente un prospect (client potentiel) étendant la classe Societe avec des détails de prospection.
 * 
 * <p>Un prospect est une entreprise qui n'est pas encore cliente mais qui présente un intérêt
 * commercial. Cette classe gère les informations spécifiques à la prospection comme la date
 * de prospection et l'intérêt du prospect.</p>
 * 
 * <p>Cette entité est mappée sur la table "prospects" en base de données et utilise
 * l'accès par champ pour les annotations JPA.</p>
 * 
 * @author CliprocoJEE
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "prospects")
@Access(AccessType.FIELD)
public class Prospect extends Societe {
    /**
     * Logger pour tracer les opérations sur les prospects
     */
    private static final Logger logger = LoggerFactory.getLogger(Prospect.class);

    /**
     * Date de prospection du prospect.
     * Cette date indique quand le prospect a été contacté pour la première fois.
     * Le champ est obligatoire en base de données.
     */
    @Column(name = "dateProspection", nullable = false)
    private java.sql.Date dateProspection;

    /**
     * Indique si le prospect est intéressé par les services de l'entreprise.
     * true = prospect intéressé, false = prospect non intéressé, null = statut non défini.
     */
    @Column(name = "prospectInteresse")
    private Boolean prospectInteresse;

    /**
     * Constructeur pour créer un prospect avec toutes ses informations.
     * 
     * <p>Ce constructeur initialise un prospect avec ses informations de base
     * héritées de Societe et ses informations spécifiques de prospection.</p>
     *
     * @param raisonSoc    Raison sociale du prospect
     * @param nom          Nom du contact principal
     * @param prenom       Prénom du contact principal
     * @param adresse      Adresse complète du prospect
     * @param telephone    Numéro de téléphone de contact
     * @param mail         Adresse email de contact
     * @param commentaires Commentaires additionnels sur le prospect
     * @param dateProsp    Date de prospection (premier contact)
     */
    public Prospect(
            final String raisonSoc,
            final String nom,
            final String prenom,
            final Adresse adresse,
            final String telephone,
            final String mail,
            final String commentaires,
            final java.sql.Date dateProsp) {
        super(raisonSoc, nom, prenom, adresse, telephone, mail, commentaires);
        this.dateProspection = dateProsp;
    }

    /**
     * Constructeur par défaut requis par JPA.
     * 
     * <p>Ce constructeur est utilisé par JPA pour créer des instances
     * lors du chargement depuis la base de données.</p>
     */
    public Prospect() {
        super();
        logger.debug("Création d'un nouveau prospect");
    }

    /**
     * Retourne la date de prospection du prospect.
     * 
     * @return La date de prospection (premier contact avec le prospect)
     */
    public java.sql.Date getDateProspection() {
        return dateProspection;
    }

    /**
     * Définit la date de prospection du prospect.
     * 
     * @param dateProspection La nouvelle date de prospection à définir
     */
    public void setDateProspection(java.sql.Date dateProspection) {
        this.dateProspection = dateProspection;
    }

    /**
     * Retourne l'état d'intérêt du prospect.
     * 
     * @return true si le prospect est intéressé, false s'il ne l'est pas, null si le statut n'est pas défini
     */
    public Boolean getProspectInteresse() {
        return prospectInteresse;
    }

    /**
     * Définit l'état d'intérêt du prospect.
     * 
     * @param prospectInteresse true si le prospect est intéressé, false sinon, null pour statut non défini
     */
    public void setProspectInteresse(Boolean prospectInteresse) {
        this.prospectInteresse = prospectInteresse;
    }

    /**
     * Retourne une représentation textuelle complète du prospect.
     * 
     * <p>Cette méthode fournit une vue d'ensemble de toutes les informations
     * du prospect, incluant les détails de prospection spécifiques.</p>
     * 
     * @return Une chaîne de caractères contenant toutes les informations du prospect
     */
    @Override
    public String toString() {
        return "Prospect{" +
                "identifiant=" + getIdentifiant() +
                ", raisonSociale='" + getRaisonSociale() + '\'' +
                ", nom='" + getNom() + '\'' +
                ", prenom='" + getPrenom() + '\'' +
                ", adresse=" + getAdresse() +
                ", telephone='" + getTelephone() + '\'' +
                ", mail='" + getMail() + '\'' +
                ", commentaires='" + getCommentaires() + '\'' +
                ", dateProspection='" + dateProspection + '\'' +
                ", prospectInteresse=" + prospectInteresse +
                '}';
    }
}
