package com.cliproco.controller;

import com.cliproco.model.Utilisateur;
import com.cliproco.service.UtilisateurService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public class UtilisateurController implements ICommand {
    private final UtilisateurService utilisateurService;
    private final ValidatorFactory factory;
    private final Validator validator;

    public UtilisateurController() {
        this.utilisateurService = new UtilisateurService();
        this.factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String action = request.getPathInfo();
        if (action == null || action.equals("/")) {
            return "WEB-INF/JSP/login.jsp";
        }

        switch (action) {
            case "/login":
                return login(request);
            case "/logout":
                return logout(request);
            case "/register":
                return register(request);
            case "/profile":
                return profile(request);
            case "/update":
                return updateProfile(request);
            default:
                return "WEB-INF/JSP/erreur.jsp";
        }
    }

    private String login(HttpServletRequest request) {
        if (request.getMethod().equals("GET")) {
            return "WEB-INF/JSP/login.jsp";
        }

        String email = request.getParameter("email");
        String motDePasse = request.getParameter("motDePasse");

        Utilisateur utilisateur = utilisateurService.authenticate(email, motDePasse);
        if (utilisateur != null) {
            HttpSession session = request.getSession();
            session.setAttribute("utilisateur", utilisateur);
            return "redirect:/accueil";
        }

        request.setAttribute("error", "Email ou mot de passe incorrect");
        return "WEB-INF/JSP/login.jsp";
    }

    private String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/utilisateur/login";
    }

    private String register(HttpServletRequest request) {
        if (request.getMethod().equals("GET")) {
            return "WEB-INF/JSP/register.jsp";
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(request.getParameter("nom"));
        utilisateur.setEmail(request.getParameter("email"));
        utilisateur.setMotDePasse(request.getParameter("motDePasse"));
        utilisateur.setRole("USER");

        Set<ConstraintViolation<Utilisateur>> violations = validator.validate(utilisateur);
        if (!violations.isEmpty()) {
            request.setAttribute("errors", violations);
            request.setAttribute("utilisateur", utilisateur);
            return "WEB-INF/JSP/register.jsp";
        }

        try {
            utilisateurService.createUtilisateur(utilisateur);
            return "redirect:/utilisateur/login";
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("utilisateur", utilisateur);
            return "WEB-INF/JSP/register.jsp";
        }
    }

    private String profile(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("utilisateur") == null) {
            return "redirect:/utilisateur/login";
        }

        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        request.setAttribute("utilisateur", utilisateur);
        return "WEB-INF/JSP/profile.jsp";
    }

    private String updateProfile(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("utilisateur") == null) {
            return "redirect:/utilisateur/login";
        }

        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        utilisateur.setNom(request.getParameter("nom"));
        utilisateur.setEmail(request.getParameter("email"));
        String nouveauMotDePasse = request.getParameter("nouveauMotDePasse");
        if (nouveauMotDePasse != null && !nouveauMotDePasse.isEmpty()) {
            utilisateur.setMotDePasse(nouveauMotDePasse);
        }

        Set<ConstraintViolation<Utilisateur>> violations = validator.validate(utilisateur);
        if (!violations.isEmpty()) {
            request.setAttribute("errors", violations);
            request.setAttribute("utilisateur", utilisateur);
            return "WEB-INF/JSP/profile.jsp";
        }

        try {
            utilisateurService.updateUtilisateur(utilisateur);
            session.setAttribute("utilisateur", utilisateur);
            return "redirect:/utilisateur/profile";
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("utilisateur", utilisateur);
            return "WEB-INF/JSP/profile.jsp";
        }
    }
} 