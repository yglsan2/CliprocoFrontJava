package utilities;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;
import java.nio.charset.StandardCharsets;

/**
 * Classe utilitaire pour la sécurité
 */
public final class Security {

    private static final Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2id);
    private static final Integer ITERATIONS = 10;
    private static final Integer MEMORY = 65536;
    private static final Integer PARALLELISM = 1;
    private static final Integer SALT_LENGTH = 16;
    private static final Integer HASH_LENGTH = 32;

    /**
     * Constructeur privé pour empêcher l'instanciation de la classe utilitaire.
     */
    private Security() {
        throw new IllegalStateException("Classe utilitaire, ne pas instancier");
    }

    /**
     * Vérifie si l'utilisateur est connecté
     * @param request La requête HTTP
     * @param jsp Le nom du fichier JSP à retourner si l'utilisateur est connecté
     * @return Le nom du fichier JSP à afficher
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
     * Hash un mot de passe avec Argon2id
     * @param password Le mot de passe à hasher
     * @return Le hash du mot de passe
     */
    public static String hashPassword(String password) {
        try {
            return argon2.hash(ITERATIONS, MEMORY, PARALLELISM, password.toCharArray(), StandardCharsets.UTF_8);
        } finally {
            // Nettoyage sécurisé du mot de passe en mémoire
            if (password != null) {
                password = null;
            }
        }
    }

    /**
     * Vérifie si un mot de passe correspond à un hash
     * @param password Le mot de passe à vérifier
     * @param hash Le hash à comparer
     * @return true si le mot de passe correspond au hash
     */
    public static boolean verifyPassword(String password, String hash) {
        try {
            return argon2.verify(hash, password.toCharArray());
        } finally {
            // Nettoyage sécurisé du mot de passe en mémoire
            if (password != null) {
                password = null;
            }
        }
    }
}
