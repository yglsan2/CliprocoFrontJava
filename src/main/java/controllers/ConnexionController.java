package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import dao.jpa.UserJpaDAO;
import dao.DatabaseException;
import logs.LogManager;
import models.User;
import jakarta.servlet.http.Cookie;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import utilities.Security;
import exceptions.ValidationException;
import exceptions.ApplicationException;
import services.UserService;
import dao.UserMySqlDAO;
import utilities.LogManager;

import java.io.IOException;
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
@WebServlet(name = "ConnexionController", urlPatterns = {"/connexion"})
public class ConnexionController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;
    private static final String LOGIN_PAGE = "/WEB-INF/views/login.jsp";
    private static final String HOME_PAGE = "/WEB-INF/views/home.jsp";
    private static final String ERROR_PAGE = "/WEB-INF/views/error.jsp";

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            UserService userService = new UserService();
            User user = userService.findByEmail(email);

            if (user != null && Security.verifyPassword(password, user.getPassword())) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                LogManager.info("Connexion réussie pour l'utilisateur: " + email);
                response.sendRedirect(request.getContextPath() + "/home");
            } else {
                LogManager.warning("Tentative de connexion échouée pour l'email: " + email);
                request.setAttribute("error", "Email ou mot de passe incorrect");
                request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
            }
        } catch (DatabaseException e) {
            LogManager.logException("Erreur lors de la connexion", e);
            request.setAttribute("error", "Une erreur est survenue lors de la connexion");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        if (userService != null) {
            try {
                userService.close();
            } catch (Exception e) {
                LogManager.logException("Erreur lors de la fermeture du service utilisateur", e);
            }
        }
    }

    /**
     * Gère la tentative de connexion.
     *
     * @param request La requête HTTP
     * @param response La réponse HTTP
     * @return La page à afficher
     */
    private String handleLogin(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            // Validation des paramètres
            if (username == null || username.trim().isEmpty() || 
                password == null || password.trim().isEmpty()) {
                throw new ValidationException("Le nom d'utilisateur et le mot de passe sont requis");
            }

            // Tentative de connexion avec gestion automatique des ressources
            try (UserService userService = new UserService()) {
                User user = userService.authenticate(username, password);
                if (user == null) {
                    LogManager.warning("Tentative de connexion échouée pour l'utilisateur: " + username);
                    request.setAttribute("errors", new ArrayList<String>() {{ add("Identifiants invalides. Veuillez vérifier votre nom d'utilisateur et votre mot de passe."); }});
                    return LOGIN_PAGE;
                }

                // Création de la session
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("token", user.getToken());

                LogManager.info("Connexion réussie pour l'utilisateur: " + username);
                return HOME_PAGE;
            }

        } catch (ValidationException e) {
            LogManager.logException("Erreur de validation lors de la connexion", e);
            request.setAttribute("errors", new ArrayList<String>() {{ add(e.getMessage()); }});
            return LOGIN_PAGE;

        } catch (ApplicationException e) {
            LogManager.logException("Erreur d'application lors de la connexion", e);
            request.setAttribute("errors", new ArrayList<String>() {{ add("Une erreur est survenue lors de la connexion. Veuillez réessayer."); }});
            return LOGIN_PAGE;
        }
    }

    /**
     * Gère la déconnexion.
     *
     * @param request La requête HTTP
     * @param response La réponse HTTP
     * @return La page à afficher
     */
    private String handleLogout(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                User user = (User) session.getAttribute("user");
                if (user != null) {
                    LogManager.info("Déconnexion de l'utilisateur: " + user.getUsername());
                }
                session.invalidate();
            }
            return LOGIN_PAGE;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la déconnexion", e);
            request.setAttribute("errors", new ArrayList<String>() {{ add("Une erreur est survenue lors de la déconnexion."); }});
            return LOGIN_PAGE;
        }
    }

    /**
     * Gère l'option "Se souvenir de moi".
     *
     * @param request La requête HTTP
     * @param response La réponse HTTP
     * @param user L'utilisateur connecté
     * @param dao Le DAO utilisateur
     * @throws DatabaseException En cas d'erreur de base de données
     */
    private void handleRememberMe(HttpServletRequest request, HttpServletResponse response,
                                User user, UserJpaDAO dao) throws DatabaseException {
        if (request.getParameter("rememberMe") != null) {
            try {
                String token = UUID.randomUUID().toString();
                Cookie cookie = new Cookie("currentUser", token);
                cookie.setMaxAge(7 * 24 * 60 * 60);
                cookie.setSecure(true); // Cookie sécurisé en HTTPS
                cookie.setHttpOnly(true); // Protection contre XSS
                response.addCookie(cookie);

                user.setToken(token);
                user.setExpire(LocalDate.now().plusDays(7));
                dao.save(user);
                LogManager.info("Cookie 'remember me' créé pour l'utilisateur: " + user.getUsername());
            } catch (DatabaseException e) {
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
    }

    /**
     * Prépare les attributs de la requête pour l'affichage.
     *
     * @param request la requête HTTP
     * @param user l'utilisateur connecté
     */
    private void prepareRequestAttributes(HttpServletRequest request, User user) {
        request.setAttribute("username", user.getUsername());
        request.setAttribute("user", user);
    }
}

