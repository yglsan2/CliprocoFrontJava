package controllers;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import dao.jpa.UserJpaDAO;
import logs.LogManager;
import models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.jetbrains.annotations.Contract;
import utilities.Security;
import services.UserService;
import utilities.LogManager;

import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;

/**
 *
 */
@WebServlet(name = "SigninController", urlPatterns = {"/signin"})
public class SigninController extends HttpServlet {
    private static final String SIGNIN_PAGE = "/WEB-INF/views/signin.jsp";
    private static final String LOGIN_PAGE = "/WEB-INF/views/login.jsp";
    private static final String ERROR_PAGE = "/WEB-INF/views/error.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher(SIGNIN_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Les mots de passe ne correspondent pas");
            request.getRequestDispatcher(SIGNIN_PAGE).forward(request, response);
            return;
        }

        try {
            UserService userService = new UserService();
            User user = new User();
            user.setEmail(email);
            user.setPassword(Security.hashPassword(password));
            
            userService.save(user);
            LogManager.info("Inscription réussie pour l'utilisateur: " + email);
            response.sendRedirect(request.getContextPath() + "/login");
        } catch (Exception e) {
            LogManager.logException("Erreur lors de l'inscription", e);
            request.setAttribute("error", "Une erreur est survenue lors de l'inscription");
            request.getRequestDispatcher(ERROR_PAGE).forward(request, response);
        }
    }
}
