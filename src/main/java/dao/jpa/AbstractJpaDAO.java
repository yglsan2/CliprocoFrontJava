package dao.jpa;

import dao.IDAO;
import exceptions.DatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import utilities.LogManager;

import java.util.List;

public abstract class AbstractJpaDAO<T> implements IDAO<T> {
    protected static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("cliproco");
    protected EntityManager em;
    protected Class<T> entityClass;

    public AbstractJpaDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected void beginTransaction() throws DatabaseException {
        try {
            em = emf.createEntityManager();
            em.getTransaction().begin();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de l'ouverture de la transaction", e);
            throw new DatabaseException("Erreur lors de l'ouverture de la transaction", e);
        }
    }

    protected void commitTransaction() throws DatabaseException {
        try {
            em.getTransaction().commit();
        } catch (Exception e) {
            LogManager.logException("Erreur lors du commit de la transaction", e);
            throw new DatabaseException("Erreur lors du commit de la transaction", e);
        } finally {
            em.close();
        }
    }

    protected void rollbackTransaction() {
        try {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } catch (Exception e) {
            LogManager.logException("Erreur lors du rollback de la transaction", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public T findById(Long id) throws DatabaseException {
        try {
            beginTransaction();
            T entity = em.find(entityClass, id);
            commitTransaction();
            return entity;
        } catch (Exception e) {
            rollbackTransaction();
            LogManager.logException("Erreur lors de la recherche par ID", e);
            throw new DatabaseException("Erreur lors de la recherche par ID", e);
        }
    }

    @Override
    public List<T> findAll() throws DatabaseException {
        try {
            beginTransaction();
            List<T> entities = em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                    .getResultList();
            commitTransaction();
            return entities;
        } catch (Exception e) {
            rollbackTransaction();
            LogManager.logException("Erreur lors de la recherche de tous les éléments", e);
            throw new DatabaseException("Erreur lors de la recherche de tous les éléments", e);
        }
    }

    @Override
    public T save(T entity) throws DatabaseException {
        try {
            beginTransaction();
            em.persist(entity);
            commitTransaction();
            return entity;
        } catch (Exception e) {
            rollbackTransaction();
            LogManager.logException("Erreur lors de la sauvegarde", e);
            throw new DatabaseException("Erreur lors de la sauvegarde", e);
        }
    }

    @Override
    public T update(T entity) throws DatabaseException {
        try {
            beginTransaction();
            T updatedEntity = em.merge(entity);
            commitTransaction();
            return updatedEntity;
        } catch (Exception e) {
            rollbackTransaction();
            LogManager.logException("Erreur lors de la mise à jour", e);
            throw new DatabaseException("Erreur lors de la mise à jour", e);
        }
    }

    @Override
    public void delete(T entity) throws DatabaseException {
        try {
            beginTransaction();
            em.remove(em.contains(entity) ? entity : em.merge(entity));
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            LogManager.logException("Erreur lors de la suppression", e);
            throw new DatabaseException("Erreur lors de la suppression", e);
        }
    }
} 