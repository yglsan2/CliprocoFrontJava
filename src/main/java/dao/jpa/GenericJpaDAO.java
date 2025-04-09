package dao.jpa;

import dao.IDAO;
import dao.SocieteDatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import utilities.JPAUtil;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class GenericJpaDAO<T> implements IDAO<T> {
    
    protected final EntityManagerFactory emf;
    protected final EntityManager em;
    protected final Class<T> entityClass;
    
    @SuppressWarnings("unchecked")
    public GenericJpaDAO() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        this.emf = Persistence.createEntityManagerFactory("cliproco");
        this.em = emf.createEntityManager();
    }
    
    @Override
    public T findById(Long id) throws SocieteDatabaseException {
        try {
            return em.find(entityClass, id);
        } catch (Exception e) {
            throw new SocieteDatabaseException("Erreur lors de la recherche par ID", e);
        }
    }
    
    @Override
    public List<T> findAll() throws SocieteDatabaseException {
        try {
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
            TypedQuery<T> query = em.createQuery(jpql, entityClass);
            return query.getResultList();
        } catch (Exception e) {
            throw new SocieteDatabaseException("Erreur lors de la récupération de toutes les entités", e);
        }
    }
    
    @Override
    public void save(T entity) throws SocieteDatabaseException {
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SocieteDatabaseException("Erreur lors de la sauvegarde", e);
        }
    }
    
    @Override
    public void update(T entity) throws SocieteDatabaseException {
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SocieteDatabaseException("Erreur lors de la mise à jour", e);
        }
    }
    
    @Override
    public void delete(T entity) throws SocieteDatabaseException {
        try {
            em.getTransaction().begin();
            em.remove(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new SocieteDatabaseException("Erreur lors de la suppression", e);
        }
    }

    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
} 