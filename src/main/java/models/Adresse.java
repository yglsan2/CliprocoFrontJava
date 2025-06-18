package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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

    @NotBlank
    @Pattern(regexp = "(?:\\d{0,3} +(bis|ter|quat)|\\G(?<!^))|(?:\\b\\d{0,3}"
            + "(?:a|b)*\\b)")
    @Column(name = "numero_rue", nullable = false)
    private String numeroRue;

    @NotBlank
    @Pattern(regexp = "\\b([a-zA-Z\\u0080-\\u024F]+(?:. |-| |'))*"
            + "[a-zA-Z\\u0080-\\u024F]*(?:[0-9]+)*"
            + "([a-zA-Z\\u0080-\\u024F])*\\b")
    @Column(name = "nom_rue", nullable = false)
    private String nomRue;

    @NotBlank
    @Size(min = 5, max = 5)
    @Pattern(regexp = "\\b\\d{5}\\b")
    @Column(name = "code_postal", nullable = false, length = 5)
    private String codePostal;

    @NotBlank
    @Pattern(regexp = "\\b([a-zA-Z\\u0080-\\u024F]+(?:. |-| |'))*"
            + "[a-zA-Z\\u0080-\\u024F]*\\b")
    @Column(nullable = false)
    private String ville;

    @NotBlank
    @Pattern(regexp = "\\b([a-zA-Z\\u0080-\\u024F]+(?:. |-| |'))*[a-zA-Z\\u0080-\\u024F]*\\b")
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
