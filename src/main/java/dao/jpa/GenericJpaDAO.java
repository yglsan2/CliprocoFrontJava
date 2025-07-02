package dao.jpa;

import dao.IDAO;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import utilities.LogManager;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

public abstract class GenericJpaDAO<T, ID> implements IDAO<T, ID> {
    
    protected final EntityManagerFactory emf;
    protected final Class<T> entityClass;
    
    /**
     * Constructeur générique pour les DAO JPA
     * 
     * Ce constructeur initialise l'EntityManagerFactory pour l'unité de persistance
     * "cliprocoUP" (Cliproco Unit of Persistance) et détermine automatiquement
     * la classe d'entité à partir des paramètres de type générique.
     * 
     * L'unité de persistance "cliprocoUP" est définie dans persistence.xml et
     * contient la configuration pour la base de données MySQL et les entités JPA.
     */
    @SuppressWarnings("unchecked")
    public GenericJpaDAO() {
        // Détermination automatique de la classe d'entité à partir des paramètres génériques
        this.entityClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        
        // Création de l'EntityManagerFactory pour l'unité de persistance "cliprocoUP"
        // UP = Unit of Persistence (Unité de Persistance)
        this.emf = Persistence.createEntityManagerFactory("cliprocoUP");
    }
    
    /**
     * Crée et retourne un nouvel EntityManager pour l'unité de persistance "cliprocoUP"
     * 
     * Cette méthode crée un nouvel EntityManager à partir de l'EntityManagerFactory
     * associé à l'unité de persistance "cliprocoUP". Chaque appel crée une nouvelle
     * instance d'EntityManager qui doit être fermée après utilisation.
     * 
     * L'EntityManager est utilisé pour effectuer les opérations de base de données
     * (CRUD) sur les entités JPA associées à l'unité de persistance.
     * 
     * @return un nouvel EntityManager pour l'unité cliprocoUP
     */
    protected EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    @Override
    public Optional<T> findById(ID id) throws ValidationException, DatabaseException {
        EntityManager em = getEntityManager();
        try {
            if (id == null) {
                throw new ValidationException("L'identifiant ne peut pas être null");
            }
            T entity = em.find(entityClass, id);
            return Optional.ofNullable(entity);
        } catch (ValidationException e) {
            throw e;
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
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
            TypedQuery<T> query = em.createQuery(jpql, entityClass);
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
    public T save(T entity) throws ValidationException, DatabaseException {
        EntityManager em = getEntityManager();
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
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    @Override
    public T update(T entity) throws ValidationException, ResourceNotFoundException, DatabaseException {
        EntityManager em = getEntityManager();
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
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    @Override
    public void delete(T entity) throws ValidationException, ResourceNotFoundException, DatabaseException {
        EntityManager em = getEntityManager();
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
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void close() throws DatabaseException {
        try {
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la fermeture des ressources", e);
            throw new DatabaseException("Erreur lors de la fermeture des ressources", e);
        }
    }
} 