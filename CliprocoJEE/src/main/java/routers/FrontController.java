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
import models.User;
import utilities.Security;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "front", value = "/front")
public final class FrontController extends HttpServlet {

    /**
     *
     */
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     *
     */
    private final HashMap<String, Object> commands = new HashMap<>();

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
     *
     * @param request
     * @param response
     */
    private void processRequest(final HttpServletRequest request,
                                final HttpServletResponse response) {
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
            ICommand com = (ICommand) commands.get(cmd);

            // on récupère dans urlSuite la JSP à dispatcher en exécutant
            // le contrôleur appelé par l'URL
            urlSuite = com.execute(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur : " + e.getMessage());
            request.setAttribute("titlePage", "Erreur");
            request.setAttribute("titleGroup", "Général");
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
     * @throws SocieteDatabaseException
     */
    private void loadCurrentUser(final HttpServletRequest request)
            throws SocieteDatabaseException {
        Cookie[] cookies = request.getCookies();
        if (request.getSession().getAttribute("currentUser") == null) {

            for (Cookie cookie : cookies) {

                if (cookie.getName().equals("currentUser")) {

                    UserJpaDAO dao = new UserJpaDAO();

                    User user = dao.findByToken(cookie.getValue());

                    if (LocalDate.now().isBefore(user.getExpire())) {
                        request.getSession()
                                .setAttribute("currentUser", user
                                        .getUsername());
                    }
                }
            }
        }
    }
}
