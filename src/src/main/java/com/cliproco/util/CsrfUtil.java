package com.cliproco.util;

import jakarta.servlet.http.HttpSession;

public class CsrfUtil {
    private static final String CSRF_TOKEN_ATTRIBUTE = "csrfToken";

    public static String getToken(HttpSession session) {
        return (String) session.getAttribute(CSRF_TOKEN_ATTRIBUTE);
    }

    public static String generateTokenInput() {
        return String.format(
            "<input type=\"hidden\" name=\"_csrf\" value=\"%s\">",
            getToken(null) // Le token sera récupéré de la session dans le JSP
        );
    }

    public static String generateTokenMeta() {
        return String.format(
            "<meta name=\"_csrf\" content=\"%s\">",
            getToken(null) // Le token sera récupéré de la session dans le JSP
        );
    }
} 