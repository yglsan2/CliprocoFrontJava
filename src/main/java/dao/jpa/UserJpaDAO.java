package dao.jpa;

import dao.IDAO;
import exceptions.DatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;
import models.User;
import utilities.LogManager;

public class UserJpaDAO extends GenericJpaDAO<User> implements IDAO<User> {
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public UserJpaDAO() {
        this.emf = Persistence.createEntityManagerFactory("cliproco");
        this.em = emf.createEntityManager();
    }

    @Override
    public User findById(Long id) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(User.class, id);
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche de l'utilisateur par ID", e);
            throw new DatabaseException("Erreur lors de la recherche de l'utilisateur par ID", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<User> findAll() throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
            return query.getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la récupération de tous les utilisateurs", e);
            throw new DatabaseException("Erreur lors de la récupération de tous les utilisateurs", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public User save(User user) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            return user;
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la sauvegarde de l'utilisateur", e);
            throw new DatabaseException("Erreur lors de la sauvegarde de l'utilisateur", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public User update(User user) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User updatedUser = em.merge(user);
            em.getTransaction().commit();
            return updatedUser;
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la mise à jour de l'utilisateur", e);
            throw new DatabaseException("Erreur lors de la mise à jour de l'utilisateur", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void delete(User user) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.remove(em.contains(user) ? user : em.merge(user));
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la suppression de l'utilisateur", e);
            throw new DatabaseException("Erreur lors de la suppression de l'utilisateur", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public User findByEmail(String email) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.email = :email", 
                User.class
            );
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche de l'utilisateur par email", e);
            throw new DatabaseException("Erreur lors de la recherche de l'utilisateur par email", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public User findByUsername(String username) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche de l'utilisateur par nom d'utilisateur", e);
            throw new DatabaseException("Erreur lors de la recherche de l'utilisateur par nom d'utilisateur", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public User findByToken(String token) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.token = :token", User.class);
            query.setParameter("token", token);
            return query.getSingleResult();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche de l'utilisateur par token", e);
            throw new DatabaseException("Erreur lors de la recherche de l'utilisateur par token", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void close() {
        em.close();
        emf.close();
    }
} 