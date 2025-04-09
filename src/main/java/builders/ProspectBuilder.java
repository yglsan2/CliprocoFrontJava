package builders;

import models.Adresse;
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
public class ProspectBuilder {
    private final Prospect prospect;

    /**
     * Constructor.
     * @throws SocieteEntityException si une erreur survient lors de la création
     */
    public ProspectBuilder() throws SocieteEntityException {
        this.prospect = new Prospect();
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
     * Setter raison sociale.
     *
     * @param raisonSociale Nouvelle raison sociale.
     * @return This builder.
     */
    public ProspectBuilder deRaisonSociale(final String raisonSociale) {
        prospect.setRaisonSociale(raisonSociale);
        return this;
    }

    /**
     * Setter téléphone.
     *
     * @param telephone Nouveau numéro de téléphone.
     * @return This builder.
     */
    public ProspectBuilder deTelephone(final String telephone) {
        prospect.setTelephone(telephone);
        return this;
    }

    /**
     * Setter email.
     *
     * @param email Nouvelle adresse email.
     * @return This builder.
     */
    public ProspectBuilder dEmail(final String email) {
        prospect.setMail(email);
        return this;
    }

    /**
     * Setter commentaire.
     *
     * @param commentaire Nouveau commentaire.
     * @return This builder.
     */
    public ProspectBuilder deCommentaire(final String commentaire) {
        prospect.setCommentaires(commentaire);
        return this;
    }

    /**
     * Setter adresse.
     *
     * @param adresse Nouvelle adresse.
     * @return This builder.
     */
    public ProspectBuilder dAdresse(final Adresse adresse) {
        prospect.setAdresse(adresse);
        return this;
    }

    /**
     * Setter date de prospection.
     *
     * @param dateProspection Nouvelle date de prospection.
     * @return This builder.
     */
    public ProspectBuilder deDateProspection(final LocalDate dateProspection) {
        prospect.setDateProspection(dateProspection);
        return this;
    }

    /**
     * Setter date de prospection.
     *
     * @param dateProspection Nouvelle date de prospection.
     * @return This builder.
     */
    public ProspectBuilder deDateProspection(final Date dateProspection) {
        prospect.setDateProspection(DateConverter.toLocalDate(dateProspection));
        return this;
    }

    /**
     * Setter date de prospection.
     *
     * @param dateProspection Nouvelle date de prospection.
     * @return This builder.
     */
    public ProspectBuilder deDateProspection(final String dateProspection) {
        prospect.setDateProspection(DateConverter.parseDate(dateProspection));
        return this;
    }

    /**
     * Setter intéressé.
     *
     * @param interesse Indique si le prospect est intéressé.
     * @return This builder.
     */
    public ProspectBuilder dInteresse(final String interesse) {
        prospect.setProspectInteresse(interesse);
        return this;
    }

    /**
     * Setter identifiant.
     *
     * @param id Nouvel identifiant.
     * @return This builder.
     */
    public ProspectBuilder dIdentifiant(final Long id) {
        prospect.setIdentifiant(id);
        return this;
    }

    /**
     * Méthode de construction de l'objet.
     *
     * @return Objet construit.
     */
    public Prospect build() {
        return prospect;
    }
}
