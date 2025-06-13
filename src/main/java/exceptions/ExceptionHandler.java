package exceptions;

import logs.LogManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestionnaire centralisé des exceptions de l'application.
 * Cette classe fournit des méthodes utilitaires pour gérer les exceptions
 * de manière cohérente dans toute l'application.
 * 
 * @author Benja2
 * @version 1.0
 * @since 1.0
 */
public final class ExceptionHandler {

    private static final String ERROR_ATTRIBUTE = "errors";
    private static final String ERROR_PAGE = "error.jsp";
    private static final String GENERIC_ERROR_MESSAGE = "Une erreur est survenue. Veuillez réessayer plus tard.";

    /**
     * Constructeur privé pour empêcher l'instanciation de la classe utilitaire.
     */
    private ExceptionHandler() {
        throw new IllegalStateException("Classe utilitaire, ne pas instancier");
    }

    /**
     * Gère une exception et prépare la réponse HTTP appropriée.
     *
     * @param request La requête HTTP
     * @param response La réponse HTTP
     * @param exception L'exception à gérer
     * @return L'URL de la page d'erreur
     */
    public static String handleException(HttpServletRequest request, 
                                      HttpServletResponse response,
                                      Exception exception) {
        List<String> errors = new ArrayList<>();
        
        if (exception instanceof ApplicationException) {
            handleApplicationException((ApplicationException) exception, errors);
        } else {
            handleUnexpectedException(exception, errors);
        }

        request.setAttribute(ERROR_ATTRIBUTE, errors);
        return ERROR_PAGE;
    }

    /**
     * Gère une exception spécifique à l'application.
     *
     * @param exception L'exception à gérer
     * @param errors La liste des erreurs à remplir
     */
    private static void handleApplicationException(ApplicationException exception, 
                                                List<String> errors) {
        String errorCode = exception.getErrorCode();
        String message = exception.getMessage();
        
        LogManager.warning("Exception de l'application - Code: {}, Message: {}", 
                         errorCode, message);
        
        if (exception instanceof ValidationException) {
            errors.add(message);
        } else if (exception instanceof DatabaseException) {
            errors.add(GENERIC_ERROR_MESSAGE);
            LogManager.logException("Erreur de base de données", exception);
        } else {
            errors.add(GENERIC_ERROR_MESSAGE);
            LogManager.logException("Erreur inattendue", exception);
        }
    }

    /**
     * Gère une exception inattendue.
     *
     * @param exception L'exception à gérer
     * @param errors La liste des erreurs à remplir
     */
    private static void handleUnexpectedException(Exception exception, 
                                               List<String> errors) {
        LogManager.logException("Exception inattendue", exception);
        errors.add(GENERIC_ERROR_MESSAGE);
    }

    /**
     * Vérifie si une exception est une exception de validation.
     *
     * @param exception L'exception à vérifier
     * @return true si c'est une exception de validation, false sinon
     */
    public static boolean isValidationException(Exception exception) {
        return exception instanceof ValidationException;
    }

    /**
     * Vérifie si une exception est une exception de base de données.
     *
     * @param exception L'exception à vérifier
     * @return true si c'est une exception de base de données, false sinon
     */
    public static boolean isDatabaseException(Exception exception) {
        return exception instanceof DatabaseException;
    }
} 