package dao.jpa;

import models.Prospect;
import models.Adresse;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour ProspectJpaDAO
 */
@DisplayName("Tests unitaires pour ProspectJpaDAO")
class ProspectJpaDAOTest {

    @Mock
    private EntityManager mockEntityManager;
    
    @Mock
    private EntityTransaction mockTransaction;
    
    @Mock
    private TypedQuery<Prospect> mockTypedQuery;
    
    @Mock
    private Query mockQuery;
    
    private ProspectJpaDAO prospectDAO;
    private Prospect testProspect;
    private Adresse testAdresse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        prospectDAO = new ProspectJpaDAO(mockEntityManager);
        // Configuration des mocks
        when(mockEntityManager.getTransaction()).thenReturn(mockTransaction);
        
        // Création des objets de test
        testAdresse = new Adresse();
        testAdresse.setIdentifiant(1);
        testAdresse.setNumeroRue("456");
        testAdresse.setNomRue("Avenue des Champs");
        testAdresse.setCodePostal("75008");
        testAdresse.setVille("Paris");
        
        testProspect = new Prospect();
        testProspect.setIdentifiant(1);
        testProspect.setRaisonSociale("Test Prospect");
        testProspect.setMail("prospect@test.com");
        testProspect.setTelephone("0987654321");
        testProspect.setAdresse(testAdresse);
        testProspect.setCommentaires("Prospect intéressant");
    }

    @Test
    @DisplayName("Doit sauvegarder un prospect avec succès")
    void testSaveSuccess() throws ValidationException, DatabaseException {
        // Arrange
        when(mockTransaction.isActive()).thenReturn(false);
        // Act
        Prospect result = prospectDAO.save(testProspect);
        // Assert
        assertNotNull(result);
        assertEquals(testProspect.getIdentifiant(), result.getIdentifiant());
        verify(mockEntityManager).persist(testProspect);
    }

    @Test
    @DisplayName("Doit gérer la sauvegarde d'un prospect null")
    void testSaveNull() {
        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            prospectDAO.save(null);
        });
    }

    @Test
    @DisplayName("Doit trouver un prospect par ID avec succès")
    void testFindByIdSuccess() throws ValidationException, DatabaseException {
        // Arrange
        when(mockEntityManager.find(Prospect.class, 1)).thenReturn(testProspect);
        
        // Act
        Optional<Prospect> result = prospectDAO.findById(1);
        
        // Assert
        assertTrue(result.isPresent());
        assertEquals(testProspect.getIdentifiant(), result.get().getIdentifiant());
        verify(mockEntityManager).find(Prospect.class, 1);
    }

    @Test
    @DisplayName("Doit retourner Optional.empty quand le prospect n'existe pas")
    void testFindByIdNotFound() throws ValidationException, DatabaseException {
        // Arrange
        when(mockEntityManager.find(Prospect.class, 999)).thenReturn(null);
        
        // Act
        Optional<Prospect> result = prospectDAO.findById(999);
        
        // Assert
        assertFalse(result.isPresent());
        verify(mockEntityManager).find(Prospect.class, 999);
    }

    @Test
    @DisplayName("Doit gérer la recherche avec un ID null")
    void testFindByIdNull() {
        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            prospectDAO.findById(null);
        });
    }

    @Test
    @DisplayName("Doit récupérer tous les prospects avec succès")
    void testFindAllSuccess() throws DatabaseException {
        // Arrange
        List<Prospect> prospects = Arrays.asList(testProspect);
        when(mockEntityManager.createQuery(anyString(), eq(Prospect.class))).thenReturn(mockTypedQuery);
        when(mockTypedQuery.getResultList()).thenReturn(prospects);
        
        // Act
        List<Prospect> result = prospectDAO.findAll();
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testProspect.getIdentifiant(), result.get(0).getIdentifiant());
        verify(mockEntityManager).createQuery("SELECT p FROM Prospect p", Prospect.class);
    }

    @Test
    @DisplayName("Doit retourner une liste vide quand aucun prospect n'existe")
    void testFindAllEmpty() throws DatabaseException {
        // Arrange
        when(mockEntityManager.createQuery(anyString(), eq(Prospect.class))).thenReturn(mockTypedQuery);
        when(mockTypedQuery.getResultList()).thenReturn(Arrays.asList());
        
        // Act
        List<Prospect> result = prospectDAO.findAll();
        
        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Doit mettre à jour un prospect avec succès")
    void testUpdateSuccess() throws ValidationException, ResourceNotFoundException, DatabaseException {
        // Arrange
        when(mockEntityManager.find(Prospect.class, 1)).thenReturn(testProspect);
        when(mockEntityManager.merge(testProspect)).thenReturn(testProspect);
        when(mockTransaction.isActive()).thenReturn(false);
        // Act
        Prospect result = prospectDAO.update(testProspect);
        // Assert
        assertNotNull(result);
        assertEquals(testProspect.getIdentifiant(), result.getIdentifiant());
        verify(mockEntityManager).find(Prospect.class, 1);
        verify(mockEntityManager).merge(testProspect);
    }

    @Test
    @DisplayName("Doit gérer la mise à jour d'un prospect null")
    void testUpdateNull() {
        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            prospectDAO.update(null);
        });
    }

    @Test
    @DisplayName("Doit supprimer un prospect avec succès")
    void testDeleteSuccess() throws ValidationException, ResourceNotFoundException, DatabaseException {
        // Arrange
        when(mockEntityManager.find(Prospect.class, 1)).thenReturn(testProspect);
        
        // Act
        prospectDAO.delete(testProspect);
        
        // Assert
        verify(mockEntityManager).find(Prospect.class, 1);
        verify(mockEntityManager).remove(testProspect);
    }

    @Test
    @DisplayName("Doit gérer la suppression d'un prospect inexistant")
    void testDeleteNotFound() {
        // Arrange
        when(mockEntityManager.find(Prospect.class, 999)).thenReturn(null);
        when(mockTransaction.isActive()).thenReturn(false);
        Prospect nonExistentProspect = new Prospect();
        nonExistentProspect.setIdentifiant(999);
        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            prospectDAO.delete(nonExistentProspect);
        });
        verify(mockTransaction).begin();
        verify(mockEntityManager).find(Prospect.class, 999);
    }

    @Test
    @DisplayName("Doit gérer la suppression d'un prospect null")
    void testDeleteNull() {
        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            prospectDAO.delete(null);
        });
    }

    @Test
    @DisplayName("Doit trouver des prospects par raison sociale")
    void testFindByRaisonSocialeSuccess() throws ValidationException, DatabaseException {
        // Arrange
        List<Prospect> prospects = Arrays.asList(testProspect);
        when(mockEntityManager.createQuery("SELECT p FROM Prospect p WHERE p.raisonSociale LIKE :raisonSociale", Prospect.class)).thenReturn(mockTypedQuery);
        when(mockTypedQuery.setParameter(eq("raisonSociale"), anyString())).thenReturn(mockTypedQuery);
        when(mockTypedQuery.getResultList()).thenReturn(prospects);
        // Act
        List<Prospect> result = prospectDAO.findByRaisonSociale("Test Prospect");
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testProspect.getIdentifiant(), result.get(0).getIdentifiant());
        verify(mockEntityManager).createQuery("SELECT p FROM Prospect p WHERE p.raisonSociale LIKE :raisonSociale", Prospect.class);
        verify(mockTypedQuery).setParameter("raisonSociale", "%Test Prospect%");
    }

    @Test
    @DisplayName("Doit retourner une liste vide pour une raison sociale inexistante")
    void testFindByRaisonSocialeNotFound() throws ValidationException, DatabaseException {
        // Arrange
        when(mockEntityManager.createQuery(anyString(), eq(Prospect.class))).thenReturn(mockTypedQuery);
        when(mockTypedQuery.setParameter(anyString(), anyString())).thenReturn(mockTypedQuery);
        when(mockTypedQuery.getResultList()).thenReturn(Arrays.asList());
        
        // Act
        List<Prospect> result = prospectDAO.findByRaisonSociale("Non Existent");
        
        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Doit gérer la recherche avec une raison sociale null")
    void testFindByRaisonSocialeNull() {
        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            prospectDAO.findByRaisonSociale(null);
        });
    }

    @Test
    @DisplayName("Doit gérer les exceptions de base de données")
    void testDatabaseException() {
        // Arrange
        when(mockEntityManager.find(Prospect.class, 1)).thenThrow(new RuntimeException("Erreur de base de données"));
        
        // Act & Assert
        assertThrows(DatabaseException.class, () -> {
            prospectDAO.findById(1);
        });
    }

    @Test
    @DisplayName("Doit gérer les exceptions de transaction")
    void testTransactionException() {
        // Arrange
        when(mockTransaction.isActive()).thenReturn(true);
        doThrow(new RuntimeException("Erreur de transaction")).when(mockEntityManager).persist(any(Prospect.class));
        // Act & Assert
        assertThrows(DatabaseException.class, () -> {
            prospectDAO.save(testProspect);
        });
        verify(mockTransaction).rollback();
    }

    @Test
    @DisplayName("Doit gérer les exceptions de requête")
    void testQueryException() {
        // Arrange
        when(mockEntityManager.createQuery(anyString(), eq(Prospect.class))).thenThrow(new RuntimeException("Erreur de base de données"));
        
        // Act & Assert
        assertThrows(DatabaseException.class, () -> {
            prospectDAO.findAll();
        });
    }

    @Test
    @DisplayName("Doit valider les données du prospect avant sauvegarde")
    void testValidateProspectBeforeSave() {
        // Arrange
        Prospect invalidProspect = new Prospect();
        invalidProspect.setRaisonSociale(""); // Raison sociale vide
        invalidProspect.setMail("valid@prospect.com");
        invalidProspect.setTelephone("0123456789");
        invalidProspect.setAdresse(testAdresse);
        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            prospectDAO.save(invalidProspect);
        });
    }

    @Test
    @DisplayName("Doit valider l'adresse du prospect")
    void testValidateProspectAddress() {
        // Arrange
        Prospect invalidProspect = new Prospect();
        invalidProspect.setRaisonSociale("Valid");
        invalidProspect.setMail("valid@prospect.com");
        invalidProspect.setTelephone("0123456789");
        invalidProspect.setAdresse(null); // Adresse manquante
        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            prospectDAO.save(invalidProspect);
        });
    }

    @Test
    @DisplayName("Doit gérer les prospects avec des données numériques invalides")
    void testProspectWithInvalidNumericData() {
        // Arrange
        Prospect invalidProspect = new Prospect();
        invalidProspect.setRaisonSociale("Valid");
        invalidProspect.setMail("valid@prospect.com");
        invalidProspect.setTelephone("abc123"); // Téléphone invalide
        invalidProspect.setAdresse(testAdresse);
        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            prospectDAO.save(invalidProspect);
        });
    }

    @Test
    @DisplayName("Doit gérer les prospects avec des emails invalides")
    void testProspectWithInvalidEmail() {
        // Arrange
        Prospect invalidProspect = new Prospect();
        invalidProspect.setRaisonSociale("Valid");
        invalidProspect.setMail("invalid-email"); // Email invalide
        invalidProspect.setTelephone("0123456789");
        invalidProspect.setAdresse(testAdresse);
        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            prospectDAO.save(invalidProspect);
        });
    }

    @Test
    @DisplayName("Doit gérer les prospects avec des téléphones invalides")
    void testProspectWithInvalidPhone() {
        // Arrange
        Prospect invalidProspect = new Prospect();
        invalidProspect.setRaisonSociale("Valid");
        invalidProspect.setMail("valid@prospect.com");
        invalidProspect.setTelephone("123"); // Téléphone trop court
        invalidProspect.setAdresse(testAdresse);
        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            prospectDAO.save(invalidProspect);
        });
    }

    @Test
    @DisplayName("Doit gérer les prospects avec des commentaires très longs")
    void testProspectWithLongComments() {
        // Arrange
        Prospect invalidProspect = new Prospect();
        invalidProspect.setRaisonSociale("Valid");
        invalidProspect.setMail("valid@prospect.com");
        invalidProspect.setTelephone("0123456789");
        invalidProspect.setAdresse(testAdresse);
        invalidProspect.setCommentaires("a".repeat(1001)); // Commentaire trop long
        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            prospectDAO.save(invalidProspect);
        });
    }

    @Test
    @DisplayName("Doit gérer les prospects avec des raisons sociales très longues")
    void testProspectWithLongRaisonSociale() {
        // Arrange
        Prospect invalidProspect = new Prospect();
        invalidProspect.setRaisonSociale("a".repeat(256)); // Raison sociale trop longue
        invalidProspect.setMail("valid@prospect.com");
        invalidProspect.setTelephone("0123456789");
        invalidProspect.setAdresse(testAdresse);
        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            prospectDAO.save(invalidProspect);
        });
    }
} 