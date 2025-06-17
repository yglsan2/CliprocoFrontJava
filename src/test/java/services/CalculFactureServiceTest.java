package services;

import dao.IDAO;
import models.CalculFacture;
import models.Facture;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculFactureServiceTest {

    @Mock
    private IDAO<CalculFacture, Long> calculFactureDAO;

    private CalculFactureService calculFactureService;

    @BeforeEach
    void setUp() {
        calculFactureService = new CalculFactureService(calculFactureDAO);
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
                new BigDecimal("100.00"),
                new BigDecimal("20.00"),
                new BigDecimal("20.00"),
                new BigDecimal("120.00")
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
            Long id = 999L;
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
                    new BigDecimal("100.00"),
                    new BigDecimal("20.00"),
                    new BigDecimal("20.00"),
                    new BigDecimal("120.00")
                ),
                new CalculFacture(
                    new BigDecimal("200.00"),
                    new BigDecimal("20.00"),
                    new BigDecimal("40.00"),
                    new BigDecimal("240.00")
                )
            );
            when(calculFactureDAO.findAll()).thenReturn(calculs);

            // Act
            List<CalculFacture> result = calculFactureService.findAll();

            // Assert
            assertEquals(calculs, result);
            verify(calculFactureDAO).findAll();
        }
    }

    @Nested
    @DisplayName("Tests de create")
    class CreateTests {
        private Facture facture;
        private BigDecimal montantHT;
        private BigDecimal tauxTVA;
        private BigDecimal montantTVA;
        private BigDecimal montantTTC;

        @BeforeEach
        void setUp() {
            facture = mock(Facture.class);
            montantHT = new BigDecimal("100.00");
            tauxTVA = new BigDecimal("20.00");
            montantTVA = new BigDecimal("20.00");
            montantTTC = new BigDecimal("120.00");
        }

        @Test
        @DisplayName("Devrait créer un nouveau calcul avec succès")
        void shouldCreateNewCalculSuccessfully() throws ValidationException, DatabaseException {
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
        @DisplayName("Devrait lancer ValidationException quand montantHT est null")
        void shouldThrowValidationExceptionWhenMontantHTIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () ->
                calculFactureService.create(null, tauxTVA, montantTVA, montantTTC, facture)
            );
            verify(calculFactureDAO, never()).save(any());
        }

        @Test
        @DisplayName("Devrait lancer ValidationException quand tauxTVA est null")
        void shouldThrowValidationExceptionWhenTauxTVAIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () ->
                calculFactureService.create(montantHT, null, montantTVA, montantTTC, facture)
            );
            verify(calculFactureDAO, never()).save(any());
        }

        @Test
        @DisplayName("Devrait lancer ValidationException quand montantTVA est null")
        void shouldThrowValidationExceptionWhenMontantTVAIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () ->
                calculFactureService.create(montantHT, tauxTVA, null, montantTTC, facture)
            );
            verify(calculFactureDAO, never()).save(any());
        }

        @Test
        @DisplayName("Devrait lancer ValidationException quand montantTTC est null")
        void shouldThrowValidationExceptionWhenMontantTTCIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () ->
                calculFactureService.create(montantHT, tauxTVA, montantTVA, null, facture)
            );
            verify(calculFactureDAO, never()).save(any());
        }

        @Test
        @DisplayName("Devrait lancer ValidationException quand facture est null")
        void shouldThrowValidationExceptionWhenFactureIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () ->
                calculFactureService.create(montantHT, tauxTVA, montantTVA, montantTTC, null)
            );
            verify(calculFactureDAO, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Tests de update")
    class UpdateTests {
        @Test
        @DisplayName("Devrait mettre à jour le calcul avec succès")
        void shouldUpdateCalculSuccessfully() throws ValidationException, ResourceNotFoundException, DatabaseException {
            // Arrange
            Long id = 1L;
            CalculFacture calcul = new CalculFacture(
                new BigDecimal("100.00"),
                new BigDecimal("20.00"),
                new BigDecimal("20.00"),
                new BigDecimal("120.00")
            );
            when(calculFactureDAO.findById(id)).thenReturn(Optional.of(calcul));
            when(calculFactureDAO.update(calcul)).thenReturn(calcul);

            // Act
            CalculFacture result = calculFactureService.update(calcul);

            // Assert
            assertNotNull(result);
            assertEquals(calcul, result);
            verify(calculFactureDAO).findById(id);
            verify(calculFactureDAO).update(calcul);
        }

        @Test
        @DisplayName("Devrait lancer ValidationException quand calcul est null")
        void shouldThrowValidationExceptionWhenCalculIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> calculFactureService.update(null));
            verify(calculFactureDAO, never()).update(any());
        }

        @Test
        @DisplayName("Devrait lancer ResourceNotFoundException quand le calcul n'existe pas")
        void shouldThrowResourceNotFoundExceptionWhenCalculDoesNotExist() {
            // Arrange
            Long id = 999L;
            CalculFacture calcul = new CalculFacture(
                new BigDecimal("100.00"),
                new BigDecimal("20.00"),
                new BigDecimal("20.00"),
                new BigDecimal("120.00")
            );
            when(calculFactureDAO.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> calculFactureService.update(calcul));
            verify(calculFactureDAO, never()).update(any());
        }
    }

    @Nested
    @DisplayName("Tests de delete")
    class DeleteTests {
        @Test
        @DisplayName("Devrait supprimer le calcul avec succès")
        void shouldDeleteCalculSuccessfully() throws ValidationException, ResourceNotFoundException, DatabaseException {
            // Arrange
            Long id = 1L;
            CalculFacture calcul = new CalculFacture(
                new BigDecimal("100.00"),
                new BigDecimal("20.00"),
                new BigDecimal("20.00"),
                new BigDecimal("120.00")
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
            Long id = 999L;
            when(calculFactureDAO.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> calculFactureService.delete(id));
            verify(calculFactureDAO, never()).delete(any());
        }
    }
} 