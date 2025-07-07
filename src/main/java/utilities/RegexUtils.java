package utilities;

/**
 * Classe utilitaire pour les validations regex.
 * Fournit des méthodes spécialisées pour différents types de validation.
 */
public final class RegexUtils {
    
    /**
     * Constructeur privé pour empêcher l'instanciation.
     */
    private RegexUtils() {
        // Classe utilitaire, pas d'instanciation
    }
    
    /**
     * Valide un email selon RFC 5322.
     * 
     * @param email Email à valider
     * @return true si l'email est valide, false sinon
     */
    public static boolean isValidEmail(final String email) {
        if (email == null) {
            System.out.println("[LOG][isValidEmail] Rejeté : null");
            return false;
        }
        String trimmedEmail = email.trim();
        if (trimmedEmail.isEmpty()) {
            System.out.println("[LOG][isValidEmail] Rejeté : chaîne vide après trim");
            return false;
        }
        int atIndex = trimmedEmail.indexOf('@');
        if (atIndex < 1 || atIndex == trimmedEmail.length() - 1) {
            System.out.println("[LOG][isValidEmail] Rejeté : position de @ invalide dans '" + trimmedEmail + "'");
            return false;
        }
        if (trimmedEmail.contains("..")) {
            System.out.println("[LOG][isValidEmail] Rejeté : double point dans '" + trimmedEmail + "'");
            return false;
        }
        String local = trimmedEmail.substring(0, atIndex);
        if (local.startsWith(".") || local.endsWith(".")) {
            System.out.println("[LOG][isValidEmail] Rejeté : partie locale commence ou finit par un point dans '" + trimmedEmail + "'");
            return false;
        }
        String domain = trimmedEmail.substring(atIndex + 1);
        if (!domain.matches("^[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            System.out.println("[LOG][isValidEmail] Rejeté : domaine invalide dans '" + trimmedEmail + "'");
            return false;
        }
        boolean match = RegexPatterns.EMAIL.matcher(trimmedEmail).matches();
        if (match) {
            System.out.println("[LOG][isValidEmail] Accepté : '" + trimmedEmail + "'");
        } else {
            System.out.println("[LOG][isValidEmail] Rejeté : ne correspond pas au pattern : '" + trimmedEmail + "'");
        }
        return match;
    }
    
    /**
     * Valide un numéro de téléphone français.
     * Accepte les formats :
     * - 0612345678 (format national)
     * - +33612345678 (format international)
     * - 0033612345678 (format international)
     * 
     * @param phone Numéro de téléphone à valider
     * @return true si le numéro est valide, false sinon
     */
    public static boolean isValidPhone(final String phone) {
        if (phone == null) {
            System.out.println("[LOG][isValidPhone] Rejeté : null");
            return false;
        }
        String trimmed = phone.trim();
        if (trimmed.isEmpty()) {
            System.out.println("[LOG][isValidPhone] Rejeté : chaîne vide après trim");
            return false;
        }
        boolean match = RegexPatterns.PHONE.matcher(trimmed).matches();
        if (match) {
            System.out.println("[LOG][isValidPhone] Accepté : '" + trimmed + "'");
        } else {
            System.out.println("[LOG][isValidPhone] Rejeté : '" + trimmed + "' ne correspond pas au pattern");
        }
        return match;
    }
    
    /**
     * Valide un code postal français.
     * Accepte :
     * - Codes métropolitains : 01xxx à 95xxx (sauf 20xxx)
     * - Corse : 2Axxx et 2Bxxx
     * - DOM-TOM : 97xxx, 98xxx, 99xxx
     * 
     * @param postalCode Code postal à valider
     * @return true si le code postal est valide, false sinon
     */
    public static boolean isValidPostalCode(final String postalCode) {
        if (postalCode == null) {
            System.out.println("[LOG][isValidPostalCode] Rejeté : null");
            return false;
        }
        String code = postalCode.trim().toUpperCase();
        if (code.isEmpty()) {
            System.out.println("[LOG][isValidPostalCode] Rejeté : chaîne vide après trim");
            return false;
        }
        if (code.equals("20000")) {
            System.out.println("[LOG][isValidPostalCode] Rejeté : 20000 interdit");
            return false;
        }
        boolean match = RegexPatterns.POSTAL_CODE.matcher(code).matches();
        if (match) {
            System.out.println("[LOG][isValidPostalCode] Accepté : '" + code + "'");
        } else {
            System.out.println("[LOG][isValidPostalCode] Rejeté : ne correspond pas au pattern : '" + code + "'");
        }
        return match;
    }
    
    /**
     * Valide un nom de personne.
     * 
     * @param name Nom à valider
     * @return true si le nom est valide, false sinon
     */
    public static boolean isValidName(final String name) {
        if (name == null) {
            System.out.println("[LOG][isValidName] Rejeté : null");
            return false;
        }
        String trimmed = name.trim();
        if (trimmed.isEmpty()) {
            System.out.println("[LOG][isValidName] Rejeté : chaîne vide après trim");
            return false;
        }
        boolean match = RegexPatterns.NAME.matcher(trimmed).matches();
        if (match) {
            System.out.println("[LOG][isValidName] Accepté : '" + trimmed + "'");
        } else {
            System.out.println("[LOG][isValidName] Rejeté : ne correspond pas au pattern : '" + trimmed + "'");
        }
        return match;
    }
    
    /**
     * Valide une raison sociale.
     * 
     * @param companyName Raison sociale à valider
     * @return true si la raison sociale est valide, false sinon
     */
    public static boolean isValidCompanyName(final String companyName) {
        if (companyName == null) {
            System.out.println("[LOG][isValidCompanyName] Rejeté : null");
            return false;
        }
        String trimmed = companyName.trim();
        if (trimmed.isEmpty()) {
            System.out.println("[LOG][isValidCompanyName] Rejeté : chaîne vide après trim");
            return false;
        }
        boolean match = RegexPatterns.COMPANY_NAME.matcher(trimmed).matches();
        if (match) {
            System.out.println("[LOG][isValidCompanyName] Accepté : '" + trimmed + "'");
        } else {
            System.out.println("[LOG][isValidCompanyName] Rejeté : ne correspond pas au pattern : '" + trimmed + "'");
        }
        return match;
    }
    
    /**
     * Valide une adresse.
     * 
     * @param address Adresse à valider
     * @return true si l'adresse est valide, false sinon
     */
    public static boolean isValidAddress(final String address) {
        if (address == null) {
            System.out.println("[LOG][isValidAddress] Rejeté : null");
            return false;
        }
        String trimmed = address.trim();
        if (trimmed.isEmpty()) {
            System.out.println("[LOG][isValidAddress] Rejeté : chaîne vide après trim");
            return false;
        }
        boolean match = RegexPatterns.ADDRESS.matcher(trimmed).matches();
        if (match) {
            System.out.println("[LOG][isValidAddress] Accepté : '" + trimmed + "'");
        } else {
            System.out.println("[LOG][isValidAddress] Rejeté : ne correspond pas au pattern : '" + trimmed + "'");
        }
        return match;
    }
    
    /**
     * Valide une ville.
     * 
     * @param city Ville à valider
     * @return true si la ville est valide, false sinon
     */
    public static boolean isValidCity(final String city) {
        if (city == null) {
            System.out.println("[LOG][isValidCity] Rejeté : null");
            return false;
        }
        String trimmed = city.trim();
        if (trimmed.isEmpty()) {
            System.out.println("[LOG][isValidCity] Rejeté : chaîne vide après trim");
            return false;
        }
        boolean match = RegexPatterns.CITY.matcher(trimmed).matches();
        if (match) {
            System.out.println("[LOG][isValidCity] Accepté : '" + trimmed + "'");
        } else {
            System.out.println("[LOG][isValidCity] Rejeté : ne correspond pas au pattern : '" + trimmed + "'");
        }
        return match;
    }
    
    /**
     * Valide un pays.
     * 
     * @param country Pays à valider
     * @return true si le pays est valide, false sinon
     */
    public static boolean isValidCountry(final String country) {
        if (country == null) {
            System.out.println("[LOG][isValidCountry] Rejeté : null");
            return false;
        }
        String trimmed = country.trim();
        if (trimmed.isEmpty()) {
            System.out.println("[LOG][isValidCountry] Rejeté : chaîne vide après trim");
            return false;
        }
        boolean match = RegexPatterns.COUNTRY.matcher(trimmed).matches();
        if (match) {
            System.out.println("[LOG][isValidCountry] Accepté : '" + trimmed + "'");
        } else {
            System.out.println("[LOG][isValidCountry] Rejeté : ne correspond pas au pattern : '" + trimmed + "'");
        }
        return match;
    }
    
    /**
     * Valide un numéro de rue.
     * 
     * @param streetNumber Numéro de rue à valider
     * @return true si le numéro est valide, false sinon
     */
    public static boolean isValidStreetNumber(final String streetNumber) {
        if (streetNumber == null) {
            System.out.println("[LOG][isValidStreetNumber] Rejeté : null");
            return false;
        }
        String trimmed = streetNumber.trim();
        if (trimmed.isEmpty()) {
            System.out.println("[LOG][isValidStreetNumber] Rejeté : chaîne vide après trim");
            return false;
        }
        boolean match = RegexPatterns.STREET_NUMBER.matcher(trimmed).matches();
        if (match) {
            System.out.println("[LOG][isValidStreetNumber] Accepté : '" + trimmed + "'");
        } else {
            System.out.println("[LOG][isValidStreetNumber] Rejeté : ne correspond pas au pattern : '" + trimmed + "'");
        }
        return match;
    }
    
    /**
     * Valide si une chaîne est alphanumérique.
     * 
     * @param str Chaîne à valider
     * @return true si la chaîne est alphanumérique, false sinon
     */
    public static boolean isAlphanumeric(final String str) {
        if (str == null) {
            System.out.println("[LOG][isAlphanumeric] Rejeté : null");
            return false;
        }
        String trimmed = str.trim();
        if (trimmed.isEmpty()) {
            System.out.println("[LOG][isAlphanumeric] Rejeté : chaîne vide après trim");
            return false;
        }
        boolean match = RegexPatterns.ALPHANUMERIC.matcher(trimmed).matches();
        if (match) {
            System.out.println("[LOG][isAlphanumeric] Accepté : '" + trimmed + "'");
        } else {
            System.out.println("[LOG][isAlphanumeric] Rejeté : ne correspond pas au pattern : '" + trimmed + "'");
        }
        return match;
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
     * Valide si un montant est positif.
     * 
     * @param amount Montant à valider
     * @return true si le montant est positif, false sinon
     */
    public static boolean isPositiveAmount(final Number amount) {
        if (amount == null) {
            System.out.println("[LOG][isPositiveAmount] Rejeté : null");
            return false;
        }
        boolean result = amount.doubleValue() > 0;
        if (result) {
            System.out.println("[LOG][isPositiveAmount] Accepté : '" + amount + "'");
        } else {
            System.out.println("[LOG][isPositiveAmount] Rejeté : non positif : '" + amount + "'");
        }
        return result;
    }

    public static boolean isValidInteger(final String str) {
        if (str == null) {
            System.out.println("[LOG][isValidInteger] Rejeté : null");
            return false;
        }
        String trimmed = str.trim();
        if (trimmed.isEmpty()) {
            System.out.println("[LOG][isValidInteger] Rejeté : chaîne vide après trim");
            return false;
        }
        boolean match = RegexPatterns.INTEGER.matcher(trimmed).matches();
        if (match) {
            System.out.println("[LOG][isValidInteger] Accepté : '" + trimmed + "'");
        } else {
            System.out.println("[LOG][isValidInteger] Rejeté : ne correspond pas au pattern : '" + trimmed + "'");
        }
        return match;
    }
} 