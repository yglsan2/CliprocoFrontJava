package dao.jpa;

import dao.IDAO;
import models.Prospect;
import utilities.LogManager;
import java.util.List;
import java.util.Optional;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;

public class ProspectJpaDAO extends AbstractJpaDAO<Prospect, Long> implements IDAO<Prospect, Long> {
    
    public ProspectJpaDAO() {
        super(Prospect.class);
    }

    @Override
    public Optional<Prospect> findById(Long id) throws ValidationException, DatabaseException {
        return super.findById(id);
    }

    @Override
    public Prospect save(Prospect entity) throws ValidationException, DatabaseException {
        return super.save(entity);
    }

    @Override
    public Prospect update(Prospect entity) throws ValidationException, ResourceNotFoundException, DatabaseException {
        return super.update(entity);
    }

    @Override
    public void delete(Prospect entity) throws ValidationException, ResourceNotFoundException, DatabaseException {
        super.delete(entity);
    }

    public List<Prospect> findByRaisonSociale(String raisonSociale) throws ValidationException, DatabaseException {
        try {
            if (raisonSociale == null) {
                throw new ValidationException("La raison sociale ne peut pas être null");
            }
            return getEntityManager()
                .createQuery("SELECT p FROM Prospect p WHERE p.raisonSociale LIKE :raisonSociale", Prospect.class)
                .setParameter("raisonSociale", "%" + raisonSociale + "%")
                .getResultList();
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche par raison sociale", e);
            throw new DatabaseException("Erreur lors de la recherche par raison sociale", e);
        }
    }

    public boolean existsByRaisonSociale(String raisonSociale) throws ValidationException, DatabaseException {
        try {
            if (raisonSociale == null) {
                throw new ValidationException("La raison sociale ne peut pas être null");
            }
            return getEntityManager()
                .createQuery("SELECT COUNT(p) FROM Prospect p WHERE p.raisonSociale = :raisonSociale", Long.class)
                .setParameter("raisonSociale", raisonSociale)
                .getSingleResult() > 0;
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la vérification de l'existence", e);
            throw new DatabaseException("Erreur lors de la vérification de l'existence", e);
        }
    }

    @Override
    public void close() throws DatabaseException {
        try {
            getEntityManager().close();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la fermeture des ressources", e);
            throw new DatabaseException("Erreur lors de la fermeture des ressources", e);
        }
    }
} 