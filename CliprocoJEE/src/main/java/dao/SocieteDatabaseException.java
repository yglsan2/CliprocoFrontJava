package dao;

public class SocieteDatabaseException extends Exception {
    
    public SocieteDatabaseException(String message) {
        super(message);
    }
    
    public SocieteDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
} 