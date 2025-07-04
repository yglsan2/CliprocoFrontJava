package builders;

import models.Adresse;
import models.Client;
import exceptions.ValidationException;
import utilities.LogManager;

import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder pour la classe Client
 */
public class ClientBuilder extends Builder<Client> {

    private Double chiffreAffaires;
    private Integer nombreEmployes;

    /**
     * Constructor.
     */
    public ClientBuilder() {
        super(new Client());
        LogManager.logInfo("Initialisation d'un nouveau ClientBuilder");
    }

    /**
     * Crée une instance de Client via réflexion.
     *
     * @return nouvelle instance de Client
     * @throws ValidationException si une erreur survient
     */
    private static Client createInstance() throws ValidationException {
        try {
            LogManager.logInfo("Création d'une nouvelle instance de Client via réflexion");
            Constructor<Client> constructor = Client.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            Client instance = constructor.newInstance();
            LogManager.logInfo("Instance de Client créée avec succès");
            return instance;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la création de l'instance de Client", e);
            throw new ValidationException("Erreur lors de la création de l'instance de Client", e);
        }
    }

    /**
     * New builder from static call.
     *
     * @return new ClientBuilder
     */
    public static ClientBuilder getNewClientBuilder() {
        LogManager.logInfo("Création d'un nouveau ClientBuilder via méthode statique");
        return new ClientBuilder();
    }

    /**
     * Setter identifiant.
     *
     * @param identifiant Nouvel identifiant.
     * @return This builder.
     * @throws ValidationException Exception set by the identifiant setter.
     */
    public ClientBuilder dIdentifiant(final Integer identifiant) throws ValidationException {
        LogManager.logInfo("Définition de l'identifiant: " + identifiant);
        getEntity().setIdentifiant(identifiant);
        return this;
    }

    /**
     * Setter raison sociale.
     *
     * @param raisonSociale Nouvelle raison sociale.
     * @return This builder.
     * @throws ValidationException Exception set by the raisonSociale setter.
     */
    public ClientBuilder deRaisonSociale(final String raisonSociale) throws ValidationException {
        LogManager.logInfo("Définition de la raison sociale: " + raisonSociale);
        getEntity().setRaisonSociale(raisonSociale);
        return this;
    }

    /**
     * Setter adresse.
     *
     * @param adresse Nouvelle adresse.
     * @return This builder.
     * @throws ValidationException Exception set by the adresse setter.
     */
    public ClientBuilder dAdresse(final Adresse adresse) throws ValidationException {
        LogManager.logInfo("Définition de l'adresse: " + adresse);
        getEntity().setAdresse(adresse);
        return this;
    }

    /**
     * Setter téléphone.
     *
     * @param telephone Nouveau téléphone.
     * @return This builder.
     * @throws ValidationException Exception set by the telephone setter.
     */
    public ClientBuilder deTelephone(final String telephone) throws ValidationException {
        LogManager.logInfo("Définition du téléphone: " + telephone);
        if (telephone == null || !telephone.matches("^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}")) {
            LogManager.logWarning("Téléphone invalide: " + telephone);
            throw new ValidationException("Le téléphone doit être un numéro français valide");
        }
        getEntity().setTelephone(telephone);
        return this;
    }

    /**
     * Setter mail.
     *
     * @param mail Nouveau mail.
     * @return This builder.
     * @throws ValidationException Exception set by the mail setter.
     */
    public ClientBuilder deMail(final String mail) throws ValidationException {
        LogManager.logInfo("Définition du mail: " + mail);
        if (mail == null || !mail.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
            LogManager.logWarning("Mail invalide: " + mail);
            throw new ValidationException("Le mail doit être une adresse email valide");
        }
        getEntity().setMail(mail);
        return this;
    }

    /**
     * Setter chiffre d'affaires.
     *
     * @param chiffreAffaires Nouveau chiffre d'affaires.
     * @return This builder.
     * @throws ValidationException Exception set by the chiffreAffaires setter.
     */
    public ClientBuilder deChiffreAffaires(final Double chiffreAffaires) throws ValidationException {
        LogManager.logInfo("Définition du chiffre d'affaires: " + chiffreAffaires);
        if (chiffreAffaires == null || chiffreAffaires < 250) {
            LogManager.logWarning("Chiffre d'affaires invalide: " + chiffreAffaires);
            throw new ValidationException("Le chiffre d'affaires doit être supérieur ou égal à 250");
        }
        getEntity().setChiffreAffaires(chiffreAffaires);
        return this;
    }

    /**
     * Setter nombre d'employés.
     *
     * @param nombreEmployes Nouveau nombre d'employés.
     * @return This builder.
     * @throws ValidationException Exception set by the nombreEmployes setter.
     */
    public ClientBuilder deNombreEmployes(final Integer nombreEmployes) throws ValidationException {
        LogManager.logInfo("Définition du nombre d'employés: " + nombreEmployes);
        if (nombreEmployes == null || nombreEmployes < 1) {
            LogManager.logWarning("Nombre d'employés invalide: " + nombreEmployes);
            throw new ValidationException("Le nombre d'employés doit être supérieur ou égal à 1");
        }
        getEntity().setNbEmployes(nombreEmployes);
        return this;
    }

