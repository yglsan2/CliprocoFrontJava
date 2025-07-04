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
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;
import utilities.Security;
import exceptions.AuthorizationException;
import services.UserService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import exceptions.BusinessException;

/**
 * Contrôleur frontal qui gère le routage des requêtes.
 * Cette classe est responsable de :
 * - L'initialisation des contrôleurs
 * - La gestion des autorisations d'accès
 * - Le routage des requêtes vers les contrôleurs appropriés
 * - La gestion des erreurs et des exceptions
 */
// @WebServlet("/app")
public class FrontController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(FrontController.class.getName());
    private Map<String, ICommand> commands;
    private Map<String, String> roles;
    private UserService userService;

    /**
     * EntityManager partagé pour les opérations de base de données.
     * 
     * Cet EntityManager est initialisé au démarrage de l'application et partagé
     * entre tous les contrôleurs et services pour effectuer les opérations CRUD
     * sur les entités JPA (Client, Prospect, Societe, Adresse, User).
     * 
     * L'EntityManager est associé à l'unité de persistance "default" qui
     * configure la connexion à la base de données MySQL.
     */
    private static EntityManager em;

    /**
     * EntityManagerFactory pour créer les EntityManagers.
     * 
     * Cette factory est utilisée pour créer l'EntityManager partagé et peut
     * être utilisée pour créer des EntityManagers supplémentaires si nécessaire.
     * 
     * L'EntityManagerFactory est associé à l'unité de persistance "default"
     * définie dans le fichier persistence.xml.
     */
    private static EntityManagerFactory emf;

    @Override
    public void init() throws ServletException {
        try {
            LogManager.logInfo("Initialisation du FrontController");
            initializeDatabase();
            initializeCommands();
            initializeRoles();
            initializeLogging();
            LogManager.logInfo("Initialisation du FrontController terminée avec succès");
        } catch (Exception e) {
            LogManager.logException("Erreur lors de l'initialisation du FrontController", e);
            throw new ServletException("Erreur lors de l'initialisation", e);
        }
    }

    private void initializeCommands() throws DatabaseException {
        LogManager.logInfo("Initialisation des commandes");
        commands = new HashMap<>();
        
        try {
            // Commandes pour les clients
            commands.put("/clients", new ListeClientsController());
            commands.put("/clients.liste", new ListeClientsController());
            commands.put("/clients.update", new UpdateClientsController());
            commands.put("/clients.view", new ViewClientsController());
            commands.put("/clients.create", new CreationClientsController());
            commands.put("/clients.delete", new DeleteClientsController());
            commands.put("/clients/update", new UpdateClientsController());
            commands.put("/clients/view", new ViewClientsController());
            commands.put("/clients/create", new CreationClientsController());
            commands.put("/clients/delete", new DeleteClientsController());

            // Commande pour la page d'accueil
            commands.put("/", new IndexController());
            commands.put("/index", new IndexController());
            commands.put("index", new IndexController());
            
            // Commandes pour les prospects
            commands.put("/prospects", new ListeProspectsController());
            commands.put("/prospects.liste", new ListeProspectsController());
            commands.put("/prospects.create", new CreationProspectsController());
            commands.put("/prospects.update", new UpdateProspectsController());
            commands.put("/prospects.delete", new DeleteProspectsController());
            commands.put("/prospects.view", new ViewProspectsController());
            commands.put("/prospects/create", new CreationProspectsController());
            commands.put("/prospects/update", new UpdateProspectsController());
            commands.put("/prospects/delete", new DeleteProspectsController());
            commands.put("/prospects/view", new ViewProspectsController());
            LogManager.logInfo("Commandes initialisées avec succès");
        } catch (Exception e) {
            LogManager.logException("Erreur lors de l'initialisation des commandes", e);
            throw new DatabaseException("Erreur lors de l'initialisation des commandes", e);
        }
    }

    private void initializeRoles() throws DatabaseException {
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
            throw new DatabaseException("Erreur lors de l'initialisation des rôles", e);
        }
    }

    private void initializeLogging() throws DatabaseException {
        LogManager.logInfo("Initialisation du logging");
        try {
            LogManager.run();
            LogManager.logInfo("Système de logging initialisé avec succès");
        } catch (Exception e) {
            LogManager.logException("Erreur lors de l'initialisation du système de logging", e);
            throw new DatabaseException("Erreur lors de l'initialisation du système de logging", e);
        }
    }

    /**
     * Initialise la connexion à la base de données via JPA
     * 
     * Cette méthode crée l'EntityManagerFactory et l'EntityManager nécessaires
     * pour interagir avec la base de données MySQL via l'unité de persistance
     * "cliprocoUP" (Cliproco Unit of Persistence).
     * 
     * L'unité de persistance "cliprocoUP" est définie dans le fichier
     * persistence.xml et contient :
     * - La configuration de connexion MySQL
     * - Les classes d'entités JPA (Client, Prospect, etc.)
     * - Les propriétés Hibernate (dialecte, format SQL, etc.)
     * 
     * @throws DatabaseException si l'initialisation échoue
     */
    private void initializeDatabase() throws DatabaseException {
        LogManager.logInfo("Initialisation de la base de données avec JPA");
        try {
            // Vérification de la présence du fichier persistence.xml
            LogManager.logInfo("Vérification de la configuration JPA...");
            
            // Création de l'EntityManagerFactory à partir de l'unité de persistance "cliprocoUP"
            LogManager.logInfo("Tentative de création de l'EntityManagerFactory pour cliprocoUP");
            emf = Persistence.createEntityManagerFactory("cliprocoUP");
            LogManager.logInfo("EntityManagerFactory créé avec succès");
            
            // Affichage des propriétés de l'EntityManagerFactory
            LogManager.logInfo("Propriétés de l'EntityManagerFactory:");
            LogManager.logInfo("- EntityManagerFactory créé avec succès");
            
            // Création de l'EntityManager pour les opérations de base de données
            LogManager.logInfo("Tentative de création de l'EntityManager");
            em = emf.createEntityManager();
            LogManager.logInfo("EntityManager initialisé avec succès pour l'unité cliprocoUP");
        } catch (Exception e) {
            LogManager.logException("Erreur lors de l'initialisation de l'EntityManager", e);
            System.err.println("=== ERREUR JPA DÉTAILLÉE ===");
            System.err.println("Message: " + e.getMessage());
            System.err.println("Type: " + e.getClass().getName());
            System.err.println("Stack trace complète:");
            e.printStackTrace(System.err);
            System.err.println("=== FIN ERREUR JPA ===");
            throw new DatabaseException("Erreur lors de l'initialisation de l'EntityManager", e);
        }
    }

    /**
     * Retourne l'EntityManager partagé pour l'unité de persistance "cliprocoUP"
     * 
     * Cet EntityManager est utilisé par tous les DAO et services pour effectuer
     * les opérations de base de données. Il est initialisé au démarrage de
     * l'application et partagé entre tous les composants.
     * 
     * L'EntityManager est associé à l'unité de persistance "cliprocoUP" qui
     * configure la connexion à la base de données MySQL.
     * 
     * @return l'EntityManager partagé pour l'unité cliprocoUP
     */
    public static EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
            handleException(e, request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
            handleException(e, request, response);
        }
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        String cmd = request.getParameter("cmd");
        
        // Si un paramètre cmd est présent, l'utiliser comme chemin
        if (cmd != null && !cmd.isEmpty()) {
            path = "/" + cmd;
        } else if (path == null) {
            path = "/";
        }

        try {
            checkAuthorization(request, path);
            executeCommand(request, response, path);
        } catch (AuthorizationException e) {
            LogManager.logWarning("Accès non autorisé: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/login");
        } catch (Exception e) {
            LogManager.logException("Erreur lors du traitement de la requête", e);
            throw new ServletException("Erreur lors du traitement de la requête", e);
        }
    }

    private void checkAuthorization(HttpServletRequest request, String path) throws AuthorizationException {
        // Temporairement désactivé pour permettre l'accès sans authentification

        /*
        String requiredRole = roles.get(path);
        if (requiredRole != null) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                throw new AuthorizationException("Utilisateur non connecté");
            }
            User user = (User) session.getAttribute("user");
            if (!user.getRole().equals(requiredRole)) {
                throw new AuthorizationException("Rôle insuffisant");
            }
        }
        */
    }

    private void executeCommand(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException {
        ICommand command = commands.get(path);
        if (command == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            String result = command.execute(request, response);
            if (result.startsWith("redirect:")) {
                response.sendRedirect(request.getContextPath() + result.substring(9));
            } else {
                request.getRequestDispatcher(result).forward(request, response);
            }
        } catch (Exception e) {
            LogManager.logException("Erreur lors de l'exécution de la commande", e);
            throw new ServletException("Erreur lors de l'exécution de la commande", e);
        }
    }

    @Override
    public void destroy() {
        try {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
            LogManager.logInfo("EntityManager et EntityManagerFactory fermés avec succès");
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la fermeture de l'EntityManager", e);
        }
    }

    private void loadCurrentUser(final HttpServletRequest request) throws DatabaseException {
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                String username = (String) session.getAttribute("username");
                if (username != null) {
                    userService.findByUsername(username).ifPresent(user -> {
                        session.setAttribute("user", user);
                        LogManager.logInfo("Utilisateur chargé: " + user.getUsername());
                    });
                }
            }
        } catch (Exception e) {
            LogManager.logException("Erreur lors du chargement de l'utilisateur", e);
            throw new DatabaseException("Erreur lors du chargement de l'utilisateur", e);
        }
    }

    private void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        LogManager.logException("Erreur lors du traitement de la requête", e);
        
        if (e instanceof AuthorizationException) {
            response.sendRedirect(request.getContextPath() + "/login");
        } else if (e instanceof ValidationException) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } else if (e instanceof ResourceNotFoundException) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } else if (e instanceof BusinessException) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Une erreur interne est survenue");
        }
    }
}
