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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dao.jpa.ClientJpaDAO;

/**
 * Service pour la gestion des clients.
 */
public class ClientService {
    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);
    private final IDAO<Client, Integer> clientDAO;
    private final IDAO<Adresse, Integer> adresseDAO;

    public ClientService() {
        this.clientDAO = new ClientJpaDAO();
        logger.info("ClientService initialisé avec le DAO par défaut");
        this.adresseDAO = null; // Assuming adresseDAO is not used in the default constructor
    }

    public ClientService(IDAO<Client, Integer> clientDAO, IDAO<Adresse, Integer> adresseDAO) {
        this.clientDAO = clientDAO;
        this.adresseDAO = adresseDAO;
        logger.info("ClientService initialisé avec un DAO personnalisé");
    }

    public Optional<Client> findById(Integer id) throws ValidationException, ResourceNotFoundException, DatabaseException {
        logger.debug("Recherche du client avec l'ID: {}", id);
        try {
            if (id == null) {
                logger.warn("Tentative de recherche avec un ID null");
                throw new ValidationException("L'ID ne peut pas être null");
            }
            Optional<Client> result = clientDAO.findById(id);
            if (result.isPresent()) {
                logger.info("Client trouvé");
                return result;
            }
            logger.warn("Client non trouvé avec l'ID: {}", id);
            throw new ResourceNotFoundException("Client non trouvé avec l'ID: " + id);
        } catch (ValidationException | ResourceNotFoundException e) {
            logger.warn("Erreur lors de la recherche du client: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche du client", e);
            throw new DatabaseException("Erreur lors de la recherche du client", e);
        }
    }

    public List<Client> findAll() throws DatabaseException {
        logger.debug("Récupération de tous les clients");
        try {
            List<Client> clients = clientDAO.findAll();
            logger.info("Nombre de clients trouvés: " + clients.size());
            return clients;
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des clients", e);
            throw new DatabaseException("Erreur lors de la récupération des clients", e);
        }
    }

    public Client create(Client client) throws ValidationException, DatabaseException {
        logger.debug("Création d'un nouveau client: {}", client);
        try {
            if (client == null) {
                logger.warn("Tentative de création d'un client null");
                throw new ValidationException("Le client ne peut pas être null");
            }
            if (client.getAdresse() != null) {
                logger.info("Sauvegarde de l'adresse du client");
                adresseDAO.save(client.getAdresse());
            }
            clientDAO.save(client);
            logger.info("Client créé avec succès: {}", client.getIdentifiant());
            return client;
        } catch (ValidationException e) {
            logger.warn("Erreur de validation lors de la création du client: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erreur lors de la création du client", e);
            throw new DatabaseException("Erreur lors de la création du client", e);
        }
    }

    public Client update(Client client) throws ValidationException, ResourceNotFoundException, DatabaseException {
        logger.debug("Mise à jour du client avec l'ID: {}", client.getIdentifiant());
        try {
            if (client == null) {
                logger.warn("Tentative de mise à jour d'un client null");
                throw new ValidationException("Le client ne peut pas être null");
            }
            if (client.getIdentifiant() == null) {
                logger.warn("Tentative de mise à jour d'un client sans ID");
                throw new ValidationException("L'ID du client ne peut pas être null");
            }
            
            logger.info("Recherche du client existant avec l'ID: {}", client.getIdentifiant());
            Client existingClient = clientDAO.findById(client.getIdentifiant())
                .orElseThrow(() -> {
                    logger.warn("Client non trouvé avec l'ID: {}", client.getIdentifiant());
                    return new ResourceNotFoundException("Client non trouvé avec l'ID: " + client.getIdentifiant());
                });
            
            if (client.getAdresse() != null) {
                if (client.getAdresse().getIdentifiant() == 0) {
                    logger.info("Création d'une nouvelle adresse pour le client");
                    adresseDAO.save(client.getAdresse());
                } else {
                    logger.info("Mise à jour de l'adresse existante du client");
                    adresseDAO.update(client.getAdresse());
                }
            }
            
            logger.info("Mise à jour du client dans la base de données");
            clientDAO.update(client);
            logger.info("Client mis à jour avec succès: {}", client.getIdentifiant());
            return client;
        } catch (ValidationException | ResourceNotFoundException e) {
            logger.warn("Erreur lors de la mise à jour du client: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du client", e);
            throw new DatabaseException("Erreur lors de la mise à jour du client", e);
        }
    }

    public void delete(Integer id) throws ValidationException, ResourceNotFoundException, DatabaseException {
        logger.debug("Suppression du client avec l'ID: {}", id);
        try {
            if (id == null) {
                logger.warn("Tentative de suppression avec un ID null");
                throw new ValidationException("L'ID ne peut pas être null");
            }
            
            logger.info("Recherche du client à supprimer");
            Client client = clientDAO.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Client non trouvé avec l'ID: {}", id);
                    return new ResourceNotFoundException("Client non trouvé avec l'ID: " + id);
                });
            
            if (client.getAdresse() != null) {
                logger.info("Suppression de l'adresse associée au client");
                adresseDAO.delete(client.getAdresse());
            }
            
            logger.info("Suppression du client de la base de données");
            clientDAO.delete(client);
            logger.info("Client supprimé avec succès: {}", id);
        } catch (ValidationException | ResourceNotFoundException e) {
            logger.warn("Erreur lors de la suppression du client: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression du client", e);
            throw new DatabaseException("Erreur lors de la suppression du client", e);
        }
    }

    public Optional<Client> findByEmail(String email) throws ValidationException, DatabaseException {
        logger.debug("Recherche du client avec l'email: {}", email);
        try {
            if (email == null || email.trim().isEmpty()) {
                logger.warn("Tentative de recherche avec un email vide");
                throw new ValidationException("L'email ne peut pas être vide");
            }
            Optional<Client> result = clientDAO.findAll().stream()
                .filter(client -> email.equals(client.getMail()))
                .findFirst();
            logger.info("Résultat de la recherche par email: {}", (result.isPresent() ? "Client trouvé" : "Client non trouvé"));
            return result;
        } catch (ValidationException e) {
            logger.warn("Erreur de validation lors de la recherche par email: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche par email", e);
            throw new DatabaseException("Erreur lors de la recherche par email", e);
        }
    }

    public List<Client> findByRaisonSociale(String raisonSociale) throws DatabaseException {
        logger.debug("Recherche des clients avec la raison sociale: {}", raisonSociale);
        try {
            List<Client> clients = clientDAO.findAll().stream()
                .filter(client -> raisonSociale.equals(client.getRaisonSociale()))
                .toList();
            logger.info("Nombre de clients trouvés: {}", clients.size());
            return clients;
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche des clients par raison sociale", e);
            throw new DatabaseException("Erreur lors de la recherche des clients par raison sociale", e);
        }
    }

    public boolean existsByRaisonSociale(String raisonSociale) throws DatabaseException {
        logger.debug("Vérification de l'existence du client avec la raison sociale: {}", raisonSociale);
        try {
            boolean exists = clientDAO.findAll().stream()
                .anyMatch(client -> raisonSociale.equals(client.getRaisonSociale()));
            logger.info("Existence du client avec la raison sociale {}: {}", raisonSociale, exists);
            return exists;
        } catch (Exception e) {
            logger.error("Erreur lors de la vérification de l'existence du client", e);
            throw new DatabaseException("Erreur lors de la vérification de l'existence du client", e);
        }
    }

    /**
     * Recherche des clients par raison sociale (recherche partielle)
     */
    public List<Client> searchByRaisonSociale(String searchTerm) throws DatabaseException {
        logger.debug("Recherche des clients avec le terme: {}", searchTerm);
        try {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                logger.warn("Terme de recherche vide");
                return List.of();
            }
            
            String lowerSearchTerm = searchTerm.toLowerCase();
            List<Client> clients = clientDAO.findAll().stream()
                .filter(client -> client.getRaisonSociale() != null && 
                                client.getRaisonSociale().toLowerCase().contains(lowerSearchTerm))
                .toList();
            logger.info("Nombre de clients trouvés: {}", clients.size());
            return clients;
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche des clients", e);
            throw new DatabaseException("Erreur lors de la recherche des clients", e);
        }
    }

    /**
     * Valide les données métier d'un client
     */
    private void validateClient(Client client) throws ValidationException {
        if (client == null) {
            throw new ValidationException("Le client ne peut pas être null");
        }
        
        if (client.getChiffreAffaires() != null && client.getChiffreAffaires() < 0) {
            throw new ValidationException("Le chiffre d'affaires ne peut pas être négatif");
        }
        
        if (client.getNbEmployes() != null && client.getNbEmployes() < 0) {
            throw new ValidationException("Le nombre d'employés ne peut pas être négatif");
        }
        
        if (client.getMail() != null && !utilities.ValidationManager.isValidEmail(client.getMail())) {
            throw new ValidationException("Format d'email invalide");
        }
        
        if (client.getTelephone() != null && !utilities.ValidationManager.isValidPhone(client.getTelephone())) {
            throw new ValidationException("Format de téléphone invalide. Formats acceptés : 0612345678, +33612345678, 0033612345678");
        }
        
        if (client.getRaisonSociale() != null && !utilities.ValidationManager.isValidCompanyName(client.getRaisonSociale())) {
            throw new ValidationException("Format de raison sociale invalide");
        }
    }

    /**
     * Création avec validation métier
     */
    public Client createWithValidation(Client client) throws ValidationException, DatabaseException {
        logger.debug("Création d'un nouveau client avec validation: {}", client);
        try {
            validateClient(client);
            return create(client);
        } catch (ValidationException e) {
            logger.warn("Erreur de validation lors de la création du client: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erreur lors de la création du client", e);
            throw new DatabaseException("Erreur lors de la création du client", e);
        }
    }
} 