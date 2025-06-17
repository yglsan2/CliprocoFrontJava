package dao.jpa;

import dao.IDAO;
import models.Facture;
import utilities.LogManager;
import java.util.List;
import java.util.Optional;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import jakarta.persistence.NoResultException;

public class FactureJpaDAO extends AbstractJpaDAO<Facture, Long> implements IDAO<Facture, Long> {
    
    public FactureJpaDAO() {
        super(Facture.class);
    }

    @Override
    public Optional<Facture> findById(Long id) throws ValidationException, DatabaseException {
        return super.findById(id);
    }

    @Override
    public Facture save(Facture entity) throws ValidationException, DatabaseException {
        return super.save(entity);
    }

    @Override
    public Facture update(Facture entity) throws ValidationException, ResourceNotFoundException, DatabaseException {
        return super.update(entity);
    }

    @Override
    public void delete(Facture entity) throws ValidationException, ResourceNotFoundException, DatabaseException {
        super.delete(entity);
    }

    public List<Facture> findByClientId(Long clientId) throws ValidationException, DatabaseException {
        try {
            if (clientId == null) {
                throw new ValidationException("L'identifiant du client ne peut pas être null");
            }
            return getEntityManager()
                .createQuery("SELECT f FROM Facture f WHERE f.client.id = :clientId", Facture.class)
                .setParameter("clientId", clientId)
                .getResultList();
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche des factures par client", e);
            throw new DatabaseException("Erreur lors de la recherche des factures par client", e);
        }
    }

    @Override
    public List<Facture> findAll() throws DatabaseException {
        try {
            return getEntityManager()
                .createQuery("SELECT f FROM Facture f", Facture.class)
                .getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la récupération de toutes les factures", e);
            throw new DatabaseException("Erreur lors de la récupération de toutes les factures", e);
        }
    }

    public List<Facture> findUnpaid() throws DatabaseException {
        try {
            return getEntityManager()
                .createQuery("SELECT f FROM Facture f WHERE f.paye = false", Facture.class)
                .getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche des factures non payées", e);
            throw new DatabaseException("Erreur lors de la recherche des factures non payées", e);
        }
    }

    public Facture findByNumeroFacture(String numeroFacture) throws ValidationException, ResourceNotFoundException, DatabaseException {
        try {
            if (numeroFacture == null) {
                throw new ValidationException("Le numéro de facture ne peut pas être null");
            }
            return getEntityManager()
                .createQuery("SELECT f FROM Facture f WHERE f.numeroFacture = :numeroFacture", Facture.class)
                .setParameter("numeroFacture", numeroFacture)
                .getSingleResult();
        } catch (NoResultException e) {
            throw new ResourceNotFoundException("Facture non trouvée avec le numéro: " + numeroFacture);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche de la facture par numéro", e);
            throw new DatabaseException("Erreur lors de la recherche de la facture par numéro", e);
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