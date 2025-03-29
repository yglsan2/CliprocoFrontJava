package com.cliproco.util;

import jakarta.servlet.http.HttpSession;
import com.cliproco.model.User;

public class SessionUtil {
    private static final String USER_SESSION_ATTRIBUTE = "user";
    private static final String LAST_ACTIVITY_ATTRIBUTE = "lastActivity";
    private static final long SESSION_TIMEOUT = 30 * 60 * 1000; // 30 minutes en millisecondes

    public static void setUser(HttpSession session, User user) {
        session.setAttribute(USER_SESSION_ATTRIBUTE, user);
        updateLastActivity(session);
    }

    public static User getUser(HttpSession session) {
        return (User) session.getAttribute(USER_SESSION_ATTRIBUTE);
    }

    public static void updateLastActivity(HttpSession session) {
        session.setAttribute(LAST_ACTIVITY_ATTRIBUTE, System.currentTimeMillis());
    }

    public static boolean isSessionValid(HttpSession session) {
        if (session == null) {
            return false;
        }

        Long lastActivity = (Long) session.getAttribute(LAST_ACTIVITY_ATTRIBUTE);
        if (lastActivity == null) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        return (currentTime - lastActivity) < SESSION_TIMEOUT;
    }

    public static void invalidateSession(HttpSession session) {
        if (session != null) {
            session.removeAttribute(USER_SESSION_ATTRIBUTE);
            session.removeAttribute(LAST_ACTIVITY_ATTRIBUTE);
            session.invalidate();
        }
    }
} 