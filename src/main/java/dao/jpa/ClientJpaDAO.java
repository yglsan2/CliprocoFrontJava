package dao.jpa;

import dao.IDAO;
import exceptions.DatabaseException;
import models.Client;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import utilities.LogManager;

public class ClientJpaDAO extends AbstractJpaDAO<Client> implements IDAO<Client> {
    
    public ClientJpaDAO() {
        super(Client.class);
    }

    @Override
    public List<Client> findAll() throws DatabaseException {
        try {
            return super.findAll();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la récupération des clients", e);
            throw new DatabaseException("Erreur lors de la récupération des clients", e);
        }
    }

    @Override
    public void save(Client client) throws DatabaseException {
        try {
            if (client.getIdentifiant() == null) {
                create(client);
                LogManager.info("Nouveau client créé: " + client.getRaisonSociale());
            } else {
                update(client);
                LogManager.info("Client mis à jour: " + client.getRaisonSociale());
            }
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la sauvegarde du client", e);
            throw new DatabaseException("Erreur lors de la sauvegarde du client", e);
        }
    }

    @Override
    public Client findById(Long id) throws DatabaseException {
        try {
            Optional<Client> client = super.findById(id);
            return client.orElse(null);
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche du client", e);
            throw new DatabaseException("Erreur lors de la recherche du client", e);
        }
    }

    @Override
    public void update(Client client) throws DatabaseException {
        try {
            super.update(client);
            LogManager.info("Client mis à jour: " + client.getRaisonSociale());
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la mise à jour du client", e);
            throw new DatabaseException("Erreur lors de la mise à jour du client", e);
        }
    }

    @Override
    public void delete(Client client) throws DatabaseException {
        try {
            super.delete(client);
            LogManager.info("Client supprimé: " + client.getRaisonSociale());
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la suppression du client", e);
            throw new DatabaseException("Erreur lors de la suppression du client", e);
        }
    }

    public List<Client> findByChiffreAffairesMin(Double minChiffreAffaires) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String jpql = "SELECT c FROM Client c WHERE c.chiffreAffaires >= :minCA";
            TypedQuery<Client> query = em.createQuery(jpql, Client.class);
            query.setParameter("minCA", minChiffreAffaires);
            return query.getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche par chiffre d'affaires", e);
            throw new DatabaseException("Erreur lors de la recherche par chiffre d'affaires", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public List<Client> findByNombreEmployesMin(Integer minEmployes) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String jpql = "SELECT c FROM Client c WHERE c.nombreEmployes >= :minEmp";
            TypedQuery<Client> query = em.createQuery(jpql, Client.class);
            query.setParameter("minEmp", minEmployes);
            return query.getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche par nombre d'employés", e);
            throw new DatabaseException("Erreur lors de la recherche par nombre d'employés", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public Optional<Client> findByEmail(String email) throws DatabaseException {
        try {
            beginTransaction();
            List<Client> clients = em.createQuery("SELECT c FROM Client c WHERE c.email = :email", Client.class)
                    .setParameter("email", email)
                    .getResultList();
            commitTransaction();
            return clients.isEmpty() ? Optional.empty() : Optional.of(clients.get(0));
        } catch (Exception e) {
            rollbackTransaction();
            LogManager.logException("Erreur lors de la recherche par email", e);
            throw new DatabaseException("Erreur lors de la recherche par email", e);
        }
    }

    public List<Client> findBySociete(Long societeId) throws DatabaseException {
        try {
            beginTransaction();
            List<Client> clients = em.createQuery("SELECT c FROM Client c WHERE c.societe.id = :societeId", Client.class)
                    .setParameter("societeId", societeId)
                    .getResultList();
            commitTransaction();
            return clients;
        } catch (Exception e) {
            rollbackTransaction();
            LogManager.logException("Erreur lors de la recherche par société", e);
            throw new DatabaseException("Erreur lors de la recherche par société", e);
        }
    }

    public List<Client> findByNom(String nom) throws DatabaseException {
        try {
            beginTransaction();
            List<Client> clients = em.createQuery("SELECT c FROM Client c WHERE c.nom LIKE :nom", Client.class)
                    .setParameter("nom", "%" + nom + "%")
                    .getResultList();
            commitTransaction();
            return clients;
        } catch (Exception e) {
            rollbackTransaction();
            LogManager.logException("Erreur lors de la recherche par nom", e);
            throw new DatabaseException("Erreur lors de la recherche par nom", e);
        }
    }

    public List<Client> findByPrenom(String prenom) throws DatabaseException {
        try {
            beginTransaction();
            List<Client> clients = em.createQuery("SELECT c FROM Client c WHERE c.prenom LIKE :prenom", Client.class)
                    .setParameter("prenom", "%" + prenom + "%")
                    .getResultList();
            commitTransaction();
            return clients;
        } catch (Exception e) {
            rollbackTransaction();
            LogManager.logException("Erreur lors de la recherche par prénom", e);
            throw new DatabaseException("Erreur lors de la recherche par prénom", e);
        }
    }

    public List<Client> findByNomAndPrenom(String nom, String prenom) throws DatabaseException {
        try {
            beginTransaction();
            List<Client> clients = em.createQuery("SELECT c FROM Client c WHERE c.nom LIKE :nom AND c.prenom LIKE :prenom", Client.class)
                    .setParameter("nom", "%" + nom + "%")
                    .setParameter("prenom", "%" + prenom + "%")
                    .getResultList();
            commitTransaction();
            return clients;
        } catch (Exception e) {
            rollbackTransaction();
            LogManager.logException("Erreur lors de la recherche par nom et prénom", e);
            throw new DatabaseException("Erreur lors de la recherche par nom et prénom", e);
        }
    }
} 