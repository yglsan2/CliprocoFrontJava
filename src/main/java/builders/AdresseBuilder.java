package builders;

import models.Adresse;
import models.SocieteEntityException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

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
    }

    /**
     * New builder from static call.
     *
     * @return new AdresseBuilder.
     */
    @Contract(" -> new")
    public static @NotNull AdresseBuilder getNewAdresseBuilder() {
        return new AdresseBuilder();
    }

    /**
     * Setter identifiant.
     *
     * @param identifiant Nouvel identifiant.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the identifiant setter.
     */
    public AdresseBuilder dIdentifiant(final int identifiant)
            throws SocieteEntityException {
        setField("identifiant", identifiant);
        return this;
    }

    /**
     * Setter identifiant.
     *
     * @param identifiant Nouvel identifiant.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the identifiant setter.
     */
    public AdresseBuilder dIdentifiant(final String identifiant)
            throws SocieteEntityException {
        return this.dIdentifiant(Integer.parseInt(identifiant));
    }

    /**
     * Setter Numéro Rue.
     *
     * @param numeroRue Nouveau numéro rue.
     * @return This builder.
     * @throws SocieteEntityException Exception set by numRue setter.
     */
    public AdresseBuilder deNumeroRue(final String numeroRue)
            throws SocieteEntityException {
        setField("numeroRue", numeroRue);
        return this;
    }

    /**
     * Setter Nom Rue.
     *
     * @param nomRue Nouveau nom rue.
     * @return This builder.
     * @throws SocieteEntityException Exception set by nomRue setter.
     */
    public AdresseBuilder deNomRue(final String nomRue)
            throws SocieteEntityException {
        setField("nomRue", nomRue);
        return this;
    }

    /**
     * Setter Code Postal.
     *
     * @param codePostal Nouveau code postal.
     * @return This builder.
     * @throws SocieteEntityException Exception set by codePostal setter.
     */
    public AdresseBuilder deCodePostal(final String codePostal)
            throws SocieteEntityException {
        setField("codePostal", codePostal);
        return this;
    }

    /**
     * Setter Ville.
     *
     * @param ville Nouvelle ville.
     * @return This builder.
     * @throws SocieteEntityException Exception set by ville setter.
     */
    public AdresseBuilder deVille(final String ville)
            throws SocieteEntityException {
        setField("ville", ville);
        return this;
    }

    /**
     * Getter Adresse construite.
     *
     * @return Adresse construite.
     */
    public Adresse build() {
        return this.getEntity();
    }

    /**
     * Définit la valeur d'un champ via réflexion.
     *
     * @param fieldName Nom du champ.
     * @param value Valeur à définir.
     * @throws SocieteEntityException Si une erreur survient
     */
    protected void setField(String fieldName, Object value) throws SocieteEntityException {
        try {
            Field field = Adresse.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(this.getEntity(), value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new SocieteEntityException("Erreur lors de la définition du champ " + fieldName, e);
        }
    }
}
