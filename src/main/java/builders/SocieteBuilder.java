package builders;

import models.Adresse;
import models.Societe;
import models.SocieteEntityException;

/**
 * Classe constructrice abstraite Société.
 *
 * @param <T> La classe fille à construire.
 */
public abstract class SocieteBuilder<T extends Societe> extends Builder<T> {

    /**
     * Constructor.
     *
     * @param entity L'entité fille à construire.
     */
    public SocieteBuilder(final T entity) {
        super(entity);
    }

    /**
     * Setter identifiant.
     *
     * @param identifiant Nouvel identifiant.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the identifiant setter.
     */
    public abstract SocieteBuilder<T> dIdentifiant(Long identifiant)
            throws SocieteEntityException;

    /**
     * Setter identifiant.
     *
     * @param identifiant Nouvel identifiant.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the identifiant setter.
     */
    public abstract SocieteBuilder<T> dIdentifiant(String identifiant)
            throws SocieteEntityException;

    /**
     * Setter Raison Sociale.
     *
     * @param raisonSociale Nouvelle raison sociale.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the raisonSociale setter.
     */
    public abstract SocieteBuilder<T> deRaisonSociale(String raisonSociale)
            throws SocieteEntityException;

    /**
     * Setter Adresse.
     *
     * @param adresse Nouvelle adresse.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the adresse setter.
     */
    public abstract SocieteBuilder<T> dAdresse(Adresse adresse)
            throws SocieteEntityException;

    /**
     * Setter Adresse.
     *
     * @param identifiant Nouvel identifiant.
     * @param numRue Nouveau numéro de rue.
     * @param nomRue Nouveau nom de rue.
     * @param codePostal Nouveau code postal.
     * @param ville Nouvelle ville.
     * @return This builder.
     * @throws SocieteEntityException Exception set by one of Adresse setter.
     */
    public abstract SocieteBuilder<T> avecAdresse(String identifiant,
                                           String numRue,
                                           String nomRue,
                                           String codePostal,
                                           String ville)
            throws SocieteEntityException;

    /**
     * Setter Telephone.
     *
     * @param telephone Nouveau numéro de téléphone.
     * @return This builder.
     * @throws SocieteEntityException Exception set by telephone setter.
     */
    public abstract SocieteBuilder<T> deTelephone(String telephone)
            throws SocieteEntityException;

    /**
     * Setter Mail.
     *
     * @param mail Nouveau mail.
     * @return This builder.
     * @throws SocieteEntityException Exception set by mail setter.
     */
    public abstract SocieteBuilder<T> deMail(String mail)
            throws SocieteEntityException;

    /**
     * Setter Commentaires.
     *
     * @param commentaires Nouveaux commentaires.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the commentaires setter.
     */
    public abstract SocieteBuilder<T> deCommentaires(String commentaires)
            throws SocieteEntityException;
}
