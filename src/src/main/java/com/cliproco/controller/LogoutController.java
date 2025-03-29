package com.cliproco.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Invalider la session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Supprimer le cookie "remember me"
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("remember_token".equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break;
                }
            }
        }

        // Rediriger vers la page d'accueil
        response.sendRedirect("index.jsp");
    }
} 