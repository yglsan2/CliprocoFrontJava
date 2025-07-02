package services;

import models.Adresse;
import dao.jpa.AdresseJpaDAO;
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

public class AdresseServiceTest {
    private AdresseService adresseService;

    @Mock
    private AdresseJpaDAO adresseDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        adresseService = new AdresseService(adresseDao);
    }

    @Test
    public void shouldCreateAdresse() throws DatabaseException, ValidationException {
        // Given
        Adresse adresse = new Adresse();
        adresse.setNumeroRue("123");
        adresse.setNomRue("Rue Test");
        adresse.setCodePostal("75000");
        adresse.setVille("Paris");
        adresse.setPays("France");

        doNothing().when(adresseDao).save(any(Adresse.class));

        // When
        adresseService.save(adresse);

        // Then
        verify(adresseDao).save(any(Adresse.class));
    }

    @Test
    public void shouldFindAdresseById() throws DatabaseException, ValidationException, ResourceNotFoundException {
        // Given
        Adresse adresse = new Adresse();
        adresse.setIdentifiant(1);
        adresse.setNumeroRue("123");
        adresse.setNomRue("Rue Test");
        adresse.setCodePostal("75000");
        adresse.setVille("Paris");
        adresse.setPays("France");

        when(adresseDao.findById(1)).thenReturn(Optional.of(adresse));

        // When
        Adresse found = adresseService.findById(1);

        // Then
        assertNotNull(found);
        assertEquals(1, found.getIdentifiant());
        assertEquals("123", found.getNumeroRue());
        assertEquals("Rue Test", found.getNomRue());
        assertEquals("75000", found.getCodePostal());
        assertEquals("Paris", found.getVille());
        assertEquals("France", found.getPays());
        verify(adresseDao).findById(1);
    }

    @Test
    public void shouldThrowExceptionWhenAdresseNotFound() {
        // Given
        when(adresseDao.findById(1)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            adresseService.findById(1);
        });
    }

    @Test
    public void shouldUpdateAdresse() throws DatabaseException, ValidationException, ResourceNotFoundException {
        // Given
        Adresse adresse = new Adresse();
        adresse.setIdentifiant(1);
        adresse.setNumeroRue("123");
        adresse.setNomRue("Rue Test");
        adresse.setCodePostal("75000");
        adresse.setVille("Paris");
        adresse.setPays("France");

        when(adresseDao.existsById(1)).thenReturn(true);
        doNothing().when(adresseDao).update(any(Adresse.class));

        // When
        adresseService.update(adresse);

        // Then
        verify(adresseDao).update(any(Adresse.class));
    }

    @Test
    public void shouldDeleteAdresse() throws DatabaseException, ValidationException, ResourceNotFoundException {
        // Given
        Adresse adresse = new Adresse();
        adresse.setIdentifiant(1);
        adresse.setNumeroRue("123");
        adresse.setNomRue("Rue Test");

        when(adresseDao.existsById(1)).thenReturn(true);
        doNothing().when(adresseDao).delete(any(Adresse.class));

        // When
        adresseService.delete(adresse);

        // Then
        verify(adresseDao).delete(any(Adresse.class));
    }

    @Test
    public void shouldFindAllAdresses() throws DatabaseException {
        // Given
        Adresse adresse1 = new Adresse();
        adresse1.setIdentifiant(1);
        adresse1.setNumeroRue("123");
        adresse1.setNomRue("Rue Test 1");

        Adresse adresse2 = new Adresse();
        adresse2.setIdentifiant(2);
        adresse2.setNumeroRue("456");
        adresse2.setNomRue("Rue Test 2");

        when(adresseDao.findAll()).thenReturn(Arrays.asList(adresse1, adresse2));

        // When
        List<Adresse> adresses = adresseService.findAll();

        // Then
        assertNotNull(adresses);
        assertEquals(2, adresses.size());
        verify(adresseDao).findAll();
    }

    @Test
    public void shouldFindAdresseByVille() throws DatabaseException, ValidationException {
        // Given
        Adresse adresse = new Adresse();
        adresse.setIdentifiant(1);
        adresse.setNumeroRue("123");
        adresse.setNomRue("Rue Test");
        adresse.setVille("Paris");

        when(adresseDao.findByVille("Paris")).thenReturn(Arrays.asList(adresse));

        // When
        List<Adresse> found = adresseService.findByVille("Paris");

        // Then
        assertNotNull(found);
        assertEquals(1, found.size());
        assertEquals("Paris", found.get(0).getVille());
        verify(adresseDao).findByVille("Paris");
    }
} 