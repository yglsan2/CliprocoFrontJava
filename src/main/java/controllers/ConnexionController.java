package controllers;

import dao.IDAO;
import dao.jpa.UserJpaDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.User;
import services.UserService;
import utilities.LogManager;
import utilities.Security;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "ConnexionController", urlPatterns = {"/connexion"})
public class ConnexionController extends HttpServlet {
    private static final String LOGIN_PAGE = "/WEB-INF/views/login.jsp";
    private static final String HOME_PAGE = "/WEB-INF/views/home.jsp";
    private static final String ERROR_PAGE = "/WEB-INF/views/error.jsp";

    private final IDAO<User, Integer> userDAO;

    public ConnexionController() {
        this.userDAO = new UserJpaDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || password == null) {
            request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
            return;
        }

        try {
            Optional<User> userOpt = userDAO.findById(Integer.parseInt(email));
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                if (Security.verifyPassword(password, user.getPassword())) {
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    session.setAttribute("role", user.getRole());
                    LogManager.logInfo("Connexion réussie pour l'utilisateur: " + email);
                    response.sendRedirect(request.getContextPath() + "/home");
                } else {
                    LogManager.logWarning("Tentative de connexion échouée pour l'email: " + email);
                    request.setAttribute("error", "Email ou mot de passe incorrect");
                    request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
                }
            } else {
                LogManager.logWarning("Utilisateur non trouvé pour l'email: " + email);
                request.setAttribute("error", "Email ou mot de passe incorrect");
                request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
            }
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la connexion", e);
            request.setAttribute("error", "Une erreur est survenue lors de la connexion");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher(LOGIN_PAGE).forward(request, response);
    }
}
