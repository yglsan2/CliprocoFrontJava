package com.cliproco.service;

import com.cliproco.dao.UtilisateurDao;
import com.cliproco.model.Utilisateur;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class UtilisateurService {
    private final UtilisateurDao utilisateurDao;
    private final ValidatorFactory factory;
    private final Validator validator;

    public UtilisateurService() {
        this.utilisateurDao = new UtilisateurDao();
        this.factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurDao.findAll();
    }

    public Utilisateur getUtilisateur(Long id) {
        return utilisateurDao.findById(id);
    }

    public Utilisateur findByEmail(String email) {
        return utilisateurDao.findByEmail(email);
    }

    public List<Utilisateur> findByRole(String role) {
        return utilisateurDao.findByRole(role);
    }

    public void createUtilisateur(Utilisateur utilisateur) {
        Set<ConstraintViolation<Utilisateur>> violations = validator.validate(utilisateur);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Utilisateur invalide : " + violations);
        }
        // Hashage du mot de passe avant la création
        utilisateur.setMotDePasse(BCrypt.hashpw(utilisateur.getMotDePasse(), BCrypt.gensalt()));
        utilisateurDao.create(utilisateur);
    }

    public void updateUtilisateur(Utilisateur utilisateur) {
        Set<ConstraintViolation<Utilisateur>> violations = validator.validate(utilisateur);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Utilisateur invalide : " + violations);
        }
        // Si le mot de passe a été modifié, on le hash
        if (utilisateur.getMotDePasse() != null && !utilisateur.getMotDePasse().startsWith("$2a$")) {
            utilisateur.setMotDePasse(BCrypt.hashpw(utilisateur.getMotDePasse(), BCrypt.gensalt()));
        }
        utilisateurDao.update(utilisateur);
    }

    public void deleteUtilisateur(Long id) {
        utilisateurDao.delete(id);
    }

    public Utilisateur authenticate(String email, String motDePasse) {
        Utilisateur utilisateur = findByEmail(email);
        if (utilisateur != null && BCrypt.checkpw(motDePasse, utilisateur.getMotDePasse())) {
            return utilisateur;
        }
        return null;
    }
} 