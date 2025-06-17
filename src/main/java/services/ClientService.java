package services;

import dao.IDAO;
import models.Client;
import models.Adresse;
import utilities.LogManager;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import exceptions.BusinessException;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des clients.
 */
public class ClientService {
    private final IDAO<Client, Long> clientDAO;
    private final IDAO<Adresse, Long> adresseDAO;

    public ClientService(IDAO<Client, Long> clientDAO, IDAO<Adresse, Long> adresseDAO) {
        this.clientDAO = clientDAO;
        this.adresseDAO = adresseDAO;
        LogManager.logInfo("ClientService initialisé avec succès - clientDAO: " + clientDAO.getClass().getName() + ", adresseDAO: " + adresseDAO.getClass().getName());
    }

    public Optional<Client> findById(Long id) throws ValidationException, DatabaseException {
        LogManager.logInfo("Recherche du client avec l'ID: " + id);
        try {
            if (id == null) {
                LogManager.logWarning("Tentative de recherche avec un ID null");
                throw new ValidationException("L'ID ne peut pas être null");
            }
            Optional<Client> result = clientDAO.findById(id);
            LogManager.logInfo("Résultat de la recherche: " + (result.isPresent() ? "Client trouvé" : "Client non trouvé"));
            return result;
        } catch (ValidationException e) {
            LogManager.logWarning("Erreur de validation lors de la recherche du client: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche du client", e);
            throw new DatabaseException("Erreur lors de la recherche du client", e);
        }
    }

    public List<Client> findAll() throws DatabaseException {
        LogManager.logInfo("Récupération de tous les clients");
        try {
            List<Client> clients = clientDAO.findAll();
            LogManager.logInfo("Nombre de clients trouvés: " + clients.size());
            return clients;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la récupération des clients", e);
            throw new DatabaseException("Erreur lors de la récupération des clients", e);
        }
    }

    public Client create(Client client) throws ValidationException, DatabaseException {
        LogManager.logInfo("Création d'un nouveau client: " + client);
        try {
            if (client == null) {
                LogManager.logWarning("Tentative de création d'un client null");
                throw new ValidationException("Le client ne peut pas être null");
            }
            if (client.getAdresse() != null) {
                LogManager.logInfo("Sauvegarde de l'adresse du client");
                adresseDAO.save(client.getAdresse());
            }
            clientDAO.save(client);
            LogManager.logInfo("Client créé avec succès: " + client.getIdentifiant());
            return client;
        } catch (ValidationException e) {
            LogManager.logWarning("Erreur de validation lors de la création du client: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la création du client", e);
            throw new DatabaseException("Erreur lors de la création du client", e);
        }
    }

    public Client update(Client client) throws ValidationException, ResourceNotFoundException, DatabaseException {
        LogManager.logInfo("Mise à jour du client: " + client);
        try {
            if (client == null) {
                LogManager.logWarning("Tentative de mise à jour d'un client null");
                throw new ValidationException("Le client ne peut pas être null");
            }
            if (client.getIdentifiant() == null) {
                LogManager.logWarning("Tentative de mise à jour d'un client sans ID");
                throw new ValidationException("L'ID du client ne peut pas être null");
            }
            
            LogManager.logInfo("Recherche du client existant avec l'ID: " + client.getIdentifiant());
            Client existingClient = clientDAO.findById(client.getIdentifiant())
                .orElseThrow(() -> {
                    LogManager.logWarning("Client non trouvé avec l'ID: " + client.getIdentifiant());
                    return new ResourceNotFoundException("Client non trouvé avec l'ID: " + client.getIdentifiant());
                });
            
            if (client.getAdresse() != null) {
                if (client.getAdresse().getIdentifiant() == 0) {
                    LogManager.logInfo("Création d'une nouvelle adresse pour le client");
                    adresseDAO.save(client.getAdresse());
                } else {
                    LogManager.logInfo("Mise à jour de l'adresse existante du client");
                    adresseDAO.update(client.getAdresse());
                }
            }
            
            LogManager.logInfo("Mise à jour du client dans la base de données");
            clientDAO.update(client);
            LogManager.logInfo("Client mis à jour avec succès: " + client.getIdentifiant());
            return client;
        } catch (ValidationException | ResourceNotFoundException e) {
            LogManager.logWarning("Erreur lors de la mise à jour du client: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la mise à jour du client", e);
            throw new DatabaseException("Erreur lors de la mise à jour du client", e);
        }
    }

    public void delete(Long id) throws ValidationException, ResourceNotFoundException, DatabaseException {
        LogManager.logInfo("Suppression du client avec l'ID: " + id);
        try {
            if (id == null) {
                LogManager.logWarning("Tentative de suppression avec un ID null");
                throw new ValidationException("L'ID ne peut pas être null");
            }
            
            LogManager.logInfo("Recherche du client à supprimer");
            Client client = clientDAO.findById(id)
                .orElseThrow(() -> {
                    LogManager.logWarning("Client non trouvé avec l'ID: " + id);
                    return new ResourceNotFoundException("Client non trouvé avec l'ID: " + id);
                });
            
            if (client.getAdresse() != null) {
                LogManager.logInfo("Suppression de l'adresse associée au client");
                clientDAO.delete(client);
            }
            
            LogManager.logInfo("Suppression du client de la base de données");
            clientDAO.delete(client);
            LogManager.logInfo("Client supprimé avec succès: " + id);
        } catch (ValidationException | ResourceNotFoundException e) {
            LogManager.logWarning("Erreur lors de la suppression du client: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la suppression du client", e);
            throw new DatabaseException("Erreur lors de la suppression du client", e);
        }
    }

    public Optional<Client> findByEmail(String email) throws ValidationException, DatabaseException {
        LogManager.logInfo("Recherche du client avec l'email: " + email);
        try {
            if (email == null || email.trim().isEmpty()) {
                LogManager.logWarning("Tentative de recherche avec un email vide");
                throw new ValidationException("L'email ne peut pas être vide");
            }
            Optional<Client> result = clientDAO.findAll().stream()
                .filter(client -> email.equals(client.getEmail()))
                .findFirst();
            LogManager.logInfo("Résultat de la recherche par email: " + (result.isPresent() ? "Client trouvé" : "Client non trouvé"));
            return result;
        } catch (ValidationException e) {
            LogManager.logWarning("Erreur de validation lors de la recherche par email: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche par email", e);
            throw new DatabaseException("Erreur lors de la recherche par email", e);
        }
    }

    public List<Client> findByRaisonSociale(String raisonSociale) throws DatabaseException {
        LogManager.logInfo("Recherche des clients avec la raison sociale: " + raisonSociale);
        try {
            List<Client> clients = clientDAO.findAll().stream()
                .filter(client -> raisonSociale.equals(client.getRaisonSociale()))
                .toList();
            LogManager.logInfo("Nombre de clients trouvés: " + clients.size());
            return clients;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche des clients par raison sociale", e);
            throw new DatabaseException("Erreur lors de la recherche des clients par raison sociale", e);
        }
    }

    public boolean existsByRaisonSociale(String raisonSociale) throws DatabaseException {
        LogManager.logInfo("Vérification de l'existence d'un client avec la raison sociale: " + raisonSociale);
        try {
            boolean exists = clientDAO.findAll().stream()
                .anyMatch(client -> raisonSociale.equals(client.getRaisonSociale()));
            LogManager.logInfo("Client " + (exists ? "existe" : "n'existe pas") + " avec cette raison sociale");
            return exists;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la vérification de l'existence du client", e);
            throw new DatabaseException("Erreur lors de la vérification de l'existence du client", e);
        }
    }
} 