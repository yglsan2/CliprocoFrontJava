package services;

import models.Societe;
import models.Adresse;
import dao.jpa.SocieteJpaDAO;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

// Classe concrète pour les tests
class TestSociete extends Societe {
    public TestSociete() {
        super();
    }

    public TestSociete(String raisonSoc, Adresse adr, String tel, String email, String comment) {
        super(raisonSoc, adr, tel, email, comment);
    }
}

public class SocieteServiceTest {
    private SocieteService societeService;

    @Mock
    private SocieteJpaDAO societeDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        societeService = new SocieteService(societeDao);
    }

    @Test
    public void shouldCreateSociete() throws DatabaseException, ValidationException {
        // Given
        TestSociete societe = new TestSociete();
        societe.setRaisonSociale("Test Company");
        societe.setMail("test@company.com");
        societe.setTelephone("0123456789");
        Adresse adresse = new Adresse();
        adresse.setNumeroRue("123");
        adresse.setNomRue("Rue Test");
        adresse.setCodePostal("75000");
        adresse.setVille("Paris");
        adresse.setPays("France");
        societe.setAdresse(adresse);

        doNothing().when(societeDao).save(any(Societe.class));

        // When
        societeService.save(societe);

        // Then
        verify(societeDao).save(any(Societe.class));
    }

    @Test
    public void shouldFindSocieteById() throws DatabaseException, ValidationException, ResourceNotFoundException {
        // Given
        TestSociete societe = new TestSociete();
        societe.setIdentifiant(1);
        societe.setRaisonSociale("Test Company");
        societe.setMail("test@company.com");

        when(societeDao.findById(1)).thenReturn(Optional.of(societe));

        // When
        Societe found = societeService.findById(1);

        // Then
        assertNotNull(found);
        assertEquals(1, found.getIdentifiant());
        assertEquals("Test Company", found.getRaisonSociale());
        assertEquals("test@company.com", found.getMail());
        verify(societeDao).findById(1);
    }

    @Test
    public void shouldThrowExceptionWhenSocieteNotFound() {
        // Given
        when(societeDao.findById(1)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            societeService.findById(1);
        });
    }

    @Test
    public void shouldUpdateSociete() throws DatabaseException, ValidationException, ResourceNotFoundException {
        // Given
        TestSociete societe = new TestSociete();
        societe.setIdentifiant(1);
        societe.setRaisonSociale("Test Company");
        societe.setMail("test@company.com");

        doNothing().when(societeDao).update(any(Societe.class));

        // When
        societeService.update(societe);

        // Then
        verify(societeDao).update(any(Societe.class));
    }

    @Test
    public void shouldDeleteSociete() throws DatabaseException, ValidationException, ResourceNotFoundException {
        // Given
        TestSociete societe = new TestSociete();
        societe.setIdentifiant(1);
        societe.setRaisonSociale("Test Company");

        doNothing().when(societeDao).delete(any(Societe.class));

        // When
        societeService.delete(societe);

        // Then
        verify(societeDao).delete(any(Societe.class));
    }

    @Test
    public void shouldFindAllSocietes() throws DatabaseException {
        // Given
        TestSociete societe1 = new TestSociete();
        societe1.setIdentifiant(1);
        societe1.setRaisonSociale("Company 1");

        TestSociete societe2 = new TestSociete();
        societe2.setIdentifiant(2);
        societe2.setRaisonSociale("Company 2");

        when(societeDao.findAll()).thenReturn(Arrays.asList(societe1, societe2));

        // When
        List<Societe> societes = societeService.findAll();

        // Then
        assertNotNull(societes);
        assertEquals(2, societes.size());
        verify(societeDao).findAll();
    }

    @Test
    public void shouldFindSocieteByRaisonSociale() throws DatabaseException, ValidationException {
        // Given
        TestSociete societe = new TestSociete();
        societe.setIdentifiant(1);
        societe.setRaisonSociale("Test Company");

        when(societeDao.findByRaisonSociale("Test Company")).thenReturn(Optional.of(societe));

        // When
        Optional<Societe> found = societeService.findByRaisonSociale("Test Company");

        // Then
        assertTrue(found.isPresent());
        assertEquals("Test Company", found.get().getRaisonSociale());
        verify(societeDao).findByRaisonSociale("Test Company");
    }
} 