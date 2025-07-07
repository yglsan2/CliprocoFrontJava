package utilities;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe utilitaire pour les messages d'erreur de validation.
 * Fournit des messages contextuels détaillés pour chaque type de validation.
 */
public final class ValidationMessages {
    
    // ===== MESSAGES D'ERREUR GÉNÉRAUX =====
    public static final String FIELD_REQUIRED = "Ce champ est obligatoire.";
    public static final String FIELD_EMPTY = "Ce champ ne peut pas être vide.";
    public static final String FIELD_TOO_SHORT = "Ce champ est trop court.";
    public static final String FIELD_TOO_LONG = "Ce champ est trop long.";
    public static final String FIELD_INVALID_FORMAT = "Format invalide.";
    
    // ===== MESSAGES EMAIL =====
    public static final String EMAIL_REQUIRED = "L'adresse email est obligatoire.";
    public static final String EMAIL_INVALID = "Format d'email invalide. Exemple : nom@domaine.com";
    public static final String EMAIL_TOO_LONG = "L'adresse email ne peut pas dépasser 254 caractères.";
    public static final String EMAIL_LOCAL_TOO_LONG = "La partie locale de l'email ne peut pas dépasser 64 caractères.";
    
    // ===== MESSAGES TÉLÉPHONE =====
    public static final String PHONE_REQUIRED = "Le numéro de téléphone est obligatoire.";
    public static final String PHONE_INVALID = "Format de téléphone invalide. Formats acceptés : 0612345678, +33612345678, 0033612345678";
    public static final String PHONE_TOO_SHORT = "Le numéro de téléphone doit contenir au moins 10 chiffres.";
    public static final String PHONE_TOO_LONG = "Le numéro de téléphone ne peut pas dépasser 15 caractères.";
    public static final String PHONE_STARTS_WITH_ZERO = "Le numéro doit commencer par 0 pour un format national.";
    public static final String PHONE_INTERNATIONAL_FORMAT = "Pour un format international, utilisez +33 ou 0033.";
    
    // ===== MESSAGES CODE POSTAL =====
    public static final String POSTAL_CODE_REQUIRED = "Le code postal est obligatoire.";
    public static final String POSTAL_CODE_INVALID = "Code postal invalide. Format attendu : 5 chiffres (ex: 75001)";
    public static final String POSTAL_CODE_WRONG_LENGTH = "Le code postal doit contenir exactement 5 caractères.";
    public static final String POSTAL_CODE_METROPOLE = "Code postal métropolitain invalide. Doit être entre 01xxx et 95xxx (sauf 20xxx).";
    public static final String POSTAL_CODE_CORSE = "Code postal Corse invalide. Doit être 2Axxx ou 2Bxxx.";
    public static final String POSTAL_CODE_DOMTOM = "Code postal DOM-TOM invalide. Doit être 97xxx, 98xxx ou 99xxx.";
    public static final String POSTAL_CODE_20000_INVALID = "Le code postal 20000 n'est pas valide.";
    
    // ===== MESSAGES NOMS =====
    public static final String NAME_REQUIRED = "Le nom est obligatoire.";
    public static final String NAME_INVALID = "Le nom ne peut contenir que des lettres, espaces, tirets et apostrophes.";
    public static final String NAME_TOO_SHORT = "Le nom doit contenir au moins 2 caractères.";
    public static final String NAME_TOO_LONG = "Le nom ne peut pas dépasser 100 caractères.";
    public static final String NAME_CONTAINS_NUMBERS = "Le nom ne peut pas contenir de chiffres.";
    public static final String NAME_CONTAINS_SPECIAL_CHARS = "Le nom ne peut contenir que des lettres, espaces, tirets (-) et apostrophes (').";
    
    // ===== MESSAGES RAISON SOCIALE =====
    public static final String COMPANY_NAME_REQUIRED = "La raison sociale est obligatoire.";
    public static final String COMPANY_NAME_INVALID = "La raison sociale contient des caractères non autorisés.";
    public static final String COMPANY_NAME_TOO_SHORT = "La raison sociale doit contenir au moins 2 caractères.";
    public static final String COMPANY_NAME_TOO_LONG = "La raison sociale ne peut pas dépasser 200 caractères.";
    
    // ===== MESSAGES ADRESSES =====
    public static final String ADDRESS_REQUIRED = "L'adresse est obligatoire.";
    public static final String ADDRESS_INVALID = "L'adresse contient des caractères non autorisés.";
    public static final String ADDRESS_TOO_SHORT = "L'adresse doit contenir au moins 5 caractères.";
    public static final String ADDRESS_TOO_LONG = "L'adresse ne peut pas dépasser 255 caractères.";
    
    public static final String CITY_REQUIRED = "La ville est obligatoire.";
    public static final String CITY_INVALID = "La ville ne peut contenir que des lettres, espaces, tirets et caractères accentués.";
    public static final String CITY_TOO_SHORT = "La ville doit contenir au moins 2 caractères.";
    public static final String CITY_TOO_LONG = "La ville ne peut pas dépasser 100 caractères.";
    
