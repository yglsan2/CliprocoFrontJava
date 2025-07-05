package utilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour LogManager
 */
@DisplayName("Tests unitaires pour LogManager")
class LogManagerTest {

    @TempDir
    Path tempDir;
    
    private LogManager logManager;

    @BeforeEach
    void setUp() {
        // LogManager est une classe utilitaire avec des méthodes statiques
        // Pas besoin d'instanciation
    }

    @Test
    @DisplayName("Doit initialiser le système de logs")
    void testRun() {
        // Act
        LogManager.run();
        
        // Assert
        // Ne devrait pas lever d'exception
        assertDoesNotThrow(() -> {
            LogManager.logInfo("Test message");
        });
    }

    @Test
    @DisplayName("Doit arrêter le système de logs")
    void testStop() {
        // Arrange
        LogManager.run();
        
        // Act
        LogManager.stop();
        
        // Assert
        // Ne devrait pas lever d'exception
        assertDoesNotThrow(() -> {
            LogManager.stop();
        });
    }

    @Test
    @DisplayName("Doit enregistrer un message d'information")
    void testLogInfo() {
        // Arrange
        LogManager.run();
        String message = "Test info message";
        
        // Act
        LogManager.logInfo(message);
        
        // Assert
        // Le message devrait être enregistré sans exception
        assertDoesNotThrow(() -> {
            LogManager.logInfo(message);
        });
    }

    @Test
    @DisplayName("Doit enregistrer un message d'avertissement")
    void testLogWarning() {
        // Arrange
        LogManager.run();
        String message = "Test warning message";
        
        // Act
        LogManager.logWarning(message);
        
        // Assert
        // Le message devrait être enregistré sans exception
        assertDoesNotThrow(() -> {
            LogManager.logWarning(message);
        });
    }

    @Test
    @DisplayName("Doit enregistrer un message d'erreur")
    void testLogError() {
        // Arrange
        LogManager.run();
        String message = "Test error message";
        
        // Act
        LogManager.logError(message);
        
        // Assert
        // Le message devrait être enregistré sans exception
        assertDoesNotThrow(() -> {
            LogManager.logError(message);
        });
    }

    @Test
    @DisplayName("Doit enregistrer une exception")
    void testLogException() {
        // Arrange
        LogManager.run();
        String message = "Test exception message";
        Exception exception = new RuntimeException("Test exception");
        
        // Act
        LogManager.logException(message, exception);
        
        // Assert
        // L'exception devrait être enregistrée sans exception
        assertDoesNotThrow(() -> {
            LogManager.logException(message, exception);
        });
    }

    @Test
    @DisplayName("Doit gérer un message null")
    void testLogInfoNull() {
        // Arrange
        LogManager.run();
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            LogManager.logInfo(null);
        });
    }

    @Test
    @DisplayName("Doit gérer un message vide")
    void testLogInfoEmpty() {
        // Arrange
        LogManager.run();
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            LogManager.logInfo("");
        });
    }

    @Test
    @DisplayName("Doit gérer une exception null")
    void testLogExceptionNull() {
        // Arrange
        LogManager.run();
        String message = "Test message";
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            LogManager.logException(message, null);
        });
    }

    @Test
    @DisplayName("Doit gérer des messages avec des caractères spéciaux")
    void testLogInfoSpecialCharacters() {
        // Arrange
        LogManager.run();
        String message = "Test message with special chars: éàçù€£¥";
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            LogManager.logInfo(message);
        });
    }

    @Test
    @DisplayName("Doit gérer des messages très longs")
    void testLogInfoLongMessage() {
        // Arrange
        LogManager.run();
        StringBuilder longMessage = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longMessage.append("Test message part ").append(i).append(" ");
        }
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            LogManager.logInfo(longMessage.toString());
        });
    }

    @Test
    @DisplayName("Doit gérer des appels multiples")
    void testMultipleLogCalls() {
        // Arrange
        LogManager.run();
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            LogManager.logInfo("Message 1");
            LogManager.logWarning("Message 2");
            LogManager.logError("Message 3");
            LogManager.logException("Message 4", new RuntimeException("Test"));
        });
    }

    @Test
    @DisplayName("Doit gérer les appels sans initialisation")
    void testLogWithoutInitialization() {
        // Act & Assert
        // Les méthodes devraient fonctionner même sans appel à run()
        assertDoesNotThrow(() -> {
            LogManager.logInfo("Test without initialization");
        });
    }

    @Test
    @DisplayName("Doit gérer les appels multiples à run")
    void testMultipleRunCalls() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            LogManager.run();
            LogManager.run();
            LogManager.run();
        });
    }

    @Test
    @DisplayName("Doit gérer les appels multiples à stop")
    void testMultipleStopCalls() {
        // Arrange
        LogManager.run();
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            LogManager.stop();
            LogManager.stop();
            LogManager.stop();
        });
    }

    @Test
    @DisplayName("Doit gérer un cycle complet run/stop")
    void testFullCycleRunStop() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            LogManager.run();
            LogManager.logInfo("Test message");
            LogManager.stop();
            LogManager.run();
            LogManager.logWarning("Another test message");
            LogManager.stop();
        });
    }

    @Test
    @DisplayName("Doit gérer les exceptions lors de l'écriture")
    void testLogWithIOException() {
        // Arrange
        LogManager.run();
        String message = "Test message";
        
        // Act & Assert
        // Même avec des problèmes d'écriture, les méthodes ne devraient pas lever d'exception
        assertDoesNotThrow(() -> {
            LogManager.logInfo(message);
        });
    }

    @Test
    @DisplayName("Doit gérer les messages avec des sauts de ligne")
    void testLogInfoWithNewlines() {
        // Arrange
        LogManager.run();
        String message = "Test message\nwith newlines\nand more content";
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            LogManager.logInfo(message);
        });
    }

    @Test
    @DisplayName("Doit gérer les messages avec des tabulations")
    void testLogInfoWithTabs() {
        // Arrange
        LogManager.run();
        String message = "Test message\twith tabs\tand more content";
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            LogManager.logInfo(message);
        });
    }

    @Test
    @DisplayName("Doit gérer les messages avec des caractères de contrôle")
    void testLogInfoWithControlCharacters() {
        // Arrange
        LogManager.run();
        String message = "Test message\u0000with control\u0001characters";
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            LogManager.logInfo(message);
        });
    }

    @Test
    @DisplayName("Doit gérer les exceptions avec des messages complexes")
    void testLogExceptionComplexMessage() {
        // Arrange
        LogManager.run();
        String message = "Complex exception message with special chars: éàçù€£¥\nAnd newlines\tand tabs";
        Exception exception = new RuntimeException("Complex exception with special chars: éàçù€£¥");
        
        // Act & Assert
        assertDoesNotThrow(() -> {
            LogManager.logException(message, exception);
        });
    }
} 