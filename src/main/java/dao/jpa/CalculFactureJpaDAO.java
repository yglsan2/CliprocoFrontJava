package dao.jpa;

import dao.IDAO;
import models.CalculFacture;
import java.util.Optional;
import exceptions.ValidationException;
import exceptions.DatabaseException;
import exceptions.ResourceNotFoundException;
import utilities.LogManager;

/**
 * Implémentation JPA du DAO pour les calculs de factures.
 */
public class CalculFactureJpaDAO extends AbstractJpaDAO<CalculFacture, Integer> implements IDAO<CalculFacture, Integer> {
    
    public CalculFactureJpaDAO() {
        super(CalculFacture.class);
        LogManager.logInfo("Initialisation de CalculFactureJpaDAO");
    }

    @Override
    public Optional<CalculFacture> findById(Integer id) throws ValidationException, DatabaseException {
        LogManager.logInfo("Recherche du calcul de facture avec l'ID: " + id);
        Optional<CalculFacture> result = super.findById(id);
        LogManager.logInfo("Résultat de la recherche: " + (result.isPresent() ? "trouvé" : "non trouvé"));
        return result;
    }

    @Override
    public CalculFacture save(CalculFacture entity) throws ValidationException, DatabaseException {
        LogManager.logInfo("Sauvegarde d'un nouveau calcul de facture");
        CalculFacture savedEntity = super.save(entity);
        LogManager.logInfo("Calcul de facture sauvegardé avec succès");
        return savedEntity;
    }

    @Override
    public CalculFacture update(CalculFacture entity) throws ValidationException, ResourceNotFoundException, DatabaseException {
        LogManager.logInfo("Mise à jour d'un calcul de facture");
        CalculFacture updatedEntity = super.update(entity);
        LogManager.logInfo("Calcul de facture mis à jour avec succès");
        return updatedEntity;
    }

    @Override
    public void delete(CalculFacture entity) throws ValidationException, ResourceNotFoundException, DatabaseException {
        LogManager.logInfo("Suppression d'un calcul de facture");
        super.delete(entity);
        LogManager.logInfo("Calcul de facture supprimé avec succès");
    }
} 