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
        try {
            return clientDao.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des clients", e);
        }
    }

    public Client getClient(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID du client ne peut pas être null");
        }
        try {
            Client client = clientDao.findById(id);
            if (client == null) {
                throw new IllegalArgumentException("Client non trouvé avec l'ID : " + id);
            }
            return client;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération du client", e);
        }
    }

    public void createClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Le client ne peut pas être null");
        }

        // Validation des contraintes
        Set<ConstraintViolation<Client>> violations = validator.validate(client);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Client invalide : " + violations);
        }

        // Validation métier
        validateClientBusinessRules(client);

        try {
            clientDao.create(client);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du client", e);
        }
    }

    public void updateClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Le client ne peut pas être null");
        }
        if (client.getId() == null) {
            throw new IllegalArgumentException("L'ID du client ne peut pas être null pour une mise à jour");
        }

        // Vérifier si le client existe
        Client existingClient = clientDao.findById(client.getId());
        if (existingClient == null) {
            throw new IllegalArgumentException("Client non trouvé avec l'ID : " + client.getId());
        }

        // Validation des contraintes
        Set<ConstraintViolation<Client>> violations = validator.validate(client);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Client invalide : " + violations);
        }

        // Validation métier
        validateClientBusinessRules(client);

        try {
            clientDao.update(client);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du client", e);
        }
    }

    public void deleteClient(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID du client ne peut pas être null");
        }

        // Vérifier si le client existe
        Client client = clientDao.findById(id);
        if (client == null) {
            throw new IllegalArgumentException("Client non trouvé avec l'ID : " + id);
        }

        try {
            clientDao.delete(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression du client", e);
        }
    }

    public List<Client> findBySecteurActivite(String secteurActivite) {
        if (secteurActivite == null || secteurActivite.trim().isEmpty()) {
            throw new IllegalArgumentException("Le secteur d'activité ne peut pas être vide");
        }
        try {
            return clientDao.findBySecteurActivite(secteurActivite);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche des clients par secteur d'activité", e);
        }
    }

    public List<Client> findByChiffreAffairesMinimal(double chiffreAffaires) {
        if (chiffreAffaires < 0) {
            throw new IllegalArgumentException("Le chiffre d'affaires ne peut pas être négatif");
        }
        try {
            return clientDao.findByChiffreAffairesMinimal(chiffreAffaires);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche des clients par chiffre d'affaires", e);
        }
    }

    private void validateClientBusinessRules(Client client) {
        // Vérifier que le chiffre d'affaires est positif
        if (client.getChiffreAffaires() < 0) {
            throw new IllegalArgumentException("Le chiffre d'affaires ne peut pas être négatif");
        }

        // Vérifier que le secteur d'activité n'est pas vide
        if (client.getSecteurActivite() == null || client.getSecteurActivite().trim().isEmpty()) {
            throw new IllegalArgumentException("Le secteur d'activité ne peut pas être vide");
        }

        // Vérifier que le nom de l'entreprise n'est pas vide
        if (client.getNomEntreprise() == null || client.getNomEntreprise().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'entreprise ne peut pas être vide");
        }
    }
} 