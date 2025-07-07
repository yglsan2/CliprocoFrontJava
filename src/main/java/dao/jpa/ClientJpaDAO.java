package dao.jpa;

import models.Client;
import dao.IDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import routers.FrontController;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Implémentation JPA du DAO pour les clients.
 */
public class ClientJpaDAO implements IDAO<Client, Integer> {
    private static final Logger logger = Logger.getLogger(ClientJpaDAO.class.getName());
    private final EntityManager entityManager;

    public ClientJpaDAO() {
        this.entityManager = FrontController.getEntityManager();
        logger.info("ClientJpaDAO initialisé");
    }

    public ClientJpaDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
        logger.info("ClientJpaDAO initialisé avec un EntityManager personnalisé");
    }

    @Override
    public Optional<Client> findById(Integer id) throws ValidationException, DatabaseException {
        logger.info("Recherche du client avec l'ID: " + id);
        try {
            if (id == null) {
                throw new ValidationException("L'ID ne peut pas être null");
            }
            Client client = entityManager.find(Client.class, id);
            return Optional.ofNullable(client);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            logger.severe("Erreur lors de la recherche du client avec l'ID: " + id + " - " + e.getMessage());
            throw new DatabaseException("Erreur lors de la recherche du client", e);
        }
    }

    @Override
    public List<Client> findAll() throws DatabaseException {
        logger.info("Récupération de tous les clients");
        try {
            TypedQuery<Client> query = entityManager.createQuery(
                "SELECT c FROM Client c", 
                Client.class
            );
            List<Client> result = query.getResultList();
            logger.info("Résultat obtenu: " + result.size() + " clients");
            return result;
        } catch (Exception e) {
            logger.severe("Erreur lors de la récupération de tous les clients: " + e.getMessage());
            throw new DatabaseException("Erreur lors de la récupération des clients", e);
        }
    }

    @Override
    public Client save(Client client) throws ValidationException, DatabaseException {
        logger.info("Sauvegarde d'un nouveau client");
        try {
            if (client == null) {
                throw new ValidationException("Le client ne peut pas être null");
            }
            
            entityManager.getTransaction().begin();
            
            // Persister l'adresse en premier si elle n'a pas d'ID
            if (client.getAdresse() != null && client.getAdresse().getIdentifiant() == null) {
                entityManager.persist(client.getAdresse());
                logger.info("Adresse persistée avec l'ID: " + client.getAdresse().getIdentifiant());
            }
            
            // Persister le client
            entityManager.persist(client);
            entityManager.getTransaction().commit();
            logger.info("Nouveau client sauvegardé avec succès");
            return client;
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            logger.severe("Erreur lors de la sauvegarde du client: " + e.getMessage());
            throw new DatabaseException("Erreur lors de la sauvegarde du client", e);
        }
    }

    @Override
    public Client update(Client client) throws ValidationException, ResourceNotFoundException, DatabaseException {
        logger.info("Mise à jour du client avec l'ID: " + client.getIdentifiant());
        try {
            if (client == null) {
                throw new ValidationException("Le client ne peut pas être null");
            }
            entityManager.getTransaction().begin();
            Client existingClient = entityManager.find(Client.class, client.getIdentifiant());
            if (existingClient == null) {
                throw new ResourceNotFoundException("Client non trouvé avec l'ID: " + client.getIdentifiant());
            }
            Client updatedClient = entityManager.merge(client);
            entityManager.getTransaction().commit();
            logger.info("Client mis à jour avec succès");
            return updatedClient;
        } catch (ValidationException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            logger.severe("Erreur lors de la mise à jour du client: " + e.getMessage());
            throw new DatabaseException("Erreur lors de la mise à jour du client", e);
        }
    }

    @Override
    public void delete(Client client) throws ValidationException, ResourceNotFoundException, DatabaseException {
        logger.info("Suppression du client avec l'ID: " + client.getIdentifiant());
        try {
            if (client == null) {
                throw new ValidationException("Le client ne peut pas être null");
            }
            entityManager.getTransaction().begin();
            Client existingClient = entityManager.find(Client.class, client.getIdentifiant());
            if (existingClient == null) {
                throw new ResourceNotFoundException("Client non trouvé avec l'ID: " + client.getIdentifiant());
            }
            entityManager.remove(existingClient);
            entityManager.getTransaction().commit();
            logger.info("Client supprimé avec succès");
        } catch (ValidationException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            logger.severe("Erreur lors de la suppression du client: " + e.getMessage());
            throw new DatabaseException("Erreur lors de la suppression du client", e);
        }
    }

    @Override
    public boolean existsById(Integer id) throws ValidationException, DatabaseException {
        logger.info("Vérification de l'existence du client avec l'ID: " + id);
        try {
            if (id == null) {
                throw new ValidationException("L'ID ne peut pas être null");
            }
            Client client = entityManager.find(Client.class, id);
            return client != null;
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            logger.severe("Erreur lors de la vérification de l'existence du client: " + e.getMessage());
            throw new DatabaseException("Erreur lors de la vérification de l'existence du client", e);
        }
    }

    @Override
    public void close() throws DatabaseException {
        try {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        } catch (Exception e) {
            logger.severe("Erreur lors de la fermeture des ressources: " + e.getMessage());
            throw new DatabaseException("Erreur lors de la fermeture des ressources", e);
        }
    }
} 