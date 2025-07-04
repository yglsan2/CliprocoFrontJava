package models;

import jakarta.persistence.*;

/**
 * Représente une entité société dans le système.
 * 
 * <p>Cette classe abstraite sert de base pour les types spécifiques de sociétés
 * (clients et prospects). Elle définit les propriétés communes à toutes les
 * entreprises gérées par l'application.</p>
 * 
 * <p>Cette classe utilise l'annotation @MappedSuperclass pour permettre
 * l'héritage en JPA, permettant aux classes filles d'hériter des propriétés
 * tout en ayant leurs propres tables en base de données.</p>
 * 
 * @author CliprocoJEE
 * @version 1.0
 * @since 1.0
 */
@MappedSuperclass
public abstract class Societe {

    /**
     * Identifiant unique de la société.
     * Généré automatiquement par la base de données avec une stratégie d'auto-incrémentation.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identifiant")
    protected Integer identifiant;

    /**
     * Raison sociale de la société.
     * Champ obligatoire et unique en base de données.
     * Représente le nom légal de l'entreprise.
     */
    @Column(name = "raisonSociale", nullable = false, unique = true)
    protected String raisonSociale;

    /**
     * Adresse physique de la société.
     * Relation Many-to-One avec l'entité Adresse.
     * Une société peut avoir une seule adresse, mais une adresse peut être partagée.
     */
    @ManyToOne
    @JoinColumn(name = "idAdresse")
    protected Adresse adresse;

    /**
     * Numéro de téléphone de contact de la société.
     * Format attendu : numéro français (10 chiffres).
     */
    @Column(name = "telephone")
    protected String telephone;

    /**
     * Adresse email de contact de la société.
     * Format attendu : email valide (exemple@domaine.com).
     */
    @Column(name = "mail")
    protected String mail;

    /**
     * Commentaires additionnels sur la société.
     * Stocké en tant que texte long (TEXT) pour permettre des commentaires détaillés.
     */
    @Column(name = "commentaires", columnDefinition = "TEXT")
    protected String commentaires;

    /**
     * Constructeur pour créer une société avec toutes ses informations.
     * 
     * <p>Ce constructeur initialise une société avec ses informations de base.
     * Il est utilisé par les classes filles (Client et Prospect) pour
     * initialiser leurs propriétés communes.</p>
     *
     * @param raisonSoc Raison sociale (nom légal) de la société
     * @param adr       Adresse physique de la société
     * @param tel       Numéro de téléphone de contact
     * @param email     Adresse email de contact
     * @param comment   Commentaires additionnels sur la société
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
     * Constructeur par défaut requis par JPA.
     * 
     * <p>Ce constructeur est utilisé par JPA pour créer des instances
     * lors du chargement depuis la base de données.</p>
     */
    public Societe() {
    }

    /**
     * Retourne les commentaires sur la société.
     * 
     * @return Les commentaires additionnels sur la société
     */
    public String getCommentaires() {
        return commentaires;
    }

    /**
     * Définit les commentaires additionnels sur la société.
     *
     * @param comment Nouveaux commentaires à définir
     */
    public void setCommentaires(final String comment) {
        this.commentaires = comment;
    }

    /**
     * Retourne l'adresse email de contact de la société.
     * 
     * @return L'adresse email de contact
     */
    public String getMail() {
        return mail;
    }

    /**
     * Définit l'adresse email de contact de la société.
     *
     * @param email Nouvelle adresse email à définir
     */
    public void setMail(final String email) {
        this.mail = email;
    }

    /**
     * Retourne le numéro de téléphone de contact de la société.
     * 
     * @return Le numéro de téléphone de contact
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Définit le numéro de téléphone de contact de la société.
     *
     * @param tel Nouveau numéro de téléphone à définir
     */
    public void setTelephone(final String tel) {
        this.telephone = tel;
    }

    /**
     * Retourne l'adresse physique de la société.
     * 
     * @return L'adresse physique de la société
     */
    public Adresse getAdresse() {
        return adresse;
    }

    /**
     * Définit l'adresse physique de la société.
     *
     * @param adr Nouvelle adresse à définir
     */
    public void setAdresse(final Adresse adr) {
        this.adresse = adr;
    }

    /**
     * Retourne la raison sociale de la société.
     * 
     * @return La raison sociale (nom légal) de la société
     */
    public String getRaisonSociale() {
        return raisonSociale;
    }

    /**
     * Définit la raison sociale de la société.
     *
     * @param raisonSoc Nouvelle raison sociale à définir
     */
    public void setRaisonSociale(final String raisonSoc) {
        this.raisonSociale = raisonSoc;
    }

    /**
     * Retourne l'identifiant unique de la société.
     * 
     * @return L'identifiant unique de la société
     */
    public Integer getIdentifiant() {
        return identifiant;
    }

    /**
     * Définit l'identifiant unique de la société.
     * 
     * <p>Attention : Cette méthode ne devrait généralement pas être utilisée
     * directement car l'identifiant est généré automatiquement par la base de données.</p>
     *
     * @param id Nouvel identifiant à définir
     */
    public void setIdentifiant(final Integer id) {
        this.identifiant = id;
    }

    /**
     * Retourne une représentation textuelle de la société.
     * 
     * <p>Cette méthode fournit une vue d'ensemble de toutes les informations
     * de base de la société. Les classes filles peuvent utiliser
     * {@code super.toString()} et ajouter leurs propres champs spécifiques.</p>
     * 
     * @return Une chaîne de caractères contenant les informations de la société
     */
    @Override
    public String toString() {
        return "Societe{" +
                "identifiant=" + getIdentifiant() +
                ", raisonSociale='" + getRaisonSociale() + '\'' +
                ", adresse=" + getAdresse() +
                ", telephone='" + getTelephone() + '\'' +
                ", mail='" + getMail() + '\'' +
                ", commentaires='" + getCommentaires() + '\'' +
                '}';
    }
}
