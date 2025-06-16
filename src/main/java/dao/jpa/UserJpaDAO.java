package dao.jpa;

import dao.IDAO;
import models.User;
import utilities.LogManager;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import dao.UserDAO;
import exceptions.DatabaseException;
import jakarta.persistence.NoResultException;

/**
 * Implémentation JPA du DAO pour les utilisateurs.
 */
public class UserJpaDAO extends AbstractJpaDAO<User, Long> implements IDAO<User, Long> {
    
    public UserJpaDAO() {
        super(User.class);
    }

    @Override
    public Optional<User> findById(Long id) {
        return super.findById(id);
    }

    @Override
    public User save(User entity) {
        return super.save(entity);
    }

    @Override
    public User update(User entity) {
        return super.update(entity);
    }

    @Override
    public void delete(User entity) {
        super.delete(entity);
    }

    public Optional<User> findByEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche par email", e);
            throw new RuntimeException("Erreur lors de la recherche par email", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public Optional<User> findByUsername(String username) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche par nom d'utilisateur", e);
            throw new RuntimeException("Erreur lors de la recherche par nom d'utilisateur", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public Optional<User> findByToken(String token) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.token = :token", User.class);
            query.setParameter("token", token);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche par token", e);
            throw new RuntimeException("Erreur lors de la recherche par token", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public boolean existsByEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class);
            query.setParameter("email", email);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la vérification de l'existence", e);
            throw new RuntimeException("Erreur lors de la vérification de l'existence", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
} 