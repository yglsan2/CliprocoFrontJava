package com.cliproco.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

public class CookieManager {
    private static final String USER_COOKIE = "user_session";
    private static final String PREFERENCE_COOKIE = "user_preferences";
    private static final int COOKIE_MAX_AGE = 30 * 24 * 60 * 60; // 30 jours

    public static void setUserCookie(HttpServletResponse response, String userId) {
        Cookie cookie = new Cookie(USER_COOKIE, userId);
        cookie.setMaxAge(COOKIE_MAX_AGE);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void setUserPreferences(HttpServletResponse response, String preferences) {
        Cookie cookie = new Cookie(PREFERENCE_COOKIE, preferences);
        cookie.setMaxAge(COOKIE_MAX_AGE);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static Optional<String> getUserId(HttpServletRequest request) {
        return getCookieValue(request, USER_COOKIE);
    }

    public static Optional<String> getUserPreferences(HttpServletRequest request) {
        return getCookieValue(request, PREFERENCE_COOKIE);
    }

    private static Optional<String> getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals(cookieName))
                    .map(Cookie::getValue)
                    .findFirst();
        }
        return Optional.empty();
    }

    public static void removeUserCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(USER_COOKIE, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void removeUserPreferences(HttpServletResponse response) {
        Cookie cookie = new Cookie(PREFERENCE_COOKIE, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
} 