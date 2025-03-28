package com.cliproco.service;

import com.cliproco.dao.ClientDao;
import com.cliproco.model.Client;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.List;
import java.util.Set;

public class ClientService {
    private final ClientDao clientDao;
    private final ValidatorFactory factory;
    private final Validator validator;

    public ClientService() {
        this.clientDao = new ClientDao();
        this.factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public List<Client> getAllClients() {
        return clientDao.findAll();
    }

    public Client getClient(Long id) {
        return clientDao.findById(id);
    }

    public void createClient(Client client) {
        Set<ConstraintViolation<Client>> violations = validator.validate(client);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Client invalide : " + violations);
        }
        clientDao.create(client);
    }

    public void updateClient(Client client) {
        Set<ConstraintViolation<Client>> violations = validator.validate(client);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Client invalide : " + violations);
        }
        clientDao.update(client);
    }

    public void deleteClient(Long id) {
        clientDao.delete(id);
    }

    public List<Client> findBySecteurActivite(String secteurActivite) {
        return clientDao.findBySecteurActivite(secteurActivite);
    }

    public List<Client> findByChiffreAffairesMinimal(double chiffreAffaires) {
        return clientDao.findByChiffreAffairesMinimal(chiffreAffaires);
    }
} 