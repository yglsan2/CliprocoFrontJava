package dao.jpa;

import models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import dao.UserDAO;
import exceptions.DatabaseException;
import utilities.LogManager;
import jakarta.persistence.NoResultException;

public class UserJpaDAO extends GenericJpaDAO<User> implements UserDAO {
    public UserJpaDAO() {
        super(User.class);
    }

    @Override
    public User findByEmail(String email) throws DatabaseException {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche par email", e);
            throw new DatabaseException("Erreur lors de la recherche par email", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public User findByUsername(String username) throws DatabaseException {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche par nom d'utilisateur", e);
            throw new DatabaseException("Erreur lors de la recherche par nom d'utilisateur", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public User findByToken(String token) throws DatabaseException {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.token = :token", User.class);
            query.setParameter("token", token);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche par token", e);
            throw new DatabaseException("Erreur lors de la recherche par token", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
} 