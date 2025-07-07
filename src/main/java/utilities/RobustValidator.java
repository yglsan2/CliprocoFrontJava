package utilities;

/**
 * Validateur robuste avec messages contextuels détaillés.
 * Fournit des validations complètes avec des retours informatifs.
 */
public final class RobustValidator {
    
    // ===== CONSTANTES DE LONGUEUR =====
    private static final int EMAIL_MAX_LENGTH = 254;
    private static final int EMAIL_LOCAL_MAX_LENGTH = 64;
    private static final int PHONE_MIN_LENGTH = 10;
    private static final int PHONE_MAX_LENGTH = 15;
    private static final int POSTAL_CODE_LENGTH = 5;
    private static final int NAME_MIN_LENGTH = 2;
    private static final int NAME_MAX_LENGTH = 100;
    private static final int COMPANY_NAME_MIN_LENGTH = 2;
    private static final int COMPANY_NAME_MAX_LENGTH = 200;
    private static final int ADDRESS_MIN_LENGTH = 5;
    private static final int ADDRESS_MAX_LENGTH = 255;
    private static final int CITY_MIN_LENGTH = 2;
    private static final int CITY_MAX_LENGTH = 100;
    private static final int COUNTRY_MIN_LENGTH = 2;
    private static final int COUNTRY_MAX_LENGTH = 100;
    private static final int STREET_NUMBER_MAX_LENGTH = 10;
    
    /**
     * Constructeur privé pour empêcher l'instanciation.
     */
    private RobustValidator() {
        // Classe utilitaire, pas d'instanciation
    }
    
    // ===== VALIDATIONS EMAIL =====
    
    /**
     * Valide un email avec retour détaillé.
     * 
     * @param email Email à valider
     * @param fieldName Nom du champ (optionnel)
     * @return ValidationResult avec détails
     */
    public static ValidationResult validateEmail(String email, String fieldName) {
        fieldName = fieldName != null ? fieldName : "email";
        
        // Vérification null/vide
        if (email == null) {
            return ValidationResult.required(fieldName, "email");
        }
        
        String trimmedEmail = email.trim();
        if (trimmedEmail.isEmpty()) {
            return ValidationResult.empty(fieldName, "email");
        }
        
        // Vérification longueur
        if (trimmedEmail.length() > EMAIL_MAX_LENGTH) {
            return ValidationResult.invalid(ValidationMessages.EMAIL_TOO_LONG, fieldName, "email", 
                                          trimmedEmail, ValidationResult.ValidationErrorType.TOO_LONG);
        }
        
        // Vérification partie locale
        int atIndex = trimmedEmail.indexOf('@');
        if (atIndex > EMAIL_LOCAL_MAX_LENGTH) {
            return ValidationResult.invalid(ValidationMessages.EMAIL_LOCAL_TOO_LONG, fieldName, "email", 
                                          trimmedEmail, ValidationResult.ValidationErrorType.TOO_LONG);
        }
        
        // Vérification format
        if (!RegexUtils.isValidEmail(trimmedEmail)) {
            return ValidationResult.invalidFormat(fieldName, "email", trimmedEmail);
        }
        
        return ValidationResult.valid();
    }
    
    // ===== VALIDATIONS TÉLÉPHONE =====
    
    /**
     * Valide un numéro de téléphone avec retour détaillé.
     * 
     * @param phone Téléphone à valider
     * @param fieldName Nom du champ (optionnel)
     * @return ValidationResult avec détails
     */
    public static ValidationResult validatePhone(String phone, String fieldName) {
        fieldName = fieldName != null ? fieldName : "phone";
        
        // Vérification null/vide
        if (phone == null) {
            return ValidationResult.required(fieldName, "phone");
        }
        
        String trimmedPhone = phone.trim();
        if (trimmedPhone.isEmpty()) {
            return ValidationResult.empty(fieldName, "phone");
        }
        
        // Vérification longueur
        if (trimmedPhone.length() < PHONE_MIN_LENGTH) {
            return ValidationResult.invalid(ValidationMessages.PHONE_TOO_SHORT, fieldName, "phone", 
                                          trimmedPhone, ValidationResult.ValidationErrorType.TOO_SHORT);
        }
        
        if (trimmedPhone.length() > PHONE_MAX_LENGTH) {
            return ValidationResult.invalid(ValidationMessages.PHONE_TOO_LONG, fieldName, "phone", 
                                          trimmedPhone, ValidationResult.ValidationErrorType.TOO_LONG);
        }
        
        // Vérification format
        if (!RegexUtils.isValidPhone(trimmedPhone)) {
            return ValidationResult.invalidFormat(fieldName, "phone", trimmedPhone);
        }
        
        return ValidationResult.valid();
    }
    
