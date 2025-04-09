package builders;

import models.Prospect;
import models.SocieteEntityException;
import utilities.DateConverter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Date;

/**
 * Classe constructrice Prospect.
 */
public class ProspectBuilder extends SocieteBuilder<Prospect> {

    /**
     * Constructor.
     * @throws SocieteEntityException si une erreur survient lors de la création
     */
    public ProspectBuilder() throws SocieteEntityException {
        super(createInstance(Prospect.class));
    }

    /**
     * New builder from static call.
     *
     * @return new ProspectBuilder
     * @throws SocieteEntityException si une erreur survient lors de la création
     */
    @Contract(" -> new")
    public static @NotNull ProspectBuilder getNewProspectBuilder() throws SocieteEntityException {
        return new ProspectBuilder();
    }

    /**
     * Setter date de prospection.
     *
     * @param dateProspection Nouvelle date de prospection.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the date de prospection setter.
     */
    public ProspectBuilder deDateProspection(final LocalDate dateProspection)
            throws SocieteEntityException {
        setField("dateProspection", dateProspection);
        return this;
    }

    /**
     * Setter date de prospection.
     *
     * @param dateProspection Nouvelle date de prospection.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the date de prospection setter.
     */
    public ProspectBuilder deDateProspection(final Date dateProspection)
            throws SocieteEntityException {
        setField("dateProspection", DateConverter.toLocalDate(dateProspection));
        return this;
    }

    /**
     * Setter date de prospection.
     *
     * @param dateProspection Nouvelle date de prospection.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the date de prospection setter.
     */
    public ProspectBuilder deDateProspection(final String dateProspection)
            throws SocieteEntityException {
        setField("dateProspection", DateConverter.parseDate(dateProspection));
        return this;
    }

    /**
     * Setter intéressé.
     *
     * @param interesse Indique si le prospect est intéressé.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the interesse setter.
     */
    public ProspectBuilder dInteresse(final String interesse)
            throws SocieteEntityException {
        setField("interesse", interesse);
        return this;
    }

    /**
     * Méthode de construction de l'objet.
     *
     * @return Objet construit.
     */
    @Override
    public Prospect build() {
        return getEntity();
    }
}
