package models;

import jakarta.persistence.*;

/**
 * Represents a physical address with validation constraints.
 * This class includes details such as street number, street name,
 * postal code, and city, ensuring data integrity through annotations.
 */
@Entity
@Table(name = "adresses")
@Access(AccessType.FIELD)
public class Adresse {

    private static final int POSTAL_LENGTH = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer identifiant;

    @Column(name = "numero_rue", nullable = false)
    private String numeroRue;

    @Column(name = "nom_rue", nullable = false)
    private String nomRue;

    @Column(name = "code_postal", nullable = false, length = 5)
    private String codePostal;

    @Column(nullable = false)
    private String ville;

    @Column(nullable = false)
    private String pays;

    /**
     * Default constructor for JPA.
     */
    public Adresse() {
    }

    /**
     * Constructs an address with specified details.
     *
     * @param numero   Street number (with optional suffix like bis, ter)
     * @param nom      Street name
     * @param code     5-digit postal code
     * @param villeParam City name
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
     * Provides a formatted string representation of the address.
     *
     * @return Address in "numeroRue nomRue, codePostal ville" format
     */
    @Override
    public String toString() {
        return numeroRue + " " + nomRue + ", " + codePostal + " " + ville;
    }

    public void setNumeroRue(String numeroRue) {
        this.numeroRue = numeroRue;
    }

    public void setNomRue(String nomRue) {
        this.nomRue = nomRue;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public Integer getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(Integer identifiant) {
        this.identifiant = identifiant;
    }

    public String getNumeroRue() {
        return numeroRue;
    }

    public String getNomRue() {
        return nomRue;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public String getVille() {
        return ville;
    }

    public String getPays() {
        return pays;
    }
}
