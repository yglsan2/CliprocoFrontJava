package dao;

import dao.jpa.CalculFactureJpaDAO;
import models.CalculFacture;
import models.Facture;
import models.Client;
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

class CalculFactureJpaDAOTest {
    private CalculFactureJpaDAO dao;
    private CalculFacture testCalcul;
    private Facture testFacture;
    private Client testClient;

    @BeforeEach
    void setUp() throws ValidationException {
        dao = new CalculFactureJpaDAO();
        testClient = new Client("Test Client", "12 Rue de Paris, 75000 Paris, France", "0123456789", "client@test.com");
        testFacture = new Facture("FACT-001", LocalDate.now(), LocalDate.now().plusDays(30), testClient);
        testCalcul = new CalculFacture(
            new BigDecimal("1000.00"),
            new BigDecimal("20.00"),
            new BigDecimal("200.00"),
            new BigDecimal("1200.00")
        );
        testFacture.setCalcul(testCalcul);
    }

    @Test
    @DisplayName("Devrait sauvegarder un calcul et le retrouver par son ID")
    void shouldSaveAndFindCalculById() throws DatabaseException, ValidationException, ResourceNotFoundException {
        dao.save(testCalcul);
        Optional<CalculFacture> found = dao.findById(testCalcul.getIdentifiant());
        assertTrue(found.isPresent());
        assertEquals(new BigDecimal("1000.00"), found.get().getMontantHT());
        assertEquals(new BigDecimal("20.00"), found.get().getTauxTVA());
        assertEquals(new BigDecimal("200.00"), found.get().getMontantTVA());
        assertEquals(new BigDecimal("1200.00"), found.get().getMontantTTC());
    }

    @Test
    @DisplayName("Devrait retourner vide si le calcul n'existe pas")
    void shouldReturnEmptyIfCalculNotFound() throws DatabaseException, ValidationException, ResourceNotFoundException {
        Optional<CalculFacture> found = dao.findById(9999L);
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Devrait supprimer un calcul")
    void shouldDeleteCalcul() throws DatabaseException, ValidationException, ResourceNotFoundException {
        dao.save(testCalcul);
        dao.delete(testCalcul);
        Optional<CalculFacture> found = dao.findById(testCalcul.getIdentifiant());
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Devrait mettre à jour un calcul")
    void shouldUpdateCalcul() throws DatabaseException, ValidationException, ResourceNotFoundException {
        dao.save(testCalcul);
        testCalcul.setMontantHT(new BigDecimal("2000.00"));
        testCalcul.setTauxTVA(new BigDecimal("10.00"));
        testCalcul.setMontantTVA(new BigDecimal("200.00"));
        testCalcul.setMontantTTC(new BigDecimal("2200.00"));
        dao.update(testCalcul);
        Optional<CalculFacture> found = dao.findById(testCalcul.getIdentifiant());
        assertTrue(found.isPresent());
        assertEquals(new BigDecimal("2000.00"), found.get().getMontantHT());
        assertEquals(new BigDecimal("10.00"), found.get().getTauxTVA());
        assertEquals(new BigDecimal("200.00"), found.get().getMontantTVA());
        assertEquals(new BigDecimal("2200.00"), found.get().getMontantTTC());
    }
} 