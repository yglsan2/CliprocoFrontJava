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
import dao.jpa.ClientJpaDAO;
import dao.jpa.ProspectJpaDAO;
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
import exceptions.AuthorizationException;
import services.ClientService;
import services.ProspectService;
import services.UserService;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;

/**
 * Contrôleur frontal qui gère le routage des requêtes.
 * Cette classe est responsable de :
 * - L'initialisation des services et des contrôleurs
 * - La gestion des autorisations d'accès
 * - Le routage des requêtes vers les contrôleurs appropriés
 * - La gestion des erreurs et des exceptions
 */
@WebServlet("/*")
public class FrontController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private Map<String, ICommand> commands;
    private Map<String, String> roles;
    private ClientService clientService;
    private ProspectService prospectService;
    private UserService userService;

    @Resource(name = "jdbc/gestionClients")
    private static DataSource datasource;
    private static Connection connection;

    @Override
    public void init() throws ServletException {
        try {
            LogManager.logInfo("Initialisation du FrontController");
            initializeServices();
            initializeCommands();
            initializeRoles();
            initializeLogging();
            initializeDatabase();
            LogManager.logInfo("Initialisation du FrontController terminée avec succès");
        } catch (Exception e) {
            LogManager.logException("Erreur lors de l'initialisation du FrontController", e);
            throw new ServletException("Erreur lors de l'initialisation", e);
        }
    }

    private void initializeServices() {
        LogManager.logInfo("Initialisation des services");
        try {
            UserJpaDAO userDAO = new UserJpaDAO();
            userService = new UserService(userDAO);
            clientService = new ClientService(new ClientJpaDAO());
            prospectService = new ProspectService(new ProspectJpaDAO());
            LogManager.logInfo("Services initialisés avec succès");
        } catch (Exception e) {
            LogManager.logException("Erreur lors de l'initialisation des services", e);
            throw new RuntimeException("Erreur lors de l'initialisation des services", e);
        }
    }

    private void initializeCommands() {
        LogManager.logInfo("Initialisation des commandes");
        commands = new HashMap<>();
        
        try {
            // Commandes pour les clients
            commands.put("/clients", new ListeClientsController(clientService));
            commands.put("/clients/update", new UpdateClientsController(clientService));
            commands.put("/clients/view", new ViewClientsController(clientService));
            commands.put("/clients/create", new CreationClientsController(clientService));
            commands.put("/clients/delete", new DeleteClientsController(clientService));

            // Commandes pour les prospects
            commands.put("/prospects", new ListeProspectsController(prospectService));
            commands.put("/prospects/create", new CreationProspectsController(prospectService));
            commands.put("/prospects/update", new UpdateProspectsController(prospectService));
            commands.put("/prospects/delete", new DeleteProspectsController(prospectService));
            commands.put("/prospects/view", new ViewProspectsController(prospectService));
            LogManager.logInfo("Commandes initialisées avec succès");
        } catch (Exception e) {
            LogManager.logException("Erreur lors de l'initialisation des commandes", e);
            throw new RuntimeException("Erreur lors de l'initialisation des commandes", e);
        }
    }

    private void initializeRoles() {
        LogManager.logInfo("Initialisation des rôles");
        roles = new HashMap<>();
        
        try {
            // Rôles pour les clients
            roles.put("/clients", "ADMIN");
            roles.put("/clients/update", "ADMIN");
            roles.put("/clients/view", "USER");
            roles.put("/clients/create", "ADMIN");
            roles.put("/clients/delete", "ADMIN");

            // Rôles pour les prospects
            roles.put("/prospects", "USER");
            roles.put("/prospects/create", "USER");
            roles.put("/prospects/update", "USER");
            roles.put("/prospects/delete", "USER");
            roles.put("/prospects/view", "USER");
            LogManager.logInfo("Rôles initialisés avec succès");
        } catch (Exception e) {
            LogManager.logException("Erreur lors de l'initialisation des rôles", e);
            throw new RuntimeException("Erreur lors de l'initialisation des rôles", e);
        }
    }

    private void initializeLogging() {
        LogManager.logInfo("Initialisation du logging");
        try {
            LogManager.run();
            LogManager.logInfo("Système de logging initialisé avec succès");
        } catch (Exception e) {
            LogManager.logException("Erreur lors de l'initialisation du système de logging", e);
            throw new RuntimeException("Erreur lors de l'initialisation du système de logging", e);
        }
    }

    private void initializeDatabase() throws SQLException {
        LogManager.logInfo("Initialisation de la base de données");
        if (datasource == null) {
            LogManager.logError("DataSource non initialisée");
            throw new SQLException("DataSource non initialisée");
        }
        try {
            connection = datasource.getConnection();
            LogManager.logInfo("Connexion à la base de données établie avec succès");
        } catch (SQLException e) {
            LogManager.logException("Erreur lors de la connexion à la base de données", e);
            throw e;
        }
    }

    public static DataSource getDatasource() {
        return datasource;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LogManager.logInfo("Traitement de la requête GET");
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LogManager.logInfo("Traitement de la requête POST");
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        if (path == null) {
            path = "/";
        }
        LogManager.logInfo("Traitement de la requête pour le chemin: " + path);

        try {
            checkAuthorization(request, path);
            executeCommand(request, response, path);
        } catch (AuthorizationException e) {
            LogManager.logWarning("Accès non autorisé pour le chemin : " + path);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        } catch (Exception e) {
            LogManager.logException("Erreur lors du traitement de la requête", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Une erreur est survenue");
        }
    }

    private void checkAuthorization(HttpServletRequest request, String path) throws AuthorizationException {
        LogManager.logInfo("Vérification de l'autorisation pour le chemin : " + path);
        String requiredRole = roles.get(path);
        if (requiredRole != null) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                LogManager.logWarning("Tentative d'accès non autorisé: utilisateur non connecté");
                throw new AuthorizationException("Utilisateur non connecté");
            }
            String userRole = (String) session.getAttribute("role");
            if (!requiredRole.equals(userRole)) {
                LogManager.logWarning("Tentative d'accès non autorisé: rôle insuffisant");
                throw new AuthorizationException("Accès non autorisé");
            }
            LogManager.logInfo("Autorisation accordée pour le rôle: " + userRole);
        }
    }

    private void executeCommand(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException {
        LogManager.logInfo("Exécution de la commande : " + path);
        ICommand command = commands.get(path);
        if (command != null) {
            try {
                String result = command.execute(request, response);
                LogManager.logInfo("Commande exécutée avec succès, redirection vers: " + result);
                request.getRequestDispatcher(result).forward(request, response);
            } catch (Exception e) {
                LogManager.logError("Erreur lors de l'exécution de la commande : " + e.getMessage());
                throw new ServletException("Erreur lors de l'exécution de la commande", e);
            }
        } else {
            LogManager.logWarning("Aucune commande trouvée pour le chemin: " + path);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    public void destroy() {
        LogManager.logInfo("Arrêt du FrontController");
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                LogManager.logInfo("Fermeture de la connexion à la base de données");
            }
        } catch (SQLException e) {
            LogManager.logException("Erreur lors de la fermeture de la connexion", e);
        } finally {
            connection = null;
            LogManager.stop();
            LogManager.logInfo("FrontController arrêté avec succès");
        }
    }

    private void loadCurrentUser(final HttpServletRequest request)
            throws DatabaseException {
        Cookie[] cookies = request.getCookies();
        if (request.getSession().getAttribute("currentUser") == null) {
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("token")) {
                        UserJpaDAO userDAO = new UserJpaDAO();
                        try {
                            User user = userDAO.findByToken(cookie.getValue());
                            if (user != null) {
                                request.getSession().setAttribute("currentUser", user);
                            }
                        } finally {
                            userDAO.close();
                        }
                        break;
                    }
                }
            }
        }
    }
}
