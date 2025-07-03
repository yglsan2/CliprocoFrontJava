package dao.jpa;

import models.Client;
import utilities.LogManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import exceptions.BusinessException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implémentation JPA du DAO pour les clients.
 */
public class ClientJpaDAO extends GenericJpaDAO<Client, Integer> {
    private static final Logger logger = LoggerFactory.getLogger(ClientJpaDAO.class);
    private final EntityManager entityManager;

    public ClientJpaDAO() {
        super();
        this.entityManager = DatabaseConnection.getEntityManager();
        logger.info("ClientJpaDAO initialisé avec l'EntityManager par défaut");
    }

    public ClientJpaDAO(EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
        logger.info("ClientJpaDAO initialisé avec un EntityManager personnalisé");
    }

    @Override
    public Optional<Client> findById(Integer id) throws ValidationException, DatabaseException {
        logger.debug("Recherche du client avec l'ID: {}", id);
        try {
            Client client = entityManager.find(Client.class, id);
            return Optional.ofNullable(client);
        } catch (IllegalArgumentException e) {
            LogManager.logWarning("ID invalide pour la recherche du client: " + id);
            throw new ValidationException("ID invalide", e);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche du client avec l'ID: {}", id, e);
            throw new DatabaseException("Erreur lors de la recherche du client", e);
        }
    }

    @Override
    public List<Client> findAll() throws DatabaseException {
        logger.debug("Récupération de tous les clients");
        try {
            logger.info("Création de la requête JPQL...");
            // Utiliser une requête JPQL simple
            TypedQuery<Client> query = entityManager.createQuery(
                "SELECT c FROM Client c", 
                Client.class
            );
            logger.info("Exécution de la requête JPQL...");
            List<Client> result = query.getResultList();
            logger.info("Résultat obtenu: " + result.size() + " clients");
            return result;
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de tous les clients", e);
            throw new DatabaseException("Erreur lors de la récupération des clients", e);
        }
    }

    @Override
    public Client save(Client client) throws ValidationException, DatabaseException {
        logger.debug("Sauvegarde d'un nouveau client");
        try {
            if (client == null) {
                throw new ValidationException("Le client ne peut pas être null");
            }
            entityManager.getTransaction().begin();
            entityManager.persist(client);
            entityManager.getTransaction().commit();
            logger.info("Nouveau client sauvegardé avec succès");
            return client;
        } catch (ValidationException e) {
            LogManager.logWarning("Erreur de validation lors de la sauvegarde du client: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            logger.error("Erreur lors de la sauvegarde du client", e);
            throw new DatabaseException("Erreur lors de la sauvegarde du client", e);
        }
    }

    @Override
    public Client update(Client client) throws ValidationException, ResourceNotFoundException, DatabaseException {
        logger.debug("Mise à jour du client avec l'ID: {}", client.getIdentifiant());
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
            LogManager.logWarning("Erreur lors de la mise à jour du client: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            logger.error("Erreur lors de la mise à jour du client", e);
            throw new DatabaseException("Erreur lors de la mise à jour du client", e);
        }
    }

    @Override
    public void delete(Client client) throws ValidationException, ResourceNotFoundException, DatabaseException {
        logger.debug("Suppression du client avec l'ID: {}", client.getIdentifiant());
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
            LogManager.logWarning("Erreur lors de la suppression du client: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            logger.error("Erreur lors de la suppression du client", e);
            throw new DatabaseException("Erreur lors de la suppression du client", e);
        }
    }

    @Override
    public boolean existsById(Integer id) throws ValidationException, DatabaseException {
        logger.debug("Vérification de l'existence du client avec l'ID: {}", id);
        try {
            if (id == null) {
                throw new ValidationException("L'ID ne peut pas être null");
            }
            TypedQuery<Integer> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM Client c WHERE c.id = :id",
                Integer.class
            );
            query.setParameter("id", id);
            return query.getSingleResult() > 0;
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Erreur lors de la vérification de l'existence du client avec l'ID: {}", id, e);
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
            LogManager.logException("Erreur lors de la fermeture des ressources", e);
            throw new DatabaseException("Erreur lors de la fermeture des ressources", e);
        }
    }

    public List<Client> findByRaisonSociale(String raisonSociale) throws ValidationException, DatabaseException {
        logger.debug("Recherche des clients avec la raison sociale: {}", raisonSociale);
        try {
            if (raisonSociale == null) {
                throw new ValidationException("La raison sociale ne peut pas être null");
            }
            TypedQuery<Client> query = entityManager.createQuery(
                "SELECT c FROM Client c WHERE c.raisonSociale LIKE :raisonSociale",
                Client.class
            );
            query.setParameter("raisonSociale", "%" + raisonSociale + "%");
            return query.getResultList();
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche des clients par raison sociale: {}", raisonSociale, e);
            throw new DatabaseException("Erreur lors de la recherche des clients par raison sociale", e);
        }
    }
    
    public boolean existsByRaisonSociale(String raisonSociale) throws ValidationException, DatabaseException {
        logger.debug("Vérification de l'existence des clients avec la raison sociale: {}", raisonSociale);
        try {
            if (raisonSociale == null) {
                throw new ValidationException("La raison sociale ne peut pas être null");
            }
            TypedQuery<Integer> query = entityManager.createQuery(
                "SELECT COUNT(c) FROM Client c WHERE c.raisonSociale = :raisonSociale",
                Integer.class
            );
            query.setParameter("raisonSociale", raisonSociale);
            return query.getSingleResult() > 0;
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Erreur lors de la vérification de l'existence des clients avec la raison sociale: {}", raisonSociale, e);
            throw new DatabaseException("Erreur lors de la vérification de l'existence des clients", e);
        }
    }
} 