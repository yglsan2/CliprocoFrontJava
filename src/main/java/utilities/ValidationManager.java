package utilities;

import java.util.regex.Pattern;

/**
 * Gestionnaire de validation pour l'application.
 * Fournit des méthodes de validation pour différents types de données.
 */
public final class ValidationManager {
    
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String PHONE_PATTERN = "^[0-9]{10}$";
    private static final String POSTAL_CODE_PATTERN = "^[0-9]{5}$";
    private static final String NAME_PATTERN = "^[a-zA-ZÀ-ÿ\\s'-]+$";
    
    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
    private static final Pattern phonePattern = Pattern.compile(PHONE_PATTERN);
    private static final Pattern postalCodePattern = Pattern.compile(POSTAL_CODE_PATTERN);
    private static final Pattern namePattern = Pattern.compile(NAME_PATTERN);
    
    /**
     * Constructeur privé pour empêcher l'instanciation.
     */
    private ValidationManager() {
        // Classe utilitaire, pas d'instanciation
    }
    
    /**
     * Valide si un objet n'est pas null.
     * 
     * @param <T> Type de l'objet à valider
     * @param object Objet à valider
     * @return true si l'objet n'est pas null, false sinon
     */
    public static <T> boolean isNotNull(final T object) {
        return object != null;
    }
    
    /**
     * Valide si une chaîne n'est pas vide ou null.
     * 
     * @param <T> Type de l'objet à valider
     * @param object Objet à valider
     * @return true si l'objet n'est pas null et non vide, false sinon
     */
    public static <T> boolean isNotEmpty(final T object) {
        if (object == null) {
            return false;
        }
        if (object instanceof String) {
            return !((String) object).trim().isEmpty();
        }
        return true;
    }
    
    /**
     * Valide si une chaîne est vide ou null.
     * 
     * @param <T> Type de l'objet à valider
     * @param object Objet à valider
     * @return true si l'objet est null ou vide, false sinon
     */
    public static <T> boolean isEmpty(final T object) {
        return !isNotEmpty(object);
    }
    
    /**
     * Valide le format d'un email.
     * 
     * @param email Email à valider
     * @return true si l'email est valide, false sinon
     */
    public static boolean isValidEmail(final String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return emailPattern.matcher(email.trim()).matches();
    }
    
    /**
     * Valide le format d'un numéro de téléphone français.
     * 
     * @param phone Numéro de téléphone à valider
     * @return true si le numéro est valide, false sinon
     */
    public static boolean isValidPhone(final String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return phonePattern.matcher(phone.trim()).matches();
    }
    
    /**
     * Valide le format d'un code postal français.
     * 
     * @param postalCode Code postal à valider
     * @return true si le code postal est valide, false sinon
     */
    public static boolean isValidPostalCode(final String postalCode) {
        if (postalCode == null || postalCode.trim().isEmpty()) {
            return false;
        }
        return postalCodePattern.matcher(postalCode.trim()).matches();
    }
    
    /**
     * Valide si un montant est positif.
     * 
     * @param amount Montant à valider
     * @return true si le montant est positif, false sinon
     */
    public static boolean isPositiveAmount(final Number amount) {
        if (amount == null) {
            return false;
        }
        return amount.doubleValue() > 0;
    }
    
    /**
     * Valide le format d'un nom (lettres, espaces, tirets, apostrophes).
     * 
     * @param name Nom à valider
     * @return true si le nom est valide, false sinon
     */
    public static boolean isValidName(final String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return namePattern.matcher(name.trim()).matches();
    }
    
    /**
     * Valide si une chaîne contient uniquement des caractères alphanumériques.
     * 
     * @param str Chaîne à valider
     * @return true si la chaîne est alphanumérique, false sinon
     */
    public static boolean isAlphanumeric(final String str) {
        if (str == null || str.trim().isEmpty()) {
            return false;
        }
        return str.matches("^[a-zA-Z0-9]+$");
    }
} 