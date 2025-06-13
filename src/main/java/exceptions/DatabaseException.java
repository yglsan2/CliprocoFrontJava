package exceptions;

/**
 * Exception levée en cas d'erreur de base de données.
 * Cette exception est utilisée pour encapsuler les erreurs liées à la base de données.
 * 
 * @author Benja2
 * @version 1.0
 * @since 1.0
 */
public class DatabaseException extends ApplicationException {
    
    private static final long serialVersionUID = 1L;
    private static final String ERROR_CODE_PREFIX = "DB-";

    /**
     * Constructeur avec message d'erreur.
     *
     * @param message le message d'erreur
     */
    public DatabaseException(String message) {
        super(message, ERROR_CODE_PREFIX + "001");
    }

    /**
     * Constructeur avec message d'erreur et code d'erreur.
     *
     * @param message le message d'erreur
     * @param errorCode le code d'erreur
     */
    public DatabaseException(String message, String errorCode) {
        super(message, ERROR_CODE_PREFIX + errorCode);
    }

    /**
     * Constructeur avec message d'erreur et cause.
     *
     * @param message le message d'erreur
     * @param cause la cause de l'exception
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, ERROR_CODE_PREFIX + "001", cause);
    }

    /**
     * Constructeur avec message d'erreur, code d'erreur et cause.
     *
     * @param message le message d'erreur
     * @param errorCode le code d'erreur
     * @param cause la cause de l'exception
     */
    public DatabaseException(String message, String errorCode, Throwable cause) {
        super(message, ERROR_CODE_PREFIX + errorCode, cause);
    }
} 