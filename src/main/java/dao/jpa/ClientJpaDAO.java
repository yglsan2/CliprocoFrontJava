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

/**
 * Implémentation JPA du DAO pour les clients.
 */
public class ClientJpaDAO extends GenericJpaDAO<Client, Long> {
    
    public ClientJpaDAO() {
        super();
    }

    @Override
    public Optional<Client> findById(Long id) throws ValidationException, DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Client client = em.find(Client.class, id);
            return Optional.ofNullable(client);
        } catch (IllegalArgumentException e) {
            LogManager.logWarning("ID invalide pour la recherche du client: " + id);
            throw new ValidationException("ID invalide", e);
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche du client par ID", e);
            throw new DatabaseException("Erreur lors de la recherche du client", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Client> findAll() throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Client> query = em.createQuery("SELECT c FROM Client c", Client.class);
            return query.getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la récupération des clients", e);
            throw new DatabaseException("Erreur lors de la récupération des clients", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Client save(Client client) throws ValidationException, DatabaseException {
        EntityManager em = null;
        try {
            if (client == null) {
                throw new ValidationException("Le client ne peut pas être null");
            }
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(client);
            em.getTransaction().commit();
            return client;
        } catch (ValidationException e) {
            LogManager.logWarning("Erreur de validation lors de la sauvegarde du client: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la sauvegarde du client", e);
            throw new DatabaseException("Erreur lors de la sauvegarde du client", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Client update(Client client) throws ValidationException, ResourceNotFoundException, DatabaseException {
        EntityManager em = null;
        try {
            if (client == null) {
                throw new ValidationException("Le client ne peut pas être null");
            }
            em = getEntityManager();
            em.getTransaction().begin();
            Client existingClient = em.find(Client.class, client.getIdentifiant());
            if (existingClient == null) {
                throw new ResourceNotFoundException("Client non trouvé avec l'ID: " + client.getIdentifiant());
            }
            Client updatedClient = em.merge(client);
            em.getTransaction().commit();
            return updatedClient;
        } catch (ValidationException | ResourceNotFoundException e) {
            LogManager.logWarning("Erreur lors de la mise à jour du client: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la mise à jour du client", e);
            throw new DatabaseException("Erreur lors de la mise à jour du client", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void delete(Client client) throws ValidationException, ResourceNotFoundException, DatabaseException {
        EntityManager em = null;
        try {
            if (client == null) {
                throw new ValidationException("Le client ne peut pas être null");
            }
            em = getEntityManager();
            em.getTransaction().begin();
            Client existingClient = em.find(Client.class, client.getIdentifiant());
            if (existingClient == null) {
                throw new ResourceNotFoundException("Client non trouvé avec l'ID: " + client.getIdentifiant());
            }
            em.remove(existingClient);
            em.getTransaction().commit();
        } catch (ValidationException | ResourceNotFoundException e) {
            LogManager.logWarning("Erreur lors de la suppression du client: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la suppression du client", e);
            throw new DatabaseException("Erreur lors de la suppression du client", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void close() throws DatabaseException {
        try {
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la fermeture des ressources", e);
            throw new DatabaseException("Erreur lors de la fermeture des ressources", e);
        }
    }

    public List<Client> findByRaisonSociale(String raisonSociale) throws ValidationException, DatabaseException {
        EntityManager em = null;
        try {
            if (raisonSociale == null) {
                throw new ValidationException("La raison sociale ne peut pas être null");
            }
            em = getEntityManager();
            TypedQuery<Client> query = em.createQuery(
                "SELECT c FROM Client c WHERE c.raisonSociale LIKE :raisonSociale",
                Client.class
            );
            query.setParameter("raisonSociale", "%" + raisonSociale + "%");
            return query.getResultList();
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche par raison sociale", e);
            throw new DatabaseException("Erreur lors de la recherche par raison sociale", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public boolean existsByRaisonSociale(String raisonSociale) throws ValidationException, DatabaseException {
        EntityManager em = null;
        try {
            if (raisonSociale == null) {
                throw new ValidationException("La raison sociale ne peut pas être null");
            }
            em = getEntityManager();
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(c) FROM Client c WHERE c.raisonSociale = :raisonSociale",
                Long.class
            );
            query.setParameter("raisonSociale", raisonSociale);
            return query.getSingleResult() > 0;
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la vérification de l'existence", e);
            throw new DatabaseException("Erreur lors de la vérification de l'existence", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
} 