    /**
     * Getter Client construit.
     *
     * @return Client construit.
     */
    @Override
    public Client build() {
        LogManager.logInfo("Construction du client final");
        Client client = getEntity();
        LogManager.logInfo("Client construit: " + client);
        return client;
    }

    /**
     * Utilise la réflexion pour définir un champ sur un objet cible.
     *
     * @param fieldName Nom du champ
     * @param value Valeur à définir
     * @param target Objet cible
     * @throws ValidationException Si une erreur survient
     */
    private void setField(String fieldName, Object value, Object target) throws ValidationException {
        try {
            LogManager.logInfo("Définition du champ " + fieldName + " avec la valeur " + value + " sur l'objet " + target.getClass().getName());
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
            LogManager.logInfo("Champ défini avec succès");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LogManager.logException("Erreur lors de la définition du champ " + fieldName, e);
            throw new ValidationException("Erreur lors de la définition du champ " + fieldName, e);
        }
    }

    /**
     * Utilise la réflexion pour définir un champ.
     *
     * @param fieldName Nom du champ
     * @param value Valeur à définir
     * @throws ValidationException Si une erreur survient
     */
    private void setField(String fieldName, Object value) throws ValidationException {
        LogManager.logInfo("Définition du champ " + fieldName + " avec la valeur " + value);
        setField(fieldName, value, getEntity());
    }

    /**
     * Utilise la réflexion pour obtenir un champ.
     *
     * @param fieldName Nom du champ
     * @return Valeur du champ
     * @throws ValidationException Si une erreur survient
     */
    private Object getField(String fieldName) throws ValidationException {
        try {
            LogManager.logInfo("Récupération du champ " + fieldName);
            Field field = getEntity().getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(getEntity());
            LogManager.logInfo("Valeur récupérée: " + value);
            return value;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LogManager.logException("Erreur lors de la récupération du champ " + fieldName, e);
            throw new ValidationException("Erreur lors de la récupération du champ " + fieldName, e);
        }
    }

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
    public ClientBuilder avecAdresse(String rue, String codePostal, String ville, String pays, String telephone) throws ValidationException {
        LogManager.logInfo("Configuration de l'adresse complète - rue: " + rue + ", codePostal: " + codePostal + ", ville: " + ville + ", pays: " + pays + ", telephone: " + telephone);
        return this.deNomRue(rue)
                .deCodePostal(codePostal)
                .deVille(ville)
                .dePays(pays)
                .deTelephone(telephone);
    }

    /**
     * Getter Raison Sociale.
     *
     * @return Raison Sociale
     * @throws ValidationException Si une erreur survient
     */
    protected String getRaisonSociale() throws ValidationException {
        return (String) getField("raisonSociale");
    }

    /**
     * Getter Adresse.
     *
     * @return Adresse
     * @throws ValidationException Si une erreur survient
     */
    protected Adresse getAdresse() throws ValidationException {
        return (Adresse) getField("adresse");
    }

    /**
     * Getter Telephone.
     *
     * @return Telephone
     * @throws ValidationException Si une erreur survient
     */
    protected String getTelephone() throws ValidationException {
        return (String) getField("telephone");
    }

    /**
     * Getter Mail.
     *
     * @return Mail
     * @throws ValidationException Si une erreur survient
     */
    protected String getMail() throws ValidationException {
        return (String) getField("mail");
    }

    /**
     * Getter Chiffre d'affaires.
     *
     * @return Chiffre d'affaires
     * @throws ValidationException Si une erreur survient
     */
    protected Double getChiffreAffaires() throws ValidationException {
        return (Double) getField("chiffreAffaires");
    }

    /**
     * Getter Nombre d'employés.
     *
     * @return Nombre d'employés
     * @throws ValidationException Si une erreur survient
     */
    protected Integer getNombreEmployes() throws ValidationException {
        return (Integer) getField("nombreEmployes");
    }

    public ClientBuilder deNomRue(String rue) {
        try {
            getEntity().getAdresse().setNomRue(rue);
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la définition de la rue", e);
        }
        return this;
    }

    public ClientBuilder deCommentaires(String commentaires) {
        getEntity().setCommentaires(commentaires);
        return this;
    }

    public ClientBuilder deCodePostal(String codePostal) {
        if (getEntity().getAdresse() == null) {
            getEntity().setAdresse(new Adresse());
        }
        getEntity().getAdresse().setCodePostal(codePostal);
        return this;
    }

    public ClientBuilder deVille(String ville) {
        if (getEntity().getAdresse() == null) {
            getEntity().setAdresse(new Adresse());
        }
        getEntity().getAdresse().setVille(ville);
        return this;
    }

    public ClientBuilder dePays(String pays) {
        if (getEntity().getAdresse() == null) {
            getEntity().setAdresse(new Adresse());
        }
        getEntity().getAdresse().setPays(pays);
        return this;
    }
}
