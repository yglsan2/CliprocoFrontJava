package dao;

import dao.jpa.ContratJpaDAO;
import models.Contrat;
import models.Client;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ContratJpaDAOTest {
    private ContratJpaDAO dao;
    private Contrat testContrat;
    private Client testClient;

    @BeforeEach
    void setUp() throws ValidationException {
        dao = new ContratJpaDAO();
        testClient = new Client("Test Client", "12 Rue de Paris, 75000 Paris, France", "0123456789", "client@test.com");
        testContrat = new Contrat(testClient, "Contrat Test", new BigDecimal("1000.00"));
        testContrat.setDateDebut(LocalDate.now());
        testContrat.setDateFin(LocalDate.now().plusYears(1L));
    }

    @Test
    @DisplayName("Devrait sauvegarder un contrat et le retrouver par son ID")
    void shouldSaveAndFindContratById() throws DatabaseException, ValidationException, ResourceNotFoundException {
        dao.save(testContrat);
        Optional<Contrat> found = dao.findById(testContrat.getIdentifiant());
        assertTrue(found.isPresent());
        assertEquals("Contrat Test", found.get().getLibelle());
        assertEquals(new BigDecimal("1000.00"), found.get().getMontant());
        assertEquals(testClient, found.get().getClient());
        assertEquals(LocalDate.now(), found.get().getDateDebut());
        assertEquals(LocalDate.now().plusYears(1L), found.get().getDateFin());
    }

    @Test
    @DisplayName("Devrait retourner vide si le contrat n'existe pas")
    void shouldReturnEmptyIfContratNotFound() throws DatabaseException, ValidationException, ResourceNotFoundException {
        Optional<Contrat> found = dao.findById(9999L);
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Devrait supprimer un contrat")
    void shouldDeleteContrat() throws DatabaseException, ValidationException, ResourceNotFoundException {
        dao.save(testContrat);
        dao.delete(testContrat);
        Optional<Contrat> found = dao.findById(testContrat.getIdentifiant());
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Devrait mettre à jour un contrat")
    void shouldUpdateContrat() throws DatabaseException, ValidationException, ResourceNotFoundException {
        dao.save(testContrat);
        testContrat.setLibelle("Contrat Modifié");
        testContrat.setMontant(new BigDecimal("2000.00"));
        testContrat.setDateFin(LocalDate.now().plusYears(2));
        dao.update(testContrat);
        Optional<Contrat> found = dao.findById(testContrat.getIdentifiant());
        assertTrue(found.isPresent());
        assertEquals("Contrat Modifié", found.get().getLibelle());
        assertEquals(new BigDecimal("2000.00"), found.get().getMontant());
        assertEquals(LocalDate.now().plusYears(2), found.get().getDateFin());
    }

    @Test
    @DisplayName("Devrait trouver tous les contrats d'un client")
    void shouldFindContratsByClient() throws DatabaseException, ValidationException, ResourceNotFoundException {
        dao.save(testContrat);
        List<Contrat> contrats = dao.findByClient(testClient);
        assertFalse(contrats.isEmpty());
        assertEquals(Integer.valueOf(1), contrats.size());
        assertEquals(testContrat.getIdentifiant(), contrats.get(0).getIdentifiant());
    }
} 