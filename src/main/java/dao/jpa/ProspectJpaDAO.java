package dao.jpa;

import dao.IDAO;
import models.Prospect;
import utilities.LogManager;
import java.util.List;
import java.util.Optional;

public class ProspectJpaDAO extends AbstractJpaDAO<Prospect, Long> implements IDAO<Prospect, Long> {
    
    public ProspectJpaDAO() {
        super(Prospect.class);
    }

    @Override
    public Optional<Prospect> findById(Long id) {
        return super.findById(id);
    }

    @Override
    public Prospect save(Prospect entity) {
        return super.save(entity);
    }

    @Override
    public Prospect update(Prospect entity) {
        return super.update(entity);
    }

    @Override
    public void delete(Prospect entity) {
        super.delete(entity);
    }

    public List<Prospect> findByRaisonSociale(String raisonSociale) {
        try {
            return getEntityManager()
                .createQuery("SELECT p FROM Prospect p WHERE p.raisonSociale LIKE :raisonSociale", Prospect.class)
                .setParameter("raisonSociale", "%" + raisonSociale + "%")
                .getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche par raison sociale", e);
            throw new RuntimeException("Erreur lors de la recherche par raison sociale", e);
        }
    }

    public boolean existsByRaisonSociale(String raisonSociale) {
        try {
            return getEntityManager()
                .createQuery("SELECT COUNT(p) FROM Prospect p WHERE p.raisonSociale = :raisonSociale", Long.class)
                .setParameter("raisonSociale", raisonSociale)
                .getSingleResult() > 0;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la vérification de l'existence", e);
            throw new RuntimeException("Erreur lors de la vérification de l'existence", e);
        }
    }
} 