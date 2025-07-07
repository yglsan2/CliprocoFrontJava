package dao.jpa;

import models.Adresse;
import models.Client;
import dao.jpa.ClientJpaDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour ClientJpaDAO
 */
@DisplayName("Tests unitaires pour ClientJpaDAO")
class ClientJpaDAOTest {

    @Mock
    private EntityManager mockEntityManager;
    
    @Mock
    private EntityTransaction mockTransaction;
    
    @Mock
    private TypedQuery<Client> mockQuery;
    
    private ClientJpaDAO clientDAO;
    private Client testClient;
    private Adresse testAdresse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientDAO = new ClientJpaDAO(mockEntityManager);
        
        // Configuration des mocks
        when(mockEntityManager.getTransaction()).thenReturn(mockTransaction);
        
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

    @Test
    @DisplayName("Doit trouver un client par ID avec succès")
    void testFindByIdSuccess() throws Exception {
        // Arrange
        when(mockEntityManager.find(Client.class, 1)).thenReturn(testClient);
        
        // Act
        Optional<Client> result = clientDAO.findById(1);
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals(testClient.getIdentifiant(), result.get().getIdentifiant());
        assertEquals(testClient.getRaisonSociale(), result.get().getRaisonSociale());
        verify(mockEntityManager).find(Client.class, 1);
    }

    @Test
    @DisplayName("Doit retourner Optional.empty quand le client n'existe pas")
    void testFindByIdNotFound() throws Exception {
        // Arrange
        when(mockEntityManager.find(Client.class, 999)).thenReturn(null);
        
        // Act
        Optional<Client> result = clientDAO.findById(999);
        
        // Assert
        assertFalse(result.isPresent());
        verify(mockEntityManager).find(Client.class, 999);
    }

    @Test
    @DisplayName("Doit lever une exception quand l'ID est null")
    void testFindByIdNull() {
        // Act & Assert
        assertThrows(Exception.class, () -> {
            clientDAO.findById(null);
        });
    }

    @Test
    @DisplayName("Doit récupérer tous les clients avec succès")
    void testFindAllSuccess() throws Exception {
        // Arrange
        Client client1 = new Client();
        client1.setIdentifiant(1);
        client1.setRaisonSociale("Client 1");

        Client client2 = new Client();
        client2.setIdentifiant(2);
        client2.setRaisonSociale("Client 2");

        List<Client> expectedClients = Arrays.asList(client1, client2);
        when(mockEntityManager.createQuery(anyString(), eq(Client.class))).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(expectedClients);
        
        // Act
        List<Client> result = clientDAO.findAll();
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(client1.getIdentifiant(), result.get(0).getIdentifiant());
        assertEquals(client2.getIdentifiant(), result.get(1).getIdentifiant());
        verify(mockEntityManager).createQuery("SELECT c FROM Client c", Client.class);
        verify(mockQuery).getResultList();
    }

    @Test
    @DisplayName("Doit sauvegarder un client avec succès")
    void testSaveSuccess() throws Exception {
        // Arrange
        when(mockTransaction.isActive()).thenReturn(false);
        
        // Act
        Client result = clientDAO.save(testClient);
        
        // Assert
        assertNotNull(result);
        assertEquals(testClient.getIdentifiant(), result.getIdentifiant());
        verify(mockTransaction).begin();
        verify(mockEntityManager).persist(testClient);
        verify(mockTransaction).commit();
    }

    @Test
    @DisplayName("Doit lever une exception lors de la sauvegarde d'un client null")
    void testSaveNull() {
        // Act & Assert
        assertThrows(Exception.class, () -> {
            clientDAO.save(null);
        });
    }

    @Test
    @DisplayName("Doit mettre à jour un client avec succès")
    void testUpdateSuccess() throws Exception {
        // Arrange
        when(mockEntityManager.find(Client.class, 1)).thenReturn(testClient);
        when(mockEntityManager.merge(testClient)).thenReturn(testClient);
        when(mockTransaction.isActive()).thenReturn(false);
        
        // Act
        Client result = clientDAO.update(testClient);
        
        // Assert
        assertNotNull(result);
        assertEquals(testClient.getIdentifiant(), result.getIdentifiant());
        verify(mockTransaction).begin();
        verify(mockEntityManager).find(Client.class, 1);
        verify(mockEntityManager).merge(testClient);
        verify(mockTransaction).commit();
    }

    @Test
    @DisplayName("Doit lever une exception lors de la mise à jour d'un client inexistant")
    void testUpdateNotFound() throws Exception {
        // Arrange
        when(mockEntityManager.find(Client.class, 999)).thenReturn(null);
        when(mockTransaction.isActive()).thenReturn(true);
        
        Client nonExistentClient = new Client();
        nonExistentClient.setIdentifiant(999);
        nonExistentClient.setRaisonSociale("Client inexistant");
        
        // Act & Assert
        assertThrows(Exception.class, () -> {
            clientDAO.update(nonExistentClient);
        });
        verify(mockTransaction).begin();
        verify(mockEntityManager).find(Client.class, 999);
    }

    @Test
    @DisplayName("Doit lever une exception lors de la mise à jour d'un client null")
    void testUpdateNull() {
        // Act & Assert
        assertThrows(Exception.class, () -> {
            clientDAO.update(null);
        });
    }

    @Test
    @DisplayName("Doit supprimer un client avec succès")
    void testDeleteSuccess() throws Exception {
        // Arrange
        when(mockEntityManager.find(Client.class, 1)).thenReturn(testClient);
        when(mockTransaction.isActive()).thenReturn(false);
        
        // Act
        clientDAO.delete(testClient);
        
        // Assert
        verify(mockTransaction).begin();
        verify(mockEntityManager).find(Client.class, 1);
        verify(mockEntityManager).remove(testClient);
        verify(mockTransaction).commit();
    }

