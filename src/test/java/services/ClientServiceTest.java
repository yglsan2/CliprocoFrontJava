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
        clientService = new ClientService(clientDAO, adresseDAO);
    }

    @Nested
    @DisplayName("Tests de findById")
    class FindByIdTests {
        @Test
        @DisplayName("Devrait retourner un client quand l'ID existe")
        void shouldReturnClientWhenIdExists() throws ValidationException, DatabaseException {
            // Arrange
            Long id = 1L;
            Client expectedClient = new Client();
            expectedClient.setIdentifiant(id);
            when(clientDAO.findById(id)).thenReturn(Optional.of(expectedClient));

            // Act
            Optional<Client> result = clientService.findById(id);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(expectedClient, result.get());
            verify(clientDAO).findById(id);
        }

        @Test
        @DisplayName("Devrait retourner empty quand l'ID n'existe pas")
        void shouldReturnEmptyWhenIdDoesNotExist() throws ValidationException, DatabaseException {
            // Arrange
            Long id = 1L;
            when(clientDAO.findById(id)).thenReturn(Optional.empty());

            // Act
            Optional<Client> result = clientService.findById(id);

            // Assert
            assertTrue(result.isEmpty());
            verify(clientDAO).findById(id);
        }

        @Test
        @DisplayName("Devrait lever une ValidationException quand l'ID est null")
        void shouldThrowValidationExceptionWhenIdIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> clientService.findById(null));
            verify(clientDAO, never()).findById(any());
        }
    }

    @Nested
    @DisplayName("Tests de findAll")
    class FindAllTests {
        @Test
        @DisplayName("Devrait retourner la liste de tous les clients")
        void shouldReturnAllClients() throws DatabaseException {
            // Arrange
            List<Client> expectedClients = Arrays.asList(
                new Client(), new Client(), new Client()
            );
            when(clientDAO.findAll()).thenReturn(expectedClients);

            // Act
            List<Client> result = clientService.findAll();

            // Assert
            assertEquals(expectedClients.size(), result.size());
            assertEquals(expectedClients, result);
            verify(clientDAO).findAll();
        }

        @Test
        @DisplayName("Devrait retourner une liste vide quand il n'y a pas de clients")
        void shouldReturnEmptyListWhenNoClients() throws DatabaseException {
            // Arrange
            when(clientDAO.findAll()).thenReturn(List.of());

            // Act
            List<Client> result = clientService.findAll();

            // Assert
            assertTrue(result.isEmpty());
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
            Client client = new Client();
            Adresse adresse = new Adresse();
            client.setAdresse(adresse);
            when(clientDAO.save(any(Client.class))).thenReturn(client);
            when(adresseDAO.save(any(Adresse.class))).thenReturn(adresse);

            // Act
            Client result = clientService.create(client);

            // Assert
            assertNotNull(result);
            verify(adresseDAO).save(adresse);
            verify(clientDAO).save(client);
        }

        @Test
        @DisplayName("Devrait lever une ValidationException quand le client est null")
        void shouldThrowValidationExceptionWhenClientIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> clientService.create(null));
            verify(clientDAO, never()).save(any());
            verify(adresseDAO, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Tests de update")
    class UpdateTests {
        @Test
        @DisplayName("Devrait mettre à jour un client avec succès")
        void shouldUpdateClientSuccessfully() throws ValidationException, ResourceNotFoundException, DatabaseException {
            // Arrange
            Client client = new Client();
            client.setIdentifiant(1L);
            Adresse adresse = new Adresse();
            client.setAdresse(adresse);
            when(clientDAO.findById(1L)).thenReturn(Optional.of(client));
            when(clientDAO.update(any(Client.class))).thenReturn(client);
            when(adresseDAO.update(any(Adresse.class))).thenReturn(adresse);

            // Act
            Client result = clientService.update(client);

            // Assert
            assertNotNull(result);
            verify(clientDAO).findById(1L);
            verify(clientDAO).update(client);
            verify(adresseDAO).update(adresse);
        }

        @Test
        @DisplayName("Devrait lever une ValidationException quand le client est null")
        void shouldThrowValidationExceptionWhenClientIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> clientService.update(null));
            verify(clientDAO, never()).update(any());
        }

        @Test
        @DisplayName("Devrait lever une ResourceNotFoundException quand le client n'existe pas")
        void shouldThrowResourceNotFoundExceptionWhenClientDoesNotExist() {
            // Arrange
            Client client = new Client();
            client.setIdentifiant(1L);
            when(clientDAO.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> clientService.update(client));
            verify(clientDAO, never()).update(any());
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
            Client client = new Client();
            client.setIdentifiant(id);
            when(clientDAO.findById(id)).thenReturn(Optional.of(client));

            // Act
            clientService.delete(id);

            // Assert
            verify(clientDAO).findById(id);
            verify(clientDAO).delete(client);
        }

        @Test
        @DisplayName("Devrait lever une ValidationException quand l'ID est null")
        void shouldThrowValidationExceptionWhenIdIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> clientService.delete(null));
            verify(clientDAO, never()).delete(any());
        }

        @Test
        @DisplayName("Devrait lever une ResourceNotFoundException quand le client n'existe pas")
        void shouldThrowResourceNotFoundExceptionWhenClientDoesNotExist() {
            // Arrange
            Long id = 1L;
            when(clientDAO.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> clientService.delete(id));
            verify(clientDAO, never()).delete(any());
        }
    }

    @Nested
    @DisplayName("Tests de findByEmail")
    class FindByEmailTests {
        @Test
        @DisplayName("Devrait trouver un client par email")
        void shouldFindClientByEmail() throws ValidationException, DatabaseException {
            // Arrange
            String email = "test@example.com";
            Client client = new Client();
            client.setEmail(email);
            when(clientDAO.findAll()).thenReturn(List.of(client));

            // Act
            Optional<Client> result = clientService.findByEmail(email);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(email, result.get().getEmail());
            verify(clientDAO).findAll();
        }

        @Test
        @DisplayName("Devrait lever une ValidationException quand l'email est null")
        void shouldThrowValidationExceptionWhenEmailIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> clientService.findByEmail(null));
            verify(clientDAO, never()).findAll();
        }
    }

    @Nested
    @DisplayName("Tests de findByRaisonSociale")
    class FindByRaisonSocialeTests {
        @Test
        @DisplayName("Devrait trouver des clients par raison sociale")
        void shouldFindClientsByRaisonSociale() throws DatabaseException {
            // Arrange
            String raisonSociale = "Test Company";
            Client client1 = new Client();
            client1.setRaisonSociale(raisonSociale);
            Client client2 = new Client();
            client2.setRaisonSociale(raisonSociale);
            when(clientDAO.findAll()).thenReturn(Arrays.asList(client1, client2));

            // Act
            List<Client> result = clientService.findByRaisonSociale(raisonSociale);

            // Assert
            assertEquals(2, result.size());
            assertTrue(result.stream().allMatch(c -> raisonSociale.equals(c.getRaisonSociale())));
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
            String raisonSociale = "Test Company";
            Client client = new Client();
            client.setRaisonSociale(raisonSociale);
            when(clientDAO.findAll()).thenReturn(List.of(client));

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
            String raisonSociale = "Test Company";
            when(clientDAO.findAll()).thenReturn(List.of());

            // Act
            boolean result = clientService.existsByRaisonSociale(raisonSociale);

            // Assert
            assertFalse(result);
            verify(clientDAO).findAll();
        }
    }
} 