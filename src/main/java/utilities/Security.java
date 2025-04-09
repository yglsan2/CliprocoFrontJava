package utilities;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

/**
 * Classe utilitaire pour la gestion de la sécurité et de l'authentification.
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
     * Vérifie si l'utilisateur est connecté et retourne le nom du fichier JSP approprié.
     *
     * @param request La requête HTTP
     * @param jsp Le nom du fichier JSP demandé
     * @return Le nom du fichier JSP accessible
     */
    public static String estConnecte(final HttpServletRequest request,
                                     final String jsp) {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("currentUser") == null) {
            return "index.jsp";
        }

        return jsp;
    }

    /**
     * Hash un mot de passe en utilisant Argon2.
     *
     * @param password Le mot de passe à hasher
     * @return Le hash du mot de passe
     */
    public static String hashPassword(String password) {
        return argon2.hash(2, 65536, 1, password);
    }

    /**
     * Vérifie si un mot de passe correspond à un hash.
     *
     * @param password Le mot de passe à vérifier
     * @param hash Le hash stocké
     * @return true si le mot de passe correspond au hash, false sinon
     */
    public static boolean verifyPassword(String password, String hash) {
        return argon2.verify(hash, password);
    }
}
