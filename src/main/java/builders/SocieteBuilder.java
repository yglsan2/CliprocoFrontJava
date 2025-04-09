package builders;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Adresse;
import models.Societe;
import models.SocieteEntityException;
import utilities.DateConverter;

/**
 * Classe constructrice abstraite Société.
 *
 * @param <T> La classe fille à construire.
 */
public abstract class SocieteBuilder<T extends Societe> extends AbstractSocieteBuilder<T> {
    private static final Logger LOGGER = Logger.getLogger(SocieteBuilder.class.getName());

    /**
     * Constructor.
     *
     * @param entity L'entité fille à construire.
     */
    public SocieteBuilder(final T entity) {
        super(entity);
    }

    @Override
    public SocieteBuilder<T> dIdentifiant(Long identifiant) throws SocieteEntityException {
        return (SocieteBuilder<T>) super.dIdentifiant(identifiant);
    }

    @Override
    public SocieteBuilder<T> deRaisonSociale(String raisonSociale) throws SocieteEntityException {
        return (SocieteBuilder<T>) super.deRaisonSociale(raisonSociale);
    }

    /**
     * Setter Adresse.
     *
     * @param adresse Nouvelle adresse.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the adresse setter.
     */
    public SocieteBuilder<T> dAdresse(Adresse adresse)
            throws SocieteEntityException {
        setField("adresse", adresse);
        return this;
    }

    /**
     * Setter chiffre d'affaires.
     *
     * @param chiffreAffaires Nouveau chiffre d'affaires.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the chiffre d'affaires setter.
     */
    public SocieteBuilder<T> avecChiffreAffaires(final Double chiffreAffaires)
            throws SocieteEntityException {
        setField("chiffreAffaires", chiffreAffaires);
        return this;
    }

    /**
     * Setter date de prospection.
     *
     * @param dateProspection Nouvelle date de prospection.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the date de prospection setter.
     */
    public SocieteBuilder<T> deDateProspection(final String dateProspection)
            throws SocieteEntityException {
        setField("dateProspection", DateConverter.parseDate(dateProspection));
        return this;
    }

    /**
     * Setter Adresse avec rue.
     *
     * @param rue Nouvelle rue.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the rue setter.
     */
    public SocieteBuilder<T> withRue(final String rue)
            throws SocieteEntityException {
        Adresse adresse = (Adresse) getField("adresse");
        if (adresse == null) {
            adresse = new Adresse();
            setField("adresse", adresse);
        }
        String[] parts = rue.split(" ", 2);
        if (parts.length > 1) {
            setField("numeroRue", parts[0], adresse);
            setField("nomRue", parts[1], adresse);
        } else {
            setField("nomRue", rue, adresse);
        }
        return this;
    }

    /**
     * Setter Adresse avec code postal.
     *
     * @param codePostal Nouveau code postal.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the code postal setter.
     */
    public SocieteBuilder<T> withCodePostal(final String codePostal)
            throws SocieteEntityException {
        if (codePostal == null || !codePostal.matches("\\b\\d{5}\\b")) {
            throw new SocieteEntityException("Le code postal doit être un nombre de 5 chiffres");
        }
        Adresse adresse = (Adresse) getField("adresse");
        if (adresse == null) {
            adresse = new Adresse();
            setField("adresse", adresse);
        }
        setField("codePostal", codePostal, adresse);
        return this;
    }

    /**
     * Setter Adresse avec ville.
     *
     * @param ville Nouvelle ville.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the ville setter.
     */
    public SocieteBuilder<T> withVille(final String ville)
            throws SocieteEntityException {
        if (ville == null || !ville.matches("\\b([a-zA-Z\\u0080-\\u024F]+(?:. |-| |'))*[a-zA-Z\\u0080-\\u024F]*\\b")) {
            throw new SocieteEntityException("La ville ne peut contenir que des lettres, espaces, tirets et points");
        }
        Adresse adresse = (Adresse) getField("adresse");
        if (adresse == null) {
            adresse = new Adresse();
            setField("adresse", adresse);
        }
        setField("ville", ville, adresse);
        return this;
    }

    /**
     * Setter Adresse avec pays.
     *
     * @param pays Nouveau pays.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the pays setter.
     */
    public SocieteBuilder<T> withPays(final String pays)
            throws SocieteEntityException {
        if (pays == null || !pays.matches("\\b([a-zA-Z\\u0080-\\u024F]+(?:. |-| |'))*[a-zA-Z\\u0080-\\u024F]*\\b")) {
            throw new SocieteEntityException("Le pays ne peut contenir que des lettres, espaces, tirets et points");
        }
        Adresse adresse = (Adresse) getField("adresse");
        if (adresse == null) {
            adresse = new Adresse();
            setField("adresse", adresse);
        }
        setField("pays", pays, adresse);
        return this;
    }

    /**
     * Setter téléphone.
     *
     * @param telephone Nouveau téléphone.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the telephone setter.
     */
    public SocieteBuilder<T> deTelephone(final String telephone)
            throws SocieteEntityException {
        setField("telephone", telephone);
        return this;
    }

    /**
     * Setter email.
     *
     * @param email Nouvel email.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the email setter.
     */
    public SocieteBuilder<T> dEmail(final String email)
            throws SocieteEntityException {
        setField("email", email);
        return this;
    }

    /**
     * Setter commentaire.
     *
     * @param commentaire Nouveau commentaire.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the commentaire setter.
     */
    public SocieteBuilder<T> deCommentaire(final String commentaire)
            throws SocieteEntityException {
        setField("commentaire", commentaire);
        return this;
    }

    /**
     * Setter nombre d'employés.
     *
     * @param nombreEmployes Nouveau nombre d'employés.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the nombre d'employés setter.
     */
    public SocieteBuilder<T> avecNombreEmployes(final Integer nombreEmployes)
            throws SocieteEntityException {
        setField("nombreEmployes", nombreEmployes);
        return this;
    }

    /**
     * Setter Adresse complète.
     *
     * @param identifiant Nouvel identifiant.
     * @param numRue Nouveau numéro de rue.
     * @param nomRue Nouveau nom de rue.
     * @param codePostal Nouveau code postal.
     * @param ville Nouvelle ville.
     * @return This builder.
     * @throws SocieteEntityException Exception set by one of Adresse setter.
     */
    public SocieteBuilder<T> avecAdresse(String identifiant,
                                           String numRue,
                                           String nomRue,
                                           String codePostal,
                                           String ville)
            throws SocieteEntityException {
        this.getEntity().setAdresse(AdresseBuilder.getNewAdresseBuilder()
                .dIdentifiant(identifiant)
                .deNumeroRue(numRue)
                .deNomRue(nomRue)
                .deCodePostal(codePostal)
                .deVille(ville)
                .build());
        return this;
    }
}
