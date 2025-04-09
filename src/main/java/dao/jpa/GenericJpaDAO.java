package dao.jpa;

import dao.IDAO;
import dao.SocieteDatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import models.Societe;
import utilities.JPAUtil;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Classe générique pour les opérations JPA.
 * @param <T> Type de l'entité
 */
public abstract class GenericJpaDAO<T> implements IDAO<T> {
    
    protected final EntityManagerFactory emf;
    protected final Class<T> entityClass;
    
    @SuppressWarnings("unchecked")
    public GenericJpaDAO() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        this.emf = Persistence.createEntityManagerFactory("cliproco");
    }
    
    protected EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    @Override
    public T findById(Long id) throws SocieteDatabaseException {
        EntityManager em = getEntityManager();
        try {
            return em.find(entityClass, id);
        } catch (Exception e) {
            throw new SocieteDatabaseException("Erreur lors de la recherche par ID", e);
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<T> findAll() throws SocieteDatabaseException {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
            TypedQuery<T> query = em.createQuery(jpql, entityClass);
            return query.getResultList();
        } catch (Exception e) {
            throw new SocieteDatabaseException("Erreur lors de la récupération de toutes les entités", e);
        } finally {
            em.close();
        }
    }
    
    @Override
    public void save(T entity) throws SocieteDatabaseException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SocieteDatabaseException("Erreur lors de la sauvegarde", e);
        } finally {
            em.close();
        }
    }
    
    @Override
    public void update(T entity) throws SocieteDatabaseException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SocieteDatabaseException("Erreur lors de la mise à jour", e);
        } finally {
            em.close();
        }
    }
    
    @Override
    public void delete(T entity) throws SocieteDatabaseException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.remove(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SocieteDatabaseException("Erreur lors de la suppression", e);
        } finally {
            em.close();
        }
    }

    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
} 