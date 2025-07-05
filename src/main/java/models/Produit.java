package models;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Entité représentant un produit dans le système.
 */
@Entity
@Table(name = "produits")
public class Produit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "nom", nullable = false, length = 100)
    private String nom;
    
    @Column(name = "description", length = 500)
    private String description;
    
    @Column(name = "prix", nullable = false, precision = 10, scale = 2)
    private BigDecimal prixUnitaire;
    
    @Column(name = "stock", nullable = false)
    private Integer stock;
    
    @Column(name = "categorie", length = 50)
    private String categorie;
    
    @Column(name = "reference", unique = true, length = 50)
    private String reference;
    
    // Constructeurs
    public Produit() {
    }
    
    public Produit(final String nom, final String description, 
                   final BigDecimal prixUnitaire) {
        this.nom = nom;
        this.description = description;
        this.prixUnitaire = prixUnitaire;
        this.stock = 0;
    }
    
    // Getters et Setters
    public Integer getId() {
        return id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(final String nom) {
        this.nom = nom;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }
    
    public void setPrixUnitaire(final BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }
    
    public Integer getStock() {
        return stock;
    }
    
    public void setStock(final Integer stock) {
        this.stock = stock;
    }
    
    public String getCategorie() {
        return categorie;
    }
    
    public void setCategorie(final String categorie) {
        this.categorie = categorie;
    }
    
    public String getReference() {
        return reference;
    }
    
    public void setReference(final String reference) {
        this.reference = reference;
    }
    
    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", prixUnitaire=" + prixUnitaire +
                ", stock=" + stock +
                ", categorie='" + categorie + '\'' +
                ", reference='" + reference + '\'' +
                '}';
    }
} 