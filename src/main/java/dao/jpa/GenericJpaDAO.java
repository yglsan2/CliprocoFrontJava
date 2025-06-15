package dao.jpa;

import dao.IDAO;
import exceptions.DatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import utilities.LogManager;
import java.util.List;

/**
 * Classe générique pour les opérations JPA.
 * @param <T> Type de l'entité
 */
public abstract class GenericJpaDAO<T> implements IDAO<T> {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("cliproco");
    private final Class<T> entityClass;
    
    protected GenericJpaDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    protected EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    protected Class<T> getEntityClass() {
        return entityClass;
    }
    
    @Override
    public T findById(Long id) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(entityClass, id);
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche par ID", e);
            throw new DatabaseException("Erreur lors de la recherche par ID", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    @Override
    public List<T> findAll() throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<T> query = em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass);
            return query.getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la récupération de toutes les entités", e);
            throw new DatabaseException("Erreur lors de la récupération de toutes les entités", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    @Override
    public void save(T entity) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la sauvegarde", e);
            throw new DatabaseException("Erreur lors de la sauvegarde", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    @Override
    public void update(T entity) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la mise à jour", e);
            throw new DatabaseException("Erreur lors de la mise à jour", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    @Override
    public void delete(T entity) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.remove(em.merge(entity));
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la suppression", e);
            throw new DatabaseException("Erreur lors de la suppression", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
} 