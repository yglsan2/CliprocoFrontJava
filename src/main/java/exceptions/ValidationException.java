package exceptions;

/**
 * Exception levée lors d'une erreur de validation des données.
 * Cette exception est utilisée pour signaler les erreurs de validation des données.
 * 
 * @author Benja2
 * @version 1.0
 * @since 1.0
 */
public class ValidationException extends Exception {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE_PREFIX = "VAL-";

    /**
     * Constructeur avec message d'erreur.
     *
     * @param message le message d'erreur
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructeur avec message d'erreur et cause.
     *
     * @param message le message d'erreur
     * @param cause la cause de l'exception
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructeur avec message d'erreur et code d'erreur.
     *
     * @param message le message d'erreur
     * @param errorCode le code d'erreur
     */
    public ValidationException(String message, String errorCode) {
        super(message, ERROR_CODE_PREFIX + errorCode);
    }

    /**
     * Constructeur avec message d'erreur, code d'erreur et cause.
     *
     * @param message le message d'erreur
     * @param errorCode le code d'erreur
     * @param cause la cause de l'exception
     */
    public ValidationException(String message, String errorCode, Throwable cause) {
        super(message, ERROR_CODE_PREFIX + errorCode, cause);
    }
} 