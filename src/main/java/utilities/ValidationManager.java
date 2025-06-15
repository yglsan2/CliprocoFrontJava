package utilities;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import exceptions.ValidationException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Gestionnaire de validation pour l'application.
 * Utilise Jakarta Validation pour valider les objets.
 * 
 * @author Benja2
 * @version 1.0
 * @since 1.0
 */
public final class ValidationManager {
    
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();
    
    /**
     * Constructeur privé pour empêcher l'instanciation de la classe utilitaire.
     */
    private ValidationManager() {
        throw new IllegalStateException("Classe utilitaire, ne pas instancier");
    }

    /**
     * Valide un objet et retourne les violations.
     *
     * @param object l'objet à valider
     * @return un ensemble de violations, vide si l'objet est valide
     * @throws ValidationException si une erreur survient lors de la validation
     */
    public static <T> Set<ConstraintViolation<T>> validate(T object) throws ValidationException {
        try {
            return validator.validate(object);
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la validation de l'objet", e);
            throw new ValidationException("Erreur lors de la validation de l'objet", e);
        }
    }

    /**
     * Vérifie si un objet est valide.
     *
     * @param object l'objet à valider
     * @return true si l'objet est valide, false sinon
     * @throws ValidationException si une erreur survient lors de la validation
     */
    public static <T> boolean isValid(T object) throws ValidationException {
        try {
            return validator.validate(object).isEmpty();
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
            Set<ConstraintViolation<T>> violations = validator.validate(object);
            if (violations.isEmpty()) {
                return null;
            }
            return violations.stream()
                    .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                    .collect(Collectors.joining(", "));
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
            Set<ConstraintViolation<T>> violations = validator.validate(object);
            if (!violations.isEmpty()) {
                String messages = getValidationMessages(object);
                throw new ValidationException("Validation failed: " + messages);
            }
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la validation de l'objet", e);
            throw new ValidationException("Erreur lors de la validation de l'objet", e);
        }
    }
} 