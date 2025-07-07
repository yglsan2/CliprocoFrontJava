package utilities;

import java.util.regex.Pattern;

/**
 * Classe utilitaire contenant tous les patterns regex utilisés dans l'application.
 * Centralise les expressions régulières pour faciliter la maintenance.
 */
public final class RegexPatterns {
    
    // ===== PATTERNS EMAIL =====
    /**
     * Email : format standard selon RFC 5322 simplifié.
     * Accepte les formats valides d'email.
     */
    public static final String EMAIL_PATTERN = 
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    
    // ===== PATTERNS TÉLÉPHONE =====
    /**
     * Téléphone français : formats nationaux et internationaux.
     * Format national : 0X XX XX XX XX (avec séparateurs optionnels)
     * Format international : +33 + 9 chiffres (sans le 0 initial)
     */
    public static final String PHONE_PATTERN = 
        "^(0[1-9]\\d{8}|0[1-9]([\\s.]\\d{2}){4}|\\+33\\s?[1-9]\\d{8}|\\+33\\s?[1-9]([\\s.]\\d{2}){4}|0033[1-9]\\d{8}|0033[1-9]([\\s.]\\d{2}){4})$";
    
    // ===== PATTERNS CODE POSTAL =====
    /**
     * Code postal français : formats métropolitains, Corse et DOM-TOM.
     * Métropolitain : 01xxx à 95xxx (sauf 20xxx)
     * Corse : 2Axxx et 2Bxxx
     * DOM-TOM : 97xxx, 98xxx, 99xxx
     */
    public static final String POSTAL_CODE_PATTERN = 
        "^((0[1-9]|[1-8][0-9]|9[0-5])\\d{3}|2[AB]\\d{3}|(97[1-8]|98[0-8])\\d{2}|99000)$";
    
    // Patterns spécifiques pour codes postaux
    public static final String POSTAL_CODE_METROPOLE_PATTERN = "^(0[1-9]|[1-8][0-9]|9[0-5])[0-9]{3}$";
    public static final String POSTAL_CODE_CORSE_PATTERN = "^(2A|2B)[0-9]{3}$";
    public static final String POSTAL_CODE_DOMTOM_PATTERN = "^(97[1-8]|98[0-8])[0-9]{2}$";
    
    // ===== PATTERNS NOMS ET TEXTES =====
    /**
     * Nom/prénom : lettres, espaces, tirets, apostrophes, caractères accentués.
     * Longueur : 2-50 caractères.
     */
    public static final String NAME_PATTERN = 
        "^[a-zA-ZÀ-ÿ\\s'-]{2,50}$";
    
    /**
     * Ville : lettres, espaces, tirets, apostrophes, caractères accentués.
     * Longueur : 2-50 caractères.
     */
    public static final String CITY_PATTERN = 
        "^[a-zA-ZÀ-ÿ\\s'-]{2,50}$";
    
    // ===== PATTERNS PAYS =====
    /**
     * Pays : lettres, espaces, tirets, apostrophes, caractères accentués.
     * Longueur : 2-50 caractères.
     */
    public static final String COUNTRY_PATTERN = 
        "^[a-zA-ZÀ-ÿ\\s'-]{2,50}$";
    
    // ===== PATTERNS RAISON SOCIALE =====
    /**
     * Raison sociale : lettres, chiffres, espaces, tirets, apostrophes, points, parenthèses, esperluette, virgule.
     * Longueur : 3-100 caractères.
     */
    public static final String COMPANY_NAME_PATTERN = 
        "^[a-zA-ZÀ-ÿ0-9'&.,()\\s-]{3,100}$";
    
    // ===== PATTERNS ADRESSES =====
    /**
     * Adresse complète : lettres, chiffres, espaces, tirets, apostrophes, points, parenthèses, virgule.
     * Longueur : 5-100 caractères.
     */
    public static final String ADDRESS_PATTERN = 
        "^[a-zA-ZÀ-ÿ0-9'.,()\\s-]{5,100}$";
    
