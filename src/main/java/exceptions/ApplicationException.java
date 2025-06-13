package exceptions;

/**
 * Exception de base pour l'application.
 * Cette classe sert de point d'entrée pour toutes les exceptions personnalisées
 * de l'application.
 * 
 * @author Benja2
 * @version 1.0
 * @since 1.0
 */
public class ApplicationException extends Exception {
    
    private static final long serialVersionUID = 1L;
    private final String errorCode;

    /**
     * Constructeur avec message d'erreur.
     *
     * @param message le message d'erreur
     */
    public ApplicationException(String message) {
        super(message);
        this.errorCode = "APP-000";
    }

    /**
     * Constructeur avec message d'erreur et code d'erreur.
     *
     * @param message le message d'erreur
     * @param errorCode le code d'erreur
     */
    public ApplicationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructeur avec message d'erreur et cause.
     *
     * @param message le message d'erreur
     * @param cause la cause de l'exception
     */
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "APP-000";
    }

    /**
     * Constructeur avec message d'erreur, code d'erreur et cause.
     *
     * @param message le message d'erreur
     * @param errorCode le code d'erreur
     * @param cause la cause de l'exception
     */
    public ApplicationException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Retourne le code d'erreur.
     *
     * @return le code d'erreur
     */
    public String getErrorCode() {
        return errorCode;
    }
} 