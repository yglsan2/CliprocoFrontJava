package services;

import dao.IDAO;
import models.Facture;
import models.Produit;
import models.CalculFacture;
import models.Client;
import utilities.LogManager;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des factures.
 */
public class FactureService {
    private final IDAO<Facture, Long> factureDAO;
    private final IDAO<Produit, Long> produitDAO;
    private final IDAO<CalculFacture, Long> calculFactureDAO;

    public FactureService(IDAO<Facture, Long> factureDAO, 
                         IDAO<Produit, Long> produitDAO,
                         IDAO<CalculFacture, Long> calculFactureDAO) {
        this.factureDAO = factureDAO;
        this.produitDAO = produitDAO;
        this.calculFactureDAO = calculFactureDAO;
        LogManager.logInfo("FactureService initialisé avec succès");
    }

    public Optional<Facture> findById(Long id) throws ValidationException, DatabaseException {
        LogManager.logInfo("Recherche de la facture avec l'ID: " + id);
        try {
            if (id == null) {
                LogManager.logWarning("Tentative de recherche avec un ID null");
                throw new ValidationException("L'ID ne peut pas être null");
            }
            Optional<Facture> result = factureDAO.findById(id);
            LogManager.logInfo("Résultat de la recherche: " + (result.isPresent() ? "Facture trouvée" : "Facture non trouvée"));
            return result;
        } catch (ValidationException e) {
            LogManager.logWarning("Erreur de validation lors de la recherche de la facture: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche de la facture", e);
            throw new DatabaseException("Erreur lors de la recherche de la facture", e);
        }
    }

    public List<Facture> findAll() throws DatabaseException {
        LogManager.logInfo("Récupération de toutes les factures");
        try {
            List<Facture> factures = factureDAO.findAll();
            LogManager.logInfo("Nombre de factures trouvées: " + factures.size());
            return factures;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la récupération des factures", e);
            throw new DatabaseException("Erreur lors de la récupération des factures", e);
        }
    }

    public List<Facture> findByClientId(Long clientId) throws ValidationException, DatabaseException {
        LogManager.logInfo("Recherche des factures pour le client: " + clientId);
        try {
            if (clientId == null) {
                LogManager.logWarning("Tentative de recherche avec un ID client null");
                throw new ValidationException("L'ID du client ne peut pas être null");
            }
            List<Facture> factures = ((FactureJpaDAO) factureDAO).findByClientId(clientId);
            LogManager.logInfo("Nombre de factures trouvées pour le client: " + factures.size());
            return factures;
        } catch (ValidationException e) {
            LogManager.logWarning("Erreur de validation lors de la recherche des factures par client: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche des factures par client", e);
            throw new DatabaseException("Erreur lors de la recherche des factures par client", e);
        }
    }

    public List<Facture> findUnpaid() throws DatabaseException {
        LogManager.logInfo("Recherche des factures non payées");
        try {
            List<Facture> factures = ((FactureJpaDAO) factureDAO).findUnpaid();
            LogManager.logInfo("Nombre de factures non payées trouvées: " + factures.size());
            return factures;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche des factures non payées", e);
            throw new DatabaseException("Erreur lors de la recherche des factures non payées", e);
        }
    }

    public Facture create(String numero, LocalDate dateEmission, LocalDate dateEcheance, 
                         Client client, List<Produit> produits, CalculFacture calcul) 
            throws ValidationException, DatabaseException {
        LogManager.logInfo("Création d'une nouvelle facture");
        try {
            if (numero == null || numero.trim().isEmpty()) {
                throw new ValidationException("Le numéro de facture ne peut pas être vide");
            }
            if (dateEmission == null) {
                throw new ValidationException("La date d'émission ne peut pas être null");
            }
            if (dateEcheance == null) {
                throw new ValidationException("La date d'échéance ne peut pas être null");
            }
            if (client == null) {
                throw new ValidationException("Le client ne peut pas être null");
            }

            Facture facture = new Facture(numero, dateEmission, dateEcheance, client);
            
            if (produits != null) {
                for (Produit produit : produits) {
                    facture.addProduit(produit);
                }
            }

            if (calcul != null) {
                facture.setCalcul(calcul);
            }

            Facture savedFacture = factureDAO.save(facture);
            LogManager.logInfo("Facture créée avec succès: " + savedFacture.getId());
            return savedFacture;
        } catch (ValidationException e) {
            LogManager.logWarning("Erreur de validation lors de la création de la facture: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la création de la facture", e);
            throw new DatabaseException("Erreur lors de la création de la facture", e);
        }
    }

    public Facture update(Facture facture) throws ValidationException, ResourceNotFoundException, DatabaseException {
        LogManager.logInfo("Mise à jour de la facture: " + facture.getId());
        try {
            if (facture == null) {
                throw new ValidationException("La facture ne peut pas être null");
            }
            if (facture.getId() == null) {
                throw new ValidationException("L'ID de la facture ne peut pas être null");
            }

            Facture existingFacture = factureDAO.findById(facture.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Facture non trouvée avec l'ID: " + facture.getId()));

            Facture updatedFacture = factureDAO.update(facture);
            LogManager.logInfo("Facture mise à jour avec succès: " + updatedFacture.getId());
            return updatedFacture;
        } catch (ValidationException | ResourceNotFoundException e) {
            LogManager.logWarning("Erreur lors de la mise à jour de la facture: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la mise à jour de la facture", e);
            throw new DatabaseException("Erreur lors de la mise à jour de la facture", e);
        }
    }

    public void delete(Long id) throws ValidationException, ResourceNotFoundException, DatabaseException {
        LogManager.logInfo("Suppression de la facture avec l'ID: " + id);
        try {
            if (id == null) {
                throw new ValidationException("L'ID ne peut pas être null");
            }

            Facture facture = factureDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facture non trouvée avec l'ID: " + id));

            factureDAO.delete(facture);
            LogManager.logInfo("Facture supprimée avec succès: " + id);
        } catch (ValidationException | ResourceNotFoundException e) {
            LogManager.logWarning("Erreur lors de la suppression de la facture: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la suppression de la facture", e);
            throw new DatabaseException("Erreur lors de la suppression de la facture", e);
        }
    }

    public Facture findByNumeroFacture(String numeroFacture) 
            throws ValidationException, ResourceNotFoundException, DatabaseException {
        LogManager.logInfo("Recherche de la facture avec le numéro: " + numeroFacture);
        try {
            if (numeroFacture == null || numeroFacture.trim().isEmpty()) {
                throw new ValidationException("Le numéro de facture ne peut pas être vide");
            }

            Facture facture = ((FactureJpaDAO) factureDAO).findByNumeroFacture(numeroFacture);
            LogManager.logInfo("Facture trouvée avec le numéro: " + numeroFacture);
            return facture;
        } catch (ValidationException | ResourceNotFoundException e) {
            LogManager.logWarning("Erreur lors de la recherche de la facture par numéro: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la recherche de la facture par numéro", e);
            throw new DatabaseException("Erreur lors de la recherche de la facture par numéro", e);
        }
    }
} 