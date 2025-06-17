package builders;

import models.Adresse;
import models.Client;
import models.Contrat;
import exceptions.ValidationException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import utilities.LogManager;

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
    @Contract(" -> new")
    public static @NotNull ClientBuilder getNewClientBuilder() {
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
    @Override
    public ClientBuilder dIdentifiant(final Long identifiant)
            throws ValidationException {
        LogManager.logInfo("Définition de l'identifiant: " + identifiant);
        setField("id", identifiant);
        return this;
    }

    /**
     * Setter identifiant.
     *
     * @param identifiant Nouvel identifiant.
     * @return This builder.
     * @throws ValidationException Exception set by the identifiant setter.
     */
    @Override
    public ClientBuilder dIdentifiant(final String identifiant)
            throws ValidationException {
        LogManager.logInfo("Définition de l'identifiant (String): " + identifiant);
        setField("id", identifiant);
        return this;
    }

    /**
     * Setter Raison Sociale.
     *
     * @param raisonSociale Nouvelle raison sociale.
     * @return This builder.
     * @throws ValidationException Exception set by the raisonSociale setter.
     */
    @Override
    public ClientBuilder deRaisonSociale(String raisonSociale) throws ValidationException {
        LogManager.logInfo("Définition de la raison sociale: " + raisonSociale);
        setField("raisonSociale", raisonSociale);
        return this;
    }

    /**
     * Setter Adresse.
     *
     * @param adresse Nouvelle adresse.
     * @return This builder.
     * @throws ValidationException Exception set by the adresse setter.
     */
    @Override
    public ClientBuilder dAdresse(final Adresse adresse)
            throws ValidationException {
        LogManager.logInfo("Définition de l'adresse: " + adresse);
        setField("adresse", adresse);
        return this;
    }

    /**
     * Setter Adresse.
     *
     * @param rue Nouvelle rue.
     * @return This builder.
     * @throws ValidationException Exception set by the rue setter.
     */
    public ClientBuilder withRue(final String rue)
            throws ValidationException {
        LogManager.logInfo("Définition de la rue: " + rue);
        Adresse adresse = (Adresse) getField("adresse");
        if (adresse == null) {
            LogManager.logInfo("Création d'une nouvelle adresse");
            adresse = new Adresse();
            setField("adresse", adresse);
        }
        String[] parts = rue.split(" ", 2);
        if (parts.length > 1) {
            LogManager.logInfo("Séparation du numéro et du nom de rue");
            setField("numeroRue", parts[0], adresse);
            setField("nomRue", parts[1], adresse);
        } else {
            LogManager.logInfo("Utilisation de la rue complète comme nom de rue");
            setField("nomRue", rue, adresse);
        }
        return this;
    }

    /**
     * Setter Adresse.
     *
     * @param codePostal Nouveau code postal.
     * @return This builder.
     * @throws ValidationException Exception set by the code postal setter.
     */
    public ClientBuilder withCodePostal(final String codePostal)
            throws ValidationException {
        LogManager.logInfo("Définition du code postal: " + codePostal);
        if (codePostal == null || !codePostal.matches("\\b\\d{5}\\b")) {
            LogManager.logWarning("Code postal invalide: " + codePostal);
            throw new ValidationException("Le code postal doit être un nombre de 5 chiffres");
        }
        Adresse adresse = (Adresse) getField("adresse");
        if (adresse == null) {
            LogManager.logInfo("Création d'une nouvelle adresse");
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
     * @throws ValidationException Exception set by the ville setter.
     */
    public ClientBuilder withVille(final String ville)
            throws ValidationException {
        LogManager.logInfo("Définition de la ville: " + ville);
        if (ville == null || !ville.matches("\\b([a-zA-Z\\u0080-\\u024F]+(?:. |-| |'))*[a-zA-Z\\u0080-\\u024F]*\\b")) {
            LogManager.logWarning("Ville invalide: " + ville);
            throw new ValidationException("La ville ne peut contenir que des lettres, espaces, tirets et points");
        }
        Adresse adresse = (Adresse) getField("adresse");
        if (adresse == null) {
            LogManager.logInfo("Création d'une nouvelle adresse");
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
     * @throws ValidationException Exception set by the pays setter.
     */
    public ClientBuilder withPays(final String pays)
            throws ValidationException {
        LogManager.logInfo("Définition du pays: " + pays);
        if (pays == null || !pays.matches("\\b([a-zA-Z\\u0080-\\u024F]+(?:. |-| |'))*[a-zA-Z\\u0080-\\u024F]*\\b")) {
            LogManager.logWarning("Pays invalide: " + pays);
            throw new ValidationException("Le pays ne peut contenir que des lettres, espaces, tirets et points");
        }
        Adresse adresse = (Adresse) getField("adresse");
        if (adresse == null) {
            LogManager.logInfo("Création d'une nouvelle adresse");
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
     * @throws ValidationException Exception set by telephone setter.
     */
    @Override
    public ClientBuilder deTelephone(final String telephone)
            throws ValidationException {
        LogManager.logInfo("Définition du téléphone: " + telephone);
        setField("telephone", telephone);
        return this;
    }

    /**
     * Setter Mail.
     *
     * @param mail Nouveau mail.
     * @return This builder.
     * @throws ValidationException Exception set by mail setter.
     */
    @Override
    public ClientBuilder deMail(String mail) throws ValidationException {
        LogManager.logInfo("Définition du mail: " + mail);
        setField("mail", mail);
        return this;
    }

    /**
     * Setter Commentaires.
     *
     * @param commentaires Nouveaux commentaires.
     * @return This builder.
     * @throws ValidationException Exception set by the commentaires setter.
     */
    @Override
    public ClientBuilder deCommentaires(String commentaires) throws ValidationException {
        LogManager.logInfo("Définition des commentaires: " + commentaires);
        setField("commentaires", commentaires);
        return this;
    }

    /**
     * Setter Chiffre d'affaires.
     *
     * @param chiffreAffaires Nouveau chiffre d'affaires.
     * @return This builder.
     * @throws ValidationException Exception set by the chiffreAffaires setter.
     */
    public ClientBuilder deChiffreAffaires(final Double chiffreAffaires)
            throws ValidationException {
        LogManager.logInfo("Définition du chiffre d'affaires: " + chiffreAffaires);
        setField("chiffreAffaires", chiffreAffaires);
        return this;
    }

    /**
     * Setter Nombre d'employés.
     *
     * @param nombreEmployes Nouveau nombre d'employés.
     * @return This builder.
     * @throws ValidationException Exception set by the nombreEmployes setter.
     */
    public ClientBuilder deNombreEmployes(final Integer nombreEmployes)
            throws ValidationException {
        LogManager.logInfo("Définition du nombre d'employés: " + nombreEmployes);
        setField("nombreEmployes", nombreEmployes);
        return this;
    }

    /**
     * Setter Contrats.
     *
     * @param contrats Nouveaux contrats.
     * @return This builder.
     * @throws ValidationException Exception set by the contrats setter.
     */
    public ClientBuilder deContrats(final ArrayList<Contrat> contrats) throws ValidationException {
        LogManager.logInfo("Définition des contrats: " + contrats);
        setField("contrats", contrats);
        return this;
    }

    /**
     * Ajoute un contrat.
     *
     * @param contrat Contrat à ajouter.
     * @return This builder.
     * @throws ValidationException Exception set by the contrat setter.
     */
    public ClientBuilder ajouterContrat(final Contrat contrat) throws ValidationException {
        LogManager.logInfo("Ajout d'un contrat: " + contrat);
        List<Contrat> contrats = (List<Contrat>) getField("contrats");
        if (contrats == null) {
            LogManager.logInfo("Création d'une nouvelle liste de contrats");
            contrats = new ArrayList<>();
            setField("contrats", contrats);
        }
        contrats.add(contrat);
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
        Client client = this.getEntity();
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
        setField(fieldName, value, this.getEntity());
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
            Field field = this.getEntity().getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object value = field.get(this.getEntity());
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
    @Override
    public ClientBuilder avecAdresse(String rue, String codePostal, String ville, String pays, String telephone) throws ValidationException {
        LogManager.logInfo("Configuration de l'adresse complète - rue: " + rue + ", codePostal: " + codePostal + ", ville: " + ville + ", pays: " + pays + ", telephone: " + telephone);
        return this.withRue(rue)
                .withCodePostal(codePostal)
                .withVille(ville)
                .withPays(pays)
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
     * Getter Commentaires.
     *
     * @return Commentaires
     * @throws ValidationException Si une erreur survient
     */
    protected String getCommentaires() throws ValidationException {
        return (String) getField("commentaires");
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

    /**
     * Getter Contrats.
     *
     * @return Contrats
     * @throws ValidationException Si une erreur survient
     */
    protected List<Contrat> getContrats() throws ValidationException {
        return (List<Contrat>) getField("contrats");
    }
}
