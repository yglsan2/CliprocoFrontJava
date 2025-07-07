package utilities;

/**
 * Résultat d'une validation avec détails sur l'erreur.
 * Permet de fournir des informations contextuelles sur les erreurs de validation.
 */
public final class ValidationResult {
    
    private final boolean valid;
    private final String message;
    private final String fieldName;
    private final String fieldType;
    private final String value;
    private final String suggestions;
    private final ValidationErrorType errorType;
    
    /**
     * Types d'erreurs de validation.
     */
    public enum ValidationErrorType {
        REQUIRED("Champ obligatoire"),
        EMPTY("Champ vide"),
        TOO_SHORT("Trop court"),
        TOO_LONG("Trop long"),
        INVALID_FORMAT("Format invalide"),
        INVALID_VALUE("Valeur invalide"),
        NEGATIVE_VALUE("Valeur négative"),
        CONTAINS_INVALID_CHARS("Caractères non autorisés"),
        WRONG_LENGTH("Longueur incorrecte"),
        CUSTOM("Erreur personnalisée");
        
        private final String description;
        
        ValidationErrorType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    /**
     * Constructeur privé. Utilisez les méthodes statiques pour créer des instances.
     */
    private ValidationResult(boolean valid, String message, String fieldName, String fieldType, 
                           String value, String suggestions, ValidationErrorType errorType) {
        this.valid = valid;
        this.message = message;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.value = value;
        this.suggestions = suggestions;
        this.errorType = errorType;
    }
    
    /**
     * Crée un résultat de validation valide.
     * 
     * @return ValidationResult valide
     */
    public static ValidationResult valid() {
        return new ValidationResult(true, null, null, null, null, null, null);
    }
    
    /**
     * Crée un résultat de validation valide avec message contextuel.
     * 
     * @param message Message contextuel
     * @return ValidationResult valide avec message
     */
    public static ValidationResult valid(String message) {
        return new ValidationResult(true, message, null, null, null, null, null);
    }
    
    /**
     * Crée un résultat de validation invalide.
     * 
     * @param message Message d'erreur
     * @param fieldName Nom du champ
     * @param fieldType Type de champ
     * @param value Valeur saisie
     * @param errorType Type d'erreur
     * @return ValidationResult invalide
     */
    public static ValidationResult invalid(String message, String fieldName, String fieldType, 
                                         String value, ValidationErrorType errorType) {
        String suggestions = ValidationMessages.getSuggestions(fieldType);
        return new ValidationResult(false, message, fieldName, fieldType, value, suggestions, errorType);
    }
    
    /**
     * Crée un résultat de validation invalide avec suggestions personnalisées.
     * 
     * @param message Message d'erreur
     * @param fieldName Nom du champ
     * @param fieldType Type de champ
     * @param value Valeur saisie
     * @param suggestions Suggestions personnalisées
     * @param errorType Type d'erreur
     * @return ValidationResult invalide avec suggestions
     */
    public static ValidationResult invalid(String message, String fieldName, String fieldType, 
                                         String value, String suggestions, ValidationErrorType errorType) {
        return new ValidationResult(false, message, fieldName, fieldType, value, suggestions, errorType);
    }
    
    /**
     * Crée un résultat pour un champ requis manquant.
     * 
     * @param fieldName Nom du champ
     * @param fieldType Type de champ
     * @return ValidationResult pour champ requis
     */
    public static ValidationResult required(String fieldName, String fieldType) {
        String message = getRequiredMessage(fieldType);
        return invalid(message, fieldName, fieldType, null, ValidationErrorType.REQUIRED);
    }
    
    /**
     * Crée un résultat pour un champ vide.
     * 
     * @param fieldName Nom du champ
     * @param fieldType Type de champ
     * @return ValidationResult pour champ vide
     */
    public static ValidationResult empty(String fieldName, String fieldType) {
        String message = getEmptyMessage(fieldType);
        return invalid(message, fieldName, fieldType, null, ValidationErrorType.EMPTY);
    }
    
