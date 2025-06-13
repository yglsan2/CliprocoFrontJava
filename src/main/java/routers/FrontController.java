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
import dao.SocieteDatabaseException;
import dao.jpa.UserJpaDAO;
import logs.LogManager;
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
import exceptions.DatabaseException;
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
@WebServlet(name = "FrontController", urlPatterns = {"/"})
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
    private Map<String, ICommand> commands;

    static {
        try {
            MysqlDataSource mysqlDS = new MysqlDataSource();
            mysqlDS.setURL("jdbc:mysql://localhost:3306/cliproco");
            mysqlDS.setUser("root");
            mysqlDS.setPassword("");
            datasource = mysqlDS;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de l'initialisation de la source de données", e);
            throw new RuntimeException("Erreur lors de l'initialisation de la source de données", e);
        }
    }

    @Override
    public void init() throws ServletException {
        // Initialisation des commandes
        commands = new HashMap<>();

        // Partie connexion
        commands.put("connexion", new ConnexionController());
        commands.put("connexion/login", new ConnexionController());
        commands.put("connexion/logout", new ConnexionController());

        // Partie prospects
        commands.put("prospects", new ListeProspectsController());
        commands.put("prospects/add", new CreationProspectsController());
        commands.put("prospects/delete", new DeleteProspectsController());
        commands.put("prospects/update", new UpdateProspectsController());
        commands.put("prospects/view", new ViewProspectsController());

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
            request.setAttribute("titlePage", "Erreur");
            request.setAttribute("titleGroup", "Général");
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception e) {
            LogManager.logException("Erreur lors de l'affichage de la page d'erreur", e);
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
     *
     * @param request
     * @param response
     */
    private void processRequest(final HttpServletRequest request,
                                final HttpServletResponse response) {
        String urlSuite = "";

        try {
            LogManager.debug("Traitement de la requête: " + request.getMethod() + " " + request.getRequestURI());
            loadCurrentUser(request);

            if (request.getParameterMap().containsKey("_csrf")) {
                LogManager.debug("Vérification du token CSRF");
                if (!request.getParameter("_csrf")
                        .equals(request.getSession()
                        .getAttribute("_csrf").toString())) {
                    throw new ValidationException("Les jetons CSRF ne correspondent pas");
                }
            }

            String cmd = request.getParameter("cmd");
            if (cmd == null) {
                throw new ValidationException("Commande non spécifiée");
            }

            LogManager.debug("Commande demandée: " + cmd);
            ICommand com = commands.get(cmd);
            if (com == null) {
                throw new ValidationException("Commande inconnue: " + cmd);
            }

            urlSuite = com.execute(request, response);
            LogManager.debug("Commande exécutée avec succès, redirection vers: " + urlSuite);
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

        try {
            final String keyword = "redirect:";

            String csrf = UUID.randomUUID().toString();
            request.getSession().setAttribute("_csrf", csrf);
            request.setAttribute("_csrf", csrf);

            if (urlSuite != null && urlSuite.startsWith(keyword)) {
                urlSuite = urlSuite.substring(keyword.length());
                response.sendRedirect(request.getContextPath()
                        + "/" + urlSuite);
            } else {
                request.getRequestDispatcher("WEB-INF/jsp/"
                        + urlSuite).forward(request, response);
            }
        } catch (ServletException e) {
            LogManager.logException("Erreur Servlet", e);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur de servlet");
            } catch (IOException ex) {
                LogManager.logException("Erreur lors de l'envoi de l'erreur HTTP", ex);
            }
        } catch (IOException e) {
            LogManager.logException("Erreur I/O", e);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur I/O");
            } catch (IOException ex) {
                LogManager.logException("Erreur lors de l'envoi de l'erreur HTTP", ex);
            }
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
        processRequest(request, response);
    }

    /**
     *
     * @param request
     * @param response
     */
    @Override
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response) {
        processRequest(request, response);
    }

    /**
     *
     * @param request
     * @throws SocieteDatabaseException
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
}
