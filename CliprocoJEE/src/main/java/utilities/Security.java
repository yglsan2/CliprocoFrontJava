package utilities;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

/**
 *
 */
public final class Security {

    private static final Argon2 argon2 = Argon2Factory.create();

    /**
     * Constructeur privé pour empêcher l'instanciation de la classe utilitaire.
     */
    private Security() {
        throw new IllegalStateException("Classe utilitaire, ne pas instancier");
    }

    /**
     *
     * @param request
     * @param jsp
     * @return the accessible jsp filename.
     */
    public static String estConnecte(final HttpServletRequest request,
                                     final String jsp) {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("currentUser") == null) {
            return "index.jsp";
        }

        return jsp;
    }

    public static String hashPassword(String password) {
        return argon2.hash(2, 65536, 1, password);
    }

    public static boolean verifyPassword(String password, String hash) {
        return argon2.verify(hash, password);
    }
}