    /**
     * Crée un résultat pour un format invalide.
     * 
     * @param fieldName Nom du champ
     * @param fieldType Type de champ
     * @param value Valeur saisie
     * @return ValidationResult pour format invalide
     */
    public static ValidationResult invalidFormat(String fieldName, String fieldType, String value) {
        String message = ValidationMessages.getCustomErrorMessage(fieldName, fieldType, value);
        return invalid(message, fieldName, fieldType, value, ValidationErrorType.INVALID_FORMAT);
    }
    
    // ===== GETTERS =====
    
    public boolean isValid() {
        return valid;
    }
    
    public String getMessage() {
        return message;
    }
    
    public String getFieldName() {
        return fieldName;
    }
    
    public String getFieldType() {
        return fieldType;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getSuggestions() {
        return suggestions;
    }
    
    public ValidationErrorType getErrorType() {
        return errorType;
    }
    
    // ===== MÉTHODES UTILITAIRES =====
    
    /**
     * Obtient le message d'erreur complet avec suggestions.
     * 
     * @return Message complet
     */
    public String getFullMessage() {
        if (valid) {
            return message != null ? message : "Validation réussie";
        }
        
        StringBuilder fullMessage = new StringBuilder(message);
        if (suggestions != null && !suggestions.isEmpty()) {
            fullMessage.append("\n").append(suggestions);
        }
        return fullMessage.toString();
    }
    
    /**
     * Obtient le message d'erreur formaté pour l'affichage.
     * 
     * @return Message formaté
     */
    public String getFormattedMessage() {
        if (valid) {
            return message != null ? "✅ " + message : "✅ Validation réussie";
        }
        
        StringBuilder formatted = new StringBuilder("❌ ").append(message);
        if (suggestions != null && !suggestions.isEmpty()) {
            formatted.append("\n💡 ").append(suggestions);
        }
        return formatted.toString();
    }
    
    // ===== MÉTHODES PRIVÉES =====
    
    private static String getRequiredMessage(String fieldType) {
        switch (fieldType.toLowerCase()) {
            case "email":
                return ValidationMessages.EMAIL_REQUIRED;
            case "phone":
                return ValidationMessages.PHONE_REQUIRED;
            case "postal":
                return ValidationMessages.POSTAL_CODE_REQUIRED;
            case "name":
                return ValidationMessages.NAME_REQUIRED;
            case "company":
                return ValidationMessages.COMPANY_NAME_REQUIRED;
            case "address":
                return ValidationMessages.ADDRESS_REQUIRED;
            case "city":
                return ValidationMessages.CITY_REQUIRED;
            case "country":
                return ValidationMessages.COUNTRY_REQUIRED;
            case "streetnumber":
                return ValidationMessages.STREET_NUMBER_REQUIRED;
            default:
                return ValidationMessages.FIELD_REQUIRED;
        }
    }
    
    private static String getEmptyMessage(String fieldType) {
        switch (fieldType.toLowerCase()) {
            case "email":
                return ValidationMessages.EMAIL_INVALID;
            case "phone":
                return ValidationMessages.PHONE_INVALID;
            case "postal":
                return ValidationMessages.POSTAL_CODE_INVALID;
            case "name":
                return ValidationMessages.NAME_INVALID;
            case "company":
                return ValidationMessages.COMPANY_NAME_INVALID;
            case "address":
                return ValidationMessages.ADDRESS_INVALID;
            case "city":
                return ValidationMessages.CITY_INVALID;
            case "country":
                return ValidationMessages.COUNTRY_INVALID;
            case "streetnumber":
                return ValidationMessages.STREET_NUMBER_INVALID;
            default:
                return ValidationMessages.FIELD_EMPTY;
        }
    }
    
    @Override
    public String toString() {
        if (valid) {
            return "ValidationResult{valid=true, message='" + message + "'}";
        }
        return String.format("ValidationResult{valid=false, field='%s', type='%s', value='%s', error='%s', message='%s'}", 
                           fieldName, fieldType, value, errorType, message);
    }
} 