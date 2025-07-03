package utilities;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;

/**
 * Classe utilitaire pour la sécurité (authentification, session).
 *
 * <p>
 * Depuis la migration Tomcat 11, la logique de hash sécurisé (Argon2) a été supprimée.
 * Les méthodes de hash sont des stubs à remplacer par une vraie implémentation en production !
 * </p>
 */
public final class Security {

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
        // Temporairement désactivé pour permettre l'accès sans authentification

        /*
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("currentUser") == null) {
            return "index.jsp";
        }
        */

        return jsp;
    }

    /**
     * Stub temporaire : retourne le mot de passe en clair (à remplacer par un vrai hash !)
     */
    public static String hashPassword(String password) {
        return password;
    }

    /**
     * Stub temporaire : vérifie toujours vrai (à remplacer par une vraie vérification !)
     */
    public static boolean verifyPassword(String password, String hash) {
        return password.equals(hash);
    }
}
