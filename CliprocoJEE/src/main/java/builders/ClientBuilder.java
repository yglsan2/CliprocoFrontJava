package builders;

import models.Adresse;
import models.Client;
import models.Contrat;
import models.SocieteEntityException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe constructrice Client.
 */
public class ClientBuilder extends SocieteBuilder<Client> {

    private Double chiffreAffaires;
    private Integer nombreEmployes;

    /**
     * Constructor.
     */
    public ClientBuilder() {
        super(new Client());
    }

    /**
     * Crée une instance de Client via réflexion.
     *
     * @return nouvelle instance de Client
     * @throws SocieteEntityException si une erreur survient
     */
    private static Client createInstance() throws SocieteEntityException {
        try {
            Constructor<Client> constructor = Client.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new SocieteEntityException("Erreur lors de la création de l'instance de Client", e);
        }
    }

    /**
     * New builder from static call.
     *
     * @return new ClientBuilder
     */
    @Contract(" -> new")
    public static @NotNull ClientBuilder getNewClientBuilder() {
        return new ClientBuilder();
    }

    /**
     * Setter identifiant.
     *
     * @param identifiant Nouvel identifiant.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the identifiant setter.
     */
    @Override
    public ClientBuilder dIdentifiant(final Long identifiant)
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
    @Override
    public ClientBuilder dIdentifiant(final String identifiant)
            throws SocieteEntityException {
        setField("id", identifiant);
        return this;
    }

    /**
     * Setter Raison Sociale.
     *
     * @param raisonSociale Nouvelle raison sociale.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the raisonSociale setter.
     */
    @Override
    public ClientBuilder deRaisonSociale(String raisonSociale) throws SocieteEntityException {
        setField("raisonSociale", raisonSociale);
        return this;
    }

    /**
     * Setter Adresse.
     *
     * @param adresse Nouvelle adresse.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the adresse setter.
     */
    @Override
    public ClientBuilder dAdresse(final Adresse adresse)
            throws SocieteEntityException {
        setField("adresse", adresse);
        return this;
    }

    /**
     * Setter Adresse.
     *
     * @param rue Nouvelle rue.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the rue setter.
     */
    public ClientBuilder withRue(final String rue)
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
     * Setter Adresse.
     *
     * @param codePostal Nouveau code postal.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the code postal setter.
     */
    public ClientBuilder withCodePostal(final String codePostal)
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
     * Setter Adresse.
     *
     * @param ville Nouvelle ville.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the ville setter.
     */
    public ClientBuilder withVille(final String ville)
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
     * Setter Adresse.
     *
     * @param pays Nouveau pays.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the pays setter.
     */
    public ClientBuilder withPays(final String pays)
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
     * Setter Telephone.
     *
     * @param telephone Nouveau numéro de téléphone.
     * @return This builder.
     * @throws SocieteEntityException Exception set by telephone setter.
     */
    @Override
    public ClientBuilder deTelephone(final String telephone)
            throws SocieteEntityException {
        setField("telephone", telephone);
        return this;
    }

    /**
     * Setter Mail.
     *
     * @param mail Nouveau mail.
     * @return This builder.
     * @throws SocieteEntityException Exception set by mail setter.
     */
    @Override
    public ClientBuilder deMail(String mail) throws SocieteEntityException {
        setField("mail", mail);
        return this;
    }

    /**
     * Setter Commentaires.
     *
     * @param commentaires Nouveaux commentaires.
     * @return This builder.
     * @throws SocieteEntityException Exception set by the commentaires setter.
     */
    @Override
    public ClientBuilder deCommentaires(String commentaires) throws SocieteEntityException {
        setField("commentaires", commentaires);
        return this;
    }

    /**
     * Setter Chiffres d'Affaires.
     *
     * @param chiffreAffaires Nouveau chiffre d'affaires.
     * @return This builder.
     * @throws SocieteEntityException Exception set par le setter.
     */
    public ClientBuilder deChiffreAffaires(final Double chiffreAffaires)
            throws SocieteEntityException {
        setField("chiffreAffaires", chiffreAffaires);
        return this;
    }

    /**
     * Setter Nombre Employés.
     *
     * @param nombreEmployes Nouveau nombre d'employés.
     * @return This builder.
     * @throws SocieteEntityException Exception set par le setter.
     */
    public ClientBuilder deNombreEmployes(final Integer nombreEmployes)
            throws SocieteEntityException {
        setField("nombreEmployes", nombreEmployes);
        return this;
    }

    /**
     * Setter Contrats.
     *
     * @param contrats Liste des contrats.
     * @return This builder.
     */
    public ClientBuilder deContrats(final ArrayList<Contrat> contrats) throws SocieteEntityException {
        setField("contrats", contrats);
        return this;
    }

