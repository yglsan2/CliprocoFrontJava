package dao;

import dao.jpa.ProduitJpaDAO;
import models.Produit;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProduitJpaDAOTest {
    private ProduitJpaDAO dao;
    private Produit testProduit;

    @BeforeEach
    void setUp() throws ValidationException {
        dao = new ProduitJpaDAO();
        testProduit = new Produit("Test Produit", "Description test", new BigDecimal("100.00"));
    }

    @Test
    @DisplayName("Devrait sauvegarder un produit et le retrouver par son ID")
    void shouldSaveAndFindProduitById() throws DatabaseException, ValidationException, ResourceNotFoundException {
        dao.save(testProduit);
        Optional<Produit> found = dao.findById(testProduit.getIdentifiant());
        assertTrue(found.isPresent());
        assertEquals("Test Produit", found.get().getNom());
        assertEquals("Description test", found.get().getDescription());
        assertEquals(new BigDecimal("100.00"), found.get().getPrixUnitaire());
    }

    @Test
    @DisplayName("Devrait retourner vide si le produit n'existe pas")
    void shouldReturnEmptyIfProduitNotFound() throws DatabaseException, ValidationException, ResourceNotFoundException {
        Optional<Produit> found = dao.findById(9999L);
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Devrait supprimer un produit")
    void shouldDeleteProduit() throws DatabaseException, ValidationException, ResourceNotFoundException {
        dao.save(testProduit);
        dao.delete(testProduit);
        Optional<Produit> found = dao.findById(testProduit.getIdentifiant());
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Devrait mettre à jour un produit")
    void shouldUpdateProduit() throws DatabaseException, ValidationException, ResourceNotFoundException {
        dao.save(testProduit);
        testProduit.setNom("Produit Modifié");
        testProduit.setDescription("Description modifiée");
        testProduit.setPrixUnitaire(new BigDecimal("200.00"));
        dao.update(testProduit);
        Optional<Produit> found = dao.findById(testProduit.getIdentifiant());
        assertTrue(found.isPresent());
        assertEquals("Produit Modifié", found.get().getNom());
        assertEquals("Description modifiée", found.get().getDescription());
        assertEquals(new BigDecimal("200.00"), found.get().getPrixUnitaire());
    }
} 