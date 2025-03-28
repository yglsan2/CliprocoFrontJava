package com.cliproco.controller;

import com.cliproco.dao.UserDAO;
import com.cliproco.model.User;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "AuthController", urlPatterns = {
    "/login",
    "/logout",
    "/create-admin"
})
public class AuthController extends HttpServlet {
    private UserDAO userDAO;
    private Argon2 argon2;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
        argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        
        switch (path) {
            case "/login":
                request.getRequestDispatcher("/WEB-INF/JSP/login.jsp").forward(request, response);
                break;
            case "/logout":
                handleLogout(request, response);
                break;
            case "/create-admin":
                handleCreateAdmin(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        
        if ("/login".equals(path)) {
            handleLogin(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String rememberMe = request.getParameter("rememberMe");

        User user = userDAO.findByUsername(username);
        
        if (user != null && argon2.verify(user.getPassword(), password)) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            
            if ("on".equals(rememberMe)) {
                String token = UUID.randomUUID().toString();
                Cookie tokenCookie = new Cookie("rememberMe", token);
                tokenCookie.setMaxAge(60 * 60 * 24 * 30); // 30 jours
                response.addCookie(tokenCookie);
            }

            String redirectUrl = (String) session.getAttribute("redirectUrl");
            if (redirectUrl != null) {
                session.removeAttribute("redirectUrl");
                response.sendRedirect(request.getContextPath() + redirectUrl);
            } else {
                response.sendRedirect(request.getContextPath() + "/accueil");
            }
        } else {
            request.setAttribute("error", "Identifiants invalides");
            request.getRequestDispatcher("/WEB-INF/JSP/login.jsp").forward(request, response);
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("rememberMe".equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    break;
                }
            }
        }

        response.sendRedirect(request.getContextPath() + "/accueil");
    }

    private void handleCreateAdmin(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String appSecret = System.getenv("APP_SECRET");
        if (appSecret == null || !appSecret.equals(request.getParameter("secret"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String username = "admin";
        String password = "admin123"; // À changer en production
        String salt = UUID.randomUUID().toString();
        
        String hashedPassword = argon2.hash(2, 65536, 1, password + salt);

        User admin = new User(username, hashedPassword, salt);
        userDAO.save(admin);

        response.getWriter().write("Administrateur créé avec succès");
    }

    @Override
    public void destroy() {
        if (userDAO != null) {
            userDAO.close();
        }
    }
} 