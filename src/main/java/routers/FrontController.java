package routers;

import controllers.ConnexionController;
import controllers.ContactController;
import controllers.DeconnexionController;
import controllers.IndexController;
import controllers.ICommand;
import controllers.clients.CreationClientsController;
import controllers.clients.DeleteClientsController;
import controllers.clients.ListeClientsController;
import controllers.clients.UpdateClientsController;
import controllers.clients.ViewClientsController;
import controllers.prospects.CreationProspectsController;
import controllers.prospects.DeleteProspectsController;
import controllers.prospects.ListeProspectsController;
import controllers.prospects.UpdateProspectsController;
import controllers.prospects.ViewProspectsController;
import dao.jpa.UserJpaDAO;
import utilities.LogManager;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;
import utilities.Security;
import services.UserService;
import exceptions.DatabaseException;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mysql.cj.jdbc.MysqlDataSource;
import exceptions.ApplicationException;
import exceptions.ValidationException;

/**
 * Contrôleur frontal de l'application.
 * Cette classe gère le routage des requêtes HTTP vers les contrôleurs appropriés.
 * Elle implémente le pattern Front Controller pour centraliser la gestion des requêtes.
 *
 * @author Benja2
 * @version 1.0
 * @since 1.0
 */
@WebServlet(name = "FrontController", urlPatterns = {"/*"})
public final class FrontController extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private static final Logger logger = Logger.getLogger(FrontController.class.getName());

    /**
     *
     */
    private static DataSource datasource;

    /**
     *
     */
    private static Connection connection;

    /**
     * Map des commandes disponibles
     */
    private static final Map<String, ICommand> commands = new HashMap<>();

    private static final String LOGIN_PAGE = "/WEB-INF/views/login.jsp";
    private static final String HOME_PAGE = "/WEB-INF/views/home.jsp";
    private static final String ERROR_PAGE = "/WEB-INF/views/error.jsp";

    private static UserService userService;

    static {
        try {
            MysqlDataSource mysqlDS = new MysqlDataSource();
            mysqlDS.setURL("jdbc:mysql://localhost:3306/cliproco");
            mysqlDS.setUser("root");
            mysqlDS.setPassword("root");
            datasource = mysqlDS;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de l'initialisation de la source de données", e);
            throw new RuntimeException("Erreur lors de l'initialisation de la source de données", e);
        }
    }

    @Override
    public void init() throws ServletException {
        try {
            // Test de la connexion à la base de données
            try (Connection connection = datasource.getConnection()) {
                LogManager.info("Connexion à la base de données établie avec succès");
            }
        } catch (SQLException e) {
            LogManager.logException("Erreur lors de la connexion à la base de données", e);
            throw new ServletException("Erreur lors de la connexion à la base de données", e);
        }

        userService = new UserService();
        
        // Initialisation des commandes
        commands.put("/clients/liste", new controllers.clients.ListeClientsController());
        commands.put("/clients/create", new controllers.clients.CreationClientsController());
        commands.put("/clients/update", new controllers.clients.UpdateClientsController());
        commands.put("/clients/delete", new controllers.clients.DeleteClientsController());
        commands.put("/clients/view", new controllers.clients.ViewClientsController());
        
        commands.put("/prospects/liste", new controllers.prospects.ListeProspectsController());
        commands.put("/prospects/create", new controllers.prospects.CreationProspectsController());
        commands.put("/prospects/update", new controllers.prospects.UpdateProspectsController());
        commands.put("/prospects/delete", new controllers.prospects.DeleteProspectsController());
        commands.put("/prospects/view", new controllers.prospects.ViewProspectsController());
        
        commands.put("/factures/liste", new controllers.factures.ListeFacturesController());
        commands.put("/factures/create", new controllers.factures.CreationFacturesController());
        commands.put("/factures/update", new controllers.factures.UpdateFacturesController());
        commands.put("/factures/delete", new controllers.factures.DeleteFacturesController());
        commands.put("/factures/view", new controllers.factures.ViewFacturesController());
        
        commands.put("/login", new controllers.ConnexionController());
        commands.put("/logout", new controllers.DeconnexionController());

        // Initialisation du logger
        LogManager.info("Initialisation du FrontController");

        try {
            connection = datasource.getConnection();
        } catch (SQLException e) {
            LogManager.logException("Erreur lors de la connexion à la base de données", e);
            throw new ServletException("Erreur lors de la connexion à la base de données", e);
        }
    }

    @Override
    public void destroy() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
            LogManager.info("Fermeture de la connexion à la base de données");
        } catch (SQLException e) {
            LogManager.logException("Erreur lors de la fermeture de la connexion", e);
        }
    }

    private void handleError(final HttpServletRequest request,
                             final HttpServletResponse response,
                             final String errorMessage) {
        try {
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        } catch (ServletException | IOException e) {
            LogManager.logException("Erreur lors de l'affichage de la page d'erreur", e);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur lors de l'affichage de la page d'erreur");
            } catch (IOException ex) {
                LogManager.logException("Erreur lors de l'envoi de l'erreur HTTP", ex);
            }
        }
    }

    /**
     *
     * @return La datasource
     */
    public static DataSource getDatasource() {
        return datasource;
    }

    /**
     * Traite la requête HTTP et la route vers le contrôleur approprié.
     *
     * @param request La requête HTTP
     * @param response La réponse HTTP
     * @throws ServletException si une erreur de servlet survient
     * @throws IOException si une erreur d'entrée/sortie survient
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LogManager.debug("Méthode de requête : " + request.getMethod());
        LogManager.debug("URI demandée : " + request.getRequestURI());

        try {
            // Vérification du token CSRF
            String csrfToken = request.getParameter("csrfToken");
            String sessionToken = (String) request.getSession().getAttribute("csrfToken");
            if (csrfToken == null || !csrfToken.equals(sessionToken)) {
                throw new ValidationException("Les jetons CSRF ne correspondent pas");
            }

            // Récupération de la commande
            String cmd = request.getParameter("cmd");
            if (cmd == null) {
                throw new ValidationException("Commande non spécifiée");
            }

            LogManager.debug("Commande demandée : " + cmd);

            ICommand command = commands.get(cmd);
            if (command == null) {
                throw new ValidationException("Commande inconnue: " + cmd);
            }

            // Exécution de la commande
            String view = command.execute(request, response);
            LogManager.debug("Commande exécutée avec succès, vue : " + view);

            // Redirection vers la vue
            request.getRequestDispatcher(view).forward(request, response);

        } catch (ValidationException e) {
            LogManager.logException("Erreur de validation", e);
            handleError(request, response, e.getMessage());
        } catch (DatabaseException e) {
            LogManager.logException("Erreur de base de données", e);
            handleError(request, response, "Une erreur est survenue lors de l'accès aux données");
        } catch (ApplicationException e) {
            LogManager.logException("Erreur d'application", e);
            handleError(request, response, "Une erreur est survenue lors du traitement de la requête");
        } catch (Exception e) {
            LogManager.logException("Erreur inattendue", e);
            handleError(request, response, "Une erreur inattendue est survenue");
        }
    }

    /**
     *
     * @param request
     * @param response
     */
    @Override
    protected void doGet(final HttpServletRequest request,
                         final HttpServletResponse response) {
        try {
            processRequest(request, response);
        } catch (ServletException | IOException e) {
            LogManager.logException("Erreur lors de la gestion de la requête GET", e);
            handleError(request, response, "Erreur lors de la gestion de la requête GET");
        } finally {
            // Nettoyage des ressources si nécessaire
            cleanupResources();
        }
    }

    /**
     *
     * @param request
     * @param response
     */
    @Override
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response) {
        try {
            processRequest(request, response);
        } catch (ServletException | IOException e) {
            LogManager.logException("Erreur lors de la gestion de la requête POST", e);
            handleError(request, response, "Erreur lors de la gestion de la requête POST");
        } finally {
            // Nettoyage des ressources si nécessaire
            cleanupResources();
        }
    }

    /**
     * Nettoie les ressources utilisées par le contrôleur.
     */
    private void cleanupResources() {
        // Nettoyage des ressources si nécessaire
        // Par exemple, fermeture des connexions, nettoyage des caches, etc.
    }

    /**
     *
     * @param request
     * @throws DatabaseException
     */
    private void loadCurrentUser(final HttpServletRequest request)
            throws DatabaseException {
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                User user = (User) session.getAttribute("user");
                if (user != null) {
                    request.setAttribute("currentUser", user);
                }
            }
        } catch (Exception e) {
            LogManager.logException("Erreur lors du chargement de l'utilisateur courant", e);
            throw new DatabaseException("Erreur lors du chargement de l'utilisateur", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path == null || path.equals("/")) {
            path = "/home";
        }

        try {
            switch (path) {
                case "/home":
                    handleHome(request, response);
                    break;
                case "/login":
                    handleLogin(request, response);
                    break;
                case "/logout":
                    handleLogout(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (DatabaseException e) {
            LogManager.logException("Erreur lors du traitement de la requête", e);
            request.setAttribute("error", "Une erreur est survenue lors du traitement de la requête");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path == null || path.equals("/")) {
            path = "/home";
        }

        try {
            switch (path) {
                case "/login":
                    handleLoginPost(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (DatabaseException e) {
            LogManager.logException("Erreur lors du traitement de la requête", e);
            request.setAttribute("error", "Une erreur est survenue lors du traitement de la requête");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
        }
    }

    private void handleHome(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, DatabaseException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");
        request.setAttribute("user", user);
        request.getRequestDispatcher(HOME_PAGE).forward(request, response);
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/login");
    }

    private void handleLoginPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, DatabaseException {
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

    /**
     * Vérifie si l'utilisateur est connecté
     * @param request La requête HTTP
     * @return true si l'utilisateur est connecté, false sinon
     * @throws DatabaseException si une erreur survient lors de la vérification
     */
    private boolean isUserConnected(HttpServletRequest request) throws DatabaseException {
        String token = (String) request.getSession().getAttribute("token");
        if (token != null) {
            try {
                User user = userService.findByToken(token);
                return user != null;
            } catch (DatabaseException e) {
                throw new DatabaseException("Erreur lors du chargement de l'utilisateur", e);
            }
        }
        return false;
    }
}
