package utilities;

/**
 * Exception levée lors d'une erreur de validation.
 */
public class ValidationException extends RuntimeException {
    
    /**
     * Constructeur avec message.
     * 
     * @param message Message d'erreur
     */
    public ValidationException(final String message) {
        super(message);
    }
    
    /**
     * Constructeur avec message et cause.
     * 
     * @param message Message d'erreur
     * @param cause Cause de l'exception
     */
    public ValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }
} 