package com.cliproco.controller;

import com.cliproco.model.GDPRPreferences;
import com.cliproco.service.GDPRService;
import com.cliproco.util.CookieManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/consent")
public class ConsentController {

    @Autowired
    private GDPRService gdprService;

    @GetMapping
    public String showConsentPage() {
        return "consent";
    }

    @PostMapping
    public String handleConsent(
            @RequestParam(required = false) boolean analytics,
            @RequestParam(required = false) boolean marketing,
            @RequestParam(required = false) boolean preferences,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model) {
        
        // Récupérer l'ID utilisateur depuis la session ou le cookie
        String userId = CookieManager.getUserId(request)
                .orElse(request.getSession().getId());

        // Sauvegarder les préférences RGPD
        GDPRPreferences gdprPreferences = gdprService.saveConsent(
                userId, analytics, marketing, preferences, request);

        // Créer le cookie de consentement
        CookieManager.setUserCookie(response, userId);
        
        // Créer le cookie de préférences
        StringBuilder prefs = new StringBuilder();
        if (analytics) prefs.append("analytics=true;");
        if (marketing) prefs.append("marketing=true;");
        if (preferences) prefs.append("preferences=true;");
        
        if (prefs.length() > 0) {
            CookieManager.setUserPreferences(response, prefs.toString());
        }

        model.addAttribute("message", "Vos préférences ont été enregistrées avec succès.");
        return "redirect:/";
    }

    @GetMapping("/preferences")
    public String showPreferences(HttpServletRequest request, Model model) {
        String userId = CookieManager.getUserId(request)
                .orElse(request.getSession().getId());
        
        gdprService.getUserPreferences(userId)
                .ifPresent(prefs -> model.addAttribute("preferences", prefs));
        
        return "preferences";
    }

    @PostMapping("/update")
    public String updatePreferences(
            @RequestParam Long preferencesId,
            @RequestParam(required = false) boolean analytics,
            @RequestParam(required = false) boolean marketing,
            @RequestParam(required = false) boolean preferences,
            Model model) {
        
        gdprService.updateConsent(preferencesId, analytics, marketing, preferences);
        model.addAttribute("message", "Vos préférences ont été mises à jour avec succès.");
        return "redirect:/consent/preferences";
    }

    @PostMapping("/delete")
    public String deleteUserData(HttpServletRequest request, Model model) {
        String userId = CookieManager.getUserId(request)
                .orElse(request.getSession().getId());
        
        gdprService.deleteUserData(userId);
        CookieManager.removeUserCookie(request.getSession().getServletContext().getResponse());
        CookieManager.removeUserPreferences(request.getSession().getServletContext().getResponse());
        
        model.addAttribute("message", "Vos données ont été supprimées avec succès.");
        return "redirect:/";
    }
} 