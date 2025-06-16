package models;

/**
 * Exception sur les entités du project.
 */
public final class SocieteEntityException extends Exception {

    /**
     *
     * @param message
     */
    public SocieteEntityException(final String message) {
        super(message);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public SocieteEntityException(final String message,
                                  final Throwable cause) {
        super(message, cause);
    }
}
