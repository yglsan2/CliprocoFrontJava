package dao.jpa;

import exceptions.DatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import utilities.LogManager;
import java.util.List;

public abstract class AbstractSocieteJpaDAO<T> extends GenericJpaDAO<T> {
    
    public List<T> findBySiret(String siret) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<T> query = em.createQuery(
                "SELECT s FROM " + getEntityClass().getSimpleName() + " s WHERE s.siret = :siret", 
                getEntityClass()
            );
            query.setParameter("siret", siret);
            return query.getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche par SIRET", e);
            throw new DatabaseException("Erreur lors de la recherche par SIRET", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public List<T> findByNom(String nom) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<T> query = em.createQuery(
                "SELECT s FROM " + getEntityClass().getSimpleName() + " s WHERE s.raisonSociale LIKE :nom", 
                getEntityClass()
            );
            query.setParameter("nom", "%" + nom + "%");
            return query.getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche par nom", e);
            throw new DatabaseException("Erreur lors de la recherche par nom", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public List<T> findByVille(String ville) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<T> query = em.createQuery(
                "SELECT s FROM " + getEntityClass().getSimpleName() + " s WHERE s.ville LIKE :ville", 
                getEntityClass()
            );
            query.setParameter("ville", "%" + ville + "%");
            return query.getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche par ville", e);
            throw new DatabaseException("Erreur lors de la recherche par ville", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public List<T> findByCodePostal(String codePostal) throws DatabaseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<T> query = em.createQuery(
                "SELECT s FROM " + getEntityClass().getSimpleName() + " s WHERE s.codePostal = :codePostal", 
                getEntityClass()
            );
            query.setParameter("codePostal", codePostal);
            return query.getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche par code postal", e);
            throw new DatabaseException("Erreur lors de la recherche par code postal", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
} 