package dao;

import dao.jpa.FactureJpaDAO;
import models.Facture;
import models.Client;
import models.Produit;
import models.CalculFacture;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FactureJpaDAOTest {
    private FactureJpaDAO dao;
    private Facture testFacture;
    private Client testClient;
    private Produit testProduit;
    private CalculFacture testCalcul;

    @BeforeEach
    void setUp() throws ValidationException {
        dao = new FactureJpaDAO();
        testClient = new Client("Test Client", "12 Rue de Paris, 75000 Paris, France", "0123456789", "client@test.com");
        testFacture = new Facture("FACT-001", LocalDate.now(), LocalDate.now().plusDays(30), testClient);
        testProduit = new Produit("Test Produit", "Description test", new BigDecimal("100.00"));
        testCalcul = new CalculFacture(
            new BigDecimal("1000.00"),
            new BigDecimal("20.00"),
            new BigDecimal("200.00"),
            new BigDecimal("1200.00")
        );
        testFacture.addProduit(testProduit);
        testFacture.setCalcul(testCalcul);
    }

    @Test
    @DisplayName("Devrait sauvegarder une facture et la retrouver par son ID")
    void shouldSaveAndFindFactureById() throws DatabaseException, ValidationException, ResourceNotFoundException {
        dao.save(testFacture);
        Optional<Facture> found = dao.findById(testFacture.getIdentifiant());
        assertTrue(found.isPresent());
        assertEquals("FACT-001", found.get().getNumero());
        assertEquals(LocalDate.now(), found.get().getDateEmission());
        assertEquals(LocalDate.now().plusDays(30), found.get().getDateEcheance());
        assertEquals(testClient, found.get().getClient());
        assertEquals(Integer.valueOf(1), found.get().getProduits().size());
        assertEquals(testCalcul, found.get().getCalcul());
    }

    @Test
    @DisplayName("Devrait retourner vide si la facture n'existe pas")
    void shouldReturnEmptyIfFactureNotFound() throws DatabaseException, ValidationException, ResourceNotFoundException {
        Optional<Facture> found = dao.findById(9999L);
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Devrait supprimer une facture")
    void shouldDeleteFacture() throws DatabaseException, ValidationException, ResourceNotFoundException {
        dao.save(testFacture);
        dao.delete(testFacture);
        Optional<Facture> found = dao.findById(testFacture.getIdentifiant());
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Devrait mettre à jour une facture")
    void shouldUpdateFacture() throws DatabaseException, ValidationException, ResourceNotFoundException {
        dao.save(testFacture);
        testFacture.setNumero("FACT-002");
        testFacture.setDateEmission(LocalDate.now().plusDays(1L));
        testFacture.setDateEcheance(LocalDate.now().plusDays(31L));
        dao.update(testFacture);
        Optional<Facture> found = dao.findById(testFacture.getIdentifiant());
        assertTrue(found.isPresent());
        assertEquals("FACT-002", found.get().getNumero());
        assertEquals(LocalDate.now().plusDays(1L), found.get().getDateEmission());
        assertEquals(LocalDate.now().plusDays(31L), found.get().getDateEcheance());
    }
} 