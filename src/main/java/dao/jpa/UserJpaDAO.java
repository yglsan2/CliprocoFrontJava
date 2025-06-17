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
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import jakarta.persistence.NoResultException;

/**
 * Implémentation JPA du DAO pour les utilisateurs.
 */
public class UserJpaDAO extends AbstractJpaDAO<User, Long> implements IDAO<User, Long> {
    
    public UserJpaDAO() {
        super(User.class);
    }

    @Override
    public Optional<User> findById(Long id) throws ValidationException, DatabaseException {
        return super.findById(id);
    }

    @Override
    public User save(User entity) throws ValidationException, DatabaseException {
        return super.save(entity);
    }

    @Override
    public User update(User entity) throws ValidationException, ResourceNotFoundException, DatabaseException {
        return super.update(entity);
    }

    @Override
    public void delete(User entity) throws ValidationException, ResourceNotFoundException, DatabaseException {
        super.delete(entity);
    }

    public Optional<User> findByEmail(String email) throws ValidationException, DatabaseException {
        EntityManager em = getEntityManager();
        try {
            if (email == null) {
                throw new ValidationException("L'email ne peut pas être null");
            }
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche par email", e);
            throw new DatabaseException("Erreur lors de la recherche par email", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public Optional<User> findByUsername(String username) throws ValidationException, DatabaseException {
        EntityManager em = getEntityManager();
        try {
            if (username == null) {
                throw new ValidationException("Le nom d'utilisateur ne peut pas être null");
            }
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche par nom d'utilisateur", e);
            throw new DatabaseException("Erreur lors de la recherche par nom d'utilisateur", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public Optional<User> findByToken(String token) throws ValidationException, DatabaseException {
        EntityManager em = getEntityManager();
        try {
            if (token == null) {
                throw new ValidationException("Le token ne peut pas être null");
            }
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.token = :token", User.class);
            query.setParameter("token", token);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche par token", e);
            throw new DatabaseException("Erreur lors de la recherche par token", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public boolean existsByEmail(String email) throws ValidationException, DatabaseException {
        EntityManager em = getEntityManager();
        try {
            if (email == null) {
                throw new ValidationException("L'email ne peut pas être null");
            }
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class);
            query.setParameter("email", email);
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