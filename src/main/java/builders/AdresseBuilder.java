package builders;

import models.Adresse;
import exceptions.ValidationException;

import java.lang.reflect.Field;

/**
 * Classe constructrice Adresse.
 */
public class AdresseBuilder extends Builder<Adresse> {

    /**
     * Constructor.
     */
    public AdresseBuilder() {
        super(new Adresse());
        System.out.println("Initialisation d'un nouveau AdresseBuilder");
    }

    /**
     * New builder from static call.
     *
     * @return new AdresseBuilder
     */
    public static AdresseBuilder getNewAdresseBuilder() {
        System.out.println("Création d'un nouveau AdresseBuilder via méthode statique");
        return new AdresseBuilder();
    }

    /**
     * Setter identifiant.
     *
     * @param identifiant Nouvel identifiant.
     * @return This builder.
     * @throws ValidationException Exception set by the identifiant setter.
     */
    public AdresseBuilder dIdentifiant(final Integer identifiant)
            throws ValidationException {
        System.out.println("Définition de l'identifiant: " + identifiant);
        setField("identifiant", identifiant);
        return this;
    }

    /**
     * Setter identifiant.
     *
     * @param identifiant Nouvel identifiant.
     * @return This builder.
     * @throws ValidationException Exception set by the identifiant setter.
     */
    public AdresseBuilder dIdentifiant(final String identifiant)
            throws ValidationException {
        System.out.println("Définition de l'identifiant (String): " + identifiant);
        return this.dIdentifiant(Integer.parseInt(identifiant));
    }

    /**
     * Setter Numéro de rue.
     *
     * @param numeroRue Nouveau numéro de rue.
     * @return This builder.
     * @throws ValidationException Exception set by the numeroRue setter.
     */
    public AdresseBuilder deNumeroRue(final String numeroRue)
            throws ValidationException {
        System.out.println("Définition du numéro de rue: " + numeroRue);
        setField("numeroRue", numeroRue);
        return this;
    }

    /**
     * Setter Nom de rue.
     *
     * @param nomRue Nouveau nom de rue.
     * @return This builder.
     * @throws ValidationException Exception set by the nomRue setter.
     */
    public AdresseBuilder deNomRue(final String nomRue)
            throws ValidationException {
        System.out.println("Définition du nom de rue: " + nomRue);
        setField("nomRue", nomRue);
        return this;
    }

    /**
     * Setter Code postal.
     *
     * @param codePostal Nouveau code postal.
     * @return This builder.
     * @throws ValidationException Exception set by the codePostal setter.
     */
    public AdresseBuilder deCodePostal(final String codePostal)
            throws ValidationException {
        System.out.println("Définition du code postal: " + codePostal);
        if (codePostal == null || !codePostal.matches("\\b\\d{5}\\b")) {
            System.out.println("Code postal invalide: " + codePostal);
            throw new ValidationException("Le code postal doit être un nombre de 5 chiffres");
        }
        setField("codePostal", codePostal);
        return this;
    }

    /**
     * Setter Ville.
     *
     * @param ville Nouvelle ville.
     * @return This builder.
     * @throws ValidationException Exception set by the ville setter.
     */
    public AdresseBuilder deVille(final String ville)
            throws ValidationException {
        System.out.println("Définition de la ville: " + ville);
        if (ville == null || !ville.matches("\\b([a-zA-Z\\u0080-\\u024F]+(?:. |-| |'))*[a-zA-Z\\u0080-\\u024F]*\\b")) {
            System.out.println("Ville invalide: " + ville);
            throw new ValidationException("La ville ne peut contenir que des lettres, espaces, tirets et points");
        }
        setField("ville", ville);
        return this;
    }

    /**
     * Setter Pays.
     *
     * @param pays Nouveau pays.
     * @return This builder.
     * @throws ValidationException Exception set by the pays setter.
     */
    public AdresseBuilder dePays(final String pays)
            throws ValidationException {
        System.out.println("Définition du pays: " + pays);
        if (pays == null || !pays.matches("\\b([a-zA-Z\\u0080-\\u024F]+(?:. |-| |'))*[a-zA-Z\\u0080-\\u024F]*\\b")) {
            System.out.println("Pays invalide: " + pays);
            throw new ValidationException("Le pays ne peut contenir que des lettres, espaces, tirets et points");
        }
        setField("pays", pays);
        return this;
    }

    /**
     * Getter Adresse construit.
     *
     * @return Adresse construit.
     */
    @Override
    public Adresse build() {
        System.out.println("Construction de l'adresse final");
        Adresse adresse = this.getEntity();
        System.out.println("Adresse construite: " + adresse);
        return adresse;
    }

    /**
     * Utilise la réflexion pour définir un champ.
     *
     * @param fieldName Nom du champ
     * @param value Valeur à définir
     * @throws ValidationException Si une erreur survient
     */
    protected void setField(String fieldName, Object value) {
        try {
            System.out.println("Définition du champ " + fieldName + " avec la valeur " + value);
            Field field = this.getEntity().getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(this.getEntity(), value);
            System.out.println("Champ défini avec succès");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la définition du champ " + fieldName, e);
        }
    }
}
