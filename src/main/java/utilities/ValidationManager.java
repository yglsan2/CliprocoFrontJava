package utilities;

/**
 * Gestionnaire de validation pour l'application.
 * Fournit des méthodes de validation pour différents types de données.
 * Utilise RegexUtils pour les validations regex.
 */
public final class ValidationManager {
    
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
        return RegexUtils.isNotNull(object);
    }
    
    /**
     * Valide si une chaîne n'est pas vide ou null.
     * 
     * @param <T> Type de l'objet à valider
     * @param object Objet à valider
     * @return true si l'objet n'est pas null et non vide, false sinon
     */
    public static <T> boolean isNotEmpty(final T object) {
        return RegexUtils.isNotEmpty(object);
    }
    
    /**
     * Valide si une chaîne est vide ou null.
     * 
     * @param <T> Type de l'objet à valider
     * @param object Objet à valider
     * @return true si l'objet est null ou vide, false sinon
     */
    public static <T> boolean isEmpty(final T object) {
        return RegexUtils.isEmpty(object);
    }
    
    /**
     * Valide le format d'un email selon RFC 5322.
     * 
     * @param email Email à valider
     * @return true si l'email est valide, false sinon
     */
    public static boolean isValidEmail(final String email) {
        return RegexUtils.isValidEmail(email);
    }
    
    /**
     * Valide le format d'un numéro de téléphone français.
     * Accepte les formats :
     * - 0612345678 (format national)
     * - +33612345678 (format international)
     * - 0033612345678 (format international)
     * 
     * @param phone Numéro de téléphone à valider
     * @return true si le numéro est valide, false sinon
     */
    public static boolean isValidPhone(final String phone) {
        return RegexUtils.isValidPhone(phone);
    }
    
    /**
     * Valide le format d'un code postal français.
     * Accepte :
     * - Codes métropolitains : 01xxx à 95xxx (sauf 20xxx)
     * - Corse : 2Axxx et 2Bxxx
     * - DOM-TOM : 97xxx, 98xxx, 99xxx
     * 
     * @param postalCode Code postal à valider
     * @return true si le code postal est valide, false sinon
     */
    public static boolean isValidPostalCode(final String postalCode) {
        return RegexUtils.isValidPostalCode(postalCode);
    }
    
    /**
     * Valide si un montant est positif.
     * 
     * @param amount Montant à valider
     * @return true si le montant est positif, false sinon
     */
    public static boolean isPositiveAmount(final Number amount) {
        return RegexUtils.isPositiveAmount(amount);
    }
    
    /**
     * Valide le format d'un nom de personne (lettres, espaces, tirets, apostrophes).
     * 
     * @param name Nom à valider
     * @return true si le nom est valide, false sinon
     */
    public static boolean isValidName(final String name) {
        return RegexUtils.isValidName(name);
    }
    
    /**
     * Valide le format d'une raison sociale.
     * 
     * @param companyName Raison sociale à valider
     * @return true si la raison sociale est valide, false sinon
     */
    public static boolean isValidCompanyName(final String companyName) {
        return RegexUtils.isValidCompanyName(companyName);
    }
    
    /**
     * Valide le format d'une adresse.
     * 
     * @param address Adresse à valider
     * @return true si l'adresse est valide, false sinon
     */
    public static boolean isValidAddress(final String address) {
        return RegexUtils.isValidAddress(address);
    }
    
    /**
     * Valide le format d'une ville.
     * 
     * @param city Ville à valider
     * @return true si la ville est valide, false sinon
     */
    public static boolean isValidCity(final String city) {
        return RegexUtils.isValidCity(city);
    }
    
    /**
     * Valide le format d'un pays.
     * 
     * @param country Pays à valider
     * @return true si le pays est valide, false sinon
     */
    public static boolean isValidCountry(final String country) {
        return RegexUtils.isValidCountry(country);
    }
    
    /**
     * Valide si une chaîne est alphanumérique.
     * 
     * @param str Chaîne à valider
     * @return true si la chaîne est alphanumérique, false sinon
     */
    public static boolean isAlphanumeric(final String str) {
        return RegexUtils.isAlphanumeric(str);
    }
    
    /**
     * Valide le format d'un numéro de rue.
     * 
     * @param streetNumber Numéro de rue à valider
     * @return true si le numéro est valide, false sinon
     */
    public static boolean isValidStreetNumber(final String streetNumber) {
        return RegexUtils.isValidStreetNumber(streetNumber);
    }
} 