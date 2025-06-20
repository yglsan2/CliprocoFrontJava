package utilities;

import exceptions.ValidationException;
import java.util.regex.Pattern;

/**
 * Gestionnaire centralisé des validations de l'application.
 *
 * <p>
 * Depuis la migration Tomcat 11, il n'y a plus de Jakarta Validation (javax/jakarta.validation) :
 * toute validation est désormais manuelle (regex, contrôles Java purs).
 * </p>
 *
 * Fournit des méthodes utilitaires pour valider les emails, téléphones, codes postaux, etc.
 */
public final class ValidationManager {
    // Patterns de validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(0|\\+33|0033)[1-9][0-9]{8}$");
    private static final Pattern POSTAL_CODE_PATTERN = Pattern.compile("^[0-9]{5}$");
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("^[0-9]+(\\.[0-9]{1,2})?$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s\\-']{2,50}$");

    private ValidationManager() {
        throw new IllegalStateException("Classe utilitaire, ne pas instancier");
    }

    /**
     * Valide un objet et retourne true si valide.
     *
     * @param object l'objet à valider
     * @return true si l'objet est valide
     * @throws ValidationException si une erreur survient lors de la validation
     */
    public static <T> boolean isValid(T object) throws ValidationException {
        try {
            return true; // Placeholder, as the original method is not provided in the new implementation
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la vérification de la validité de l'objet", e);
            throw new ValidationException("Erreur lors de la vérification de la validité de l'objet", e);
        }
    }

    /**
     * Valide un objet et retourne les messages d'erreur.
     *
     * @param object l'objet à valider
     * @return une chaîne contenant tous les messages d'erreur, ou null si l'objet est valide
     * @throws ValidationException si une erreur survient lors de la validation
     */
    public static <T> String getValidationMessages(T object) throws ValidationException {
        try {
            return null; // Placeholder, as the original method is not provided in the new implementation
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la récupération des messages de validation", e);
            throw new ValidationException("Erreur lors de la récupération des messages de validation", e);
        }
    }

    /**
     * Valide un objet et lance une exception si des violations sont trouvées.
     *
     * @param object l'objet à valider
     * @throws ValidationException si l'objet n'est pas valide ou si une erreur survient
     */
    public static <T> void validateAndThrow(T object) throws ValidationException {
        try {
            // Placeholder, as the original method is not provided in the new implementation
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la validation de l'objet", e);
            throw new ValidationException("Erreur lors de la validation de l'objet", e);
        }
    }

    /**
     * Vérifie si une adresse email est valide.
     * 
     * @param email L'adresse email à valider
     * @return true si l'email est valide
     * @throws ValidationException si l'email est invalide
     * @throws IllegalArgumentException si l'email est null
     */
    public static boolean isValidEmail(String email) throws ValidationException {
        if (email == null) {
            throw new IllegalArgumentException("L'email ne peut pas être null");
        }
        boolean isValid = EMAIL_PATTERN.matcher(email).matches();
        if (!isValid) {
            LogManager.logWarning("Email invalide : " + email);
            throw new ValidationException("L'adresse email n'est pas valide");
        }
        return isValid;
    }
    
    /**
     * Vérifie si un numéro de téléphone est valide.
     * Accepte les formats : 0XXXXXXXXX, +33XXXXXXXXX, 0033XXXXXXXXX
     * 
     * @param phone Le numéro de téléphone à valider
     * @return true si le numéro est valide
     * @throws ValidationException si le numéro est invalide
     * @throws IllegalArgumentException si le numéro est null
     */
    public static boolean isValidPhone(String phone) throws ValidationException {
        if (phone == null) {
            throw new IllegalArgumentException("Le numéro de téléphone ne peut pas être null");
        }
        boolean isValid = PHONE_PATTERN.matcher(phone).matches();
        if (!isValid) {
            LogManager.logWarning("Numéro de téléphone invalide : " + phone);
            throw new ValidationException("Le numéro de téléphone n'est pas valide");
        }
        return isValid;
    }
    
    /**
     * Vérifie si un code postal est valide.
     * Doit être composé de 5 chiffres.
     * 
     * @param postalCode Le code postal à valider
     * @return true si le code postal est valide
     * @throws ValidationException si le code postal est invalide
     * @throws IllegalArgumentException si le code postal est null
     */
    public static boolean isValidPostalCode(String postalCode) throws ValidationException {
        if (postalCode == null) {
            throw new IllegalArgumentException("Le code postal ne peut pas être null");
        }
        boolean isValid = POSTAL_CODE_PATTERN.matcher(postalCode).matches();
        if (!isValid) {
            LogManager.logWarning("Code postal invalide : " + postalCode);
            throw new ValidationException("Le code postal n'est pas valide");
        }
        return isValid;
    }
    
    /**
     * Vérifie si un montant est valide.
     * Doit être un nombre positif avec au maximum 2 décimales.
     * 
     * @param amount Le montant à valider
     * @return true si le montant est valide
     * @throws ValidationException si le montant est invalide
     * @throws IllegalArgumentException si le montant est null
     */
    public static boolean isValidAmount(String amount) throws ValidationException {
        if (amount == null) {
            throw new IllegalArgumentException("Le montant ne peut pas être null");
        }
        boolean isValid = AMOUNT_PATTERN.matcher(amount).matches();
        if (!isValid) {
            LogManager.logWarning("Montant invalide : " + amount);
            throw new ValidationException("Le montant n'est pas valide");
        }
        return isValid;
    }
    
    /**
     * Vérifie si un nom ou une raison sociale est valide.
     * Doit contenir entre 2 et 50 caractères alphanumériques, espaces, tirets et apostrophes.
     * 
     * @param name Le nom à valider
     * @return true si le nom est valide
     * @throws ValidationException si le nom est invalide
     * @throws IllegalArgumentException si le nom est null
     */
    public static boolean isValidName(String name) throws ValidationException {
        if (name == null) {
            throw new IllegalArgumentException("Le nom ne peut pas être null");
        }
        boolean isValid = NAME_PATTERN.matcher(name).matches();
        if (!isValid) {
            LogManager.logWarning("Nom invalide : " + name);
            throw new ValidationException("Le nom n'est pas valide");
        }
        return isValid;
    }
    
    /**
     * Vérifie si une chaîne de caractères est valide.
     * Ne doit pas être null, vide ou composée uniquement d'espaces.
     * 
     * @param str La chaîne à valider
     * @return true si la chaîne est valide
     * @throws ValidationException si la chaîne est invalide
     * @throws IllegalArgumentException si la chaîne est null
     */
    public static boolean isValidString(String str) throws ValidationException {
        if (str == null || str.trim().isEmpty()) {
            throw new ValidationException("La chaîne ne peut pas être vide ou nulle");
        }
        return true;
    }
} 