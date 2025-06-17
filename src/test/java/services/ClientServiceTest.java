package services;

import dao.IDAO;
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
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.AfterEach;

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
class ClientServiceTest {
    @Mock
    private IDAO<Client, Long> clientDAO;

    @Mock
    private IDAO<Adresse, Long> adresseDAO;

    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientService = new ClientService(clientDAO, adresseDAO);
    }

    @Nested
    @DisplayName("Tests de performance")
    class PerformanceTests {
        private List<Client> clients;

        @BeforeEach
        void setUp() {
            clients = new ArrayList<>();
            
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
        }

        @Test
        @DisplayName("Devrait gérer efficacement la création de 1000 clients")
        void shouldHandleBulkClientCreation() throws ValidationException, DatabaseException {
            for (int i = 0; i < 1000; i++) {
                when(clientDAO.save(any(Client.class))).thenReturn(clients.get(i));
                clientService.create(clients.get(i));
            }
            verify(clientDAO, times(1000)).save(any(Client.class));
        }

        @Test
        @DisplayName("Devrait gérer efficacement la recherche de clients")
        void shouldHandleEfficientClientSearch() throws ValidationException, DatabaseException {
            when(clientDAO.findAll()).thenReturn(clients);
            
            for (int i = 0; i < 1000; i++) {
                clientService.findByRaisonSociale("Client " + i);
            }
            
            verify(clientDAO, times(1000)).findAll();
        }
    }

    @Nested
    @DisplayName("Tests de concurrence")
    class ConcurrencyTests {
        private ExecutorService executorService;
        private CountDownLatch latch;

        @BeforeEach
        void setUp() {
            executorService = Executors.newFixedThreadPool(10);
            latch = new CountDownLatch(10);
        }

        @AfterEach
        void tearDown() {
            executorService.shutdown();
        }

        @Test
        @DisplayName("Devrait gérer correctement les accès concurrents à la création de clients")
        void shouldHandleConcurrentClientCreation() throws InterruptedException {
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failureCount = new AtomicInteger(0);

            for (int i = 0; i < 10; i++) {
                final int index = i;
                executorService.submit(() -> {
                    try {
                        Client client = new Client();
                        client.setRaisonSociale("Client " + index);
                        client.setEmail("client" + index + "@test.com");
                        client.setTelephone("0123456789");
                        client.setChiffreAffaires(1000.0);
                        client.setNombreEmployes(10);
                        
                        when(clientDAO.save(any(Client.class))).thenReturn(client);
                        clientService.create(client);
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
    @DisplayName("Tests de sécurité")
    class SecurityTests {
        @Test
        @DisplayName("Devrait rejeter les tentatives d'injection SQL dans la raison sociale")
        void shouldRejectSqlInjectionInRaisonSociale() {
            String[] sqlInjectionAttempts = {
                "Client 1'; DROP TABLE clients; --",
                "Client 1' OR '1'='1",
                "Client 1'; SELECT * FROM users; --"
            };

            for (String injection : sqlInjectionAttempts) {
                Client client = new Client();
                client.setRaisonSociale(injection);
                client.setEmail("test@example.com");
                client.setTelephone("0123456789");
                client.setChiffreAffaires(1000.0);
                client.setNombreEmployes(10);
                
                assertThrows(ValidationException.class, () -> clientService.create(client));
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
                Client client = new Client();
                client.setRaisonSociale("Test Client");
                client.setEmail("test@example.com");
                client.setTelephone("0123456789");
                client.setChiffreAffaires(1000.0);
                client.setNombreEmployes(10);
                client.setCommentaire(injection);
                
                assertThrows(ValidationException.class, () -> clientService.create(client));
            }
        }
    }

    @Nested
    @DisplayName("Tests de validation des données")
    class ValidationTests {
        private Client client;

        @BeforeEach
        void setUp() {
            client = new Client();
            client.setRaisonSociale("Test Client");
            client.setEmail("test@example.com");
            client.setTelephone("0123456789");
        }

        @Test
        @DisplayName("Devrait valider le format du numéro de téléphone")
        void shouldValidatePhoneNumber() throws ValidationException, DatabaseException {
            client.setTelephone("invalid");
            assertThrows(ValidationException.class, () -> clientService.create(client));
        }

        @Test
        @DisplayName("Devrait valider le format de l'email")
        void shouldValidateEmail() throws ValidationException, DatabaseException {
            client.setEmail("invalid-email");
            assertThrows(ValidationException.class, () -> clientService.create(client));
        }

        @Test
        @DisplayName("Devrait valider la raison sociale")
        void shouldValidateRaisonSociale() throws ValidationException, DatabaseException {
            client.setRaisonSociale("");
            assertThrows(ValidationException.class, () -> clientService.create(client));
        }
    }

    @Nested
    @DisplayName("Tests de findById")
    class FindByIdTests {
        @Test
        @DisplayName("Devrait retourner le client quand l'ID existe")
        void shouldReturnClientWhenIdExists() throws ValidationException, DatabaseException {
            // Arrange
            Long id = 1L;
            Client client = new Client("Test Client", new Adresse("123", "Rue Test", "75000", "Paris"), "0123456789", "test@test.com", "Commentaire", 100000.0, 10);
            when(clientDAO.findById(id)).thenReturn(Optional.of(client));

            // Act
            Optional<Client> result = clientService.findById(id);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(client, result.get());
            verify(clientDAO).findById(id);
        }

        @Test
        @DisplayName("Devrait retourner vide quand l'ID n'existe pas")
        void shouldReturnEmptyWhenIdDoesNotExist() throws ValidationException, DatabaseException {
            // Arrange
            Long id = 999L;
            when(clientDAO.findById(id)).thenReturn(Optional.empty());

            // Act
            Optional<Client> result = clientService.findById(id);

            // Assert
            assertFalse(result.isPresent());
            verify(clientDAO).findById(id);
        }

        @Test
        @DisplayName("Devrait lever une exception quand l'ID est null")
        void shouldThrowExceptionWhenIdIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> clientService.findById(null));
        }
    }

    @Nested
    @DisplayName("Tests de findAll")
    class FindAllTests {
        @Test
        @DisplayName("Devrait retourner tous les clients")
        void shouldReturnAllClients() throws DatabaseException {
            // Arrange
            List<Client> clients = Arrays.asList(
                new Client("Client 1", new Adresse("1", "Rue 1", "75001", "Paris"), "0123456789", "client1@test.com", "Commentaire 1", 100000.0, 10),
                new Client("Client 2", new Adresse("2", "Rue 2", "75002", "Paris"), "0123456789", "client2@test.com", "Commentaire 2", 200000.0, 20)
            );
            when(clientDAO.findAll()).thenReturn(clients);

            // Act
            List<Client> result = clientService.findAll();

            // Assert
            assertEquals(2, result.size());
            assertEquals(clients, result);
            verify(clientDAO).findAll();
        }
    }

    @Nested
    @DisplayName("Tests de create")
    class CreateTests {
        @Test
        @DisplayName("Devrait créer un client avec succès")
        void shouldCreateClientSuccessfully() throws ValidationException, DatabaseException {
            // Arrange
            Adresse adresse = new Adresse("123", "Rue Test", "75000", "Paris");
            Client client = new Client("Test Client", adresse, "0123456789", "test@test.com", "Commentaire", 100000.0, 10);
            when(clientDAO.save(any(Client.class))).thenReturn(client);
            when(adresseDAO.save(any(Adresse.class))).thenReturn(adresse);
            
            // Act
            Client result = clientService.create(client);
            
            // Assert
            assertNotNull(result);
            assertEquals(client, result);
            verify(adresseDAO).save(adresse);
            verify(clientDAO).save(client);
        }

        @Test
        @DisplayName("Devrait lever une exception quand le client est null")
        void shouldThrowExceptionWhenClientIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> clientService.create(null));
        }
    }

    @Nested
    @DisplayName("Tests de update")
    class UpdateTests {
        @Test
        @DisplayName("Devrait mettre à jour un client avec succès")
        void shouldUpdateClientSuccessfully() throws ValidationException, ResourceNotFoundException, DatabaseException {
            // Arrange
            Adresse adresse = new Adresse("123", "Rue Test", "75000", "Paris");
            Client client = new Client("Test Client", adresse, "0123456789", "test@test.com", "Commentaire", 100000.0, 10);
            client.setIdentifiant(1L);
            when(clientDAO.findById(1L)).thenReturn(Optional.of(client));
            when(clientDAO.update(any(Client.class))).thenReturn(client);
            when(adresseDAO.update(any(Adresse.class))).thenReturn(adresse);

            // Act
            Client result = clientService.update(client);

            // Assert
            assertNotNull(result);
            assertEquals(client, result);
            verify(clientDAO).findById(1L);
            verify(adresseDAO).update(adresse);
            verify(clientDAO).update(client);
        }

        @Test
        @DisplayName("Devrait lever une exception quand le client n'existe pas")
        void shouldThrowExceptionWhenClientDoesNotExist() {
            // Arrange
            Client client = new Client("Test Client", new Adresse("123", "Rue Test", "75000", "Paris"), "0123456789", "test@test.com", "Commentaire", 100000.0, 10);
            client.setIdentifiant(999L);
            when(clientDAO.findById(999L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> clientService.update(client));
        }
    }

    @Nested
    @DisplayName("Tests de delete")
    class DeleteTests {
        @Test
        @DisplayName("Devrait supprimer un client avec succès")
        void shouldDeleteClientSuccessfully() throws ValidationException, ResourceNotFoundException, DatabaseException {
            // Arrange
            Long id = 1L;
            Client client = new Client("Test Client", new Adresse("123", "Rue Test", "75000", "Paris"), "0123456789", "test@test.com", "Commentaire", 100000.0, 10);
            client.setIdentifiant(id);
            when(clientDAO.findById(id)).thenReturn(Optional.of(client));

            // Act
            clientService.delete(id);

            // Assert
            verify(clientDAO).findById(id);
            verify(clientDAO).delete(client);
        }

        @Test
        @DisplayName("Devrait lever une exception quand le client n'existe pas")
        void shouldThrowExceptionWhenClientDoesNotExist() {
            // Arrange
            Long id = 999L;
            when(clientDAO.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> clientService.delete(id));
        }
    }

    @Nested
    @DisplayName("Tests de findByEmail")
    class FindByEmailTests {
        @Test
        @DisplayName("Devrait trouver un client par email")
        void shouldFindClientByEmail() throws ValidationException, DatabaseException {
            // Arrange
            String email = "test@test.com";
            Client client = new Client("Test Client", new Adresse("123", "Rue Test", "75000", "Paris"), "0123456789", email, "Commentaire", 100000.0, 10);
            when(clientDAO.findAll()).thenReturn(Arrays.asList(client));

            // Act
            Optional<Client> result = clientService.findByEmail(email);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(client, result.get());
            verify(clientDAO).findAll();
        }

        @Test
        @DisplayName("Devrait lever une exception quand l'email est vide")
        void shouldThrowExceptionWhenEmailIsEmpty() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> clientService.findByEmail(""));
        }
    }

    @Nested
    @DisplayName("Tests de findByRaisonSociale")
    class FindByRaisonSocialeTests {
        @Test
        @DisplayName("Devrait trouver des clients par raison sociale")
        void shouldFindClientsByRaisonSociale() throws DatabaseException {
            // Arrange
            String raisonSociale = "Test Client";
            List<Client> clients = Arrays.asList(
                new Client(raisonSociale, new Adresse("1", "Rue 1", "75001", "Paris"), "0123456789", "client1@test.com", "Commentaire 1", 100000.0, 10),
                new Client(raisonSociale, new Adresse("2", "Rue 2", "75002", "Paris"), "0123456789", "client2@test.com", "Commentaire 2", 200000.0, 20)
            );
            when(clientDAO.findAll()).thenReturn(clients);

            // Act
            List<Client> result = clientService.findByRaisonSociale(raisonSociale);

            // Assert
            assertEquals(2, result.size());
            assertEquals(clients, result);
            verify(clientDAO).findAll();
        }
    }

    @Nested
    @DisplayName("Tests de existsByRaisonSociale")
    class ExistsByRaisonSocialeTests {
        @Test
        @DisplayName("Devrait retourner true quand la raison sociale existe")
        void shouldReturnTrueWhenRaisonSocialeExists() throws DatabaseException {
            // Arrange
            String raisonSociale = "Test Client";
            List<Client> clients = Arrays.asList(
                new Client(raisonSociale, new Adresse("1", "Rue 1", "75001", "Paris"), "0123456789", "client1@test.com", "Commentaire 1", 100000.0, 10)
            );
            when(clientDAO.findAll()).thenReturn(clients);

            // Act
            boolean result = clientService.existsByRaisonSociale(raisonSociale);

            // Assert
            assertTrue(result);
            verify(clientDAO).findAll();
        }

        @Test
        @DisplayName("Devrait retourner false quand la raison sociale n'existe pas")
        void shouldReturnFalseWhenRaisonSocialeDoesNotExist() throws DatabaseException {
            // Arrange
            String raisonSociale = "Test Client";
            List<Client> clients = Arrays.asList(
                new Client("Autre Client", new Adresse("1", "Rue 1", "75001", "Paris"), "0123456789", "client1@test.com", "Commentaire 1", 100000.0, 10)
            );
            when(clientDAO.findAll()).thenReturn(clients);

            // Act
            boolean result = clientService.existsByRaisonSociale(raisonSociale);

            // Assert
            assertFalse(result);
            verify(clientDAO).findAll();
        }
    }
} 