    // ===== VALIDATIONS CODE POSTAL =====
    
    /**
     * Valide un code postal avec retour détaillé.
     * 
     * @param postalCode Code postal à valider
     * @param fieldName Nom du champ (optionnel)
     * @return ValidationResult avec détails
     */
    public static ValidationResult validatePostalCode(String postalCode, String fieldName) {
        fieldName = fieldName != null ? fieldName : "postal";
        
        // Vérification null/vide
        if (postalCode == null) {
            return ValidationResult.required(fieldName, "postal");
        }
        
        String trimmedCode = postalCode.trim().toUpperCase();
        if (trimmedCode.isEmpty()) {
            return ValidationResult.empty(fieldName, "postal");
        }
        
        // Vérification longueur
        if (trimmedCode.length() != POSTAL_CODE_LENGTH) {
            return ValidationResult.invalid(ValidationMessages.POSTAL_CODE_WRONG_LENGTH, fieldName, "postal", 
                                          trimmedCode, ValidationResult.ValidationErrorType.WRONG_LENGTH);
        }
        
        // Vérification format spécifique
        if (trimmedCode.equals("20000")) {
            return ValidationResult.invalid(ValidationMessages.POSTAL_CODE_20000_INVALID, fieldName, "postal", 
                                          trimmedCode, ValidationResult.ValidationErrorType.INVALID_VALUE);
        }
        
        // Vérification format général
        if (!RegexUtils.isValidPostalCode(trimmedCode)) {
            return ValidationResult.invalidFormat(fieldName, "postal", trimmedCode);
        }
        
        // Message contextuel si disponible
        String contextualMessage = ValidationMessages.getContextualMessage(trimmedCode);
        return ValidationResult.valid(contextualMessage);
    }
    
    // ===== VALIDATIONS NOMS =====
    
    /**
     * Valide un nom avec retour détaillé.
     * 
     * @param name Nom à valider
     * @param fieldName Nom du champ (optionnel)
     * @return ValidationResult avec détails
     */
    public static ValidationResult validateName(String name, String fieldName) {
        fieldName = fieldName != null ? fieldName : "name";
        
        // Vérification null/vide
        if (name == null) {
            return ValidationResult.required(fieldName, "name");
        }
        
        String trimmedName = name.trim();
        if (trimmedName.isEmpty()) {
            return ValidationResult.empty(fieldName, "name");
        }
        
        // Vérification longueur
        if (trimmedName.length() < NAME_MIN_LENGTH) {
            return ValidationResult.invalid(ValidationMessages.NAME_TOO_SHORT, fieldName, "name", 
                                          trimmedName, ValidationResult.ValidationErrorType.TOO_SHORT);
        }
        
        if (trimmedName.length() > NAME_MAX_LENGTH) {
            return ValidationResult.invalid(ValidationMessages.NAME_TOO_LONG, fieldName, "name", 
                                          trimmedName, ValidationResult.ValidationErrorType.TOO_LONG);
        }
        
        // Vérification format
        if (!RegexUtils.isValidName(trimmedName)) {
            return ValidationResult.invalidFormat(fieldName, "name", trimmedName);
        }
        
        return ValidationResult.valid();
    }
    
    // ===== VALIDATIONS RAISON SOCIALE =====
    
