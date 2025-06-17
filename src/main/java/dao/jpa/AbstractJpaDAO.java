package dao.jpa;

import dao.IDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import utilities.LogManager;
import java.util.List;
import java.util.Optional;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;

/**
 * Implémentation générique des opérations CRUD avec JPA.
 * @param <T> Le type d'entité
 * @param <ID> Le type d'identifiant
 */
public abstract class AbstractJpaDAO<T, ID> implements IDAO<T, ID> {
    protected EntityManagerFactory emf;
    protected EntityManager em;
    protected Class<T> entityClass;

    public AbstractJpaDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.emf = Persistence.createEntityManagerFactory("cliproco");
        this.em = emf.createEntityManager();
    }

    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public Optional<T> findById(ID id) throws ValidationException, DatabaseException {
        try {
            if (id == null) {
                throw new ValidationException("L'identifiant ne peut pas être null");
            }
            return Optional.ofNullable(em.find(entityClass, id));
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche par ID", e);
            throw new DatabaseException("Erreur lors de la recherche par ID", e);
        }
    }

    @Override
    public List<T> findAll() throws DatabaseException {
        try {
            TypedQuery<T> query = em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass);
            return query.getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la récupération de toutes les entités", e);
            throw new DatabaseException("Erreur lors de la récupération de toutes les entités", e);
        }
    }

    @Override
    public T save(T entity) throws ValidationException, DatabaseException {
        try {
            if (entity == null) {
                throw new ValidationException("L'entité ne peut pas être null");
            }
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            return entity;
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la sauvegarde", e);
            throw new DatabaseException("Erreur lors de la sauvegarde", e);
        }
    }

    @Override
    public T update(T entity) throws ValidationException, ResourceNotFoundException, DatabaseException {
        try {
            if (entity == null) {
                throw new ValidationException("L'entité ne peut pas être null");
            }
            em.getTransaction().begin();
            T updatedEntity = em.merge(entity);
            if (updatedEntity == null) {
                throw new ResourceNotFoundException("Entité non trouvée");
            }
            em.getTransaction().commit();
            return updatedEntity;
        } catch (ValidationException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la mise à jour", e);
            throw new DatabaseException("Erreur lors de la mise à jour", e);
        }
    }

    @Override
    public void delete(T entity) throws ValidationException, ResourceNotFoundException, DatabaseException {
        try {
            if (entity == null) {
                throw new ValidationException("L'entité ne peut pas être null");
            }
            em.getTransaction().begin();
            T mergedEntity = em.merge(entity);
            if (mergedEntity == null) {
                throw new ResourceNotFoundException("Entité non trouvée");
            }
            em.remove(mergedEntity);
            em.getTransaction().commit();
        } catch (ValidationException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la suppression", e);
            throw new DatabaseException("Erreur lors de la suppression", e);
        }
    }

    public void close() throws DatabaseException {
        try {
            if (em != null && em.isOpen()) {
                em.close();
            }
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la fermeture des ressources", e);
            throw new DatabaseException("Erreur lors de la fermeture des ressources", e);
        }
    }
} 