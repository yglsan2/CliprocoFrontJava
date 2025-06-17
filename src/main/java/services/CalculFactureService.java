package services;

import dao.IDAO;
import models.CalculFacture;
import models.Facture;
import utilities.LogManager;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des calculs de factures.
 */
public class CalculFactureService {
    private final IDAO<CalculFacture, Long> calculFactureDAO;

    public CalculFactureService(IDAO<CalculFacture, Long> calculFactureDAO) {
        this.calculFactureDAO = calculFactureDAO;
        LogManager.logInfo("CalculFactureService initialisé avec succès");
    }

    public Optional<CalculFacture> findById(Long id) throws ValidationException, DatabaseException {
        LogManager.logInfo("Recherche du calcul de facture avec l'ID: " + id);
        try {
            if (id == null) {
                LogManager.logWarning("Tentative de recherche avec un ID null");
                throw new ValidationException("L'ID ne peut pas être null");
            }
            Optional<CalculFacture> result = calculFactureDAO.findById(id);
            LogManager.logInfo("Résultat de la recherche: " + (result.isPresent() ? "Calcul trouvé" : "Calcul non trouvé"));
            return result;
        } catch (ValidationException e) {
            LogManager.logWarning("Erreur de validation lors de la recherche du calcul: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche du calcul", e);
            throw new DatabaseException("Erreur lors de la recherche du calcul", e);
        }
    }

    public List<CalculFacture> findAll() throws DatabaseException {
        LogManager.logInfo("Récupération de tous les calculs de factures");
        try {
            List<CalculFacture> calculs = calculFactureDAO.findAll();
            LogManager.logInfo("Nombre de calculs trouvés: " + calculs.size());
            return calculs;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la récupération des calculs", e);
            throw new DatabaseException("Erreur lors de la récupération des calculs", e);
        }
    }

    public CalculFacture create(BigDecimal montantHT, BigDecimal tauxTVA, 
                              BigDecimal montantTVA, BigDecimal montantTTC, 
                              Facture facture) throws ValidationException, DatabaseException {
        LogManager.logInfo("Création d'un nouveau calcul de facture");
        try {
            if (montantHT == null || montantHT.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidationException("Le montant HT doit être supérieur à 0");
            }
            if (tauxTVA == null || tauxTVA.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidationException("Le taux de TVA doit être supérieur à 0");
            }
            if (montantTVA == null || montantTVA.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidationException("Le montant de TVA doit être supérieur à 0");
            }
            if (montantTTC == null || montantTTC.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidationException("Le montant TTC doit être supérieur à 0");
            }
            if (facture == null) {
                throw new ValidationException("La facture ne peut pas être null");
            }

            CalculFacture calcul = new CalculFacture(montantHT, tauxTVA, montantTVA, montantTTC);
            calcul.setFacture(facture);

            CalculFacture savedCalcul = calculFactureDAO.save(calcul);
            LogManager.logInfo("Calcul de facture créé avec succès: " + savedCalcul.getId());
            return savedCalcul;
        } catch (ValidationException e) {
            LogManager.logWarning("Erreur de validation lors de la création du calcul: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la création du calcul", e);
            throw new DatabaseException("Erreur lors de la création du calcul", e);
        }
    }

    public CalculFacture update(CalculFacture calcul) throws ValidationException, ResourceNotFoundException, DatabaseException {
        LogManager.logInfo("Mise à jour du calcul de facture: " + calcul.getId());
        try {
            if (calcul == null) {
                throw new ValidationException("Le calcul ne peut pas être null");
            }
            if (calcul.getId() == null) {
                throw new ValidationException("L'ID du calcul ne peut pas être null");
            }

            CalculFacture existingCalcul = calculFactureDAO.findById(calcul.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Calcul non trouvé avec l'ID: " + calcul.getId()));

            CalculFacture updatedCalcul = calculFactureDAO.update(calcul);
            LogManager.logInfo("Calcul de facture mis à jour avec succès: " + updatedCalcul.getId());
            return updatedCalcul;
        } catch (ValidationException | ResourceNotFoundException e) {
            LogManager.logWarning("Erreur lors de la mise à jour du calcul: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la mise à jour du calcul", e);
            throw new DatabaseException("Erreur lors de la mise à jour du calcul", e);
        }
    }

    public void delete(Long id) throws ValidationException, ResourceNotFoundException, DatabaseException {
        LogManager.logInfo("Suppression du calcul de facture avec l'ID: " + id);
        try {
            if (id == null) {
                throw new ValidationException("L'ID ne peut pas être null");
            }

            CalculFacture calcul = calculFactureDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Calcul non trouvé avec l'ID: " + id));

            calculFactureDAO.delete(calcul);
            LogManager.logInfo("Calcul de facture supprimé avec succès: " + id);
        } catch (ValidationException | ResourceNotFoundException e) {
            LogManager.logWarning("Erreur lors de la suppression du calcul: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la suppression du calcul", e);
            throw new DatabaseException("Erreur lors de la suppression du calcul", e);
        }
    }
} 