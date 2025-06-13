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

import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;

/**
 *
 */
@WebServlet("/signin")
public class SigninController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/signin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        try {
            User user = new User(username, password, email);
            final int magicNumber = 65536;

            String appSecret = System.getenv("APP_SECRET");

            Validator validator = Validation.buildDefaultValidatorFactory()
                                            .getValidator();
            Set<ConstraintViolation<User>> violations =
                    validator.validate(user);

            request.setAttribute("violations", violations);

            if (violations.isEmpty()) {
                UserJpaDAO dao = new UserJpaDAO();

                Argon2 argon2 = Argon2Factory.create();
                char[] cast = (appSecret + user.getPassword()).toCharArray();
                String hash = argon2.hash(
                        2, magicNumber, 1,
                        cast
                );

                user.setPassword(hash);

                dao.save(user);
                LogManager.LOGS.log(Level.INFO, "User created successfully: {0}", user.getUsername());
                response.sendRedirect(request.getContextPath() + "/signin");
            } else {
                request.setAttribute("violations", violations);
                request.getRequestDispatcher("/WEB-INF/jsp/signin.jsp").forward(request, response);
            }
        } catch (Exception e) {
            LogManager.LOGS.log(Level.SEVERE, "Erreur lors de la création de l'utilisateur", e);
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/signin.jsp").forward(request, response);
        }
    }
}