    public static final String COUNTRY_REQUIRED = "Le pays est obligatoire.";
    public static final String COUNTRY_INVALID = "Le pays ne peut contenir que des lettres, espaces, tirets et caractères accentués.";
    public static final String COUNTRY_TOO_SHORT = "Le pays doit contenir au moins 2 caractères.";
    public static final String COUNTRY_TOO_LONG = "Le pays ne peut pas dépasser 100 caractères.";
    
    public static final String STREET_NUMBER_REQUIRED = "Le numéro de rue est obligatoire.";
    public static final String STREET_NUMBER_INVALID = "Format de numéro de rue invalide. Exemple : 123, 12B, A, etc.";
    public static final String STREET_NUMBER_TOO_LONG = "Le numéro de rue ne peut pas dépasser 10 caractères.";
    
    // ===== MESSAGES NUMÉRIQUES =====
    public static final String AMOUNT_REQUIRED = "Le montant est obligatoire.";
    public static final String AMOUNT_NEGATIVE = "Le montant doit être positif.";
    public static final String AMOUNT_TOO_HIGH = "Le montant est trop élevé.";
    public static final String AMOUNT_INVALID_FORMAT = "Format de montant invalide. Utilisez des chiffres et une virgule pour les décimales.";
    
    public static final String ALPHANUMERIC_REQUIRED = "Ce champ est obligatoire.";
    public static final String ALPHANUMERIC_INVALID = "Ce champ ne peut contenir que des lettres et des chiffres.";
    
    // ===== MESSAGES CONTEXTUELS =====
    private static final Map<String, String> CONTEXTUAL_MESSAGES = new HashMap<>();
    
    static {
        // Messages contextuels pour les codes postaux
        CONTEXTUAL_MESSAGES.put("75001", "Code postal de Paris valide.");
        CONTEXTUAL_MESSAGES.put("69001", "Code postal de Lyon valide.");
        CONTEXTUAL_MESSAGES.put("2A000", "Code postal de Corse-du-Sud valide.");
        CONTEXTUAL_MESSAGES.put("2B000", "Code postal de Haute-Corse valide.");
        CONTEXTUAL_MESSAGES.put("97000", "Code postal DOM-TOM valide.");
        
        // Messages contextuels pour les téléphones
        CONTEXTUAL_MESSAGES.put("0612345678", "Format de téléphone mobile valide.");
        CONTEXTUAL_MESSAGES.put("0123456789", "Format de téléphone fixe valide.");
        CONTEXTUAL_MESSAGES.put("+33612345678", "Format de téléphone international valide.");
    }
    
    /**
     * Constructeur privé pour empêcher l'instanciation.
     */
    private ValidationMessages() {
        // Classe utilitaire, pas d'instanciation
    }
    
    /**
     * Obtient un message contextuel pour une valeur donnée.
     * 
     * @param value Valeur à vérifier
     * @return Message contextuel ou null si aucun message spécifique
     */
    public static String getContextualMessage(String value) {
        return CONTEXTUAL_MESSAGES.get(value);
    }
    
    /**
     * Obtient un message d'erreur personnalisé avec des suggestions.
     * 
     * @param fieldName Nom du champ
     * @param fieldType Type de champ (email, phone, postal, etc.)
     * @param value Valeur saisie
     * @return Message d'erreur personnalisé
     */
    public static String getCustomErrorMessage(String fieldName, String fieldType, String value) {
        switch (fieldType.toLowerCase()) {
            case "email":
                return String.format("L'adresse email '%s' n'est pas valide. Format attendu : nom@domaine.com", value);
            case "phone":
                return String.format("Le numéro de téléphone '%s' n'est pas valide. Formats acceptés : 0612345678, +33612345678", value);
            case "postal":
                return String.format("Le code postal '%s' n'est pas valide. Format attendu : 5 chiffres (ex: 75001)", value);
            case "name":
                return String.format("Le nom '%s' contient des caractères non autorisés. Utilisez uniquement des lettres, espaces, tirets et apostrophes.", value);
            default:
                return String.format("Le champ '%s' avec la valeur '%s' n'est pas valide.", fieldName, value);
        }
    }
    
    /**
     * Obtient des suggestions pour corriger une erreur.
     * 
     * @param fieldType Type de champ
     * @return Suggestions de correction
     */
    public static String getSuggestions(String fieldType) {
        switch (fieldType.toLowerCase()) {
            case "email":
                return "Suggestions : vérifiez que l'email contient un @ et un domaine valide (ex: nom@domaine.com)";
            case "phone":
                return "Suggestions : utilisez 0612345678 (national) ou +33612345678 (international)";
            case "postal":
                return "Suggestions : utilisez 5 chiffres (ex: 75001 pour Paris, 69001 pour Lyon)";
            case "name":
                return "Suggestions : utilisez uniquement des lettres, espaces, tirets (-) et apostrophes (')";
            default:
                return "Vérifiez le format attendu pour ce champ.";
        }
    }
} 