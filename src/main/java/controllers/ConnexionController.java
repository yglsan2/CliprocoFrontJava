package controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import dao.jpa.UserJpaDAO;
import dao.SocieteDatabaseException;
import logs.LogManager;
import models.User;
import jakarta.servlet.http.Cookie;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import utilities.Security;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ApplicationException;
import services.UserService;
import dao.UserMySqlDAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Contrôleur gérant l'authentification des utilisateurs.
 * Ce contrôleur gère le processus de connexion, incluant la validation
 * des identifiants et la gestion des cookies "remember me".
 * 
 * @author Benja2
 * @version 1.0
 * @since 1.0
 */
public final class ConnexionController implements ICommand {

    private static final String USERNAME_PARAM = "username";
    private static final String PASSWORD_PARAM = "password";
    private static final String REMEMBER_ME_PARAM = "rememberMe";
    private static final String CURRENT_USER_COOKIE = "currentUser";
    private static final int REMEMBER_ME_DAYS = 7;
    private static final String CONNEXION_PAGE = "connexion.jsp";
    private static final String INDEX_REDIRECT = "redirect:?cmd=index";
    private static final String ERROR_MESSAGE = "Une erreur est survenue lors de la connexion. Veuillez réessayer.";

    /**
     * Exécute la logique de connexion.
     * Cette méthode est le point d'entrée principal du contrôleur.
     * Elle gère le flux complet de l'authentification :
     * 1. Récupération des paramètres
     * 2. Validation des identifiants
     * 3. Tentative de connexion
     * 4. Gestion des erreurs
     * 5. Nettoyage des ressources
     *
     * @param request la requête HTTP
     * @param response la réponse HTTP
     * @return l'URL de la page à afficher
     * @throws Exception si une erreur survient lors du traitement
     */
    @Contract(pure = true)
    @Override
    public @NotNull String execute(final HttpServletRequest request,
                                 final HttpServletResponse response)
            throws Exception {
        // Variables locales pour stocker l'état
        User user = new User();
        ArrayList<String> errors = new ArrayList<>();
        String urlSuite = CONNEXION_PAGE;
        UserService userService = null;
        UserMySqlDAO userDAO = null;

        try {
            // Récupération des paramètres de la requête
            String username = request.getParameter(USERNAME_PARAM);
            String password = request.getParameter(PASSWORD_PARAM);

            // Préparation de l'objet user pour la réaffichage du formulaire
            user.setUsername(username != null ? username : "");

            // Traitement de la connexion si les paramètres sont présents
            if (username != null && password != null) {
                // Validation des identifiants
                validateCredentials(username, password, errors);

                if (errors.isEmpty()) {
                    // Initialisation des services
                    userService = new UserService();
                    userDAO = new UserMySqlDAO();

                    // Tentative de connexion
                    urlSuite = processLogin(request, response, username, password, errors, userService, userDAO);
                }

                if (!errors.isEmpty()) {
                    handleErrors(request, errors, username);
                }
            }

            // Préparation des attributs de la requête
            prepareRequestAttributes(request, user);
            return urlSuite;

        } catch (ValidationException e) {
            // Erreur de validation (identifiants invalides, format incorrect, etc.)
            LogManager.logException("Erreur de validation lors de la connexion", e);
            request.setAttribute("errors", new ArrayList<String>() {{ add(e.getMessage()); }});
            return CONNEXION_PAGE;

        } catch (DatabaseException e) {
            // Erreur de base de données (connexion, requête, etc.)
            LogManager.logException("Erreur de base de données lors de la connexion", e);
            request.setAttribute("errors", new ArrayList<String>() {{ add(ERROR_MESSAGE); }});
            return CONNEXION_PAGE;

        } catch (ApplicationException e) {
            // Erreur d'application (logique métier, etc.)
            LogManager.logException("Erreur d'application lors de la connexion", e);
            request.setAttribute("errors", new ArrayList<String>() {{ add(ERROR_MESSAGE); }});
            return CONNEXION_PAGE;

        } catch (Exception e) {
            // Erreur inattendue (système, runtime, etc.)
            LogManager.logException("Erreur inattendue lors de la connexion", e);
            request.setAttribute("errors", new ArrayList<String>() {{ add(ERROR_MESSAGE); }});
            return CONNEXION_PAGE;

        } finally {
            // Nettoyage des ressources
            if (userService != null) {
                try {
                    // Fermeture des connexions si nécessaire
                    if (userService instanceof AutoCloseable) {
                        ((AutoCloseable) userService).close();
                    }
                } catch (Exception e) {
                    LogManager.warning("Erreur lors de la fermeture du service: " + e.getMessage());
                }
            }
            if (userDAO != null) {
                try {
                    // Fermeture des connexions si nécessaire
                    if (userDAO instanceof AutoCloseable) {
                        ((AutoCloseable) userDAO).close();
                    }
                } catch (Exception e) {
                    LogManager.warning("Erreur lors de la fermeture du DAO: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Valide les identifiants de connexion.
     * Vérifie que les identifiants ne sont pas vides et respectent les critères de validation.
     *
     * @param username le nom d'utilisateur
     * @param password le mot de passe
     * @param errors la liste des erreurs à remplir
     * @throws ValidationException si les identifiants sont invalides
     */
    private void validateCredentials(String username, String password, ArrayList<String> errors) 
            throws ValidationException {
        if (username.trim().isEmpty()) {
            throw new ValidationException("Le nom d'utilisateur est requis");
        }
        if (password.trim().isEmpty()) {
            throw new ValidationException("Le mot de passe est requis");
        }
    }

    /**
     * Traite la tentative de connexion.
     * Gère l'authentification et la création de session.
     *
     * @param request la requête HTTP
     * @param response la réponse HTTP
     * @param username le nom d'utilisateur
     * @param password le mot de passe
     * @param errors la liste des erreurs à remplir
     * @param userService le service d'authentification
     * @param userDAO le DAO pour la mise à jour du token
     * @return l'URL de la page à afficher
     * @throws ApplicationException si une erreur d'application survient
     */
    private String processLogin(HttpServletRequest request, HttpServletResponse response,
                              String username, String password, ArrayList<String> errors,
                              UserService userService, UserMySqlDAO userDAO)
            throws ApplicationException {
        try {
            // Tentative d'authentification
            User user = userService.authenticate(username, password);
            if (user != null) {
                // Génération et mise à jour du token
                String token = UUID.randomUUID().toString();
                user.setToken(token);
                userDAO.update(user);
                
                // Création de la session
                request.getSession().setAttribute("user", user);
                request.getSession().setAttribute("token", token);
                
                return "redirect:?cmd=index";
            } else {
                throw new ValidationException("Nom d'utilisateur ou mot de passe incorrect");
            }
        } catch (Exception e) {
            LogManager.logException("Erreur inattendue lors de l'authentification", e);
            throw new ApplicationException("Erreur lors de l'authentification", e);
        }
    }

    /**
     * Gère l'option "Se souvenir de moi".
     *
     * @param request La requête HTTP
     * @param response La réponse HTTP
     * @param user L'utilisateur connecté
     * @param dao Le DAO utilisateur
     * @throws SocieteDatabaseException En cas d'erreur de base de données
     */
    private void handleRememberMe(HttpServletRequest request, HttpServletResponse response,
                                User user, UserJpaDAO dao) throws SocieteDatabaseException {
        if (request.getParameterMap().get(REMEMBER_ME_PARAM) != null) {
            try {
                String token = UUID.randomUUID().toString();
                Cookie cookie = new Cookie(CURRENT_USER_COOKIE, token);
                cookie.setMaxAge(REMEMBER_ME_DAYS * 24 * 60 * 60);
                cookie.setSecure(true); // Cookie sécurisé en HTTPS
                cookie.setHttpOnly(true); // Protection contre XSS
                response.addCookie(cookie);

                user.setToken(token);
                user.setExpire(LocalDate.now().plusDays(REMEMBER_ME_DAYS));
                dao.save(user);
                LogManager.info("Cookie 'remember me' créé pour l'utilisateur: " + user.getUsername());
            } catch (SocieteDatabaseException e) {
                LogManager.warning("Erreur lors de la sauvegarde du token remember me: " + e.getMessage());
                throw e;
            }
        }
    }

    /**
     * Gère les erreurs de connexion.
     * Ajoute les erreurs à la requête pour l'affichage.
     *
     * @param request la requête HTTP
     * @param errors la liste des erreurs
     * @param username le nom d'utilisateur
     */
    private void handleErrors(HttpServletRequest request, ArrayList<String> errors, String username) {
        request.setAttribute("errors", errors);
        request.setAttribute("username", username);
        LogManager.warning("Tentative de connexion échouée pour l'utilisateur: " + username);
    }

    /**
     * Prépare les attributs de la requête.
     * Configure les attributs nécessaires pour l'affichage de la page.
     *
     * @param request la requête HTTP
     * @param user l'utilisateur
     */
    private void prepareRequestAttributes(HttpServletRequest request, User user) {
        request.setAttribute("user", user);
        request.setAttribute("titlePage", "Connexion");
        request.setAttribute("titleGroup", "Authentification");
    }
}
