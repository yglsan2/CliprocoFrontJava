package com.cliproco.util;

import com.cliproco.exception.SecurityException;
import java.util.regex.Pattern;

public class InputValidator {
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern XSS_PATTERN = Pattern.compile("<script>|javascript:|onload=|onerror=", Pattern.CASE_INSENSITIVE);

    public static void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new SecurityException("INVALID_USERNAME", "Le nom d'utilisateur ne peut pas être vide");
        }
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new SecurityException("INVALID_USERNAME", 
                "Le nom d'utilisateur doit contenir entre 3 et 20 caractères alphanumériques ou underscores");
        }
    }

    public static void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new SecurityException("INVALID_PASSWORD", "Le mot de passe ne peut pas être vide");
        }
        if (password.length() < SecurityConstants.PASSWORD_MIN_LENGTH) {
            throw new SecurityException("INVALID_PASSWORD", 
                "Le mot de passe doit contenir au moins " + SecurityConstants.PASSWORD_MIN_LENGTH + " caractères");
        }
        if (password.length() > SecurityConstants.PASSWORD_MAX_LENGTH) {
            throw new SecurityException("INVALID_PASSWORD", 
                "Le mot de passe ne peut pas dépasser " + SecurityConstants.PASSWORD_MAX_LENGTH + " caractères");
        }
        if (!password.matches(SecurityConstants.PASSWORD_PATTERN)) {
            throw new SecurityException("INVALID_PASSWORD", 
                "Le mot de passe doit contenir au moins une majuscule, une minuscule, un chiffre et un caractère spécial");
        }
    }

    public static void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new SecurityException("INVALID_EMAIL", "L'email ne peut pas être vide");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new SecurityException("INVALID_EMAIL", "Format d'email invalide");
        }
    }

    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        // Supprimer les caractères HTML
        String sanitized = input.replaceAll("<[^>]*>", "");
        // Vérifier les attaques XSS
        if (XSS_PATTERN.matcher(sanitized).find()) {
            throw new SecurityException("XSS_DETECTED", "Contenu malveillant détecté");
        }
        return sanitized;
    }

    public static void validateCSRFToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new SecurityException("INVALID_CSRF", SecurityConstants.ERROR_INVALID_CSRF);
        }
    }
} 