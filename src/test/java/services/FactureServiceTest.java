package services;

import dao.IDAO;
import dao.jpa.FactureJpaDAO;
import models.Facture;
import models.Produit;
import models.CalculFacture;
import models.Client;
import models.Adresse;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FactureServiceTest {

    @Mock
    private FactureJpaDAO factureDAO;

    @Mock
    private IDAO<Produit, Long> produitDAO;

    @Mock
    private IDAO<CalculFacture, Long> calculFactureDAO;

    private FactureService factureService;

    @BeforeEach
    void setUp() {
        factureService = new FactureService(factureDAO, produitDAO, calculFactureDAO);
    }

    @Nested
    @DisplayName("Tests de findById")
    class FindByIdTests {
        @Test
        @DisplayName("Devrait retourner la facture quand l'ID existe")
        void shouldReturnFactureWhenIdExists() throws ValidationException, DatabaseException {
            // Arrange
            Long id = 1L;
            Facture facture = new Facture("F001", LocalDate.now(), LocalDate.now().plusDays(30), createTestClient());
            when(factureDAO.findById(id)).thenReturn(Optional.of(facture));
            
            // Act
            Optional<Facture> result = factureService.findById(id);
            
            // Assert
            assertTrue(result.isPresent());
            assertEquals(facture, result.get());
            verify(factureDAO).findById(id);
        }
        
        @Test
        @DisplayName("Devrait retourner Optional.empty quand l'ID n'existe pas")
        void shouldReturnEmptyWhenIdDoesNotExist() throws ValidationException, DatabaseException {
            // Arrange
            Long id = 1L;
            when(factureDAO.findById(id)).thenReturn(Optional.empty());
            
            // Act
            Optional<Facture> result = factureService.findById(id);
            
            // Assert
            assertFalse(result.isPresent());
            verify(factureDAO).findById(id);
        }
        
        @Test
        @DisplayName("Devrait lancer ValidationException quand l'ID est null")
        void shouldThrowValidationExceptionWhenIdIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> factureService.findById(null));
            verify(factureDAO, never()).findById(any());
        }
        
        @Test
        @DisplayName("Devrait lancer DatabaseException en cas d'erreur de base de données")
        void shouldThrowDatabaseExceptionOnDatabaseError() {
            // Arrange
            Long id = 1L;
            when(factureDAO.findById(id)).thenThrow(new RuntimeException("Erreur DB"));
            
            // Act & Assert
            assertThrows(DatabaseException.class, () -> factureService.findById(id));
            verify(factureDAO).findById(id);
        }
    }

    @Nested
    @DisplayName("Tests de findAll")
    class FindAllTests {
        @Test
        @DisplayName("Devrait retourner toutes les factures")
        void shouldReturnAllFactures() throws DatabaseException {
            // Arrange
            List<Facture> factures = Arrays.asList(
                new Facture("F001", LocalDate.now(), LocalDate.now().plusDays(30), createTestClient()),
                new Facture("F002", LocalDate.now(), LocalDate.now().plusDays(30), createTestClient())
            );
            when(factureDAO.findAll()).thenReturn(factures);
            
            // Act
            List<Facture> result = factureService.findAll();
            
            // Assert
            assertEquals(factures, result);
            verify(factureDAO).findAll();
        }
        
        @Test
        @DisplayName("Devrait lancer DatabaseException en cas d'erreur de base de données")
        void shouldThrowDatabaseExceptionOnDatabaseError() {
            // Arrange
            when(factureDAO.findAll()).thenThrow(new RuntimeException("Erreur DB"));
            
            // Act & Assert
            assertThrows(DatabaseException.class, () -> factureService.findAll());
            verify(factureDAO).findAll();
        }
    }

    @Nested
    @DisplayName("Tests de findByClientId")
    class FindByClientIdTests {
        @Test
        @DisplayName("Devrait retourner les factures du client")
        void shouldReturnClientFactures() throws ValidationException, DatabaseException {
            // Arrange
            Long clientId = 1L;
            List<Facture> factures = Arrays.asList(
                new Facture("F001", LocalDate.now(), LocalDate.now().plusDays(30), createTestClient()),
                new Facture("F002", LocalDate.now(), LocalDate.now().plusDays(30), createTestClient())
            );
            when(factureDAO.findByClientId(clientId)).thenReturn(factures);
            
            // Act
            List<Facture> result = factureService.findByClientId(clientId);
            
            // Assert
            assertEquals(factures, result);
            verify(factureDAO).findByClientId(clientId);
        }
        
        @Test
        @DisplayName("Devrait lancer ValidationException quand l'ID client est null")
        void shouldThrowValidationExceptionWhenClientIdIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> factureService.findByClientId(null));
            verify(factureDAO, never()).findByClientId(any());
        }
    }

    @Nested
    @DisplayName("Tests de create")
    class CreateTests {
        private Client client;
        private LocalDate dateEmission;
        private LocalDate dateEcheance;
        
        @BeforeEach
        void setUp() {
            client = createTestClient();
            dateEmission = LocalDate.now();
            dateEcheance = LocalDate.now().plusDays(30);
        }
        
        @Test
        @DisplayName("Devrait créer une facture avec succès")
        void shouldCreateFactureSuccessfully() throws ValidationException, DatabaseException {
            // Arrange
            String numero = "F001";
            Facture facture = new Facture(numero, dateEmission, dateEcheance, client);
            when(factureDAO.save(any(Facture.class))).thenReturn(facture);
            
            // Act
            Facture result = factureService.create(numero, dateEmission, dateEcheance, client, null, null);
            
            // Assert
            assertNotNull(result);
            assertEquals(numero, result.getNumero());
            assertEquals(dateEmission, result.getDateEmission());
            assertEquals(dateEcheance, result.getDateEcheance());
            assertEquals(client, result.getClient());
            verify(factureDAO).save(any(Facture.class));
        }
        
        @Test
        @DisplayName("Devrait lancer ValidationException quand le numéro est vide")
        void shouldThrowValidationExceptionWhenNumeroIsEmpty() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> 
                factureService.create("", dateEmission, dateEcheance, client, null, null));
            verify(factureDAO, never()).save(any());
        }
        
        @Test
        @DisplayName("Devrait lancer ValidationException quand la date d'émission est null")
        void shouldThrowValidationExceptionWhenDateEmissionIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> 
                factureService.create("F001", null, dateEcheance, client, null, null));
            verify(factureDAO, never()).save(any());
        }
        
        @Test
        @DisplayName("Devrait lancer ValidationException quand la date d'échéance est null")
        void shouldThrowValidationExceptionWhenDateEcheanceIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> 
                factureService.create("F001", dateEmission, null, client, null, null));
            verify(factureDAO, never()).save(any());
        }
        
        @Test
        @DisplayName("Devrait lancer ValidationException quand le client est null")
        void shouldThrowValidationExceptionWhenClientIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> 
                factureService.create("F001", dateEmission, dateEcheance, null, null, null));
            verify(factureDAO, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Tests de update")
    class UpdateTests {
        @Test
        @DisplayName("Devrait mettre à jour une facture existante")
        void shouldUpdateExistingFacture() throws ValidationException, ResourceNotFoundException, DatabaseException {
            // Arrange
            Facture facture = new Facture("F001", LocalDate.now(), LocalDate.now().plusDays(30), createTestClient());
            facture.setId(1L);
            when(factureDAO.findById(1L)).thenReturn(Optional.of(facture));
            when(factureDAO.update(any(Facture.class))).thenReturn(facture);
            
            // Act
            Facture result = factureService.update(facture);
            
            // Assert
            assertNotNull(result);
            assertEquals(facture, result);
            verify(factureDAO).findById(1L);
            verify(factureDAO).update(facture);
        }
        
        @Test
        @DisplayName("Devrait lancer ValidationException quand la facture est null")
        void shouldThrowValidationExceptionWhenFactureIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> factureService.update(null));
            verify(factureDAO, never()).update(any());
        }
        
        @Test
        @DisplayName("Devrait lancer ValidationException quand l'ID de la facture est null")
        void shouldThrowValidationExceptionWhenFactureIdIsNull() {
            // Arrange
            Facture facture = new Facture("F001", LocalDate.now(), LocalDate.now().plusDays(30), createTestClient());
            
            // Act & Assert
            assertThrows(ValidationException.class, () -> factureService.update(facture));
            verify(factureDAO, never()).update(any());
        }
        
        @Test
        @DisplayName("Devrait lancer ResourceNotFoundException quand la facture n'existe pas")
        void shouldThrowResourceNotFoundExceptionWhenFactureDoesNotExist() {
            // Arrange
            Facture facture = new Facture("F001", LocalDate.now(), LocalDate.now().plusDays(30), createTestClient());
            facture.setId(1L);
            when(factureDAO.findById(1L)).thenReturn(Optional.empty());
            
            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> factureService.update(facture));
            verify(factureDAO).findById(1L);
            verify(factureDAO, never()).update(any());
        }
    }

    @Nested
    @DisplayName("Tests de delete")
    class DeleteTests {
        @Test
        @DisplayName("Devrait supprimer une facture existante")
        void shouldDeleteExistingFacture() throws ValidationException, ResourceNotFoundException, DatabaseException {
            // Arrange
            Long id = 1L;
            Facture facture = new Facture("F001", LocalDate.now(), LocalDate.now().plusDays(30), createTestClient());
            when(factureDAO.findById(id)).thenReturn(Optional.of(facture));
            
            // Act
            factureService.delete(id);
            
            // Assert
            verify(factureDAO).findById(id);
            verify(factureDAO).delete(facture);
        }
        
        @Test
        @DisplayName("Devrait lancer ValidationException quand l'ID est null")
        void shouldThrowValidationExceptionWhenIdIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> factureService.delete(null));
            verify(factureDAO, never()).delete(any());
        }
        
        @Test
        @DisplayName("Devrait lancer ResourceNotFoundException quand la facture n'existe pas")
        void shouldThrowResourceNotFoundExceptionWhenFactureDoesNotExist() {
            // Arrange
            Long id = 1L;
            when(factureDAO.findById(id)).thenReturn(Optional.empty());
            
            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> factureService.delete(id));
            verify(factureDAO).findById(id);
            verify(factureDAO, never()).delete(any());
        }
    }

    @Nested
    @DisplayName("Tests de findByNumeroFacture")
    class FindByNumeroFactureTests {
        @Test
        @DisplayName("Devrait retourner la facture quand le numéro existe")
        void shouldReturnFactureWhenNumeroExists() throws ValidationException, ResourceNotFoundException, DatabaseException {
            // Arrange
            String numero = "F001";
            Facture facture = new Facture(numero, LocalDate.now(), LocalDate.now().plusDays(30), createTestClient());
            when(factureDAO.findByNumeroFacture(numero)).thenReturn(facture);
            
            // Act
            Facture result = factureService.findByNumeroFacture(numero);
            
            // Assert
            assertNotNull(result);
            assertEquals(facture, result);
            verify(factureDAO).findByNumeroFacture(numero);
        }
        
        @Test
        @DisplayName("Devrait lancer ValidationException quand le numéro est vide")
        void shouldThrowValidationExceptionWhenNumeroIsEmpty() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> factureService.findByNumeroFacture(""));
            verify(factureDAO, never()).findByNumeroFacture(any());
        }
        
        @Test
        @DisplayName("Devrait lancer ValidationException quand le numéro est null")
        void shouldThrowValidationExceptionWhenNumeroIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> factureService.findByNumeroFacture(null));
            verify(factureDAO, never()).findByNumeroFacture(any());
        }
    }

    private Client createTestClient() {
        Adresse adresse = new Adresse("123", "Rue de Test", "75000", "Paris");
        return new Client("Test Client", adresse, "0123456789", "test@example.com", "Commentaire test", 1000.0, 10);
    }
} 