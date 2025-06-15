package utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import org.apache.logging.log4j.LogManager;

/**
 * Classe utilitaire pour la gestion de la sécurité et de l'authentification.
 */
public final class Security {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;

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

    public static String generateSalt() {
        StringBuilder returnValue = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            LogManager.logException("Erreur lors du hachage du mot de passe", e);
            throw new RuntimeException("Erreur lors du hachage du mot de passe", e);
        }
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        String hashedInput = hashPassword(password);
        return hashedInput.equals(hashedPassword);
    }

    public static String generateToken() {
        byte[] randomBytes = new byte[32];
        RANDOM.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
