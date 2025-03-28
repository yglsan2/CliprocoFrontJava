package com.cliproco.service;

import com.cliproco.dao.ProspectDao;
import com.cliproco.model.Prospect;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.List;
import java.util.Set;

public class ProspectService {
    private final ProspectDao prospectDao;
    private final ValidatorFactory factory;
    private final Validator validator;

    public ProspectService() {
        this.prospectDao = new ProspectDao();
        this.factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public List<Prospect> getAllProspects() {
        return prospectDao.findAll();
    }

    public Prospect getProspect(Long id) {
        return prospectDao.findById(id);
    }

    public void createProspect(Prospect prospect) {
        Set<ConstraintViolation<Prospect>> violations = validator.validate(prospect);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Prospect invalide : " + violations);
        }
        prospectDao.create(prospect);
    }

    public void updateProspect(Prospect prospect) {
        Set<ConstraintViolation<Prospect>> violations = validator.validate(prospect);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Prospect invalide : " + violations);
        }
        prospectDao.update(prospect);
    }

    public void deleteProspect(Long id) {
        prospectDao.delete(id);
    }

    public List<Prospect> findByStatut(String statut) {
        return prospectDao.findByStatut(statut);
    }

    public List<Prospect> findBySecteurActivite(String secteurActivite) {
        return prospectDao.findBySecteurActivite(secteurActivite);
    }
} 