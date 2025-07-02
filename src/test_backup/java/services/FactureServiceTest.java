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
import org.junit.jupiter.api.AfterEach;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FactureServiceTest {
    @Mock
    private FactureJpaDAO factureDAO;

    @Mock
    private IDAO<Produit, Integer> produitDAO;

    @Mock
    private IDAO<CalculFacture, Integer> calculFactureDAO;

    private FactureService factureService;

    @BeforeEach
    void setUp() {
        factureService = new FactureService(factureDAO, produitDAO, calculFactureDAO);
    }

    @Nested
<<<<<<< HEAD:src/test/java/services/FactureServiceTest.java
    @DisplayName("Tests de performance")
    class PerformanceTests {
        private List<Client> clients;
        private List<Produit> produits;
        private List<CalculFacture> calculs;

=======
    @DisplayName("Tests de findById")
    class FindByIdTests {
        @Test
        @DisplayName("Devrait retourner une facture quand l'ID existe")
        void shouldReturnFactureWhenIdExists() throws DatabaseException, ValidationException {
            // Arrange
            Integer id = 1L;
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
            Integer id = 1L;
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
            Integer id = 1L;
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
        @DisplayName("Devrait retourner la liste complète des factures")
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
            Integer clientId = 1L;
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
        
>>>>>>> jakarta-ee-migration-final:src/test_backup/java/services/FactureServiceTest.java
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
<<<<<<< HEAD:src/test/java/services/FactureServiceTest.java
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
            List<Facture> factures = new ArrayList<>();
            LocalDate dateEmission = LocalDate.now();
            LocalDate dateEcheance = dateEmission.plusDays(30);

            for (int i = 0; i < 100; i++) {
                Client client = clients.get(0);
                List<Produit> factureProduits = produits.subList(0, 5);
                CalculFacture calcul = calculs.get(i % calculs.size());
                
                Facture facture = new Facture("FACT-" + i, dateEmission, dateEcheance, client);
                factures.add(facture);
            }

            when(factureDAO.findByClientId(any())).thenReturn(factures);
            
            List<Facture> result = factureService.findByClientId(1L);
            
            assertEquals(100, result.size());
            verify(factureDAO).findByClientId(1L);
        }

        @Test
        @DisplayName("Devrait gérer efficacement la mise à jour en masse")
        void shouldHandleBulkUpdate() throws ValidationException, ResourceNotFoundException, DatabaseException {
            List<Facture> factures = new ArrayList<>();
            LocalDate dateEmission = LocalDate.now();
            LocalDate dateEcheance = dateEmission.plusDays(30);

            for (int i = 0; i < 100; i++) {
                Client client = clients.get(i % clients.size());
                Facture facture = new Facture("FACT-" + i, dateEmission, dateEcheance, client);
                factures.add(facture);
            }

            when(factureDAO.findAll()).thenReturn(factures);
            when(factureDAO.save(any(Facture.class))).thenAnswer(invocation -> invocation.getArgument(0));

            for (Facture facture : factures) {
                facture.setDateEcheance(dateEcheance.plusDays(30));
                factureService.update(facture);
            }

            verify(factureDAO, times(100)).save(any(Facture.class));
        }
    }

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
=======
        @DisplayName("Devrait sauvegarder une nouvelle facture")
        void shouldSaveNewFacture() throws DatabaseException, ValidationException {
            // Arrange
            String numero = "F001";
            Facture facture = new Facture(numero, dateEmission, dateEcheance, client);
>>>>>>> jakarta-ee-migration-final:src/test_backup/java/services/FactureServiceTest.java
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

    @Nested
<<<<<<< HEAD:src/test/java/services/FactureServiceTest.java
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
=======
    @DisplayName("Tests de update")
    class UpdateTests {
        @Test
        @DisplayName("Devrait mettre à jour une facture existante")
        void shouldUpdateExistingFacture() throws DatabaseException, ValidationException, ResourceNotFoundException {
            // Arrange
            Facture facture = new Facture(
                "FACT-001",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                new Client()
            );
            when(factureDAO.findById(1L)).thenReturn(Optional.of(facture));
            when(factureDAO.update(any(Facture.class))).thenReturn(facture);
>>>>>>> jakarta-ee-migration-final:src/test_backup/java/services/FactureServiceTest.java
            
            produits = new ArrayList<>();
            Produit produit1 = new Produit();
            produit1.setNom("Produit 1");
            produit1.setDescription("Description du produit 1");
            produit1.setPrixUnitaire(BigDecimal.valueOf(100));
            produits.add(produit1);
            
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
<<<<<<< HEAD:src/test/java/services/FactureServiceTest.java
        @DisplayName("Devrait rejeter les tentatives d'injection XSS dans les commentaires")
        void shouldRejectXssInjectionInComments() {
            String[] xssInjectionAttempts = {
                "<script>alert('XSS')</script>",
                "javascript:alert('XSS')",
                "<img src='x' onerror='alert(\"XSS\")'>"
            };

            for (String injection : xssInjectionAttempts) {
                Facture facture = new Facture("FACT-001", LocalDate.now(), LocalDate.now().plusDays(30), client);
                facture.setCommentaire(injection);
                assertThrows(ValidationException.class, () -> factureService.update(facture));
            }
=======
        @DisplayName("Devrait lancer ValidationException quand l'ID de la facture est null")
        void shouldThrowValidationExceptionWhenFactureIdIsNull() {
            // Arrange
            Facture facture = new Facture(
                "FACT-001",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                new Client()
            );
            
            // Act & Assert
            assertThrows(ValidationException.class, () -> factureService.update(facture));
            verify(factureDAO, never()).update(any());
>>>>>>> jakarta-ee-migration-final:src/test_backup/java/services/FactureServiceTest.java
        }

        @Test
<<<<<<< HEAD:src/test/java/services/FactureServiceTest.java
        @DisplayName("Devrait rejeter les tentatives d'injection de commandes système")
        void shouldRejectCommandInjection() {
            String[] commandInjectionAttempts = {
                "FACT-001; rm -rf /",
                "FACT-001 && del /f /s /q C:\\",
                "FACT-001 | cat /etc/passwd"
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
                "1000,00",
                "1.000,00",
                "1,000.00",
                "1000.00€",
                "€1000.00"
            };

            for (String amount : invalidAmounts) {
                Produit produit = new Produit();
                produit.setNom("Test Product");
                produit.setDescription("Test Description");
                produit.setPrixUnitaire(new BigDecimal(amount));
                produits.add(produit);

                assertThrows(ValidationException.class, () -> 
                    factureService.create(
                        "FACT-001",
                        LocalDate.now(),
                        LocalDate.now().plusDays(30),
                        client,
                        produits,
                        calcul
                    )
                );
            }
        }
    }

    // ... existing code ...
=======
        @DisplayName("Devrait lancer ResourceNotFoundException quand la facture n'existe pas")
        void shouldThrowResourceNotFoundExceptionWhenFactureDoesNotExist() {
            // Arrange
            Facture facture = new Facture(
                "FACT-001",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                new Client()
            );
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
        void shouldDeleteExistingFacture() throws DatabaseException, ValidationException, ResourceNotFoundException {
            // Arrange
            Integer id = 1L;
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
            Integer id = 1L;
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
>>>>>>> jakarta-ee-migration-final:src/test_backup/java/services/FactureServiceTest.java
} 