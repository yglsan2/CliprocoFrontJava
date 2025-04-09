package builders;

import models.Contrat;
import models.SocieteEntityException;
import org.jetbrains.annotations.NotNull;
import java.math.BigDecimal;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;

/**
 * Classe constructrice Contrat.
 */
public class ContratBuilder extends Builder<Contrat> {

    /**
     * Constructor.
     */
    ContratBuilder() {
        super(createInstance());
    }

    /**
     * Crée une instance de Contrat via réflexion.
     *
     * @return nouvelle instance de Contrat
     */
    private static Contrat createInstance() {
        try {
            Constructor<Contrat> constructor = Contrat.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de l'instance de Contrat", e);
        }
    }

    /**
     * New builder from static call.
     *
     * @return new ContratBuilder.
     */
    public static @NotNull ContratBuilder getNewContratBuilder() {
        return new ContratBuilder();
    }

    /**
     * Setter identifiant.
     *
     * @param identifiant Nouvel identifiant.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the identifiant setter.
     */
    public ContratBuilder dIdentifiant(final Long identifiant)
            throws SocieteEntityException {
        setField("id", identifiant);
        return this;
    }

    /**
     * Setter identifiant.
     *
     * @param identifiant Nouvel identifiant.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the identifiant setter.
     */
    public ContratBuilder dIdentifiant(@NotNull final String identifiant)
            throws SocieteEntityException {
        return this.dIdentifiant(Long.parseLong(identifiant));
    }

    /**
     * Setter libellé.
     *
     * @param libelle Nouveau libellé.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the libelle setter.
     */
    public ContratBuilder deLibelle(final String libelle)
            throws SocieteEntityException {
        setField("libelle", libelle);
        return this;
    }

    /**
     * Setter Montant.
     *
     * @param montant Nouveau montant.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the montant setter.
     */
    public ContratBuilder deMontant(final BigDecimal montant)
            throws SocieteEntityException {
        setField("montant", montant);
        return this;
    }

    /**
     * Setter Montant.
     *
     * @param montant Nouveau montant.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the montant setter.
     */
    public ContratBuilder deMontant(final String montant)
            throws SocieteEntityException {
        return this.deMontant(new BigDecimal(montant));
    }

    /**
     * Setter id client.
     *
     * @param idClient Nouvel identifiant client.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the idClient setter.
     */
    public ContratBuilder dIdClient(final int idClient)
            throws SocieteEntityException {
        setField("idClient", idClient);
        return this;
    }

    /**
     * Setter id client.
     * @param idClient Nouvel identifiant client.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the idClient setter.
     */
    public ContratBuilder dIdClient(final String idClient)
            throws SocieteEntityException {
        return this.dIdClient(Integer.parseInt(idClient));
    }

    /**
     * Getter Contrat construit.
     * @return Contrat construit.
     */
    @Override
    public Contrat build() {
        return this.getEntity();
    }

    /**
     * Utilise la réflexion pour définir un champ.
     *
     * @param fieldName Nom du champ
     * @param value Valeur à définir
     * @throws SocieteEntityException Si une erreur survient
     */
    private void setField(String fieldName, Object value) throws SocieteEntityException {
        try {
            Field field = Contrat.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(this.getEntity(), value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new SocieteEntityException("Erreur lors de la définition du champ " + fieldName, e);
        }
    }
}
