package exceptions;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utilities.LogManager;
import java.io.IOException;

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

    private static final String ERROR_ATTRIBUTE = "error";
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
     * @param viewPath Le chemin de la vue à afficher
     * @throws ServletException Si une erreur survient lors de la gestion de la requête
     * @throws IOException Si une erreur survient lors de la gestion de la réponse
     */
    public static void handleException(HttpServletRequest request, HttpServletResponse response, 
                                     Exception exception, String viewPath) throws ServletException, IOException {
        String message = getErrorMessage(exception);
        logException(message, exception);
        request.setAttribute(ERROR_ATTRIBUTE, message);
        request.getRequestDispatcher(viewPath).forward(request, response);
    }

    private static String getErrorMessage(Exception exception) {
        if (exception instanceof ValidationException) {
            return exception.getMessage();
        } else if (exception instanceof DatabaseException) {
            return "Une erreur de base de données s'est produite";
        } else {
            return "Une erreur inattendue s'est produite";
        }
    }

    private static void logException(String message, Throwable e) {
        if (e instanceof ValidationException) {
            LogManager.info(message + ": " + e.getMessage());
        } else if (e instanceof DatabaseException) {
            LogManager.info(message + ": " + e.getMessage());
        } else {
            LogManager.logException(message, e);
        }
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