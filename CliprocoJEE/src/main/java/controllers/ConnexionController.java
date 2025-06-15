package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;
import services.UserService;
import utilities.LogManager;
import utilities.Security;
import java.io.IOException;

@WebServlet(name = "ConnexionController", urlPatterns = {"/connexion"})
public class ConnexionController extends HttpServlet {
    private static final String LOGIN_PAGE = "/WEB-INF/views/login.jsp";
    private static final String HOME_PAGE = "/WEB-INF/views/home.jsp";
    private static final String ERROR_PAGE = "/WEB-INF/views/error.jsp";

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
