package exceptions;

public class ApplicationException extends Exception {
    private static final long serialVersionUID = 1L;
    private final String code;

    public ApplicationException(String message, String code) {
        super(message);
        this.code = code;
    }

    public ApplicationException(String message, String code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
} 