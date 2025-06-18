package dao.jpa;

import models.Produit;
import utilities.LogManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import exceptions.ValidationException;
import exceptions.DatabaseException;

public class ProduitJpaDAO extends GenericJpaDAO<Produit, Integer> {
    public ProduitJpaDAO() {
        super();
    }

    public Optional<Produit> findByCode(String code) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Produit> query = em.createQuery(
                "SELECT p FROM Produit p WHERE p.code = :code", 
                Produit.class
            );
            query.setParameter("code", code);
            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche du produit par code", e);
            return Optional.empty();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public List<Produit> findByPrixBetween(double minPrix, double maxPrix) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Produit> query = em.createQuery(
                "SELECT p FROM Produit p WHERE p.prix BETWEEN :minPrix AND :maxPrix", 
                Produit.class
            );
            query.setParameter("minPrix", minPrix);
            query.setParameter("maxPrix", maxPrix);
            return query.getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche des produits par prix", e);
            return List.of();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean existsById(Integer id) throws ValidationException, DatabaseException {
        try {
            if (id == null) {
                throw new ValidationException("L'ID ne peut pas être null");
            }
            EntityManager em = getEntityManager();
            TypedQuery<Integer> query = em.createQuery(
                "SELECT COUNT(p) FROM Produit p WHERE p.id = :id",
                Integer.class
            );
            query.setParameter("id", id);
            return query.getSingleResult() > 0;
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la vérification de l'existence du produit", e);
            throw new DatabaseException("Erreur lors de la vérification de l'existence du produit", e);
        }
    }
} 