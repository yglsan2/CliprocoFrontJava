package exceptions;

/**
 * Exception levée lorsqu'une opération n'est pas autorisée.
 */
public class AuthorizationException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur avec message d'erreur.
     * @param message Le message d'erreur
     */
    public AuthorizationException(String message) {
        super(message);
    }

    /**
     * Constructeur avec message d'erreur et cause.
     * @param message Le message d'erreur
     * @param cause La cause de l'erreur
     */
    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
} 