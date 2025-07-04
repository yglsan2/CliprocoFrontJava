package models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un produit dans le système de gestion commerciale.
 * 
 * <p>Cette classe gère les informations d'un produit commercialisable :
 * nom, description, prix, stock, catégorie et référence. Elle permet
 * de gérer l'inventaire et les informations commerciales des produits.</p>
 * 
 * <p>Cette entité est mappée sur la table "produits" en base de données
 * et utilise l'accès par champ pour les annotations JPA.</p>
 * 
 * @author CliprocoJEE
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "produits")
@Access(AccessType.FIELD)
public class Produit {

    /**
     * Identifiant unique du produit.
     * Généré automatiquement par la base de données avec une stratégie d'auto-incrémentation.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer identifiant;

    /**
     * Nom du produit.
     * Champ obligatoire en base de données.
     * Représente le nom commercial du produit.
     */
    @Column(name = "nom", nullable = false)
    private String nom;

    /**
     * Description détaillée du produit.
     * Stocké en tant que texte long (TEXT) pour permettre des descriptions détaillées.
     * Peut inclure les caractéristiques, avantages, utilisations, etc.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Prix unitaire du produit.
     * Champ obligatoire en base de données.
     * Prix en euros (format décimal).
     */
    @Column(name = "prix", nullable = false)
    private Double prix;

    /**
     * Quantité en stock du produit.
     * Représente le nombre d'unités disponibles en inventaire.
     * Peut être null si le stock n'est pas géré.
     */
    @Column(name = "stock")
    private Integer stock;

    /**
     * Catégorie du produit.
     * Permet de classer les produits par type ou famille.
     * Exemples : "Électronique", "Vêtements", "Alimentation", etc.
     */
    @Column(name = "categorie")
    private String categorie;

    /**
     * Référence unique du produit.
     * Code de référence interne ou externe pour identifier le produit.
     * Peut être utilisé pour la gestion des stocks ou la facturation.
     */
    @Column(name = "reference")
    private String reference;

    /**
     * Constructeur par défaut requis par JPA.
     * 
     * <p>Ce constructeur est utilisé par JPA pour créer des instances
     * lors du chargement depuis la base de données.</p>
     */
    protected Produit() {
    }

    /**
     * Constructeur pour créer un produit avec les informations de base.
     * 
     * <p>Ce constructeur initialise un produit avec ses informations essentielles :
     * nom, description et prix. Les autres propriétés (stock, catégorie, référence)
     * peuvent être définies ultérieurement.</p>
     *
     * @param nom          Nom du produit
     * @param description  Description détaillée du produit
     * @param prixUnitaire Prix unitaire du produit en euros
     */
    public Produit(String nom, String description, Double prixUnitaire) {
        this.nom = nom;
        this.description = description;
        this.prix = prixUnitaire;
    }

    /**
     * Retourne le nom du produit.
     * 
     * @return Le nom commercial du produit
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom du produit.
     * 
     * @param nom Le nouveau nom du produit à définir
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne l'identifiant unique du produit.
     * 
     * @return L'identifiant unique du produit
     */
    public Integer getId() {
        return identifiant;
    }

    /**
     * Définit l'identifiant unique du produit.
     * 
     * <p>Attention : Cette méthode ne devrait généralement pas être utilisée
     * directement car l'identifiant est généré automatiquement par la base de données.</p>
     * 
     * @param id Le nouvel identifiant à définir
     */
    public void setId(Integer id) {
        this.identifiant = id;
    }

    /**
     * Retourne la description du produit.
     * 
     * @return La description détaillée du produit
     */
    public String getDescription() {
        return description;
    }

    /**
     * Définit la description du produit.
     * 
     * @param description La nouvelle description du produit à définir
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retourne le prix unitaire du produit.
     * 
     * @return Le prix unitaire du produit en euros
     */
    public Double getPrixUnitaire() {
        return prix;
    }

    /**
     * Définit le prix unitaire du produit.
     * 
     * @param prixUnitaire Le nouveau prix unitaire à définir en euros
     */
    public void setPrixUnitaire(Double prixUnitaire) {
        this.prix = prixUnitaire;
    }

    /**
     * Retourne la quantité en stock du produit.
     * 
     * @return La quantité en stock (peut être null si le stock n'est pas géré)
     */
    public Integer getStock() {
        return stock;
    }

    /**
     * Définit la quantité en stock du produit.
     * 
     * @param stock La nouvelle quantité en stock à définir
     */
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    /**
     * Retourne la catégorie du produit.
     * 
     * @return La catégorie du produit (peut être null si non définie)
     */
    public String getCategorie() {
        return categorie;
    }

    /**
     * Définit la catégorie du produit.
     * 
     * @param categorie La nouvelle catégorie du produit à définir
     */
    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    /**
     * Retourne la référence du produit.
     * 
     * @return La référence unique du produit (peut être null si non définie)
     */
    public String getReference() {
        return reference;
    }

    /**
     * Définit la référence du produit.
     * 
     * @param reference La nouvelle référence du produit à définir
     */
    public void setReference(String reference) {
        this.reference = reference;
    }
} 