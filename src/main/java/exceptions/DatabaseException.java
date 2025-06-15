package exceptions;

/**
 * Exception levée lors d'une erreur d'accès à la base de données.
 */
public class DatabaseException extends Exception {
    private static final long serialVersionUID = 1L;

    public DatabaseException(String message) {
        super(message);
    }
    
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
} 