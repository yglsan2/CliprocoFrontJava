package controllers;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import dao.jpa.UserJpaDAO;
import logs.LogManager;
import models.User;
import jakarta.servlet.http.Cookie;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import utilities.Security;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

public final class ConnexionController implements ICommand {

    /**
     *
     * @param request La requete à répondre.
     * @param response La réponse à la requête.
     * @return
     * @throws Exception
     */
    @Contract(pure = true)
    @Override
    public @NotNull String execute(final HttpServletRequest request,
                                   final HttpServletResponse response)
            throws Exception {
        User user = new User();
        ArrayList<String> errors = new ArrayList<>();
        String urlSuite = "connexion.jsp";

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Populate user object for form repopulation
        user.setUsername(username != null ? username : "");

        if (username != null && password != null) {
            // Validate required fields
            if (username.isEmpty()) {
                errors.add("Username est requis");
            }
            if (password.isEmpty()) {
                errors.add("Password est requis");
            }

            if (errors.isEmpty()) {
                UserJpaDAO dao = new UserJpaDAO();
                User dbUser = dao.findByUsername(username);

                if (dbUser != null) {
                    if (Security.verifyPassword(password, dbUser.getPassword())) {
                        if (request.getParameterMap().get("rememberMe")
                                != null) {
                            final int secToMinutes = 60;
                            final int minToHours = 60;
                            final int hoursToDays = 24;
                            final int daysToWeek = 7;

                            Cookie cookie = new Cookie("currentUser",
                                    UUID.randomUUID().toString());
                            cookie.setMaxAge(secToMinutes * minToHours
                                    * hoursToDays * daysToWeek);
                            response.addCookie(cookie);

                            dbUser.setToken(cookie.getValue());
                            dbUser.setExpire(LocalDate
                                    .now()
                                    .plusDays(daysToWeek));

                            dao.save(dbUser);
                        }

                        request.getSession()
                                .setAttribute("currentUser", dbUser
                                .getUsername());
                        urlSuite = "redirect:?cmd=index";
                    } else {
                        errors.add("Username ou password incorrecte");
                    }
                } else {
                    errors.add("Username inconnu.");
                }
            }

            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                LogManager.LOGS.warning("Erreur de connexion "
                        + "pour l'utilisateur: " + username);
            }
        }

        request.setAttribute("titlePage", "Connexion");
        request.setAttribute("titleGroup", "Général");
        request.setAttribute("user", user);

        return urlSuite;
    }
}
