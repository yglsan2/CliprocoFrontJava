package models;

import jakarta.persistence.*;

/**
 * Représente une adresse physique avec des contraintes de validation.
 * 
 * <p>Cette classe inclut les détails d'une adresse complète : numéro de rue,
 * nom de rue, code postal et ville. Elle assure l'intégrité des données
 * grâce aux annotations JPA et aux contraintes de validation.</p>
 * 
 * <p>Cette entité est mappée sur la table "adresses" en base de données
 * et utilise l'accès par champ pour les annotations JPA.</p>
 * 
 * @author CliprocoJEE
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "adresses")
@Access(AccessType.FIELD)
public class Adresse {

    /**
     * Longueur standard d'un code postal français (5 chiffres).
     */
    private static final int POSTAL_LENGTH = 5;

    /**
     * Identifiant unique de l'adresse.
     * Généré automatiquement par la base de données avec une stratégie d'auto-incrémentation.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "identifiant")
    private Integer identifiant;

    /**
     * Numéro de rue de l'adresse.
     * Champ obligatoire en base de données.
     * Peut inclure des suffixes comme "bis", "ter", etc.
     */
    @Column(name = "numRue", nullable = false)
    private String numeroRue;

    /**
     * Nom de la rue de l'adresse.
     * Champ obligatoire en base de données.
     */
    @Column(name = "nomRue", nullable = false)
    private String nomRue;

    /**
     * Code postal de l'adresse.
     * Champ obligatoire en base de données, limité à 5 caractères.
     * Format attendu : 5 chiffres (exemple : 75001).
     */
    @Column(name = "codePostal", nullable = false, length = 5)
    private String codePostal;

    /**
     * Ville de l'adresse.
     * Champ obligatoire en base de données.
     */
    @Column(name = "ville", nullable = false)
    private String ville;

    /**
     * Pays de l'adresse.
     * Champ optionnel en base de données.
     * Par défaut, la France est supposée si non spécifié.
     */
    @Column(name = "pays", nullable = true)
    private String pays;

    /**
     * Constructeur par défaut requis par JPA.
     * 
     * <p>Ce constructeur est utilisé par JPA pour créer des instances
     * lors du chargement depuis la base de données.</p>
     */
    public Adresse() {
    }

    /**
     * Constructeur pour créer une adresse avec tous ses détails.
     * 
     * <p>Ce constructeur initialise une adresse complète avec numéro de rue,
     * nom de rue, code postal et ville. Le pays est optionnel et peut être
     * défini ultérieurement.</p>
     *
     * @param numero     Numéro de rue (peut inclure des suffixes comme "bis", "ter")
     * @param nom        Nom de la rue
     * @param code       Code postal (5 chiffres)
     * @param villeParam Nom de la ville
     */
    public Adresse(final String numero,
                   final String nom,
                   final String code,
                   final String villeParam) {
        this.numeroRue = numero;
        this.nomRue = nom;
        this.codePostal = code;
        this.ville = villeParam;
    }

    /**
     * Retourne une représentation formatée de l'adresse.
     * 
     * <p>Cette méthode fournit l'adresse dans un format lisible :
     * "numéroRue nomRue, codePostal ville"</p>
     *
     * @return L'adresse formatée sous forme de chaîne de caractères
     */
    @Override
    public String toString() {
        return numeroRue + " " + nomRue + ", " + codePostal + " " + ville;
    }

    /**
     * Définit le numéro de rue de l'adresse.
     * 
     * @param numeroRue Le nouveau numéro de rue à définir
     */
    public void setNumeroRue(String numeroRue) {
        this.numeroRue = numeroRue;
    }

    /**
     * Définit le nom de rue de l'adresse.
     * 
     * @param nomRue Le nouveau nom de rue à définir
     */
    public void setNomRue(String nomRue) {
        this.nomRue = nomRue;
    }

    /**
     * Définit le code postal de l'adresse.
     * 
     * @param codePostal Le nouveau code postal à définir (5 chiffres)
     */
    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    /**
     * Définit la ville de l'adresse.
     * 
     * @param ville La nouvelle ville à définir
     */
    public void setVille(String ville) {
        this.ville = ville;
    }

    /**
     * Définit le pays de l'adresse.
     * 
     * @param pays Le nouveau pays à définir (optionnel)
     */
    public void setPays(String pays) {
        this.pays = pays;
    }

    /**
     * Retourne l'identifiant unique de l'adresse.
     * 
     * @return L'identifiant unique de l'adresse
     */
    public Integer getIdentifiant() {
        return identifiant;
    }

    /**
     * Définit l'identifiant unique de l'adresse.
     * 
     * <p>Attention : Cette méthode ne devrait généralement pas être utilisée
     * directement car l'identifiant est généré automatiquement par la base de données.</p>
     * 
     * @param identifiant Le nouvel identifiant à définir
     */
    public void setIdentifiant(Integer identifiant) {
        this.identifiant = identifiant;
    }

    /**
     * Retourne le numéro de rue de l'adresse.
     * 
     * @return Le numéro de rue
     */
    public String getNumeroRue() {
        return numeroRue;
    }

    /**
     * Retourne le nom de rue de l'adresse.
     * 
     * @return Le nom de rue
     */
    public String getNomRue() {
        return nomRue;
    }

    /**
     * Retourne le code postal de l'adresse.
     * 
     * @return Le code postal (5 chiffres)
     */
    public String getCodePostal() {
        return codePostal;
    }

    /**
     * Retourne la ville de l'adresse.
     * 
     * @return La ville
     */
    public String getVille() {
        return ville;
    }

    /**
     * Retourne le pays de l'adresse.
     * 
     * @return Le pays (peut être null si non défini)
     */
    public String getPays() {
        return pays;
    }
}
