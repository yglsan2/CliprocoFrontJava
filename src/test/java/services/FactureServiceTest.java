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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FactureServiceTest {
    @Nested
    @DisplayName("Tests de performance")
    class PerformanceTests {
    @Nested
    @DisplayName("Tests de concurrence")
    class ConcurrencyTests {
        private Client client;
        private List<Produit> produits;
        private CalculFacture calcul;
        private ExecutorService executorService;
        private CountDownLatch latch;

        @BeforeEach
        void setUp() {
            client = new Client();
            client.setRaisonSociale("Test Client");
            client.setEmail("test@example.com");
            client.setTelephone("0123456789");
            client.setChiffreAffaires(1000.0);
            client.setNombreEmployes(10);
            
            produits = new ArrayList<>();
            Produit produit1 = new Produit();
            produit1.setNom("Produit 1");
            produit1.setDescription("Description du produit 1");
            produit1.setPrixUnitaire(BigDecimal.valueOf(100));
            produits.add(produit1);
            
            Produit produit2 = new Produit();
            produit2.setNom("Produit 2");
            produit2.setDescription("Description du produit 2");
            produit2.setPrixUnitaire(BigDecimal.valueOf(200));
            produits.add(produit2);
            
            calcul = new CalculFacture(
                BigDecimal.valueOf(300),
                BigDecimal.valueOf(0.20),
                BigDecimal.valueOf(60),
                BigDecimal.valueOf(360)
            );
            
            executorService = Executors.newFixedThreadPool(10);
            latch = new CountDownLatch(10);
        }

        @AfterEach
        void tearDown() {
            executorService.shutdown();
        }

        @Test
        @DisplayName("Devrait gérer correctement les accès concurrents à la création de factures")
        void shouldHandleConcurrentFactureCreation() throws InterruptedException {
            LocalDate dateEmission = LocalDate.now();
            LocalDate dateEcheance = dateEmission.plusDays(30);
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failureCount = new AtomicInteger(0);

            for (int i = 0; i < 10; i++) {
                final int index = i;
                executorService.submit(() -> {
                    try {
                        when(factureDAO.save(any(Facture.class))).thenReturn(
                            new Facture("FACT-" + index, dateEmission, dateEcheance, client)
                        );
                        
                        factureService.create(
                            "FACT-" + index,
                            dateEmission,
                            dateEcheance,
                            client,
                            produits,
                            calcul
                        );
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(5, TimeUnit.SECONDS);
            assertEquals(10, successCount.get() + failureCount.get());
        }

        @Test
        @DisplayName("Devrait gérer correctement les mises à jour concurrentes")
        void shouldHandleConcurrentUpdates() throws InterruptedException, ValidationException, DatabaseException {
            Facture facture = new Facture("FACT-001", LocalDate.now(), LocalDate.now().plusDays(30), client);
            when(factureDAO.findById(any())).thenReturn(Optional.of(facture));
            when(factureDAO.save(any(Facture.class))).thenReturn(facture);
            
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failureCount = new AtomicInteger(0);

            for (int i = 0; i < 10; i++) {
                executorService.submit(() -> {
                    try {
                        facture.setDateEcheance(LocalDate.now().plusDays(30 + i));
                        factureService.update(facture);
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(5, TimeUnit.SECONDS);
            assertEquals(10, successCount.get() + failureCount.get());
        }

        @Test
        @DisplayName("Devrait gérer correctement les suppressions concurrentes")
        void shouldHandleConcurrentDeletions() throws InterruptedException, ValidationException, DatabaseException {
            Facture facture = new Facture("FACT-001", LocalDate.now(), LocalDate.now().plusDays(30), client);
            when(factureDAO.findById(any())).thenReturn(Optional.of(facture));
            
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failureCount = new AtomicInteger(0);

            for (int i = 0; i < 10; i++) {
                executorService.submit(() -> {
                    try {
                        factureService.delete(1L);
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(5, TimeUnit.SECONDS);
            assertEquals(10, successCount.get() + failureCount.get());
        }
    }
        private List<Client> clients;
        private List<Produit> produits;
        private List<CalculFacture> calculs;

        @BeforeEach
        void setUp() {
            clients = new ArrayList<>();
            produits = new ArrayList<>();
            calculs = new ArrayList<>();
            
            // Création de 1000 clients de test
            for (int i = 0; i < 1000; i++) {
                Client client = new Client();
                client.setRaisonSociale("Client " + i);
                client.setEmail("client" + i + "@test.com");
                client.setTelephone("0123456789");
                client.setChiffreAffaires(1000.0);
                client.setNombreEmployes(10);
                clients.add(client);
            }
            
            // Création de 100 produits de test
            for (int i = 0; i < 100; i++) {
                Produit produit = new Produit();
                produit.setNom("Produit " + i);
                produit.setDescription("Description du produit " + i);
                produit.setPrixUnitaire(BigDecimal.valueOf(100 + i));
                produits.add(produit);
            }
            
            // Création de 100 calculs de test
            for (int i = 0; i < 100; i++) {
                calculs.add(new CalculFacture(
                    BigDecimal.valueOf(1000 + i),
                    BigDecimal.valueOf(0.20),
                    BigDecimal.valueOf(200 + i),
                    BigDecimal.valueOf(1200 + i)
                ));
            }
        }

        @Test
        @DisplayName("Devrait gérer efficacement la création de 1000 factures")
        void shouldHandleBulkFactureCreation() throws ValidationException, DatabaseException {
            List<Facture> factures = new ArrayList<>();
            LocalDate dateEmission = LocalDate.now();
            LocalDate dateEcheance = dateEmission.plusDays(30);

            for (int i = 0; i < 1000; i++) {
                Client client = clients.get(i % clients.size());
                List<Produit> factureProduits = produits.subList(0, 5);
                CalculFacture calcul = calculs.get(i % calculs.size());
                
                when(factureDAO.save(any(Facture.class))).thenReturn(
                    new Facture("FACT-" + i, dateEmission, dateEcheance, client)
                );
                
                Facture facture = factureService.create(
                    "FACT-" + i,
                    dateEmission,
                    dateEcheance,
                    client,
                    factureProduits,
                    calcul
                );
                factures.add(facture);
            }

            assertEquals(1000, factures.size());
            verify(factureDAO, times(1000)).save(any(Facture.class));
        }

        @Test
        @DisplayName("Devrait gérer efficacement la recherche de factures par client")
        void shouldHandleEfficientClientFactureSearch() throws ValidationException, DatabaseException {
            // Création de 100 factures pour un client
            Client client = clients.get(0);
            List<Facture> clientFactures = new ArrayList<>();
            
            for (int i = 0; i < 100; i++) {
                Facture facture = new Facture(
                    "FACT-" + i,
                    LocalDate.now(),
                    LocalDate.now().plusDays(30),
                    client
                );
                clientFactures.add(facture);
            }
            
            when(factureDAO.findByClientId(any())).thenReturn(clientFactures);
            
            List<Facture> foundFactures = factureService.findByClientId(1L);
            
            assertEquals(100, foundFactures.size());
            verify(factureDAO, times(1)).findByClientId(any());
        }

        @Test
        @DisplayName("Devrait gérer efficacement la mise à jour en masse")
        void shouldHandleBulkUpdate() throws ValidationException, ResourceNotFoundException, DatabaseException {
            List<Facture> factures = new ArrayList<>();
            LocalDate dateEmission = LocalDate.now();
            LocalDate dateEcheance = dateEmission.plusDays(30);

            // Création de 100 factures
            for (int i = 0; i < 100; i++) {
                Client client = clients.get(i % clients.size());
                Facture facture = new Facture("FACT-" + i, dateEmission, dateEcheance, client);
                factures.add(facture);
            }

            when(factureDAO.findById(any())).thenReturn(Optional.of(factures.get(0)));
            when(factureDAO.save(any(Facture.class))).thenReturn(factures.get(0));

            // Mise à jour de toutes les factures
            for (Facture facture : factures) {
                facture.setDateEcheance(dateEcheance.plusDays(15));
                factureService.update(facture);
            }

            verify(factureDAO, times(100)).save(any(Facture.class));
        }
    }
    @Nested
    @DisplayName("Tests de transactions")
    class TransactionTests {
        private Facture facture;
        private Client client;
        private List<Produit> produits;
        private CalculFacture calcul;

        @BeforeEach
        void setUp() {
            client = new Client("Test Client", "test@example.com");
            produits = Arrays.asList(
                new Produit("Produit 1", BigDecimal.valueOf(100)),
                new Produit("Produit 2", BigDecimal.valueOf(200))
            );
            calcul = new CalculFacture(
                BigDecimal.valueOf(300),
                BigDecimal.valueOf(0.20),
                BigDecimal.valueOf(60),
                BigDecimal.valueOf(360)
            );
            facture = new Facture("FACT-001", LocalDate.now(), LocalDate.now().plusDays(30), client);
        }

        @Test
        @DisplayName("Devrait gérer correctement la création d'une facture avec tous ses éléments")
        void shouldHandleFactureCreationWithAllElements() throws ValidationException, DatabaseException {
            when(factureDAO.save(any(Facture.class))).thenReturn(facture);
            when(produitDAO.save(any(Produit.class))).thenReturn(produits.get(0));
            when(calculFactureDAO.save(any(CalculFacture.class))).thenReturn(calcul);

            Facture savedFacture = factureService.create("FACT-001", LocalDate.now(), LocalDate.now().plusDays(30), client, produits, calcul);

            assertNotNull(savedFacture);
            assertEquals(client, savedFacture.getClient());
            assertEquals(produits.size(), savedFacture.getProduits().size());
            assertEquals(calcul, savedFacture.getCalcul());
        }

        @Test
        @DisplayName("Devrait gérer correctement la suppression d'une facture et ses éléments associés")
        void shouldHandleFactureDeletionWithAssociatedElements() throws ValidationException, DatabaseException, ResourceNotFoundException {
            when(factureDAO.findById(any())).thenReturn(Optional.of(facture));

            factureService.delete(1L);

            verify(factureDAO).delete(any(Facture.class));
        }
    }
    @Nested
    @DisplayName("Tests de validation métier")
    class ValidationMetierTests {
    @Nested
    @DisplayName("Tests des relations")
    class RelationTests {
    @Nested
    @DisplayName("Tests de cas limites")
    class CasLimitesTests {
        private Client client;
        private List<Produit> produits;
        private CalculFacture calcul;

        @BeforeEach
        void setUp() {
            client = new Client("Test Client", "test@example.com");
            produits = Arrays.asList(
                new Produit("Produit 1", BigDecimal.valueOf(100)),
                new Produit("Produit 2", BigDecimal.valueOf(200))
            );
            calcul = new CalculFacture(
                BigDecimal.valueOf(300),
                BigDecimal.valueOf(0.20),
                BigDecimal.valueOf(60),
                BigDecimal.valueOf(360)
            );
        }

        @Test
        @DisplayName("Devrait gérer correctement les montants nuls")
        void shouldHandleNullAmounts() throws ValidationException, DatabaseException {
            CalculFacture calculAvecMontantsNuls = new CalculFacture(
                null,
                BigDecimal.valueOf(0.20),
                null,
                null
            );
            assertThrows(ValidationException.class, () ->
                factureService.create("FACT-001", LocalDate.now(), LocalDate.now().plusDays(30), client, produits, calculAvecMontantsNuls)
            );
        }

        @Test
        @DisplayName("Devrait gérer correctement les dates invalides")
        void shouldHandleInvalidDates() throws ValidationException, DatabaseException {
            LocalDate dateEmission = LocalDate.now();
            LocalDate dateEcheance = dateEmission.minusDays(1);
            assertThrows(ValidationException.class, () ->
                factureService.create("FACT-001", dateEmission, dateEcheance, client, produits, calcul)
            );
        }

        @Test
        @DisplayName("Devrait gérer correctement les produits avec montants négatifs")
        void shouldHandleNegativeAmounts() throws ValidationException, DatabaseException {
            List<Produit> produitsNegatifs = Arrays.asList(
                new Produit("Produit 1", BigDecimal.valueOf(-100)),
                new Produit("Produit 2", BigDecimal.valueOf(-200))
            );
            assertThrows(ValidationException.class, () ->
                factureService.create("FACT-001", LocalDate.now(), LocalDate.now().plusDays(30), client, produitsNegatifs, calcul)
            );
        }
    }
        private Facture facture;
        private Client client;
        private List<Produit> produits;
        private CalculFacture calcul;

        @BeforeEach
        void setUp() {
            client = new Client("Test Client", "test@example.com");
            produits = Arrays.asList(
                new Produit("Produit 1", BigDecimal.valueOf(100)),
                new Produit("Produit 2", BigDecimal.valueOf(200))
            );
            calcul = new CalculFacture(
                BigDecimal.valueOf(300),
                BigDecimal.valueOf(0.20),
                BigDecimal.valueOf(60),
                BigDecimal.valueOf(360)
            );
            facture = new Facture("FACT-001", LocalDate.now(), LocalDate.now().plusDays(30), client);
        }

        @Test
        @DisplayName("Devrait maintenir la relation bidirectionnelle entre Facture et CalculFacture")
        void shouldMaintainBidirectionalRelationBetweenFactureAndCalculFacture() throws ValidationException, DatabaseException {
            when(factureDAO.save(any(Facture.class))).thenReturn(facture);
            Facture savedFacture = factureService.create("FACT-001", LocalDate.now(), LocalDate.now().plusDays(30), client, produits, calcul);
            assertNotNull(savedFacture.getCalcul());
            assertEquals(calcul, savedFacture.getCalcul());
            assertEquals(savedFacture, calcul.getFacture());
        }

        @Test
        @DisplayName("Devrait maintenir la relation entre Facture et Produits")
        void shouldMaintainRelationBetweenFactureAndProduits() throws ValidationException, DatabaseException {
            when(factureDAO.save(any(Facture.class))).thenReturn(facture);
            Facture savedFacture = factureService.create("FACT-001", LocalDate.now(), LocalDate.now().plusDays(30), client, produits, calcul);
            assertNotNull(savedFacture.getProduits());
            assertEquals(produits.size(), savedFacture.getProduits().size());
            assertTrue(savedFacture.getProduits().containsAll(produits));
        }

        @Test
        @DisplayName("Devrait maintenir la relation entre Facture et Client")
        void shouldMaintainRelationBetweenFactureAndClient() throws ValidationException, DatabaseException {
            when(factureDAO.save(any(Facture.class))).thenReturn(facture);
            Facture savedFacture = factureService.create("FACT-001", LocalDate.now(), LocalDate.now().plusDays(30), client, produits, calcul);
            assertNotNull(savedFacture.getClient());
            assertEquals(client, savedFacture.getClient());
        }
    }
        private LocalDate dateEmission;
        private LocalDate dateEcheance;
        private Client client;
        private List<Produit> produits;
        private CalculFacture calcul;

        @BeforeEach
        void setUp() {
            dateEmission = LocalDate.now();
            dateEcheance = LocalDate.now().plusDays(30);
            client = new Client("Test Client", "test@example.com");
            produits = new ArrayList<>();
            calcul = new CalculFacture(
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(0.20),
                BigDecimal.valueOf(20),
                BigDecimal.valueOf(120)
            );
        }

        @Test
        @DisplayName("Devrait lancer ValidationException quand la date d'échéance est antérieure à la date d'émission")
        void shouldThrowValidationExceptionWhenDateEcheanceIsBeforeDateEmission() throws ValidationException, DatabaseException {
            LocalDate dateEcheanceInvalide = dateEmission.minusDays(1);
            assertThrows(ValidationException.class, () ->
                factureService.create("FACT-001", dateEmission, dateEcheanceInvalide, client, produits, calcul)
            );
        }

        @Test
        @DisplayName("Devrait lancer ValidationException quand le numéro de facture est invalide")
        void shouldThrowValidationExceptionWhenNumeroFactureIsInvalid() throws ValidationException, DatabaseException {
            String numeroFactureInvalide = "INVALID";
            assertThrows(ValidationException.class, () ->
                factureService.create(numeroFactureInvalide, dateEmission, dateEcheance, client, produits, calcul)
            );
        }

        @Test
        @DisplayName("Devrait lancer ValidationException quand la liste de produits est vide")
        void shouldThrowValidationExceptionWhenProduitsListIsEmpty() throws ValidationException, DatabaseException {
            List<Produit> produitsVides = new ArrayList<>();
            assertThrows(ValidationException.class, () ->
                factureService.create("FACT-001", dateEmission, dateEcheance, client, produitsVides, calcul)
            );
        }

        @Test
        @DisplayName("Devrait lancer ValidationException quand le calcul est null")
        void shouldThrowValidationExceptionWhenCalculIsNull() throws ValidationException, DatabaseException {
            assertThrows(ValidationException.class, () ->
                factureService.create("FACT-001", dateEmission, dateEcheance, client, produits, null)
            );
        }
    }

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

    @Nested
    @DisplayName("Tests de sécurité")
    class SecurityTests {
        private Client client;
        private List<Produit> produits;
        private CalculFacture calcul;

        @BeforeEach
        void setUp() {
            client = new Client();
            client.setRaisonSociale("Test Client");
            client.setEmail("test@example.com");
            client.setTelephone("0123456789");
            client.setChiffreAffaires(1000.0);
            client.setNombreEmployes(10);
            
            produits = new ArrayList<>();
            Produit produit1 = new Produit();
            produit1.setNom("Produit 1");
            produit1.setDescription("Description du produit 1");
            produit1.setPrixUnitaire(BigDecimal.valueOf(100));
            produits.add(produit1);
            
            Produit produit2 = new Produit();
            produit2.setNom("Produit 2");
            produit2.setDescription("Description du produit 2");
            produit2.setPrixUnitaire(BigDecimal.valueOf(200));
            produits.add(produit2);
            
            calcul = new CalculFacture(
                BigDecimal.valueOf(300),
                BigDecimal.valueOf(0.20),
                BigDecimal.valueOf(60),
                BigDecimal.valueOf(360)
            );
        }

        @Test
        @DisplayName("Devrait rejeter les tentatives d'injection SQL dans le numéro de facture")
        void shouldRejectSqlInjectionInFactureNumber() {
            String[] sqlInjectionAttempts = {
                "FACT-001'; DROP TABLE factures; --",
                "FACT-001' OR '1'='1",
                "FACT-001'; SELECT * FROM users; --"
            };

            for (String injection : sqlInjectionAttempts) {
                assertThrows(ValidationException.class, () ->
                    factureService.create(
                        injection,
                        LocalDate.now(),
                        LocalDate.now().plusDays(30),
                        client,
                        produits,
                        calcul
                    )
                );
            }
        }

        @Test
        @DisplayName("Devrait rejeter les tentatives d'injection XSS dans les commentaires")
        void shouldRejectXssInjectionInComments() {
            String[] xssInjectionAttempts = {
                "<script>alert('XSS')</script>",
                "javascript:alert('XSS')",
                "<img src='x' onerror='alert(\"XSS\")'>"
            };

            for (String injection : xssInjectionAttempts) {
                assertThrows(ValidationException.class, () -> {
                    Facture facture = new Facture(
                        "FACT-001",
                        LocalDate.now(),
                        LocalDate.now().plusDays(30),
                        client
                    );
                    facture.setCommentaire(injection);
                    factureService.update(facture);
                });
            }
        }

        @Test
        @DisplayName("Devrait rejeter les tentatives d'injection de commandes système")
        void shouldRejectCommandInjection() {
            String[] commandInjectionAttempts = {
                "FACT-001; rm -rf /",
                "FACT-001 && cat /etc/passwd",
                "FACT-001 | ls -la"
            };

            for (String injection : commandInjectionAttempts) {
                assertThrows(ValidationException.class, () ->
                    factureService.create(
                        injection,
                        LocalDate.now(),
                        LocalDate.now().plusDays(30),
                        client,
                        produits,
                        calcul
                    )
                );
            }
        }

        @Test
        @DisplayName("Devrait valider le format des montants")
        void shouldValidateAmountFormat() {
            String[] invalidAmounts = {
                "100,000.00",
                "1e6",
                "NaN",
                "Infinity",
                "1.234.567,89"
            };

            for (String amount : invalidAmounts) {
                assertThrows(ValidationException.class, () -> {
                    Produit produit = new Produit("Produit Test", new BigDecimal(amount));
                    List<Produit> produitsWithInvalidAmount = Arrays.asList(produit);
                    factureService.create(
                        "FACT-001",
                        LocalDate.now(),
                        LocalDate.now().plusDays(30),
                        client,
                        produitsWithInvalidAmount,
                        calcul
                    );
                });
            }
        }
    }

    private Client createTestClient() {
        Adresse adresse = new Adresse("123", "Rue de Test", "75000", "Paris");
        return new Client("Test Client", adresse, "0123456789", "test@example.com", "Commentaire test", 1000.0, 10);
    }

    @Nested
    @DisplayName("Tests de performance")
    class PerformanceTests {
    @Nested
    @DisplayName("Tests de concurrence")
    class ConcurrencyTests {
        private Client client;
        private List<Produit> produits;
        private CalculFacture calcul;
        private ExecutorService executorService;
        private CountDownLatch latch;

        @BeforeEach
        void setUp() {
            client = new Client();
            client.setRaisonSociale("Test Client");
            client.setEmail("test@example.com");
            client.setTelephone("0123456789");
            client.setChiffreAffaires(1000.0);
            client.setNombreEmployes(10);
            
            produits = new ArrayList<>();
            Produit produit1 = new Produit();
            produit1.setNom("Produit 1");
            produit1.setDescription("Description du produit 1");
            produit1.setPrixUnitaire(BigDecimal.valueOf(100));
            produits.add(produit1);
            
            Produit produit2 = new Produit();
            produit2.setNom("Produit 2");
            produit2.setDescription("Description du produit 2");
            produit2.setPrixUnitaire(BigDecimal.valueOf(200));
            produits.add(produit2);
            
            calcul = new CalculFacture(
                BigDecimal.valueOf(300),
                BigDecimal.valueOf(0.20),
                BigDecimal.valueOf(60),
                BigDecimal.valueOf(360)
            );
            
            executorService = Executors.newFixedThreadPool(10);
            latch = new CountDownLatch(10);
        }

        @AfterEach
        void tearDown() {
            executorService.shutdown();
        }

        @Test
        @DisplayName("Devrait gérer correctement les accès concurrents à la création de factures")
        void shouldHandleConcurrentFactureCreation() throws InterruptedException {
            LocalDate dateEmission = LocalDate.now();
            LocalDate dateEcheance = dateEmission.plusDays(30);
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failureCount = new AtomicInteger(0);

            for (int i = 0; i < 10; i++) {
                final int index = i;
                executorService.submit(() -> {
                    try {
                        when(factureDAO.save(any(Facture.class))).thenReturn(
                            new Facture("FACT-" + index, dateEmission, dateEcheance, client)
                        );
                        
                        factureService.create(
                            "FACT-" + index,
                            dateEmission,
                            dateEcheance,
                            client,
                            produits,
                            calcul
                        );
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(5, TimeUnit.SECONDS);
            assertEquals(10, successCount.get() + failureCount.get());
        }

        @Test
        @DisplayName("Devrait gérer correctement les mises à jour concurrentes")
        void shouldHandleConcurrentUpdates() throws InterruptedException, ValidationException, DatabaseException {
            Facture facture = new Facture("FACT-001", LocalDate.now(), LocalDate.now().plusDays(30), client);
            when(factureDAO.findById(any())).thenReturn(Optional.of(facture));
            when(factureDAO.save(any(Facture.class))).thenReturn(facture);
            
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failureCount = new AtomicInteger(0);

            for (int i = 0; i < 10; i++) {
                executorService.submit(() -> {
                    try {
                        facture.setDateEcheance(LocalDate.now().plusDays(30 + i));
                        factureService.update(facture);
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(5, TimeUnit.SECONDS);
            assertEquals(10, successCount.get() + failureCount.get());
        }

        @Test
        @DisplayName("Devrait gérer correctement les suppressions concurrentes")
        void shouldHandleConcurrentDeletions() throws InterruptedException, ValidationException, DatabaseException {
            Facture facture = new Facture("FACT-001", LocalDate.now(), LocalDate.now().plusDays(30), client);
            when(factureDAO.findById(any())).thenReturn(Optional.of(facture));
            
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failureCount = new AtomicInteger(0);

            for (int i = 0; i < 10; i++) {
                executorService.submit(() -> {
                    try {
                        factureService.delete(1L);
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(5, TimeUnit.SECONDS);
            assertEquals(10, successCount.get() + failureCount.get());
        }
    }
        private List<Client> clients;
        private List<Produit> produits;
        private List<CalculFacture> calculs;

        @BeforeEach
        void setUp() {
            clients = new ArrayList<>();
            produits = new ArrayList<>();
            calculs = new ArrayList<>();
            
            // Création de 1000 clients de test
            for (int i = 0; i < 1000; i++) {
                Client client = new Client();
                client.setRaisonSociale("Client " + i);
                client.setEmail("client" + i + "@test.com");
                client.setTelephone("0123456789");
                client.setChiffreAffaires(1000.0);
                client.setNombreEmployes(10);
                clients.add(client);
            }
            
            // Création de 100 produits de test
            for (int i = 0; i < 100; i++) {
                Produit produit = new Produit();
                produit.setNom("Produit " + i);
                produit.setDescription("Description du produit " + i);
                produit.setPrixUnitaire(BigDecimal.valueOf(100 + i));
                produits.add(produit);
            }
            
            // Création de 100 calculs de test
            for (int i = 0; i < 100; i++) {
                calculs.add(new CalculFacture(
                    BigDecimal.valueOf(1000 + i),
                    BigDecimal.valueOf(0.20),
                    BigDecimal.valueOf(200 + i),
                    BigDecimal.valueOf(1200 + i)
                ));
            }
        }

        @Test
        @DisplayName("Devrait gérer efficacement la création de 1000 factures")
        void shouldHandleBulkFactureCreation() throws ValidationException, DatabaseException {
            List<Facture> factures = new ArrayList<>();
            LocalDate dateEmission = LocalDate.now();
            LocalDate dateEcheance = dateEmission.plusDays(30);

            for (int i = 0; i < 1000; i++) {
                Client client = clients.get(i % clients.size());
                List<Produit> factureProduits = produits.subList(0, 5);
                CalculFacture calcul = calculs.get(i % calculs.size());
                
                when(factureDAO.save(any(Facture.class))).thenReturn(
                    new Facture("FACT-" + i, dateEmission, dateEcheance, client)
                );
                
                Facture facture = factureService.create(
                    "FACT-" + i,
                    dateEmission,
                    dateEcheance,
                    client,
                    factureProduits,
                    calcul
                );
                factures.add(facture);
            }

            assertEquals(1000, factures.size());
            verify(factureDAO, times(1000)).save(any(Facture.class));
        }

        @Test
        @DisplayName("Devrait gérer efficacement la recherche de factures par client")
        void shouldHandleEfficientClientFactureSearch() throws ValidationException, DatabaseException {
            // Création de 100 factures pour un client
            Client client = clients.get(0);
            List<Facture> clientFactures = new ArrayList<>();
            
            for (int i = 0; i < 100; i++) {
                Facture facture = new Facture(
                    "FACT-" + i,
                    LocalDate.now(),
                    LocalDate.now().plusDays(30),
                    client
                );
                clientFactures.add(facture);
            }
            
            when(factureDAO.findByClientId(any())).thenReturn(clientFactures);
            
            List<Facture> foundFactures = factureService.findByClientId(1L);
            
            assertEquals(100, foundFactures.size());
            verify(factureDAO, times(1)).findByClientId(any());
        }

        @Test
        @DisplayName("Devrait gérer efficacement la mise à jour en masse")
        void shouldHandleBulkUpdate() throws ValidationException, ResourceNotFoundException, DatabaseException {
            List<Facture> factures = new ArrayList<>();
            LocalDate dateEmission = LocalDate.now();
            LocalDate dateEcheance = dateEmission.plusDays(30);

            // Création de 100 factures
            for (int i = 0; i < 100; i++) {
                Client client = clients.get(i % clients.size());
                Facture facture = new Facture("FACT-" + i, dateEmission, dateEcheance, client);
                factures.add(facture);
            }

            when(factureDAO.findById(any())).thenReturn(Optional.of(factures.get(0)));
            when(factureDAO.save(any(Facture.class))).thenReturn(factures.get(0));

            // Mise à jour de toutes les factures
            for (Facture facture : factures) {
                facture.setDateEcheance(dateEcheance.plusDays(15));
                factureService.update(facture);
            }

            verify(factureDAO, times(100)).save(any(Facture.class));
        }
    }

    @Nested
    @DisplayName("Tests de concurrence")
    class ConcurrencyTests {
    @Nested
    @DisplayName("Tests de sécurité")
    class SecurityTests {
        private Client client;
        private List<Produit> produits;
        private CalculFacture calcul;

        @BeforeEach
        void setUp() {
            client = new Client();
            client.setRaisonSociale("Test Client");
            client.setEmail("test@example.com");
            client.setTelephone("0123456789");
            client.setChiffreAffaires(1000.0);
            client.setNombreEmployes(10);
            
            produits = new ArrayList<>();
            Produit produit1 = new Produit();
            produit1.setNom("Produit 1");
            produit1.setDescription("Description du produit 1");
            produit1.setPrixUnitaire(BigDecimal.valueOf(100));
            produits.add(produit1);
            
            Produit produit2 = new Produit();
            produit2.setNom("Produit 2");
            produit2.setDescription("Description du produit 2");
            produit2.setPrixUnitaire(BigDecimal.valueOf(200));
            produits.add(produit2);
            
            calcul = new CalculFacture(
                BigDecimal.valueOf(300),
                BigDecimal.valueOf(0.20),
                BigDecimal.valueOf(60),
                BigDecimal.valueOf(360)
            );
        }

        @Test
        @DisplayName("Devrait rejeter les tentatives d'injection SQL dans le numéro de facture")
        void shouldRejectSqlInjectionInFactureNumber() {
            String[] sqlInjectionAttempts = {
                "FACT-001'; DROP TABLE factures; --",
                "FACT-001' OR '1'='1",
                "FACT-001'; SELECT * FROM users; --"
            };

            for (String injection : sqlInjectionAttempts) {
                assertThrows(ValidationException.class, () ->
                    factureService.create(
                        injection,
                        LocalDate.now(),
                        LocalDate.now().plusDays(30),
                        client,
                        produits,
                        calcul
                    )
                );
            }
        }

        @Test
        @DisplayName("Devrait rejeter les tentatives d'injection XSS dans les commentaires")
        void shouldRejectXssInjectionInComments() {
            String[] xssInjectionAttempts = {
                "<script>alert('XSS')</script>",
                "javascript:alert('XSS')",
                "<img src='x' onerror='alert("XSS")'>"
            };

            for (String injection : xssInjectionAttempts) {
                assertThrows(ValidationException.class, () -> {
                    Facture facture = new Facture(
                        "FACT-001",
                        LocalDate.now(),
                        LocalDate.now().plusDays(30),
                        client
                    );
                    facture.setCommentaire(injection);
                    factureService.update(facture);
                });
            }
        }

        @Test
        @DisplayName("Devrait rejeter les tentatives d'injection de commandes système")
        void shouldRejectCommandInjection() {
            String[] commandInjectionAttempts = {
                "FACT-001; rm -rf /",
                "FACT-001 && cat /etc/passwd",
                "FACT-001 | ls -la"
            };

            for (String injection : commandInjectionAttempts) {
                assertThrows(ValidationException.class, () ->
                    factureService.create(
                        injection,
                        LocalDate.now(),
                        LocalDate.now().plusDays(30),
                        client,
                        produits,
                        calcul
                    )
                );
            }
        }

        @Test
        @DisplayName("Devrait valider le format des montants")
        void shouldValidateAmountFormat() {
            String[] invalidAmounts = {
                "100,000.00",
                "1e6",
                "NaN",
                "Infinity",
                "1.234.567,89"
            };

            for (String amount : invalidAmounts) {
                assertThrows(ValidationException.class, () -> {
                    Produit produit = new Produit();
                    produit.setNom("Produit Test");
                    produit.setDescription("Description test");
                    produit.setPrixUnitaire(new BigDecimal(amount));
                    List<Produit> produitsWithInvalidAmount = Arrays.asList(produit);
                    factureService.create(
                        "FACT-001",
                        LocalDate.now(),
                        LocalDate.now().plusDays(30),
                        client,
                        produitsWithInvalidAmount,
                        calcul
                    );
                });
            }
        }
    }
        private Client client;
        private List<Produit> produits;
        private CalculFacture calcul;
        private ExecutorService executorService;
        private CountDownLatch latch;

        @BeforeEach
        void setUp() {
            client = new Client();
            client.setRaisonSociale("Test Client");
            client.setEmail("test@example.com");
            client.setTelephone("0123456789");
            client.setChiffreAffaires(1000.0);
            client.setNombreEmployes(10);
            
            produits = new ArrayList<>();
            Produit produit1 = new Produit();
            produit1.setNom("Produit 1");
            produit1.setDescription("Description du produit 1");
            produit1.setPrixUnitaire(BigDecimal.valueOf(100));
            produits.add(produit1);
            
            Produit produit2 = new Produit();
            produit2.setNom("Produit 2");
            produit2.setDescription("Description du produit 2");
            produit2.setPrixUnitaire(BigDecimal.valueOf(200));
            produits.add(produit2);
            
            calcul = new CalculFacture(
                BigDecimal.valueOf(300),
                BigDecimal.valueOf(0.20),
                BigDecimal.valueOf(60),
                BigDecimal.valueOf(360)
            );
            
            executorService = Executors.newFixedThreadPool(10);
            latch = new CountDownLatch(10);
        }

        @AfterEach
        void tearDown() {
            executorService.shutdown();
        }

        @Test
        @DisplayName("Devrait gérer correctement les accès concurrents à la création de factures")
        void shouldHandleConcurrentFactureCreation() throws InterruptedException {
            LocalDate dateEmission = LocalDate.now();
            LocalDate dateEcheance = dateEmission.plusDays(30);
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failureCount = new AtomicInteger(0);

            for (int i = 0; i < 10; i++) {
                final int index = i;
                executorService.submit(() -> {
                    try {
                        when(factureDAO.save(any(Facture.class))).thenReturn(
                            new Facture("FACT-" + index, dateEmission, dateEcheance, client)
                        );
                        
                        factureService.create(
                            "FACT-" + index,
                            dateEmission,
                            dateEcheance,
                            client,
                            produits,
                            calcul
                        );
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(5, TimeUnit.SECONDS);
            assertEquals(10, successCount.get() + failureCount.get());
        }

        @Test
        @DisplayName("Devrait gérer correctement les mises à jour concurrentes")
        void shouldHandleConcurrentUpdates() throws InterruptedException, ValidationException, DatabaseException {
            Facture facture = new Facture("FACT-001", LocalDate.now(), LocalDate.now().plusDays(30), client);
            when(factureDAO.findById(any())).thenReturn(Optional.of(facture));
            when(factureDAO.save(any(Facture.class))).thenReturn(facture);
            
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failureCount = new AtomicInteger(0);

            for (int i = 0; i < 10; i++) {
                executorService.submit(() -> {
                    try {
                        facture.setDateEcheance(LocalDate.now().plusDays(30 + i));
                        factureService.update(facture);
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(5, TimeUnit.SECONDS);
            assertEquals(10, successCount.get() + failureCount.get());
        }

        @Test
        @DisplayName("Devrait gérer correctement les suppressions concurrentes")
        void shouldHandleConcurrentDeletions() throws InterruptedException, ValidationException, DatabaseException {
            Facture facture = new Facture("FACT-001", LocalDate.now(), LocalDate.now().plusDays(30), client);
            when(factureDAO.findById(any())).thenReturn(Optional.of(facture));
            
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failureCount = new AtomicInteger(0);

            for (int i = 0; i < 10; i++) {
                executorService.submit(() -> {
                    try {
                        factureService.delete(1L);
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(5, TimeUnit.SECONDS);
            assertEquals(10, successCount.get() + failureCount.get());
        }
    }
} 