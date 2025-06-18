package utilities;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatabaseManagerTest {
    private MockedStatic<DatabaseManager> mockedDatabaseManager;
    private EntityManagerFactory mockEntityManagerFactory;
    private EntityManager mockEntityManager;

    @BeforeEach
    void setUp() {
        mockEntityManagerFactory = mock(EntityManagerFactory.class);
        mockEntityManager = mock(EntityManager.class);
        mockedDatabaseManager = Mockito.mockStatic(DatabaseManager.class);
    }

    @AfterEach
    void tearDown() {
        mockedDatabaseManager.close();
    }

    @Test
    @DisplayName("initialize devrait créer un EntityManagerFactory")
    void initializeShouldCreateEntityManagerFactory() {
        // Arrange
        when(mockEntityManagerFactory.isOpen()).thenReturn(true);

        // Act
        DatabaseManager.initialize();

        // Assert
        verify(mockEntityManagerFactory, times(1L)).isOpen();
    }

    @Test
    @DisplayName("getEntityManager devrait retourner un EntityManager valide")
    void getEntityManagerShouldReturnValidEntityManager() {
        // Arrange
        when(mockEntityManagerFactory.createEntityManager()).thenReturn(mockEntityManager);
        when(mockEntityManager.isOpen()).thenReturn(true);

        // Act
        EntityManager result = DatabaseManager.getEntityManager();

        // Assert
        assertNotNull(result);
        assertTrue(result.isOpen());
    }

    @Test
    @DisplayName("closeEntityManager devrait fermer l'EntityManager")
    void closeEntityManagerShouldCloseEntityManager() {
        // Arrange
        when(mockEntityManager.isOpen()).thenReturn(true);

        // Act
        DatabaseManager.closeEntityManager();

        // Assert
        verify(mockEntityManager, times(1L)).close();
    }

    @Test
    @DisplayName("closeEntityManagerFactory devrait fermer l'EntityManagerFactory")
    void closeEntityManagerFactoryShouldCloseEntityManagerFactory() {
        // Arrange
        when(mockEntityManagerFactory.isOpen()).thenReturn(true);

        // Act
        DatabaseManager.closeEntityManagerFactory();

        // Assert
        verify(mockEntityManagerFactory, times(1L)).close();
    }

    @Test
    @DisplayName("shutdown devrait fermer l'EntityManager et l'EntityManagerFactory")
    void shutdownShouldCloseBothEntityManagerAndFactory() {
        // Arrange
        when(mockEntityManager.isOpen()).thenReturn(true);
        when(mockEntityManagerFactory.isOpen()).thenReturn(true);

        // Act
        DatabaseManager.shutdown();

        // Assert
        verify(mockEntityManager, times(1L)).close();
        verify(mockEntityManagerFactory, times(1L)).close();
    }
} 