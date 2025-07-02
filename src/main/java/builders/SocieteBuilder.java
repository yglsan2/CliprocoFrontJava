package builders;

import models.Adresse;
import models.Societe;
import exceptions.ValidationException;
import utilities.LogManager;

/**
 * Classe constructrice abstraite pour les sociétés.
 *
 * @param <T> Type de société
 */
public abstract class SocieteBuilder<T extends Societe> extends Builder<T> {

    /**
     * Constructor.
     *
     * @param entity Entité à construire
     */
    public SocieteBuilder(T entity) {
        super(entity);
        LogManager.logInfo("Initialisation d'un nouveau SocieteBuilder pour " + entity.getClass().getSimpleName());
    }

    /**
     * Setter identifiant.
     *
     * @param identifiant Nouvel identifiant.
     * @return This builder.
     * @throws ValidationException Exception set by the identifiant setter.
     */
    public abstract SocieteBuilder<T> dIdentifiant(Integer identifiant) throws ValidationException;

    /**
     * Setter identifiant.
     *
     * @param identifiant Nouvel identifiant.
     * @return This builder.
     * @throws ValidationException Exception set by the identifiant setter.
     */
    public abstract SocieteBuilder<T> dIdentifiant(String identifiant) throws ValidationException;

    /**
     * Setter Raison Sociale.
     *
     * @param raisonSociale Nouvelle raison sociale.
     * @return This builder.
     * @throws ValidationException Exception set by the raisonSociale setter.
     */
    public abstract SocieteBuilder<T> deRaisonSociale(String raisonSociale) throws ValidationException;

    /**
     * Setter Adresse.
     *
     * @param adresse Nouvelle adresse.
     * @return This builder.
     * @throws ValidationException Exception set by the adresse setter.
     */
    public abstract SocieteBuilder<T> dAdresse(Adresse adresse) throws ValidationException;

    /**
     * Setter Telephone.
     *
     * @param telephone Nouveau numéro de téléphone.
     * @return This builder.
     * @throws ValidationException Exception set by telephone setter.
     */
    public abstract SocieteBuilder<T> deTelephone(String telephone) throws ValidationException;

    /**
     * Setter Mail.
     *
     * @param mail Nouveau mail.
     * @return This builder.
     * @throws ValidationException Exception set by mail setter.
     */
    public abstract SocieteBuilder<T> deMail(String mail) throws ValidationException;

    /**
     * Setter Commentaires.
     *
     * @param commentaires Nouveaux commentaires.
     * @return This builder.
     * @throws ValidationException Exception set by the commentaires setter.
     */
    public abstract SocieteBuilder<T> deCommentaires(String commentaires) throws ValidationException;

    /**
     * Configure l'adresse complète.
     *
     * @param rue Rue
     * @param codePostal Code postal
     * @param ville Ville
     * @param pays Pays
     * @param telephone Téléphone
     * @return This builder
     * @throws ValidationException Si une erreur survient
     */
    public abstract SocieteBuilder<T> avecAdresse(String rue, String codePostal, String ville, String pays, String telephone) throws ValidationException;
}
