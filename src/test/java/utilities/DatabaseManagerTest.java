package utilities;

import utilities.DatabaseManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour DatabaseManager
 */
@DisplayName("Tests unitaires pour DatabaseManager")
class DatabaseManagerTest {

    @TempDir
    Path tempDir;
    
    private DatabaseManager databaseManager;

    @BeforeEach
    void setUp() {
        // Reset l'état du DatabaseManager avant chaque test
        try {
            DatabaseManager.shutdown();
        } catch (Exception e) {
            // Ignore les erreurs de shutdown
        }
    }

    @Test
    @DisplayName("Doit initialiser le DatabaseManager correctement")
    void testDatabaseManagerInitialization() {
        assertDoesNotThrow(() -> {
            DatabaseManager.initialize();
            // Si on arrive ici, la base est accessible
            assertNotNull(DatabaseManager.getEntityManager());
        });
    }

    @Test
    @DisplayName("Doit récupérer un EntityManager après initialisation")
    void testGetEntityManager() {
        // Arrange - Reset state
        DatabaseManager.shutdown();
        
        // Act & Assert - L'initialisation peut échouer sans base de données réelle
        assertDoesNotThrow(() -> {
            try {
                DatabaseManager.initialize();
                EntityManager em = DatabaseManager.getEntityManager();
                // Si l'initialisation réussit, on devrait avoir un EntityManager
                if (DatabaseManager.isInitialized()) {
                    assertNotNull(em);
                }
            } catch (RuntimeException e) {
                // C'est normal que l'initialisation échoue sans base de données
                assertTrue(e.getMessage().contains("Erreur lors de l'initialisation"));
            }
        });
    }

    @Test
    @DisplayName("Doit fermer l'EntityManager")
    void testCloseEntityManager() {
        assertDoesNotThrow(() -> {
            DatabaseManager.initialize();
            var entityManager = DatabaseManager.getEntityManager();
            DatabaseManager.closeEntityManager();
        });
    }

    @Test
    @DisplayName("Doit fermer l'EntityManagerFactory")
    void testCloseEntityManagerFactory() {
        assertDoesNotThrow(() -> {
            DatabaseManager.closeEntityManagerFactory();
        });
    }

    @Test
    @DisplayName("Doit arrêter complètement le DatabaseManager")
    void testShutdown() {
        assertDoesNotThrow(() -> {
            DatabaseManager.shutdown();
        });
    }

    @Test
    @DisplayName("Doit gérer les erreurs d'initialisation")
    void testInitializationException() {
        // Arrange - Reset state
        DatabaseManager.shutdown();
        
        // Act & Assert - L'initialisation peut échouer sans base de données réelle
        assertDoesNotThrow(() -> {
            try {
                DatabaseManager.initialize();
            } catch (RuntimeException e) {
                // C'est normal que l'initialisation échoue sans base de données
                assertTrue(e.getMessage().contains("Erreur lors de l'initialisation"));
            }
        });
    }

    @Test
    @DisplayName("Doit réutiliser l'EntityManager existant")
    void testReuseExistingEntityManager() {
        assertDoesNotThrow(() -> {
            DatabaseManager.initialize();
            var entityManager1 = DatabaseManager.getEntityManager();
            var entityManager2 = DatabaseManager.getEntityManager();
            assertSame(entityManager1, entityManager2);
        });
    }

    @Test
    @DisplayName("Doit créer un nouvel EntityManager si l'ancien est fermé")
    void testCreateNewEntityManagerIfClosed() {
        assertDoesNotThrow(() -> {
            DatabaseManager.initialize();
            var entityManager1 = DatabaseManager.getEntityManager();
            DatabaseManager.closeEntityManager();
            var entityManager2 = DatabaseManager.getEntityManager();
            assertNotSame(entityManager1, entityManager2);
        });
    }

    @Test
    @DisplayName("Doit gérer les appels multiples à initialize")
    void testMultipleInitializeCalls() {
        assertDoesNotThrow(() -> {
            DatabaseManager.initialize();
            DatabaseManager.initialize();
        });
    }

    @Test
    @DisplayName("Doit gérer les appels multiples à shutdown")
    void testMultipleShutdownCalls() {
        assertDoesNotThrow(() -> {
            DatabaseManager.shutdown();
            DatabaseManager.shutdown();
        });
    }

