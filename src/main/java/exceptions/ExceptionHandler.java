package exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.persistence.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire centralisé des exceptions de l'application.
 * Cette classe fournit des méthodes utilitaires pour gérer les exceptions
 * de manière cohérente dans toute l'application.
 */
public final class ExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);
    private static final Map<Class<? extends Exception>, String> ERROR_VIEWS = new HashMap<>();
    private static final Map<Class<? extends Exception>, Integer> ERROR_CODES = new HashMap<>();

    static {
        // Configuration des vues d'erreur
        ERROR_VIEWS.put(ValidationException.class, "/WEB-INF/jsp/error/validation.jsp");
        ERROR_VIEWS.put(AuthorizationException.class, "/WEB-INF/jsp/error/authorization.jsp");
        ERROR_VIEWS.put(ResourceNotFoundException.class, "/WEB-INF/jsp/error/404.jsp");
        ERROR_VIEWS.put(DatabaseException.class, "/WEB-INF/jsp/error/database.jsp");
        ERROR_VIEWS.put(BusinessException.class, "/WEB-INF/jsp/error/business.jsp");

        // Configuration des codes d'erreur HTTP
        ERROR_CODES.put(ValidationException.class, HttpServletResponse.SC_BAD_REQUEST);
        ERROR_CODES.put(AuthorizationException.class, HttpServletResponse.SC_FORBIDDEN);
        ERROR_CODES.put(ResourceNotFoundException.class, HttpServletResponse.SC_NOT_FOUND);
        ERROR_CODES.put(DatabaseException.class, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        ERROR_CODES.put(BusinessException.class, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    private ExceptionHandler() {
        throw new IllegalStateException("Classe utilitaire, ne pas instancier");
    }

    /**
     * Gère une exception et prépare la réponse HTTP appropriée.
     *
     * @param request La requête HTTP
     * @param response La réponse HTTP
     * @param exception L'exception à gérer
     * @throws IOException Si une erreur survient lors de la gestion de la réponse
     */
    public static void handleException(Exception exception, HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        try {
            String message = getErrorMessage(exception);
            String view = getErrorView(exception);
            int statusCode = getErrorCode(exception);

            logException(exception);
            request.setAttribute("error", message);
            request.setAttribute("statusCode", statusCode);
            request.setAttribute("exception", exception);

            if (isAjaxRequest(request)) {
                handleAjaxError(response, message, statusCode);
            } else {
                handleViewError(request, response, view);
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la gestion de l'exception", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Une erreur inattendue s'est produite");
        }
    }

    private static String getErrorMessage(Exception exception) {
        if (exception instanceof ValidationException) {
            return "Erreur de validation : " + exception.getMessage();
        } else if (exception instanceof AuthorizationException) {
            return "Accès non autorisé : " + exception.getMessage();
        } else if (exception instanceof ResourceNotFoundException) {
            return "Ressource non trouvée : " + exception.getMessage();
        } else if (exception instanceof DatabaseException) {
            return "Erreur de base de données : " + exception.getMessage();
        } else if (exception instanceof BusinessException) {
            return "Erreur métier : " + exception.getMessage();
        } else {
            return "Une erreur inattendue s'est produite";
        }
    }

    private static String getErrorView(Exception exception) {
        return ERROR_VIEWS.getOrDefault(exception.getClass(), "/WEB-INF/jsp/error/500.jsp");
    }

    private static int getErrorCode(Exception exception) {
        return ERROR_CODES.getOrDefault(exception.getClass(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    private static void logException(Exception exception) {
        if (exception instanceof ValidationException) {
            logger.warn("Erreur de validation : {}", exception.getMessage());
        } else if (exception instanceof AuthorizationException) {
            logger.warn("Erreur d'autorisation : {}", exception.getMessage());
        } else if (exception instanceof ResourceNotFoundException) {
            logger.warn("Ressource non trouvée : {}", exception.getMessage());
        } else if (exception instanceof DatabaseException) {
            logger.error("Erreur de base de données", exception);
        } else if (exception instanceof BusinessException) {
            logger.error("Erreur métier", exception);
        } else {
            logger.error("Erreur inattendue", exception);
        }
    }

    private static boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    private static void handleAjaxError(HttpServletResponse response, String message, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"" + message + "\"}");
    }

    private static void handleViewError(HttpServletRequest request, HttpServletResponse response, String view) 
            throws IOException {
        try {
            request.getRequestDispatcher(view).forward(request, response);
        } catch (Exception e) {
            logger.error("Erreur lors de la redirection vers la page d'erreur", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur lors de l'affichage de la page d'erreur");
        }
    }

    /**
     * Vérifie si une exception est une exception de validation.
     */
    public static boolean isValidationException(Exception exception) {
        return exception instanceof ValidationException;
    }

    /**
     * Vérifie si une exception est une exception d'autorisation.
     */
    public static boolean isAuthorizationException(Exception exception) {
        return exception instanceof AuthorizationException;
    }

    /**
     * Vérifie si une exception est une exception de ressource non trouvée.
     */
    public static boolean isResourceNotFoundException(Exception exception) {
        return exception instanceof ResourceNotFoundException;
    }

    /**
     * Vérifie si une exception est une exception de base de données.
     */
    public static boolean isDatabaseException(Exception exception) {
        return exception instanceof DatabaseException || exception instanceof PersistenceException;
    }

    /**
     * Vérifie si une exception est une exception métier.
     */
    public static boolean isBusinessException(Exception exception) {
        return exception instanceof BusinessException;
    }
} 