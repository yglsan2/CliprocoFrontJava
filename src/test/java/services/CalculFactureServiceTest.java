package services;

import dao.IDAO;
import models.CalculFacture;
import models.Facture;
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
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculFactureServiceTest {

    @Mock
    private IDAO<CalculFacture, Long> calculFactureDAO;

    @InjectMocks
    private CalculFactureService calculFactureService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Tests de findById")
    class FindByIdTests {
        @Test
        @DisplayName("Devrait retourner le calcul quand l'ID existe")
        void shouldReturnCalculWhenIdExists() throws ValidationException, DatabaseException {
            // Arrange
            Long id = 1L;
            CalculFacture calcul = new CalculFacture(
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(0.20),
                BigDecimal.valueOf(20),
                BigDecimal.valueOf(120)
            );
            when(calculFactureDAO.findById(id)).thenReturn(Optional.of(calcul));
            
            // Act
            Optional<CalculFacture> result = calculFactureService.findById(id);
            
            // Assert
            assertTrue(result.isPresent());
            assertEquals(calcul, result.get());
            verify(calculFactureDAO).findById(id);
        }
        
        @Test
        @DisplayName("Devrait retourner Optional.empty quand l'ID n'existe pas")
        void shouldReturnEmptyWhenIdDoesNotExist() throws ValidationException, DatabaseException {
            // Arrange
            Long id = 1L;
            when(calculFactureDAO.findById(id)).thenReturn(Optional.empty());
            
            // Act
            Optional<CalculFacture> result = calculFactureService.findById(id);
            
            // Assert
            assertFalse(result.isPresent());
            verify(calculFactureDAO).findById(id);
        }
        
        @Test
        @DisplayName("Devrait lancer ValidationException quand l'ID est null")
        void shouldThrowValidationExceptionWhenIdIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> calculFactureService.findById(null));
            verify(calculFactureDAO, never()).findById(any());
        }
        
        @Test
        @DisplayName("Devrait lancer DatabaseException en cas d'erreur de base de données")
        void shouldThrowDatabaseExceptionOnDatabaseError() {
            // Arrange
            Long id = 1L;
            when(calculFactureDAO.findById(id)).thenThrow(new RuntimeException("Erreur DB"));
            
            // Act & Assert
            assertThrows(DatabaseException.class, () -> calculFactureService.findById(id));
            verify(calculFactureDAO).findById(id);
        }
    }

    @Nested
    @DisplayName("Tests de findAll")
    class FindAllTests {
        @Test
        @DisplayName("Devrait retourner tous les calculs")
        void shouldReturnAllCalculs() throws DatabaseException {
            // Arrange
            List<CalculFacture> calculs = Arrays.asList(
                new CalculFacture(
                    BigDecimal.valueOf(100),
                    BigDecimal.valueOf(0.20),
                    BigDecimal.valueOf(20),
                    BigDecimal.valueOf(120)
                ),
                new CalculFacture(
                    BigDecimal.valueOf(200),
                    BigDecimal.valueOf(0.20),
                    BigDecimal.valueOf(40),
                    BigDecimal.valueOf(240)
                )
            );
            when(calculFactureDAO.findAll()).thenReturn(calculs);
            
            // Act
            List<CalculFacture> result = calculFactureService.findAll();
            
            // Assert
            assertEquals(calculs, result);
            verify(calculFactureDAO).findAll();
        }
        
        @Test
        @DisplayName("Devrait lancer DatabaseException en cas d'erreur de base de données")
        void shouldThrowDatabaseExceptionOnDatabaseError() {
            // Arrange
            when(calculFactureDAO.findAll()).thenThrow(new RuntimeException("Erreur DB"));
            
            // Act & Assert
            assertThrows(DatabaseException.class, () -> calculFactureService.findAll());
            verify(calculFactureDAO).findAll();
        }
    }

    @Nested
    @DisplayName("Tests de create")
    class CreateTests {
        private BigDecimal montantHT;
        private BigDecimal tauxTVA;
        private BigDecimal montantTVA;
        private BigDecimal montantTTC;
        private Facture facture;
        
        @BeforeEach
        void setUp() {
            montantHT = BigDecimal.valueOf(100);
            tauxTVA = BigDecimal.valueOf(0.20);
            montantTVA = BigDecimal.valueOf(20);
            montantTTC = BigDecimal.valueOf(120);
            facture = createTestFacture();
        }
        
        @Test
        @DisplayName("Devrait créer un calcul avec succès")
        void shouldCreateCalculSuccessfully() throws ValidationException, DatabaseException {
            // Arrange
            CalculFacture calcul = new CalculFacture(montantHT, tauxTVA, montantTVA, montantTTC);
            when(calculFactureDAO.save(any(CalculFacture.class))).thenReturn(calcul);
            
            // Act
            CalculFacture result = calculFactureService.create(montantHT, tauxTVA, montantTVA, montantTTC, facture);
            
            // Assert
            assertNotNull(result);
            assertEquals(montantHT, result.getMontantHT());
            assertEquals(tauxTVA, result.getTauxTVA());
            assertEquals(montantTVA, result.getMontantTVA());
            assertEquals(montantTTC, result.getMontantTTC());
            verify(calculFactureDAO).save(any(CalculFacture.class));
        }
        
        @Test
        @DisplayName("Devrait lancer ValidationException quand le montant HT est null")
        void shouldThrowValidationExceptionWhenMontantHTIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> 
                calculFactureService.create(null, tauxTVA, montantTVA, montantTTC, facture));
            verify(calculFactureDAO, never()).save(any());
        }
        
        @Test
        @DisplayName("Devrait lancer ValidationException quand le taux de TVA est null")
        void shouldThrowValidationExceptionWhenTauxTVAIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> 
                calculFactureService.create(montantHT, null, montantTVA, montantTTC, facture));
            verify(calculFactureDAO, never()).save(any());
        }
        
        @Test
        @DisplayName("Devrait lancer ValidationException quand le montant de TVA est null")
        void shouldThrowValidationExceptionWhenMontantTVAIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> 
                calculFactureService.create(montantHT, tauxTVA, null, montantTTC, facture));
            verify(calculFactureDAO, never()).save(any());
        }
        
        @Test
        @DisplayName("Devrait lancer ValidationException quand le montant TTC est null")
        void shouldThrowValidationExceptionWhenMontantTTCIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> 
                calculFactureService.create(montantHT, tauxTVA, montantTVA, null, facture));
            verify(calculFactureDAO, never()).save(any());
        }
        
        @Test
        @DisplayName("Devrait lancer ValidationException quand la facture est null")
        void shouldThrowValidationExceptionWhenFactureIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> 
                calculFactureService.create(montantHT, tauxTVA, montantTVA, montantTTC, null));
            verify(calculFactureDAO, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Tests de update")
    class UpdateTests {
        @Test
        @DisplayName("Devrait mettre à jour un calcul existant")
        void shouldUpdateExistingCalcul() throws ValidationException, ResourceNotFoundException, DatabaseException {
            // Arrange
            CalculFacture calcul = new CalculFacture(
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(0.20),
                BigDecimal.valueOf(20),
                BigDecimal.valueOf(120)
            );
            calcul.setId(1L);
            when(calculFactureDAO.findById(1L)).thenReturn(Optional.of(calcul));
            when(calculFactureDAO.update(any(CalculFacture.class))).thenReturn(calcul);
            
            // Act
            CalculFacture result = calculFactureService.update(calcul);
            
            // Assert
            assertNotNull(result);
            assertEquals(calcul, result);
            verify(calculFactureDAO).findById(1L);
            verify(calculFactureDAO).update(calcul);
        }
        
        @Test
        @DisplayName("Devrait lancer ValidationException quand le calcul est null")
        void shouldThrowValidationExceptionWhenCalculIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> calculFactureService.update(null));
            verify(calculFactureDAO, never()).update(any());
        }
        
        @Test
        @DisplayName("Devrait lancer ValidationException quand l'ID du calcul est null")
        void shouldThrowValidationExceptionWhenCalculIdIsNull() {
            // Arrange
            CalculFacture calcul = new CalculFacture(
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(0.20),
                BigDecimal.valueOf(20),
                BigDecimal.valueOf(120)
            );
            
            // Act & Assert
            assertThrows(ValidationException.class, () -> calculFactureService.update(calcul));
            verify(calculFactureDAO, never()).update(any());
        }
        
        @Test
        @DisplayName("Devrait lancer ResourceNotFoundException quand le calcul n'existe pas")
        void shouldThrowResourceNotFoundExceptionWhenCalculDoesNotExist() {
            // Arrange
            CalculFacture calcul = new CalculFacture(
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(0.20),
                BigDecimal.valueOf(20),
                BigDecimal.valueOf(120)
            );
            calcul.setId(1L);
            when(calculFactureDAO.findById(1L)).thenReturn(Optional.empty());
            
            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> calculFactureService.update(calcul));
            verify(calculFactureDAO).findById(1L);
            verify(calculFactureDAO, never()).update(any());
        }
    }

    @Nested
    @DisplayName("Tests de delete")
    class DeleteTests {
        @Test
        @DisplayName("Devrait supprimer un calcul existant")
        void shouldDeleteExistingCalcul() throws ValidationException, ResourceNotFoundException, DatabaseException {
            // Arrange
            Long id = 1L;
            CalculFacture calcul = new CalculFacture(
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(0.20),
                BigDecimal.valueOf(20),
                BigDecimal.valueOf(120)
            );
            when(calculFactureDAO.findById(id)).thenReturn(Optional.of(calcul));
            
            // Act
            calculFactureService.delete(id);
            
            // Assert
            verify(calculFactureDAO).findById(id);
            verify(calculFactureDAO).delete(calcul);
        }
        
        @Test
        @DisplayName("Devrait lancer ValidationException quand l'ID est null")
        void shouldThrowValidationExceptionWhenIdIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> calculFactureService.delete(null));
            verify(calculFactureDAO, never()).delete(any());
        }
        
        @Test
        @DisplayName("Devrait lancer ResourceNotFoundException quand le calcul n'existe pas")
        void shouldThrowResourceNotFoundExceptionWhenCalculDoesNotExist() {
            // Arrange
            Long id = 1L;
            when(calculFactureDAO.findById(id)).thenReturn(Optional.empty());
            
            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> calculFactureService.delete(id));
            verify(calculFactureDAO).findById(id);
            verify(calculFactureDAO, never()).delete(any());
        }
    }

    private Facture createTestFacture() {
        Adresse adresse = new Adresse("123", "Rue de Test", "75000", "Paris");
        Client client = new Client("Test Client", adresse, "0123456789", "test@example.com", "Commentaire test", 1000.0, 10);
        return new Facture("F001", LocalDate.now(), LocalDate.now().plusDays(30), client);
    }
} 