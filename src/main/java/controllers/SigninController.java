package controllers;

import dao.IDAO;
import dao.jpa.UserJpaDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;
import utilities.LogManager;
import utilities.Security;
import java.util.Optional;

public class SigninController implements ICommand {
    private final IDAO<User, Long> userDAO;

    public SigninController() {
        this.userDAO = new UserJpaDAO();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (email == null || password == null || confirmPassword == null) {
            return "inscription.jsp";
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Les mots de passe ne correspondent pas");
            return "inscription.jsp";
        }

        try {
            Optional<User> existingUser = userDAO.findById(Long.parseLong(email));
            if (existingUser.isPresent()) {
                request.setAttribute("error", "Cet email est déjà utilisé");
                return "inscription.jsp";
            }

            User newUser = new User();
            newUser.setEmail(email);
            newUser.setPassword(Security.hashPassword(password));
            newUser.setRole("USER");

            userDAO.save(newUser);

            HttpSession session = request.getSession();
            session.setAttribute("user", newUser);
            session.setAttribute("role", newUser.getRole());

            return "index.jsp";
        } catch (Exception e) {
            LogManager.logException("Erreur lors de l'inscription", e);
            request.setAttribute("error", "Une erreur est survenue");
            return "inscription.jsp";
        }
    }
}