    /**
     * Ajouter un contrat.
     *
     * @param contrat Nouveau contrat.
     * @return This builder.
     */
    public ClientBuilder ajouterContrat(final Contrat contrat) throws SocieteEntityException {
        ArrayList<Contrat> contrats = (ArrayList<Contrat>) getField("contrats");
        if (contrats == null) {
            contrats = new ArrayList<>();
        }
        contrats.add(contrat);
        setField("contrats", contrats);
        return this;
    }

    /**
     * Build method.
     *
     * @return Built client.
     * @throws SocieteEntityException if build fails.
     */
    @Override
    public Client build() throws SocieteEntityException {
        try {
            return new Client(
                    getRaisonSociale(),
                    getAdresse(),
                    getTelephone(),
                    getMail(),
                    getCommentaires(),
                    getChiffreAffaires(),
                    getNombreEmployes(),
                    getContrats()
            );
        } catch (Exception e) {
            throw new SocieteEntityException("Erreur lors de la construction du client", e);
        }
    }

    /**
     * Set field value using reflection.
     *
     * @param fieldName Field name.
     * @param value     New value.
     * @throws SocieteEntityException if field not found or not accessible.
     */
    private void setField(String fieldName, Object value, Object target) throws SocieteEntityException {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException e) {
            try {
                Field field = target.getClass().getSuperclass().getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(target, value);
            } catch (Exception ex) {
                throw new SocieteEntityException("Erreur lors de la définition du champ " + fieldName, ex);
            }
        } catch (Exception e) {
            throw new SocieteEntityException("Erreur lors de la définition du champ " + fieldName, e);
        }
    }

    private void setField(String fieldName, Object value) throws SocieteEntityException {
        setField(fieldName, value, getEntity());
    }

    /**
     * Get field value using reflection.
     *
     * @param fieldName Field name.
     * @return Field value.
     * @throws SocieteEntityException if field not found or not accessible.
     */
    private Object getField(String fieldName) throws SocieteEntityException {
        try {
            Field field = getEntity().getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(getEntity());
        } catch (NoSuchFieldException e) {
            try {
                Field field = getEntity().getClass().getSuperclass().getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(getEntity());
            } catch (Exception ex) {
                throw new SocieteEntityException("Erreur lors de l'accès au champ " + fieldName, ex);
            }
        } catch (Exception e) {
            throw new SocieteEntityException("Erreur lors de l'accès au champ " + fieldName, e);
        }
    }

    /**
     * Setter Adresse complète.
     *
     * @param rue        Nouvelle rue.
     * @param codePostal Nouveau code postal.
     * @param ville      Nouvelle ville.
     * @param pays       Nouveau pays.
     * @param telephone  Nouveau téléphone.
     * @return This builder.
     */
    @Override
    public ClientBuilder avecAdresse(String rue, String codePostal, String ville, String pays, String telephone) throws SocieteEntityException {
        try {
            String[] parts = rue.split(" ", 2);
            String numero = parts.length > 1 ? parts[0] : "";
            String nomRue = parts.length > 1 ? parts[1] : rue;
            
            Adresse adresse = new Adresse(numero, nomRue, codePostal, ville);
            setField("pays", pays, adresse);
            setField("adresse", adresse);
            setField("telephone", telephone);
            return this;
        } catch (Exception e) {
            throw new SocieteEntityException("Erreur lors de la création de l'adresse", e);
        }
    }

    /**
     * Getter pour la raison sociale.
     * @return La raison sociale
     */
    protected String getRaisonSociale() throws SocieteEntityException {
        return (String) getField("raisonSociale");
    }

    /**
     * Getter pour l'adresse.
     * @return L'adresse
     */
    protected Adresse getAdresse() throws SocieteEntityException {
        return (Adresse) getField("adresse");
    }

    /**
     * Getter pour le téléphone.
     * @return Le numéro de téléphone
     */
    protected String getTelephone() throws SocieteEntityException {
        return (String) getField("telephone");
    }

    /**
     * Getter pour le mail.
     * @return L'adresse mail
     */
    protected String getMail() throws SocieteEntityException {
        return (String) getField("mail");
    }

    /**
     * Getter pour les commentaires.
     * @return Les commentaires
     */
    protected String getCommentaires() throws SocieteEntityException {
        return (String) getField("commentaires");
    }

    /**
     * Getter pour le chiffre d'affaires.
     * @return Le chiffre d'affaires
     */
    protected Double getChiffreAffaires() throws SocieteEntityException {
        return (Double) getField("chiffreAffaires");
    }

    /**
     * Getter pour le nombre d'employés.
     * @return Le nombre d'employés
     */
    protected Integer getNombreEmployes() throws SocieteEntityException {
        return (Integer) getField("nombreEmployes");
    }

    /**
     * Getter pour les contrats.
     * @return La liste des contrats
     */
    protected List<Contrat> getContrats() throws SocieteEntityException {
        return (List<Contrat>) getField("contrats");
    }
}
