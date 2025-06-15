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
import models.User;
import utilities.Security;
import exceptions.DatabaseException;

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

@WebServlet(name = "front", value = "/front")
public final class FrontController extends HttpServlet {

    /**
     *
     */
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     *
     */
    private final Map<String, ICommand> commands = new HashMap<>();

    /**
     *
     */
    @Resource(name = "jdbc/gestionClients")
    private static DataSource datasource;

    /**
     *
     */
    private static Connection connection;

    /**
     *
     */
    public void init() throws ServletException {
        // Partie générale
        commands.put(null, new IndexController());
        commands.put("index", new IndexController());
        commands.put("contact", new ContactController());
        commands.put("connexion", new ConnexionController());
        commands.put("deconnexion", new DeconnexionController());
//        commands.put("signin", new SigninController());

        // Partie clients
        commands.put("clients", new ListeClientsController());
        commands.put("clients/add", new CreationClientsController());
        commands.put("clients/delete", new DeleteClientsController());
        commands.put("clients/update", new UpdateClientsController());
        commands.put("clients/view", new ViewClientsController());

        // Partie prospects
        commands.put("prospects", new ListeProspectsController());
        commands.put("prospects/add", new CreationProspectsController());
        commands.put("prospects/delete", new DeleteProspectsController());
        commands.put("prospects/update", new UpdateProspectsController());
        commands.put("prospects/view", new ViewProspectsController());

        LogManager.run();

        try {
            connection = datasource.getConnection();
        } catch (SQLException e) {
            throw new ServletException(e);
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
     * Traite la requête HTTP en déterminant la commande à exécuter.
     *
     * @param request la requête HTTP
     * @param response la réponse HTTP
     * @throws ServletException si une erreur de servlet survient
     * @throws IOException si une erreur d'entrée/sortie survient
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String urlSuite = "";

        try {
            loadCurrentUser(request);

            if (request.getParameterMap().containsKey("_csrf")) {
                if (!request.getParameter("_csrf")
                        .equals(request.getSession()
                        .getAttribute("_csrf").toString())) {
                    throw new IllegalAccessException("Les jetons csrf ne "
                            + "correspondent pas!");
                }
            }

            // cmd correspond au nom du paramètre passé avec l'url
            String cmd = request.getParameter("cmd");

            // on récupère l'objet de la classe du contrôleur voulu
            ICommand com = commands.get(cmd);

            // on récupère dans urlSuite la JSP à dispatcher en exécutant
            // le contrôleur appelé par l'URL
            urlSuite = com.execute(request, response);
        } catch (DatabaseException e) {
            LogManager.logException("Erreur de base de données", e);
            request.setAttribute("error", "Une erreur est survenue lors de l'accès à la base de données");
            urlSuite = "error.jsp";
        } catch (Exception e) {
            LogManager.logException("Erreur inattendue", e);
            request.setAttribute("error", "Une erreur inattendue est survenue");
            urlSuite = "error.jsp";
        } finally {
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
                logger.log(Level.SEVERE, "ServletException", e);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "IOException", e);
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
     */
    public void destroy() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        connection = null;

        LogManager.stop();
    }

    /**
     *
     * @param request
     * @throws DatabaseException
     */
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
