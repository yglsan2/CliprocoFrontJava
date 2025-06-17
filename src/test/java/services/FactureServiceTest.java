package services;

import dao.IDAO;
import models.Facture;
import models.Produit;
import models.CalculFacture;
import models.Client;
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
    private IDAO<Facture, Long> factureDAO;

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
        @DisplayName("Devrait retourner une facture quand l'ID existe")
        void shouldReturnFactureWhenIdExists() throws ValidationException, DatabaseException {
            // Arrange
            Long id = 1L;
            Facture expectedFacture = new Facture();
            expectedFacture.setId(id);
            when(factureDAO.findById(id)).thenReturn(Optional.of(expectedFacture));

            // Act
            Optional<Facture> result = factureService.findById(id);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(expectedFacture, result.get());
            verify(factureDAO).findById(id);
        }

        @Test
        @DisplayName("Devrait lever une ValidationException quand l'ID est null")
        void shouldThrowValidationExceptionWhenIdIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> factureService.findById(null));
            verify(factureDAO, never()).findById(any());
        }
    }

    @Nested
    @DisplayName("Tests de findAll")
    class FindAllTests {
        @Test
        @DisplayName("Devrait retourner la liste de toutes les factures")
        void shouldReturnAllFactures() throws DatabaseException {
            // Arrange
            List<Facture> expectedFactures = Arrays.asList(
                new Facture(), new Facture(), new Facture()
            );
            when(factureDAO.findAll()).thenReturn(expectedFactures);

            // Act
            List<Facture> result = factureService.findAll();

            // Assert
            assertEquals(expectedFactures.size(), result.size());
            assertEquals(expectedFactures, result);
            verify(factureDAO).findAll();
        }

        @Test
        @DisplayName("Devrait retourner une liste vide quand il n'y a pas de factures")
        void shouldReturnEmptyListWhenNoFactures() throws DatabaseException {
            // Arrange
            when(factureDAO.findAll()).thenReturn(List.of());

            // Act
            List<Facture> result = factureService.findAll();

            // Assert
            assertTrue(result.isEmpty());
            verify(factureDAO).findAll();
        }
    }

    @Nested
    @DisplayName("Tests de create")
    class CreateTests {
        @Test
        @DisplayName("Devrait créer une facture avec succès")
        void shouldCreateFactureSuccessfully() throws ValidationException, DatabaseException {
            // Arrange
            String numero = "FACT-001";
            LocalDate dateEmission = LocalDate.now();
            LocalDate dateEcheance = LocalDate.now().plusDays(30);
            Client client = new Client();
            List<Produit> produits = Arrays.asList(
                new Produit("Produit 1", "Description 1", BigDecimal.TEN),
                new Produit("Produit 2", "Description 2", BigDecimal.valueOf(20))
            );
            CalculFacture calcul = new CalculFacture(
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(0.20),
                BigDecimal.valueOf(20),
                BigDecimal.valueOf(120)
            );

            Facture expectedFacture = new Facture(numero, dateEmission, dateEcheance, client);
            when(factureDAO.save(any(Facture.class))).thenReturn(expectedFacture);

            // Act
            Facture result = factureService.create(numero, dateEmission, dateEcheance, client, produits, calcul);

            // Assert
            assertNotNull(result);
            assertEquals(numero, result.getNumero());
            assertEquals(dateEmission, result.getDateEmission());
            assertEquals(dateEcheance, result.getDateEcheance());
            assertEquals(client, result.getClient());
            verify(factureDAO).save(any(Facture.class));
        }

        @Test
        @DisplayName("Devrait lever une ValidationException quand le numéro est null")
        void shouldThrowValidationExceptionWhenNumeroIsNull() {
            // Arrange
            LocalDate dateEmission = LocalDate.now();
            LocalDate dateEcheance = LocalDate.now().plusDays(30);
            Client client = new Client();

            // Act & Assert
            assertThrows(ValidationException.class, () -> 
                factureService.create(null, dateEmission, dateEcheance, client, null, null));
            verify(factureDAO, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Tests de update")
    class UpdateTests {
        @Test
        @DisplayName("Devrait mettre à jour une facture avec succès")
        void shouldUpdateFactureSuccessfully() throws ValidationException, ResourceNotFoundException, DatabaseException {
            // Arrange
            Facture facture = new Facture();
            facture.setId(1L);
            when(factureDAO.findById(1L)).thenReturn(Optional.of(facture));
            when(factureDAO.update(any(Facture.class))).thenReturn(facture);

            // Act
            Facture result = factureService.update(facture);

            // Assert
            assertNotNull(result);
            verify(factureDAO).findById(1L);
            verify(factureDAO).update(facture);
        }

        @Test
        @DisplayName("Devrait lever une ValidationException quand la facture est null")
        void shouldThrowValidationExceptionWhenFactureIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> factureService.update(null));
            verify(factureDAO, never()).update(any());
        }

        @Test
        @DisplayName("Devrait lever une ResourceNotFoundException quand la facture n'existe pas")
        void shouldThrowResourceNotFoundExceptionWhenFactureDoesNotExist() {
            // Arrange
            Facture facture = new Facture();
            facture.setId(1L);
            when(factureDAO.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> factureService.update(facture));
            verify(factureDAO, never()).update(any());
        }
    }

    @Nested
    @DisplayName("Tests de delete")
    class DeleteTests {
        @Test
        @DisplayName("Devrait supprimer une facture avec succès")
        void shouldDeleteFactureSuccessfully() throws ValidationException, ResourceNotFoundException, DatabaseException {
            // Arrange
            Long id = 1L;
            Facture facture = new Facture();
            facture.setId(id);
            when(factureDAO.findById(id)).thenReturn(Optional.of(facture));

            // Act
            factureService.delete(id);

            // Assert
            verify(factureDAO).findById(id);
            verify(factureDAO).delete(facture);
        }

        @Test
        @DisplayName("Devrait lever une ValidationException quand l'ID est null")
        void shouldThrowValidationExceptionWhenIdIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> factureService.delete(null));
            verify(factureDAO, never()).delete(any());
        }

        @Test
        @DisplayName("Devrait lever une ResourceNotFoundException quand la facture n'existe pas")
        void shouldThrowResourceNotFoundExceptionWhenFactureDoesNotExist() {
            // Arrange
            Long id = 1L;
            when(factureDAO.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> factureService.delete(id));
            verify(factureDAO, never()).delete(any());
        }
    }

    @Nested
    @DisplayName("Tests de findByNumeroFacture")
    class FindByNumeroFactureTests {
        @Test
        @DisplayName("Devrait trouver une facture par son numéro")
        void shouldFindFactureByNumero() throws ValidationException, ResourceNotFoundException, DatabaseException {
            // Arrange
            String numero = "FACT-001";
            Facture expectedFacture = new Facture();
            expectedFacture.setNumero(numero);
            when(((FactureJpaDAO) factureDAO).findByNumeroFacture(numero)).thenReturn(expectedFacture);

            // Act
            Facture result = factureService.findByNumeroFacture(numero);

            // Assert
            assertNotNull(result);
            assertEquals(numero, result.getNumero());
            verify(((FactureJpaDAO) factureDAO)).findByNumeroFacture(numero);
        }

        @Test
        @DisplayName("Devrait lever une ValidationException quand le numéro est null")
        void shouldThrowValidationExceptionWhenNumeroIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> factureService.findByNumeroFacture(null));
            verify(((FactureJpaDAO) factureDAO), never()).findByNumeroFacture(any());
        }
    }
} 