package dao.jpa;

import dao.IDAO;
import models.Facture;
import utilities.LogManager;
import java.util.List;
import java.util.Optional;

public class FactureJpaDAO extends AbstractJpaDAO<Facture, Long> implements IDAO<Facture, Long> {
    
    public FactureJpaDAO() {
        super(Facture.class);
    }

    @Override
    public Optional<Facture> findById(Long id) {
        return super.findById(id);
    }

    @Override
    public Facture save(Facture entity) {
        return super.save(entity);
    }

    @Override
    public Facture update(Facture entity) {
        return super.update(entity);
    }

    @Override
    public void delete(Facture entity) {
        super.delete(entity);
    }

    public List<Facture> findByClientId(Long clientId) {
        try {
            return getEntityManager()
                .createQuery("SELECT f FROM Facture f WHERE f.client.id = :clientId", Facture.class)
                .setParameter("clientId", clientId)
                .getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche des factures par client", e);
            throw new RuntimeException("Erreur lors de la recherche des factures par client", e);
        }
    }

    public List<Facture> findAll() {
        try {
            return getEntityManager()
                .createQuery("SELECT f FROM Facture f", Facture.class)
                .getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la récupération de toutes les factures", e);
            throw new RuntimeException("Erreur lors de la récupération de toutes les factures", e);
        }
    }

    public List<Facture> findUnpaid() {
        try {
            return getEntityManager()
                .createQuery("SELECT f FROM Facture f WHERE f.paye = false", Facture.class)
                .getResultList();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche des factures non payées", e);
            throw new RuntimeException("Erreur lors de la recherche des factures non payées", e);
        }
    }

    public Facture findByNumeroFacture(String numeroFacture) {
        try {
            return getEntityManager()
                .createQuery("SELECT f FROM Facture f WHERE f.numeroFacture = :numeroFacture", Facture.class)
                .setParameter("numeroFacture", numeroFacture)
                .getSingleResult();
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche de la facture par numéro", e);
            throw new RuntimeException("Erreur lors de la recherche de la facture par numéro", e);
        }
    }

    public void close() {
        getEntityManager().close();
    }
} 