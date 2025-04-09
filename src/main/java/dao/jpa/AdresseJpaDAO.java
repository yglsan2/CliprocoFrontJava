package dao.jpa;

import models.Adresse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class AdresseJpaDAO extends GenericJpaDAO<Adresse> {
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public AdresseJpaDAO() {
        this.emf = Persistence.createEntityManagerFactory("cliproco");
        this.em = emf.createEntityManager();
    }

    public Adresse findById(Long id) {
        return em.find(Adresse.class, id);
    }

    public List<Adresse> findAll() {
        TypedQuery<Adresse> query = em.createQuery("SELECT a FROM Adresse a", Adresse.class);
        return query.getResultList();
    }

    public void save(Adresse adresse) {
        em.getTransaction().begin();
        em.persist(adresse);
        em.getTransaction().commit();
    }

    public void update(Adresse adresse) {
        em.getTransaction().begin();
        em.merge(adresse);
        em.getTransaction().commit();
    }

    public void delete(Adresse adresse) {
        em.getTransaction().begin();
        em.remove(adresse);
        em.getTransaction().commit();
    }

    public void close() {
        em.close();
        emf.close();
    }
} 