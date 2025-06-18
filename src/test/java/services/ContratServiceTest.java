package services;

import models.Contrat;
import models.Client;
import dao.jpa.ContratJpaDAO;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ContratServiceTest {
    private ContratService contratService;

    @Mock
    private ContratJpaDAO contratDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        contratService = new ContratService(contratDao);
    }

    @Test
    public void shouldCreateContrat() throws DatabaseException, ValidationException {
        // Given
        Contrat contrat = new Contrat();
        contrat.setLibelle("Test Contract");
        contrat.setMontant(new BigDecimal("1000.0"));
        Client client = new Client();
        client.setIdentifiant(1);
        client.setRaisonSociale("Test Company");
        contrat.setClient(client);
        contrat.setDateDebut(LocalDate.now());
        contrat.setDateFin(LocalDate.now().plusYears(1));

        doNothing().when(contratDao).save(any(Contrat.class));

        // When
        contratService.save(contrat);

        // Then
        verify(contratDao).save(any(Contrat.class));
    }

    @Test
    public void shouldFindContratById() throws DatabaseException, ValidationException, ResourceNotFoundException {
        // Given
        Contrat contrat = new Contrat();
        contrat.setIdentifiant(1);
        contrat.setLibelle("Test Contract");
        contrat.setMontant(new BigDecimal("1000.0"));

        when(contratDao.findById(1)).thenReturn(Optional.of(contrat));

        // When
        Contrat found = contratService.findById(1);

        // Then
        assertNotNull(found);
        assertEquals(1, found.getIdentifiant());
        assertEquals("Test Contract", found.getLibelle());
        assertEquals(new BigDecimal("1000.0"), found.getMontant());
        verify(contratDao).findById(1);
    }

    @Test
    public void shouldThrowExceptionWhenContratNotFound() {
        // Given
        when(contratDao.findById(1)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            contratService.findById(1);
        });
    }

    @Test
    public void shouldUpdateContrat() throws DatabaseException, ValidationException, ResourceNotFoundException {
        // Given
        Contrat contrat = new Contrat();
        contrat.setIdentifiant(1);
        contrat.setLibelle("Test Contract");
        contrat.setMontant(new BigDecimal("1000.0"));

        doNothing().when(contratDao).update(any(Contrat.class));

        // When
        contratService.update(contrat);

        // Then
        verify(contratDao).update(any(Contrat.class));
    }

    @Test
    public void shouldDeleteContrat() throws DatabaseException, ValidationException, ResourceNotFoundException {
        // Given
        Contrat contrat = new Contrat();
        contrat.setIdentifiant(1);
        contrat.setLibelle("Test Contract");

        doNothing().when(contratDao).delete(any(Contrat.class));

        // When
        contratService.delete(contrat);

        // Then
        verify(contratDao).delete(any(Contrat.class));
    }

    @Test
    public void shouldFindAllContrats() throws DatabaseException {
        // Given
        Contrat contrat1 = new Contrat();
        contrat1.setIdentifiant(1);
        contrat1.setLibelle("Contract 1");

        Contrat contrat2 = new Contrat();
        contrat2.setIdentifiant(2);
        contrat2.setLibelle("Contract 2");

        when(contratDao.findAll()).thenReturn(Arrays.asList(contrat1, contrat2));

        // When
        List<Contrat> contrats = contratService.findAll();

        // Then
        assertNotNull(contrats);
        assertEquals(2, contrats.size());
        verify(contratDao).findAll();
    }

    @Test
    public void shouldFindContratsByClient() throws DatabaseException, ValidationException {
        // Given
        Client client = new Client();
        client.setIdentifiant(1);
        client.setRaisonSociale("Test Company");

        Contrat contrat = new Contrat();
        contrat.setIdentifiant(1);
        contrat.setLibelle("Test Contract");
        contrat.setClient(client);

        when(contratDao.findByClient(client)).thenReturn(Arrays.asList(contrat));

        // When
        List<Contrat> found = contratService.findByClient(client);

        // Then
        assertNotNull(found);
        assertEquals(1, found.size());
        assertEquals("Test Contract", found.get(0).getLibelle());
        verify(contratDao).findByClient(client);
    }
} 