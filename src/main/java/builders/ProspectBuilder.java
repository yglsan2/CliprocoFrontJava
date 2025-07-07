package builders;

import models.Prospect;
import models.Adresse;
import exceptions.ValidationException;
import utilities.Formatters;
import utilities.LogManager;
import utilities.ValidationManager;

import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Classe constructrice Prospect.
 */
public class ProspectBuilder extends SocieteBuilder<Prospect> {

    private String dateProspection;
    private String prospectInteresse;

    /**
     * Constructor.
     */
    public ProspectBuilder() {
        super(new Prospect());
        LogManager.logInfo("Initialisation d'un nouveau ProspectBuilder");
    }

    /**
     * Crée une instance de Prospect via réflexion.
     *
     * @return nouvelle instance de Prospect
     * @throws ValidationException si une erreur survient
     */
    private static Prospect createInstance() throws ValidationException {
        try {
            LogManager.logInfo("Création d'une nouvelle instance de Prospect via réflexion");
            Constructor<Prospect> constructor = Prospect.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            Prospect instance = constructor.newInstance();
            LogManager.logInfo("Instance de Prospect créée avec succès");
            return instance;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la création de l'instance de Prospect", e);
            throw new ValidationException("Erreur lors de la création de l'instance de Prospect", e);
        }
    }

    /**
     * New builder from static call.
     *
     * @return new ProspectBuilder
     */
    public static ProspectBuilder getNewProspectBuilder() {
        LogManager.logInfo("Création d'un nouveau ProspectBuilder via méthode statique");
        return new ProspectBuilder();
    }

    /**
     * Setter identifiant.
     *
     * @param identifiant Nouvel identifiant.
     * @return This builder.
     * @throws ValidationException Exception set by the identifiant setter.
     */
    @Override
    public ProspectBuilder dIdentifiant(final Integer identifiant)
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
    public ProspectBuilder dIdentifiant(final String identifiant)
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
    public ProspectBuilder deRaisonSociale(String raisonSociale) throws ValidationException {
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
    public ProspectBuilder dAdresse(final Adresse adresse)
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
    public ProspectBuilder deNomRue(final String rue)
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
    public ProspectBuilder deCodePostal(final String codePostal) throws ValidationException {
        LogManager.logInfo("Définition du code postal: " + codePostal);
        if (!ValidationManager.isValidPostalCode(codePostal)) {
            LogManager.logWarning("Code postal invalide: " + codePostal);
            throw new ValidationException("Le code postal doit être un code français valide");
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
    public ProspectBuilder deVille(final String ville) throws ValidationException {
        LogManager.logInfo("Définition de la ville: " + ville);
        if (!ValidationManager.isValidCity(ville)) {
            LogManager.logWarning("Ville invalide: " + ville);
            throw new ValidationException("La ville doit contenir uniquement des lettres, espaces et tirets");
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
    public ProspectBuilder dePays(final String pays) throws ValidationException {
        LogManager.logInfo("Définition du pays: " + pays);
        if (!ValidationManager.isValidCountry(pays)) {
            LogManager.logWarning("Pays invalide: " + pays);
            throw new ValidationException("Le pays doit contenir uniquement des lettres, espaces et tirets");
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
    public ProspectBuilder deTelephone(final String telephone) throws ValidationException {
        LogManager.logInfo("Définition du téléphone: " + telephone);
        if (!ValidationManager.isValidPhone(telephone)) {
            LogManager.logWarning("Téléphone invalide: " + telephone);
            throw new ValidationException("Le téléphone doit être un numéro français valide");
        }
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
    public ProspectBuilder deMail(String mail) throws ValidationException {
        LogManager.logInfo("Définition du mail: " + mail);
        if (!ValidationManager.isValidEmail(mail)) {
            LogManager.logWarning("Email invalide: " + mail);
            throw new ValidationException("L'email doit être au format valide");
        }
        setField("mail", mail);
        return this;
    }

    /**
     * Setter Commentaires.
     *
     * @param commentaires Nouveaux commentaires.
     * @return This builder.
     * @throws ValidationException Exception set by commentaires setter.
     */
    @Override
    public ProspectBuilder deCommentaires(String commentaires) throws ValidationException {
        LogManager.logInfo("Définition des commentaires: " + commentaires);
        setField("commentaires", commentaires);
        return this;
    }

    /**
     * Setter Date Prospection.
     *
     * @param dateProspection Nouvelle date de prospection.
     * @return This builder.
     * @throws ValidationException Exception set by dateProspection setter.
     */
    public ProspectBuilder deDateProspection(final String dateProspection)
            throws ValidationException {
        LogManager.logInfo("Définition de la date de prospection: " + dateProspection);
        try {
            LocalDate date = LocalDate.parse(dateProspection, DateTimeFormatter.ISO_LOCAL_DATE);
            if (date.isAfter(LocalDate.now())) {
                LogManager.logWarning("Date de prospection future invalide: " + dateProspection);
                throw new ValidationException("La date de prospection ne peut pas être dans le futur");
            }
            setField("dateProspection", dateProspection);
        } catch (DateTimeParseException e) {
            LogManager.logWarning("Format de date invalide: " + dateProspection);
            throw new ValidationException("Le format de la date doit être YYYY-MM-DD");
        }
        return this;
    }

    /**
     * Setter Prospect intéressé.
     *
     * @param prospectInteresse Nouveau prospect intéressé.
     * @return This builder.
     * @throws ValidationException Exception set by the prospectInteresse setter.
     */
    public ProspectBuilder deProspectInteresse(final String prospectInteresse)
            throws ValidationException {
        LogManager.logInfo("Définition du prospect intéressé: " + prospectInteresse);
        setField("prospectInteresse", prospectInteresse);
        return this;
    }

    /**
     * Getter Prospect construit.
     *
     * @return Prospect construit.
     */
    @Override
    public Prospect build() {
        LogManager.logInfo("Construction du prospect final");
        Prospect prospect = this.getEntity();
        LogManager.logInfo("Prospect construit: " + prospect);
        return prospect;
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
    public ProspectBuilder avecAdresse(String rue, String codePostal, String ville, String pays, String telephone) throws ValidationException {
        LogManager.logInfo("Configuration de l'adresse complète - rue: " + rue + ", codePostal: " + codePostal + ", ville: " + ville + ", pays: " + pays + ", telephone: " + telephone);
        return this.deNomRue(rue)
                .deCodePostal(codePostal)
                .deVille(ville)
                .dePays(pays)
                .deTelephone(telephone);
    }

    public ProspectBuilder deNumeroRue(final String numeroRue) throws ValidationException {
        LogManager.logInfo("Définition du numéro de rue: " + numeroRue);
        if (!ValidationManager.isValidStreetNumber(numeroRue)) {
            LogManager.logWarning("Numéro de rue invalide: " + numeroRue);
            throw new ValidationException("Le numéro de rue doit être au format valide");
        }
        setField("numeroRue", numeroRue);
        return this;
    }
}
