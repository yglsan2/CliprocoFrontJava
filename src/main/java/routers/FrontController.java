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
// import jakarta.annotation.Resource;
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
import dao.jpa.AdresseJpaDAO;

/**
 * Contrôleur frontal qui gère le routage des requêtes.
 * Cette classe est responsable de :
 * - L'initialisation des services et des contrôleurs
 * - La gestion des autorisations d'accès
 * - Le routage des requêtes vers les contrôleurs appropriés
 * - La gestion des erreurs et des exceptions
 */
public class FrontController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(FrontController.class.getName());
    private Map<String, ICommand> commands;
    private Map<String, String> roles;
    private ClientService clientService;
    private ProspectService prospectService;
    private UserService userService;

    private static EntityManagerFactory emf;
    private static EntityManager em;

    @Override
    public void init() throws ServletException {
        try {
            LogManager.logInfo("Initialisation du FrontController");
            initializeDatabase();
            initializeServices();
            initializeCommands();
            initializeRoles();
            initializeLogging();
            LogManager.logInfo("Initialisation du FrontController terminée avec succès");
        } catch (Exception e) {
            LogManager.logException("Erreur lors de l'initialisation du FrontController", e);
            throw new ServletException("Erreur lors de l'initialisation", e);
        }
    }

    private void initializeServices() throws DatabaseException {
        LogManager.logInfo("Initialisation des services");
        try {
            // Vérifier que l'EntityManager est disponible
            if (em == null) {
                throw new DatabaseException("EntityManager non initialisé");
            }
            
            UserJpaDAO userDAO = new UserJpaDAO();
            userService = new UserService(userDAO);
            clientService = new ClientService(new ClientJpaDAO(), new AdresseJpaDAO());
            prospectService = new ProspectService(new ProspectJpaDAO());
            LogManager.logInfo("Services initialisés avec succès");
        } catch (Exception e) {
            LogManager.logException("Erreur lors de l'initialisation des services", e);
            throw new DatabaseException("Erreur lors de l'initialisation des services", e);
        }
    }

    private void initializeCommands() throws DatabaseException {
        LogManager.logInfo("Initialisation des commandes");
        commands = new HashMap<>();
        
        try {
            // Commandes pour les clients
            commands.put("/clients", new ListeClientsController(clientService));
            commands.put("/clients.liste", new ListeClientsController(clientService));
            commands.put("/clients.update", new UpdateClientsController(clientService));
            commands.put("/clients.view", new ViewClientsController(clientService));
            commands.put("/clients.create", new CreationClientsController(clientService));
            commands.put("/clients.delete", new DeleteClientsController(clientService));
            commands.put("/clients/update", new UpdateClientsController(clientService));
            commands.put("/clients/view", new ViewClientsController(clientService));
            commands.put("/clients/create", new CreationClientsController(clientService));
            commands.put("/clients/delete", new DeleteClientsController(clientService));

            // Commande pour la page d'accueil
            commands.put("/", new IndexController());
            commands.put("/index", new IndexController());
            
            // Commandes pour les prospects
            commands.put("/prospects", new ListeProspectsController(prospectService));
            commands.put("/prospects.liste", new ListeProspectsController(prospectService));
            commands.put("/prospects.create", new CreationProspectsController(prospectService));
            commands.put("/prospects.update", new UpdateProspectsController(prospectService));
            commands.put("/prospects.delete", new DeleteProspectsController(prospectService));
            commands.put("/prospects.view", new ViewProspectsController(prospectService));
            commands.put("/prospects/create", new CreationProspectsController(prospectService));
            commands.put("/prospects/update", new UpdateProspectsController(prospectService));
            commands.put("/prospects/delete", new DeleteProspectsController(prospectService));
            commands.put("/prospects/view", new ViewProspectsController(prospectService));
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
            // Création de l'EntityManagerFactory à partir de l'unité de persistance "cliprocoUP"
            // UP = Unit of Persistence (Unité de Persistance)
            emf = Persistence.createEntityManagerFactory("cliprocoUP");
            
            // Création de l'EntityManager pour les opérations de base de données
            em = emf.createEntityManager();
            LogManager.logInfo("EntityManager initialisé avec succès pour l'unité cliprocoUP");
        } catch (Exception e) {
            LogManager.logException("Erreur lors de l'initialisation de l'EntityManager", e);
            throw new DatabaseException("Erreur lors de l'initialisation de l'EntityManager", e);
        }
    }

    /**
     * Retourne l'EntityManager partagé pour l'unité de persistance "cliprocoUP"
     * 
     * Cette méthode permet aux autres classes d'accéder à l'EntityManager
     * initialisé par le FrontController. L'EntityManager est utilisé pour
     * effectuer les opérations CRUD sur les entités JPA.
     * 
     * L'EntityManager est associé à l'unité de persistance "cliprocoUP" qui
     * gère la connexion à la base de données MySQL et la configuration Hibernate.
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
        // TODO: Réactiver l'authentification une fois la page de connexion implémentée
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
