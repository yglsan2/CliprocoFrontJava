package dao.jpa;

import models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;
import dao.IDAO;
import dao.SocieteDatabaseException;
import dao.UserDAO;

public class UserJpaDAO extends GenericJpaDAO<User> implements IDAO<User>, UserDAO {
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public UserJpaDAO() {
        this.emf = Persistence.createEntityManagerFactory("cliproco");
        this.em = emf.createEntityManager();
    }

    @Override
    public User findById(Long id) throws SocieteDatabaseException {
        try {
            return em.find(User.class, id);
        } catch (Exception e) {
            throw new SocieteDatabaseException("Erreur lors de la recherche de l'utilisateur par ID", e);
        }
    }

    @Override
    public List<User> findAll() throws SocieteDatabaseException {
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new SocieteDatabaseException("Erreur lors de la récupération de tous les utilisateurs", e);
        }
    }

    @Override
    public void save(User user) throws SocieteDatabaseException {
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SocieteDatabaseException("Erreur lors de la sauvegarde de l'utilisateur", e);
        }
    }

    @Override
    public void update(User user) throws SocieteDatabaseException {
        try {
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SocieteDatabaseException("Erreur lors de la mise à jour de l'utilisateur", e);
        }
    }

    @Override
    public void delete(User user) throws SocieteDatabaseException {
        try {
            em.getTransaction().begin();
            em.remove(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SocieteDatabaseException("Erreur lors de la suppression de l'utilisateur", e);
        }
    }

    public User findByUsername(String username) throws SocieteDatabaseException {
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new SocieteDatabaseException("Erreur lors de la recherche de l'utilisateur par nom d'utilisateur", e);
        }
    }

    public User findByToken(String token) throws SocieteDatabaseException {
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.token = :token", User.class);
            query.setParameter("token", token);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new SocieteDatabaseException("Erreur lors de la recherche de l'utilisateur par token", e);
        }
    }

    public void close() {
        em.close();
        emf.close();
    }
} 