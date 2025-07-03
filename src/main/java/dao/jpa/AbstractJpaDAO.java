package dao.jpa;

import dao.IDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import routers.FrontController;
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
    protected EntityManager em;
    protected Class<T> entityClass;

    public AbstractJpaDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.em = FrontController.getEntityManager();
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
            LogManager.logInfo("Création de la requête SQL native pour " + entityClass.getSimpleName());
            // Utiliser une requête SQL native simple pour éviter les problèmes de relations JPA
            String tableName = entityClass.getSimpleName().toLowerCase() + "s"; // clients, prospects, etc.
            var query = em.createNativeQuery(
                "SELECT * FROM " + tableName + " LIMIT 5", 
                entityClass
            );
            LogManager.logInfo("Exécution de la requête SQL native pour " + entityClass.getSimpleName());
            @SuppressWarnings("unchecked")
            List<T> result = query.getResultList();
            LogManager.logInfo("Résultat obtenu: " + result.size() + " " + entityClass.getSimpleName());
            return result;
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
        // L'EntityManager est géré par le FrontController
        // Pas besoin de le fermer ici
    }

    @Override
    public boolean existsById(ID id) throws ValidationException, DatabaseException {
        try {
            if (id == null) {
                throw new ValidationException("L'identifiant ne peut pas être null");
            }
            TypedQuery<Integer> query = em.createQuery(
                "SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e WHERE e.id = :id", 
                Integer.class
            );
            query.setParameter("id", id);
            return query.getSingleResult() > 0;
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la vérification de l'existence par ID", e);
            throw new DatabaseException("Erreur lors de la vérification de l'existence par ID", e);
        }
    }
} 