    /**
     * Valide une raison sociale avec retour détaillé.
     * 
     * @param companyName Raison sociale à valider
     * @param fieldName Nom du champ (optionnel)
     * @return ValidationResult avec détails
     */
    public static ValidationResult validateCompanyName(String companyName, String fieldName) {
        fieldName = fieldName != null ? fieldName : "company";
        
        // Vérification null/vide
        if (companyName == null) {
            return ValidationResult.required(fieldName, "company");
        }
        
        String trimmedName = companyName.trim();
        if (trimmedName.isEmpty()) {
            return ValidationResult.empty(fieldName, "company");
        }
        
        // Vérification longueur
        if (trimmedName.length() < COMPANY_NAME_MIN_LENGTH) {
            return ValidationResult.invalid(ValidationMessages.COMPANY_NAME_TOO_SHORT, fieldName, "company", 
                                          trimmedName, ValidationResult.ValidationErrorType.TOO_SHORT);
        }
        
        if (trimmedName.length() > COMPANY_NAME_MAX_LENGTH) {
            return ValidationResult.invalid(ValidationMessages.COMPANY_NAME_TOO_LONG, fieldName, "company", 
                                          trimmedName, ValidationResult.ValidationErrorType.TOO_LONG);
        }
        
        // Vérification format
        if (!RegexUtils.isValidCompanyName(trimmedName)) {
            return ValidationResult.invalidFormat(fieldName, "company", trimmedName);
        }
        
        return ValidationResult.valid();
    }
    
    // ===== VALIDATIONS ADRESSES =====
    
    /**
     * Valide une adresse avec retour détaillé.
     * 
     * @param address Adresse à valider
     * @param fieldName Nom du champ (optionnel)
     * @return ValidationResult avec détails
     */
    public static ValidationResult validateAddress(String address, String fieldName) {
        fieldName = fieldName != null ? fieldName : "address";
        
        // Vérification null/vide
        if (address == null) {
            return ValidationResult.required(fieldName, "address");
        }
        
        String trimmedAddress = address.trim();
        if (trimmedAddress.isEmpty()) {
            return ValidationResult.empty(fieldName, "address");
        }
        
        // Vérification longueur
        if (trimmedAddress.length() < ADDRESS_MIN_LENGTH) {
            return ValidationResult.invalid(ValidationMessages.ADDRESS_TOO_SHORT, fieldName, "address", 
                                          trimmedAddress, ValidationResult.ValidationErrorType.TOO_SHORT);
        }
        
        if (trimmedAddress.length() > ADDRESS_MAX_LENGTH) {
            return ValidationResult.invalid(ValidationMessages.ADDRESS_TOO_LONG, fieldName, "address", 
                                          trimmedAddress, ValidationResult.ValidationErrorType.TOO_LONG);
        }
        
        // Vérification format
        if (!RegexUtils.isValidAddress(trimmedAddress)) {
            return ValidationResult.invalidFormat(fieldName, "address", trimmedAddress);
        }
        
        return ValidationResult.valid();
    }
    
    /**
     * Valide une ville avec retour détaillé.
     * 
     * @param city Ville à valider
     * @param fieldName Nom du champ (optionnel)
     * @return ValidationResult avec détails
     */
    public static ValidationResult validateCity(String city, String fieldName) {
        fieldName = fieldName != null ? fieldName : "city";
        
        // Vérification null/vide
        if (city == null) {
            return ValidationResult.required(fieldName, "city");
        }
        
        String trimmedCity = city.trim();
        if (trimmedCity.isEmpty()) {
            return ValidationResult.empty(fieldName, "city");
        }
        
        // Vérification longueur
        if (trimmedCity.length() < CITY_MIN_LENGTH) {
            return ValidationResult.invalid(ValidationMessages.CITY_TOO_SHORT, fieldName, "city", 
                                          trimmedCity, ValidationResult.ValidationErrorType.TOO_SHORT);
        }
        
        if (trimmedCity.length() > CITY_MAX_LENGTH) {
            return ValidationResult.invalid(ValidationMessages.CITY_TOO_LONG, fieldName, "city", 
                                          trimmedCity, ValidationResult.ValidationErrorType.TOO_LONG);
        }
        
        // Vérification format
        if (!RegexUtils.isValidCity(trimmedCity)) {
            return ValidationResult.invalidFormat(fieldName, "city", trimmedCity);
        }
        
        return ValidationResult.valid();
    }
    
