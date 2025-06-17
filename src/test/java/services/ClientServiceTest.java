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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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