    @Test
    @DisplayName("Doit lever une exception lors de la suppression d'un client inexistant")
    void testDeleteNotFound() throws Exception {
        // Arrange
        when(mockEntityManager.find(Client.class, 999)).thenReturn(null);
        when(mockTransaction.isActive()).thenReturn(true);
        
        Client nonExistentClient = new Client();
        nonExistentClient.setIdentifiant(999);
        nonExistentClient.setRaisonSociale("Client inexistant");
        
        // Act & Assert
        assertThrows(Exception.class, () -> {
            clientDAO.delete(nonExistentClient);
        });
        verify(mockTransaction).begin();
        verify(mockEntityManager).find(Client.class, 999);
    }

    @Test
    @DisplayName("Doit lever une exception lors de la suppression d'un client null")
    void testDeleteNull() {
        // Act & Assert
        assertThrows(Exception.class, () -> {
            clientDAO.delete(null);
        });
    }

    @Test
    @DisplayName("Doit vérifier l'existence d'un client avec succès")
    void testExistsByIdSuccess() throws Exception {
        // Arrange
        when(mockEntityManager.find(Client.class, 1)).thenReturn(testClient);
        
        // Act
        boolean result = clientDAO.existsById(1);
        
        // Assert
        assertTrue(result);
        verify(mockEntityManager).find(Client.class, 1);
    }

    @Test
    @DisplayName("Doit retourner false quand le client n'existe pas")
    void testExistsByIdNotFound() throws Exception {
        // Arrange
        when(mockEntityManager.find(Client.class, 999)).thenReturn(null);
        
        // Act
        boolean result = clientDAO.existsById(999);
        
        // Assert
        assertFalse(result);
        verify(mockEntityManager).find(Client.class, 999);
    }

    @Test
    @DisplayName("Doit lever une exception quand l'ID est null pour existsById")
    void testExistsByIdNull() {
        // Act & Assert
        assertThrows(Exception.class, () -> {
            clientDAO.existsById(null);
        });
    }

    @Test
    @DisplayName("Doit fermer les ressources correctement")
    void testClose() throws Exception {
        // Arrange
        when(mockEntityManager.isOpen()).thenReturn(true);
        
        // Act
        clientDAO.close();
        
        // Assert
        verify(mockEntityManager).isOpen();
        verify(mockEntityManager).close();
    }

    @Test
    @DisplayName("Doit gérer les exceptions lors de la recherche")
    void testFindByIdException() {
        // Arrange
        when(mockEntityManager.find(Client.class, 1)).thenThrow(new RuntimeException("Erreur de base de données"));
        
        // Act & Assert
        assertThrows(Exception.class, () -> {
            clientDAO.findById(1);
        });
    }

    @Test
    @DisplayName("Doit gérer les exceptions lors de la récupération de tous les clients")
    void testFindAllException() {
        // Arrange
        when(mockEntityManager.createQuery(anyString(), eq(Client.class))).thenThrow(new RuntimeException("Erreur de requête"));
        
        // Act & Assert
        assertThrows(Exception.class, () -> {
            clientDAO.findAll();
        });
    }

    @Test
    @DisplayName("Doit gérer les exceptions lors de la sauvegarde")
    void testSaveException() {
        // Arrange
        when(mockTransaction.isActive()).thenReturn(true);
        doThrow(new RuntimeException("Erreur de persistance")).when(mockEntityManager).persist(any(Client.class));
        
        // Act & Assert
        assertThrows(Exception.class, () -> {
            clientDAO.save(testClient);
        });
        verify(mockTransaction).begin();
        verify(mockTransaction).rollback();
    }

    @Test
    @DisplayName("Doit gérer les exceptions lors de la mise à jour")
    void testUpdateException() {
        // Arrange
        when(mockEntityManager.find(Client.class, 1)).thenReturn(testClient);
        when(mockTransaction.isActive()).thenReturn(true);
        when(mockEntityManager.merge(any(Client.class))).thenThrow(new RuntimeException("Erreur de mise à jour"));
        
        // Act & Assert
        assertThrows(Exception.class, () -> {
            clientDAO.update(testClient);
        });
        verify(mockTransaction).begin();
        verify(mockTransaction).rollback();
    }

    @Test
    @DisplayName("Doit gérer les exceptions lors de la suppression")
    void testDeleteException() {
        // Arrange
        when(mockEntityManager.find(Client.class, 1)).thenReturn(testClient);
        when(mockTransaction.isActive()).thenReturn(true);
        doThrow(new RuntimeException("Erreur de suppression")).when(mockEntityManager).remove(any(Client.class));
        
        // Act & Assert
        assertThrows(Exception.class, () -> {
            clientDAO.delete(testClient);
        });
        verify(mockTransaction).begin();
        verify(mockTransaction).rollback();
    }

    @Test
    @DisplayName("Doit gérer les exceptions lors de la vérification d'existence")
    void testExistsByIdException() {
        // Arrange
        when(mockEntityManager.find(Client.class, 1)).thenThrow(new RuntimeException("Erreur de recherche"));
        
        // Act & Assert
        assertThrows(Exception.class, () -> {
            clientDAO.existsById(1);
        });
    }

    @Test
    @DisplayName("Doit gérer les exceptions lors de la fermeture")
    void testCloseException() {
        // Arrange
        when(mockEntityManager.isOpen()).thenReturn(true);
        doThrow(new RuntimeException("Erreur de fermeture")).when(mockEntityManager).close();
        
        // Act & Assert
        assertThrows(Exception.class, () -> {
            clientDAO.close();
        });
    }
} 