    /**
     * Valide un pays avec retour détaillé.
     * 
     * @param country Pays à valider
     * @param fieldName Nom du champ (optionnel)
     * @return ValidationResult avec détails
     */
    public static ValidationResult validateCountry(String country, String fieldName) {
        fieldName = fieldName != null ? fieldName : "country";
        
        // Vérification null/vide
        if (country == null) {
            return ValidationResult.required(fieldName, "country");
        }
        
        String trimmedCountry = country.trim();
        if (trimmedCountry.isEmpty()) {
            return ValidationResult.empty(fieldName, "country");
        }
        
        // Vérification longueur
        if (trimmedCountry.length() < COUNTRY_MIN_LENGTH) {
            return ValidationResult.invalid(ValidationMessages.COUNTRY_TOO_SHORT, fieldName, "country", 
                                          trimmedCountry, ValidationResult.ValidationErrorType.TOO_SHORT);
        }
        
        if (trimmedCountry.length() > COUNTRY_MAX_LENGTH) {
            return ValidationResult.invalid(ValidationMessages.COUNTRY_TOO_LONG, fieldName, "country", 
                                          trimmedCountry, ValidationResult.ValidationErrorType.TOO_LONG);
        }
        
        // Vérification format
        if (!RegexUtils.isValidCountry(trimmedCountry)) {
            return ValidationResult.invalidFormat(fieldName, "country", trimmedCountry);
        }
        
        return ValidationResult.valid();
    }
    
    /**
     * Valide un numéro de rue avec retour détaillé.
     * 
     * @param streetNumber Numéro de rue à valider
     * @param fieldName Nom du champ (optionnel)
     * @return ValidationResult avec détails
     */
    public static ValidationResult validateStreetNumber(String streetNumber, String fieldName) {
        fieldName = fieldName != null ? fieldName : "streetnumber";
        
        // Vérification null/vide
        if (streetNumber == null) {
            return ValidationResult.required(fieldName, "streetnumber");
        }
        
        String trimmedNumber = streetNumber.trim();
        if (trimmedNumber.isEmpty()) {
            return ValidationResult.empty(fieldName, "streetnumber");
        }
        
        // Vérification longueur
        if (trimmedNumber.length() > STREET_NUMBER_MAX_LENGTH) {
            return ValidationResult.invalid(ValidationMessages.STREET_NUMBER_TOO_LONG, fieldName, "streetnumber", 
                                          trimmedNumber, ValidationResult.ValidationErrorType.TOO_LONG);
        }
        
        // Vérification format
        if (!RegexUtils.isValidStreetNumber(trimmedNumber)) {
            return ValidationResult.invalidFormat(fieldName, "streetnumber", trimmedNumber);
        }
        
        return ValidationResult.valid();
    }
    
    // ===== VALIDATIONS NUMÉRIQUES =====
    
    /**
     * Valide un montant avec retour détaillé.
     * 
     * @param amount Montant à valider
     * @param fieldName Nom du champ (optionnel)
     * @return ValidationResult avec détails
     */
    public static ValidationResult validateAmount(Number amount, String fieldName) {
        fieldName = fieldName != null ? fieldName : "amount";
        
        // Vérification null
        if (amount == null) {
            return ValidationResult.required(fieldName, "amount");
        }
        
        // Vérification positif
        if (!RegexUtils.isPositiveAmount(amount)) {
            return ValidationResult.invalid(ValidationMessages.AMOUNT_NEGATIVE, fieldName, "amount", 
                                          amount.toString(), ValidationResult.ValidationErrorType.NEGATIVE_VALUE);
        }
        
        return ValidationResult.valid();
    }
    
    /**
     * Valide une chaîne alphanumérique avec retour détaillé.
     * 
     * @param str Chaîne à valider
     * @param fieldName Nom du champ (optionnel)
     * @return ValidationResult avec détails
     */
    public static ValidationResult validateAlphanumeric(String str, String fieldName) {
        fieldName = fieldName != null ? fieldName : "alphanumeric";
        
        // Vérification null/vide
        if (str == null) {
            return ValidationResult.required(fieldName, "alphanumeric");
        }
        
        String trimmedStr = str.trim();
        if (trimmedStr.isEmpty()) {
            return ValidationResult.empty(fieldName, "alphanumeric");
        }
        
        // Vérification format
        if (!RegexUtils.isAlphanumeric(trimmedStr)) {
            return ValidationResult.invalid(ValidationMessages.ALPHANUMERIC_INVALID, fieldName, "alphanumeric", 
                                          trimmedStr, ValidationResult.ValidationErrorType.INVALID_FORMAT);
        }
        
        return ValidationResult.valid();
    }
} 