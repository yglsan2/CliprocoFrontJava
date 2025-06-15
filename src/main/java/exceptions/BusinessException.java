package exceptions;

/**
 * Exception levée lors d'une erreur métier.
 */
public class BusinessException extends ApplicationException {
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
} 