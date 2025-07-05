package utilities;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

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
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("currentUser") == null) {
            return "index.jsp";
        }

        return jsp;
    }

    /**
     * Génère un salt sécurisé pour le hashage des mots de passe.
     * @return String salt encodé en Base64
     */
    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hash un mot de passe avec SHA-256 et un salt.
     * @param password Le mot de passe à hasher
     * @return String hash encodé en Base64 (format: salt:hash)
     */
    public static String hashPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être null ou vide");
        }
        
        try {
            String salt = generateSalt();
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            md.update(password.getBytes(StandardCharsets.UTF_8));
            byte[] hash = md.digest();
            String hashString = Base64.getEncoder().encodeToString(hash);
            
            return salt + ":" + hashString;
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Erreur lors du hashage du mot de passe: " + e.getMessage());
            throw new RuntimeException("Erreur lors du hashage du mot de passe", e);
        }
    }

    /**
     * Vérifie un mot de passe contre son hash.
     * @param password Le mot de passe à vérifier
     * @param storedHash Le hash stocké (format: salt:hash)
     * @return boolean true si le mot de passe correspond
     */
    public static boolean verifyPassword(String password, String storedHash) {
        if (password == null || storedHash == null) {
            return false;
        }
        
        try {
            String[] parts = storedHash.split(":");
            if (parts.length != 2) {
                // Ancien format sans salt, on utilise une vérification simple
                return password.equals(storedHash);
            }
            
            String salt = parts[0];
            String hash = parts[1];
            
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            md.update(password.getBytes(StandardCharsets.UTF_8));
            byte[] computedHash = md.digest();
            String computedHashString = Base64.getEncoder().encodeToString(computedHash);
            
            return hash.equals(computedHashString);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Erreur lors de la vérification du mot de passe: " + e.getMessage());
            return false;
        }
    }

    /**
     * Génère un token CSRF sécurisé.
     * @return String token CSRF
     */
    public static String generateCSRFToken() {
        SecureRandom random = new SecureRandom();
        byte[] tokenBytes = new byte[32];
        random.nextBytes(tokenBytes);
        return Base64.getEncoder().encodeToString(tokenBytes);
    }

    /**
     * Vérifie si un token CSRF est valide.
     * @param sessionToken Le token stocké en session
     * @param formToken Le token envoyé par le formulaire
     * @return boolean true si les tokens correspondent
     */
    public static boolean verifyCSRFToken(String sessionToken, String formToken) {
        if (sessionToken == null || formToken == null) {
            return false;
        }
        return sessionToken.equals(formToken);
    }

    /**
     * Génère et stocke un token CSRF en session.
     * @param session La session HTTP
     * @return String le token généré
     */
    public static String generateAndStoreCSRFToken(HttpSession session) {
        String token = generateCSRFToken();
        session.setAttribute("csrfToken", token);
        return token;
    }

    /**
     * Récupère le token CSRF de la session.
     * @param session La session HTTP
     * @return String le token ou null s'il n'existe pas
     */
    public static String getCSRFToken(HttpSession session) {
        if (session == null) {
            return null;
        }
        return (String) session.getAttribute("csrfToken");
    }
}
