package dao.jpa;

import models.Client;
import dao.SocieteDatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 * Implémentation JPA du DAO Client.
 */
public class ClientJpaDAO extends AbstractSocieteJpaDAO<Client> {
    public ClientJpaDAO() {
        super();
    }

    /**
     * Recherche les clients par chiffre d'affaires minimum.
     * @param minChiffreAffaires Le chiffre d'affaires minimum
     * @return Liste des clients correspondants
     * @throws SocieteDatabaseException En cas d'erreur de base de données
     */
    public List<Client> findByChiffreAffairesMin(Double minChiffreAffaires) throws SocieteDatabaseException {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT c FROM Client c WHERE c.chiffreAffaires >= :minCA";
            TypedQuery<Client> query = em.createQuery(jpql, Client.class);
            query.setParameter("minCA", minChiffreAffaires);
            return query.getResultList();
        } catch (Exception e) {
            throw new SocieteDatabaseException("Erreur lors de la recherche par chiffre d'affaires", e);
        } finally {
            em.close();
        }
    }

    /**
     * Recherche les clients par nombre d'employés minimum.
     * @param minEmployes Le nombre minimum d'employés
     * @return Liste des clients correspondants
     * @throws SocieteDatabaseException En cas d'erreur de base de données
     */
    public List<Client> findByNombreEmployesMin(Integer minEmployes) throws SocieteDatabaseException {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT c FROM Client c WHERE c.nombreEmployes >= :minEmp";
            TypedQuery<Client> query = em.createQuery(jpql, Client.class);
            query.setParameter("minEmp", minEmployes);
            return query.getResultList();
        } catch (Exception e) {
            throw new SocieteDatabaseException("Erreur lors de la recherche par nombre d'employés", e);
        } finally {
            em.close();
        }
    }

    /**
     * Recherche un client par son email.
     * @param email L'email à rechercher
     * @return Le client correspondant ou null si non trouvé
     * @throws SocieteDatabaseException En cas d'erreur de base de données
     */
    public Client findByEmail(String email) throws SocieteDatabaseException {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT c FROM Client c WHERE c.email = :email";
            TypedQuery<Client> query = em.createQuery(jpql, Client.class);
            query.setParameter("email", email);
            List<Client> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            throw new SocieteDatabaseException("Erreur lors de la recherche par email", e);
        } finally {
            em.close();
        }
    }
} 