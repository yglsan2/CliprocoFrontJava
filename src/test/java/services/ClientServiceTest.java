package services;

import dao.IDAO;
import models.Adresse;
import models.Client;
import models.Societe;
import exceptions.ValidationException;
import exceptions.DatabaseException;
import exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Timeout;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires optimisés pour ClientService
 */
@DisplayName("Tests unitaires optimisés pour ClientService")
class ClientServiceTest {

    @Mock
    private IDAO<Client, Integer> mockClientDAO;
    
    @Mock
    private IDAO<Adresse, Integer> mockAdresseDAO;
    
    private ClientService clientService;
    private Client testClient;
    private Adresse testAdresse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientService = new ClientService(mockClientDAO, mockAdresseDAO);
        
        // Création des objets de test
        testAdresse = new Adresse();
        testAdresse.setIdentifiant(1);
        testAdresse.setNumeroRue("123");
        testAdresse.setNomRue("Rue de Test");
        testAdresse.setCodePostal("75001");
        testAdresse.setVille("Paris");
        testAdresse.setPays("France");
        
        testClient = new Client();
        testClient.setIdentifiant(1);
        testClient.setRaisonSociale("Test Client");
        testClient.setMail("test@client.com");
        testClient.setTelephone("0123456789");
        testClient.setAdresse(testAdresse);
        testClient.setChiffreAffaires(100000.0);
        testClient.setNbEmployes(50);
        testClient.setCommentaires("Client de test");
    }

    @Nested
    @DisplayName("Tests de base")
    class BasicTests {
        
        @Test
        @DisplayName("Doit créer un client avec succès")
        void testCreateClientSuccess() throws Exception {
            // Arrange
            when(mockAdresseDAO.save(any(Adresse.class))).thenReturn(testAdresse);
            when(mockClientDAO.save(any(Client.class))).thenReturn(testClient);
            
            // Act
            Client result = clientService.create(testClient);
            
            // Assert
            assertNotNull(result);
            assertEquals(testClient.getIdentifiant(), result.getIdentifiant());
            assertEquals(testClient.getRaisonSociale(), result.getRaisonSociale());
            verify(mockAdresseDAO).save(any(Adresse.class));
            verify(mockClientDAO).save(any(Client.class));
        }

        @Test
        @DisplayName("Doit récupérer un client par son identifiant")
        void testGetClientById() throws Exception {
            // Arrange
            when(mockClientDAO.findById(1)).thenReturn(Optional.of(testClient));
            
            // Act
            Optional<Client> result = clientService.findById(1);
            
            // Assert
            assertTrue(result.isPresent());
            assertEquals(testClient.getIdentifiant(), result.get().getIdentifiant());
            assertEquals(testClient.getRaisonSociale(), result.get().getRaisonSociale());
            verify(mockClientDAO).findById(1);
        }

        @Test
        @DisplayName("Doit lever ResourceNotFoundException si le client n'existe pas")
        void testGetClientByIdNotFound() throws Exception {
            // Arrange
            when(mockClientDAO.findById(999)).thenReturn(Optional.empty());
            
            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> {
                clientService.findById(999);
            });
            verify(mockClientDAO).findById(999);
        }

        @Test
        @DisplayName("Doit récupérer tous les clients")
        void testGetAllClients() throws Exception {
            // Arrange
            Client client1 = new Client();
            client1.setIdentifiant(1);
            client1.setRaisonSociale("Client 1");

            Client client2 = new Client();
            client2.setIdentifiant(2);
            client2.setRaisonSociale("Client 2");

            List<Client> expectedClients = Arrays.asList(client1, client2);
            when(mockClientDAO.findAll()).thenReturn(expectedClients);
            
            // Act
            List<Client> result = clientService.findAll();
            
            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(client1.getIdentifiant(), result.get(0).getIdentifiant());
            assertEquals(client2.getIdentifiant(), result.get(1).getIdentifiant());
            verify(mockClientDAO).findAll();
        }
    }

    @Nested
    @DisplayName("Tests de validation métier")
    class BusinessValidationTests {
        
        @Test
        @DisplayName("Doit valider le chiffre d'affaires positif")
        void testValidatePositiveChiffreAffaires() throws Exception {
            // Arrange
            Client clientWithNegativeCA = new Client();
            clientWithNegativeCA.setChiffreAffaires(-1000.0);
            clientWithNegativeCA.setRaisonSociale("Client Test");
            
            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                clientService.createWithValidation(clientWithNegativeCA);
            });
        }
        
        @Test
        @DisplayName("Doit valider le nombre d'employés positif")
        void testValidatePositiveNbEmployes() throws Exception {
            // Arrange
            Client clientWithNegativeEmployes = new Client();
            clientWithNegativeEmployes.setNbEmployes(-5);
            clientWithNegativeEmployes.setRaisonSociale("Client Test");
            
            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                clientService.createWithValidation(clientWithNegativeEmployes);
            });
        }
        
        @Test
        @DisplayName("Doit valider l'email au format correct")
        void testValidateEmailFormat() throws Exception {
            // Arrange
            Client clientWithInvalidEmail = new Client();
            clientWithInvalidEmail.setMail("invalid-email");
            clientWithInvalidEmail.setRaisonSociale("Client Test");
            
            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                clientService.createWithValidation(clientWithInvalidEmail);
            });
        }
        
        @Test
        @DisplayName("Doit valider le téléphone au format français")
        void testValidatePhoneFormat() throws Exception {
            // Arrange
            Client clientWithInvalidPhone = new Client();
            clientWithInvalidPhone.setTelephone("123");
            clientWithInvalidPhone.setRaisonSociale("Client Test");
            
            // Act & Assert
            assertThrows(ValidationException.class, () -> {
                clientService.createWithValidation(clientWithInvalidPhone);
            });
        }
    }

    @Nested
    @DisplayName("Tests de performance")
    class PerformanceTests {
        
        @Test
        @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
        @DisplayName("Doit récupérer 1000 clients rapidement")
        void testGetLargeClientList() throws Exception {
            // Arrange
            List<Client> largeClientList = IntStream.range(1, 1001)
                .mapToObj(i -> {
                    Client client = new Client();
                    client.setIdentifiant(i);
                    client.setRaisonSociale("Client " + i);
                    return client;
                })
                .collect(Collectors.toList());
            
            when(mockClientDAO.findAll()).thenReturn(largeClientList);
            
            // Act
            long startTime = System.currentTimeMillis();
            List<Client> result = clientService.findAll();
            long endTime = System.currentTimeMillis();
            
            // Assert
            assertEquals(1000, result.size());
            assertTrue(endTime - startTime < 50, "L'opération doit être rapide (< 50ms)");
        }
        
        @RepeatedTest(5)
        @DisplayName("Doit maintenir des performances constantes sur plusieurs exécutions")
        void testConsistentPerformance() throws Exception {
            // Arrange
            when(mockClientDAO.findById(1)).thenReturn(Optional.of(testClient));
            
            // Act
            long startTime = System.nanoTime();
            clientService.findById(1);
            long endTime = System.nanoTime();
            
            // Assert
            long duration = endTime - startTime;
            assertTrue(duration < 50_000_000, "L'opération doit être rapide (< 50ms)");
        }
    }

    @Nested
    @DisplayName("Tests de cas limites")
    class EdgeCaseTests {
        
        @Test
        @DisplayName("Doit gérer un client avec des valeurs maximales")
        void testClientWithMaxValues() throws Exception {
            // Arrange
            Client maxClient = new Client();
            maxClient.setIdentifiant(Integer.MAX_VALUE);
            maxClient.setRaisonSociale("A".repeat(100)); // Nom très long
            maxClient.setChiffreAffaires(Double.MAX_VALUE);
            maxClient.setNbEmployes(Integer.MAX_VALUE);
            maxClient.setCommentaires("A".repeat(1000)); // Commentaire très long
            
            when(mockClientDAO.save(any(Client.class))).thenReturn(maxClient);
            
            // Act
            Client result = clientService.create(maxClient);
            
            // Assert
            assertNotNull(result);
            assertEquals(Integer.MAX_VALUE, result.getIdentifiant());
            assertEquals(Double.MAX_VALUE, result.getChiffreAffaires());
        }
        
        @Test
        @DisplayName("Doit gérer un client avec des valeurs minimales")
        void testClientWithMinValues() throws Exception {
            // Arrange
            Client minClient = new Client();
            minClient.setIdentifiant(1);
            minClient.setRaisonSociale("A"); // Nom très court
            minClient.setChiffreAffaires(0.01);
            minClient.setNbEmployes(1);
            minClient.setCommentaires(""); // Commentaire vide
            
            when(mockClientDAO.save(any(Client.class))).thenReturn(minClient);
            
            // Act
            Client result = clientService.create(minClient);
            
            // Assert
            assertNotNull(result);
            assertEquals(0.01, result.getChiffreAffaires());
            assertEquals(1, result.getNbEmployes());
        }
        
        @Test
        @DisplayName("Doit gérer une liste vide de clients")
        void testEmptyClientList() throws Exception {
            // Arrange
            when(mockClientDAO.findAll()).thenReturn(Arrays.asList());
            
            // Act
            List<Client> result = clientService.findAll();
            
            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Tests de résilience")
    class ResilienceTests {
        
        @Test
        @DisplayName("Doit gérer les exceptions lors de la création")
        void testCreateClientException() throws Exception {
            // Arrange
            when(mockAdresseDAO.save(any(Adresse.class)))
                .thenThrow(new DatabaseException("Erreur de base de données"));
            
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                clientService.create(testClient);
            });
        }

        @Test
        @DisplayName("Doit gérer les exceptions lors de la lecture")
        void testGetClientByIdException() throws Exception {
            // Arrange
            when(mockClientDAO.findById(1))
                .thenThrow(new DatabaseException("Erreur de base de données"));
            
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                clientService.findById(1);
            });
        }

        @Test
        @DisplayName("Doit gérer les exceptions lors de la mise à jour")
        void testUpdateClientException() throws Exception {
            // Arrange
            when(mockClientDAO.findById(1)).thenReturn(Optional.of(testClient));
            when(mockClientDAO.update(any(Client.class)))
                .thenThrow(new DatabaseException("Erreur de base de données"));
            
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                clientService.update(testClient);
            });
        }

        @Test
        @DisplayName("Doit gérer les exceptions lors de la suppression")
        void testDeleteClientException() throws Exception {
            // Arrange
            when(mockClientDAO.findById(1)).thenReturn(Optional.of(testClient));
            doThrow(new DatabaseException("Erreur de base de données"))
                .when(mockClientDAO).delete(any(Client.class));
            
            // Act & Assert
            assertThrows(DatabaseException.class, () -> {
                clientService.delete(1);
            });
        }
    }

    @Nested
    @DisplayName("Tests de concurrence")
    class ConcurrencyTests {
        
        @Test
        @DisplayName("Doit gérer les accès concurrents en lecture")
        void testConcurrentReads() throws Exception {
            // Arrange
            when(mockClientDAO.findById(1)).thenReturn(Optional.of(testClient));
            
            // Act
            List<CompletableFuture<Optional<Client>>> futures = IntStream.range(0, 10)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return clientService.findById(1);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }))
                .collect(Collectors.toList());
            
            // Assert
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            futures.forEach(future -> {
                try {
                    Optional<Client> result = future.get();
                    assertTrue(result.isPresent());
                    assertEquals(testClient.getIdentifiant(), result.get().getIdentifiant());
                } catch (Exception e) {
                    fail("Erreur lors de l'accès concurrent: " + e.getMessage());
                }
            });
        }
    }

    @Nested
    @DisplayName("Tests de métriques")
    class MetricsTests {
        
        @Test
        @DisplayName("Doit mesurer le temps de réponse pour findById")
        void testFindByIdResponseTime() throws Exception {
            // Arrange
            when(mockClientDAO.findById(1)).thenReturn(Optional.of(testClient));
            
            // Act
            long startTime = System.nanoTime();
            clientService.findById(1);
            long endTime = System.nanoTime();
            
            // Assert
            long responseTime = endTime - startTime;
            assertTrue(responseTime < 10_000_000, 
                "Le temps de réponse doit être inférieur à 10ms, actuel: " + responseTime + "ns");
        }
        
        @Test
        @DisplayName("Doit mesurer le temps de réponse pour findAll")
        void testFindAllResponseTime() throws Exception {
            // Arrange
            List<Client> clients = IntStream.range(1, 101)
                .mapToObj(i -> {
                    Client client = new Client();
                    client.setIdentifiant(i);
                    client.setRaisonSociale("Client " + i);
                    return client;
                })
                .collect(Collectors.toList());
            when(mockClientDAO.findAll()).thenReturn(clients);
            
            // Act
            long startTime = System.nanoTime();
            clientService.findAll();
            long endTime = System.nanoTime();
            
            // Assert
            long responseTime = endTime - startTime;
            assertTrue(responseTime < 50_000_000, 
                "Le temps de réponse doit être inférieur à 50ms, actuel: " + responseTime + "ns");
        }
    }

    // Tests existants conservés pour compatibilité
    @Test
    @DisplayName("Doit mettre à jour un client existant")
    void testUpdateClient() throws Exception {
        // Arrange
        when(mockClientDAO.findById(1)).thenReturn(Optional.of(testClient));
        when(mockClientDAO.update(any(Client.class))).thenReturn(testClient);
        
        Client updatedClient = new Client();
        updatedClient.setIdentifiant(1);
        updatedClient.setRaisonSociale("Nouveau nom");
        updatedClient.setMail("updated@client.com");
        updatedClient.setTelephone("0987654321");
        updatedClient.setAdresse(testAdresse);
        updatedClient.setChiffreAffaires(150000.0);
        updatedClient.setNbEmployes(75);
        updatedClient.setCommentaires("Client mis à jour");
        
        // Act
        Client result = clientService.update(updatedClient);
        
        // Assert
        assertNotNull(result);
        assertEquals(updatedClient.getIdentifiant(), result.getIdentifiant());
        assertEquals(updatedClient.getRaisonSociale(), result.getRaisonSociale());
        verify(mockClientDAO).findById(1);
        verify(mockClientDAO).update(any(Client.class));
    }

    @Test
    @DisplayName("Doit lever ResourceNotFoundException si le client à mettre à jour n'existe pas")
    void testUpdateClientNotFound() throws Exception {
        // Arrange
        Client nonExistentClient = new Client();
        nonExistentClient.setIdentifiant(999);
        nonExistentClient.setRaisonSociale("Client inexistant");

        when(mockClientDAO.findById(999)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            clientService.update(nonExistentClient);
        });
        verify(mockClientDAO).findById(999);
        verify(mockClientDAO, never()).update(any(Client.class));
    }

    @Test
    @DisplayName("Doit supprimer un client existant")
    void testDeleteClient() throws Exception {
        // Arrange
        when(mockClientDAO.findById(1)).thenReturn(Optional.of(testClient));
        doNothing().when(mockClientDAO).delete(any(Client.class));
        doNothing().when(mockAdresseDAO).delete(any(Adresse.class));
        
        // Act
        clientService.delete(1);
        
        // Assert
        verify(mockClientDAO).findById(1);
        verify(mockAdresseDAO).delete(any(Adresse.class));
        verify(mockClientDAO).delete(any(Client.class));
    }

    @Test
    @DisplayName("Doit lever ResourceNotFoundException si le client à supprimer n'existe pas")
    void testDeleteClientNotFound() throws Exception {
        // Arrange
        when(mockClientDAO.findById(999)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            clientService.delete(999);
        });
        verify(mockClientDAO).findById(999);
        verify(mockClientDAO, never()).delete(any(Client.class));
        verify(mockAdresseDAO, never()).delete(any(Adresse.class));
    }

    @Test
    @DisplayName("Doit rechercher des clients par raison sociale")
    void testSearchClientsByRaisonSociale() throws Exception {
        // Arrange
        Client client1 = new Client();
        client1.setIdentifiant(1);
        client1.setRaisonSociale("Tech Solutions");

        Client client2 = new Client();
        client2.setIdentifiant(2);
        client2.setRaisonSociale("Tech Corp");

        List<Client> allClients = Arrays.asList(client1, client2);
        when(mockClientDAO.findAll()).thenReturn(allClients);
        
        // Act
        List<Client> result = clientService.searchByRaisonSociale("Tech");
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(c -> c.getRaisonSociale().contains("Tech")));
    }

    @Test
    @DisplayName("Doit retourner une liste vide si aucun client ne correspond")
    void testSearchClientsByRaisonSocialeNoMatch() throws Exception {
        // Arrange
        Client client1 = new Client();
        client1.setIdentifiant(1);
        client1.setRaisonSociale("Tech Solutions");

        List<Client> allClients = Arrays.asList(client1);
        when(mockClientDAO.findAll()).thenReturn(allClients);
        
        // Act
        List<Client> result = clientService.searchByRaisonSociale("Inexistant");
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Doit rechercher un client par email")
    void testFindByEmail() throws Exception {
        // Arrange
        Client client1 = new Client();
        client1.setIdentifiant(1);
        client1.setMail("test@example.com");

        List<Client> allClients = Arrays.asList(client1);
        when(mockClientDAO.findAll()).thenReturn(allClients);
        
        // Act
        Optional<Client> result = clientService.findByEmail("test@example.com");
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getMail());
    }

    @Test
    @DisplayName("Doit retourner empty si l'email n'existe pas")
    void testFindByEmailNotFound() throws Exception {
        // Arrange
        Client client1 = new Client();
        client1.setIdentifiant(1);
        client1.setMail("test@example.com");

        List<Client> allClients = Arrays.asList(client1);
        when(mockClientDAO.findAll()).thenReturn(allClients);
        
        // Act
        Optional<Client> result = clientService.findByEmail("nonexistent@example.com");
        
        // Assert
        assertFalse(result.isPresent());
    }
} 