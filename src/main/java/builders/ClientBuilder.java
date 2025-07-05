package builders;

import models.Adresse;
import models.Client;
import exceptions.ValidationException;
import utilities.LogManager;

import java.lang.reflect.Field;
import java.lang.reflect.Constructor;

/**
 * Builder pour la classe Client
 */
public class ClientBuilder extends Builder<Client> {

    /**
     * Constructor.
     */
    public ClientBuilder() {
        super(new Client());
        System.out.println("Initialisation d'un nouveau ClientBuilder");
    }

    /**
     * New builder from static call.
     *
     * @return new ClientBuilder
     */
    public static ClientBuilder getNewClientBuilder() {
        System.out.println("Création d'un nouveau ClientBuilder via méthode statique");
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
        System.out.println("Définition de l'identifiant: " + identifiant);
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
        System.out.println("Définition de la raison sociale: " + raisonSociale);
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
        System.out.println("Définition de l'adresse: " + adresse);
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
        System.out.println("Définition du téléphone: " + telephone);
        if (telephone == null || !telephone.matches("^(?:(?:\\+|00)33|0)\\s*[1-9](?:[\\s.-]*\\d{2}){4}")) {
            System.out.println("Téléphone invalide: " + telephone);
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
        System.out.println("Définition du mail: " + mail);
        if (mail == null || !mail.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
            System.out.println("Mail invalide: " + mail);
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
        System.out.println("Définition du chiffre d'affaires: " + chiffreAffaires);
        if (chiffreAffaires == null || chiffreAffaires < 250) {
            System.out.println("Chiffre d'affaires invalide: " + chiffreAffaires);
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
        System.out.println("Définition du nombre d'employés: " + nombreEmployes);
        if (nombreEmployes == null || nombreEmployes < 1) {
            System.out.println("Nombre d'employés invalide: " + nombreEmployes);
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
        System.out.println("Construction du client final");
        Client client = getEntity();
        System.out.println("Client construit: " + client);
        return client;
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
        System.out.println("Configuration de l'adresse complète - rue: " + rue + ", codePostal: " + codePostal + ", ville: " + ville + ", pays: " + pays + ", telephone: " + telephone);
        return this.deNomRue(rue)
                .deCodePostal(codePostal)
                .deVille(ville)
                .dePays(pays)
                .deTelephone(telephone);
    }

    public ClientBuilder deNomRue(String rue) {
        try {
            if (getEntity().getAdresse() == null) {
                getEntity().setAdresse(new Adresse());
            }
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

    protected void setField(String fieldName, Object value) {
        try {
            System.out.println("Définition du champ " + fieldName + " avec la valeur " + value);
            Field field = this.getEntity().getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(this.getEntity(), value);
            System.out.println("Champ défini avec succès");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la définition du champ " + fieldName, e);
        }
    }
}
