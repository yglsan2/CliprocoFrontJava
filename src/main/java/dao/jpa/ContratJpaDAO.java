package dao.jpa;

import models.Contrat;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class ContratJpaDAO extends GenericJpaDAO<Contrat> {
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public ContratJpaDAO() {
        this.emf = Persistence.createEntityManagerFactory("cliproco");
        this.em = emf.createEntityManager();
    }

    public Contrat findById(Long id) {
        return em.find(Contrat.class, id);
    }

    public List<Contrat> findAll() {
        TypedQuery<Contrat> query = em.createQuery("SELECT c FROM Contrat c", Contrat.class);
        return query.getResultList();
    }

    public void save(Contrat contrat) {
        em.getTransaction().begin();
        em.persist(contrat);
        em.getTransaction().commit();
    }

    public void update(Contrat contrat) {
        em.getTransaction().begin();
        em.merge(contrat);
        em.getTransaction().commit();
    }

    public void delete(Contrat contrat) {
        em.getTransaction().begin();
        em.remove(contrat);
        em.getTransaction().commit();
    }

    public void close() {
        em.close();
        emf.close();
    }
} 