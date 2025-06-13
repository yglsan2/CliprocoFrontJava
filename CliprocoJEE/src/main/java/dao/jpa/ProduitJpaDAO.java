package dao.jpa;

import models.Produit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;
import dao.IDAO;

public class ProduitJpaDAO extends GenericJpaDAO<Produit> {
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public ProduitJpaDAO() {
        this.emf = Persistence.createEntityManagerFactory("cliproco");
        this.em = emf.createEntityManager();
    }

    @Override
    public Produit findById(Long id) {
        return em.find(Produit.class, id);
    }

    @Override
    public List<Produit> findAll() {
        TypedQuery<Produit> query = em.createQuery("SELECT p FROM Produit p", Produit.class);
        return query.getResultList();
    }

    @Override
    public void save(Produit produit) {
        em.getTransaction().begin();
        em.persist(produit);
        em.getTransaction().commit();
    }

    @Override
    public void update(Produit produit) {
        em.getTransaction().begin();
        em.merge(produit);
        em.getTransaction().commit();
    }

    @Override
    public void delete(Produit produit) {
        em.getTransaction().begin();
        em.remove(produit);
        em.getTransaction().commit();
    }

    public Produit findByCode(String code) {
        TypedQuery<Produit> query = em.createQuery(
            "SELECT p FROM Produit p WHERE p.code = :code", 
            Produit.class
        );
        query.setParameter("code", code);
        return query.getSingleResult();
    }

    public List<Produit> findByPrixBetween(double minPrix, double maxPrix) {
        TypedQuery<Produit> query = em.createQuery(
            "SELECT p FROM Produit p WHERE p.prix BETWEEN :minPrix AND :maxPrix", 
            Produit.class
        );
        query.setParameter("minPrix", minPrix);
        query.setParameter("maxPrix", maxPrix);
        return query.getResultList();
    }

    public void close() {
        em.close();
        emf.close();
    }
} 