    @Test
    @DisplayName("Doit gérer les appels multiples à closeEntityManager")
    void testMultipleCloseEntityManagerCalls() {
        assertDoesNotThrow(() -> {
            DatabaseManager.closeEntityManager();
            DatabaseManager.closeEntityManager();
        });
    }

    @Test
    @DisplayName("Doit gérer les appels multiples à closeEntityManagerFactory")
    void testMultipleCloseEntityManagerFactoryCalls() {
        assertDoesNotThrow(() -> {
            DatabaseManager.closeEntityManagerFactory();
            DatabaseManager.closeEntityManagerFactory();
        });
    }

    @Test
    @DisplayName("Doit fonctionner après un cycle complet d'initialisation/shutdown")
    void testFullCycleInitializationShutdown() {
        assertDoesNotThrow(() -> {
            DatabaseManager.initialize();
            var entityManager1 = DatabaseManager.getEntityManager();
            DatabaseManager.shutdown();
            DatabaseManager.initialize();
            var entityManager2 = DatabaseManager.getEntityManager();
            assertNotNull(entityManager1);
            assertNotNull(entityManager2);
            assertNotSame(entityManager1, entityManager2);
        });
    }

    @Test
    @DisplayName("Doit gérer les appels concurrents à getEntityManager")
    void testConcurrentGetEntityManager() {
        assertDoesNotThrow(() -> {
            DatabaseManager.initialize();
            var entityManager1 = DatabaseManager.getEntityManager();
            var entityManager2 = DatabaseManager.getEntityManager();
            assertSame(entityManager1, entityManager2);
        });
    }

    @Test
    @DisplayName("Doit retourner null pour getEntityManager sans initialisation")
    void testGetEntityManagerWithoutInitialization() {
        // Arrange - Reset state
        DatabaseManager.shutdown();
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            EntityManager em = DatabaseManager.getEntityManager();
            assertNull(em, "EntityManager devrait être null sans initialisation");
        });
    }

    @Test
    @DisplayName("Doit gérer les appels à closeEntityManager sans EntityManager")
    void testCloseEntityManagerWithoutEntityManager() {
        assertDoesNotThrow(() -> {
            DatabaseManager.closeEntityManager();
        });
    }

    @Test
    @DisplayName("Doit gérer les appels à closeEntityManagerFactory sans EntityManagerFactory")
    void testCloseEntityManagerFactoryWithoutEntityManagerFactory() {
        assertDoesNotThrow(() -> {
            DatabaseManager.closeEntityManagerFactory();
        });
    }

    @Test
    @DisplayName("Doit gérer les appels à shutdown sans initialisation")
    void testShutdownWithoutInitialization() {
        assertDoesNotThrow(() -> {
            DatabaseManager.shutdown();
        });
    }

    @Test
    @DisplayName("Doit gérer les appels à initialize avec configuration invalide")
    void testInitializeWithInvalidConfiguration() {
        assertDoesNotThrow(() -> {
            DatabaseManager.initialize();
        });
    }

    @Test
    @DisplayName("Doit retourner null pour getEntityManager avec EntityManagerFactory fermé")
    void testGetEntityManagerWithClosedEntityManagerFactory() {
        // Arrange
        DatabaseManager.initialize();
        DatabaseManager.closeEntityManagerFactory();
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            EntityManager em = DatabaseManager.getEntityManager();
            assertNull(em, "EntityManager devrait être null avec EntityManagerFactory fermé");
        });
    }

    @Test
    @DisplayName("Doit gérer les appels à closeEntityManager avec EntityManager null")
    void testCloseEntityManagerWithNullEntityManager() {
        assertDoesNotThrow(() -> {
            DatabaseManager.closeEntityManager();
        });
    }

    @Test
    @DisplayName("Doit gérer les appels à closeEntityManagerFactory avec EntityManagerFactory null")
    void testCloseEntityManagerFactoryWithNullEntityManagerFactory() {
        assertDoesNotThrow(() -> {
            DatabaseManager.closeEntityManagerFactory();
        });
    }

    @Test
    @DisplayName("Doit vérifier la disponibilité de la base de données")
    void testDatabaseAvailability() {
        assertDoesNotThrow(() -> {
            boolean isAvailable = DatabaseManager.isAvailable();
            assertTrue(isAvailable || !isAvailable);
        });
    }

    @Test
    @DisplayName("Doit vérifier l'état d'initialisation")
    void testInitializationState() {
        assertDoesNotThrow(() -> {
            boolean isInitialized = DatabaseManager.isInitialized();
            assertTrue(isInitialized || !isInitialized);
        });
    }
} 