    /**
     * Numéro de rue : chiffres + lettre optionnelle, ou suffixe bis/ter.
     * Formats acceptés : 123, 123A, 1, 1 bis, 23 ter, 42 bis
     * Formats rejetés : A123, A, bis, 1 quater
     */
    public static final String STREET_NUMBER_PATTERN = 
        "^([0-9]+[a-zA-Z]?|[0-9]+\\s+(bis|ter))$";
    
    // ===== PATTERNS NOM DE RUE =====
    /**
     * Nom de rue : lettres, chiffres, espaces, tirets, apostrophes, points.
     * Longueur : 2-100 caractères.
     */
    public static final String STREET_NAME_PATTERN = 
        "^[a-zA-ZÀ-ÿ0-9\\s'.,-]{2,100}$";
    
    // ===== PATTERNS NUMÉRIQUES =====
    /**
     * Montant : nombre positif avec décimales optionnelles (virgule ou point, max 2).
     * Longueur : 1-20 caractères.
     */
    public static final String AMOUNT_PATTERN = 
        "^[0-9]{1,18}([.,][0-9]{1,2})?$";
    
    /**
     * Entier positif : chiffres uniquement.
     * Longueur : 1-10 caractères.
     */
    public static final String INTEGER_PATTERN = 
        "^[0-9]{1,10}$";
    
    /**
     * Commentaire : tout sauf caractères de contrôle.
     * Longueur : 0-500 caractères.
     */
    public static final String COMMENT_PATTERN = 
        "^[\\s\\S]{0,500}$";
    
    /**
     * Alphanumérique : lettres et chiffres uniquement.
     * Longueur : 1-50 caractères.
     */
    public static final String ALPHANUMERIC_PATTERN = 
        "^[a-zA-Z0-9]{1,50}$";
    
    // ===== PATTERNS COMPILÉS =====
    public static final Pattern EMAIL = Pattern.compile(EMAIL_PATTERN);
    public static final Pattern PHONE = Pattern.compile(PHONE_PATTERN);
    public static final Pattern POSTAL_CODE = Pattern.compile(POSTAL_CODE_PATTERN);
    public static final Pattern POSTAL_CODE_CORSE = Pattern.compile(POSTAL_CODE_CORSE_PATTERN);
    public static final Pattern POSTAL_CODE_METROPOLE = Pattern.compile(POSTAL_CODE_METROPOLE_PATTERN);
    public static final Pattern POSTAL_CODE_DOMTOM = Pattern.compile(POSTAL_CODE_DOMTOM_PATTERN);
    public static final Pattern NAME = Pattern.compile(NAME_PATTERN);
    public static final Pattern COMPANY_NAME = Pattern.compile(COMPANY_NAME_PATTERN);
    public static final Pattern ADDRESS = Pattern.compile(ADDRESS_PATTERN);
    public static final Pattern STREET_NUMBER = Pattern.compile(STREET_NUMBER_PATTERN);
    public static final Pattern STREET_NAME = Pattern.compile(STREET_NAME_PATTERN);
    public static final Pattern AMOUNT = Pattern.compile(AMOUNT_PATTERN);
    public static final Pattern INTEGER = Pattern.compile(INTEGER_PATTERN);
    public static final Pattern COMMENT = Pattern.compile(COMMENT_PATTERN);
    public static final Pattern ALPHANUMERIC = Pattern.compile(ALPHANUMERIC_PATTERN);
    public static final Pattern CITY = Pattern.compile(CITY_PATTERN);
    public static final Pattern COUNTRY = Pattern.compile(COUNTRY_PATTERN);
    
    /**
     * Constructeur privé pour empêcher l'instanciation.
     */
    private RegexPatterns() {
        // Classe utilitaire, pas d'instanciation
    }
} 