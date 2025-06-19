package utilities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LogManagerTest {

    @TempDir
    Path tempDir;

    private Path logFile;

    @BeforeEach
    void setUp() {
        logFile = tempDir.resolve("application.log");
        System.setProperty("user.dir", tempDir.toString());
        LogManager.run();
    }

    @AfterEach
    void tearDown() {
        LogManager.stop();
    }

    @Test
    @DisplayName("logInfo devrait écrire un message de niveau INFO dans le fichier de log")
    void logInfoShouldWriteInfoMessageToLogFile() throws IOException {
        // Arrange
        String message = "Test info message";

        // Act
        LogManager.logInfo(message);

        // Assert
        List<String> lines = Files.readAllLines(logFile);
        assertFalse(lines.isEmpty());
        assertTrue(lines.get(0).contains("[INFO]"));
        assertTrue(lines.get(0).contains(message));
    }

    @Test
    @DisplayName("logWarning devrait écrire un message de niveau WARNING dans le fichier de log")
    void logWarningShouldWriteWarningMessageToLogFile() throws IOException {
        // Arrange
        String message = "Test warning message";

        // Act
        LogManager.logWarning(message);

        // Assert
        List<String> lines = Files.readAllLines(logFile);
        assertFalse(lines.isEmpty());
        assertTrue(lines.get(0).contains("[WARNING]"));
        assertTrue(lines.get(0).contains(message));
    }

    @Test
    @DisplayName("logError devrait écrire un message de niveau ERROR dans le fichier de log")
    void logErrorShouldWriteErrorMessageToLogFile() throws IOException {
        // Arrange
        String message = "Test error message";

        // Act
        LogManager.logError(message);

        // Assert
        List<String> lines = Files.readAllLines(logFile);
        assertFalse(lines.isEmpty());
        assertTrue(lines.get(0).contains("[ERROR]"));
        assertTrue(lines.get(0).contains(message));
    }

    @Test
    @DisplayName("logException devrait écrire une exception dans le fichier de log")
    void logExceptionShouldWriteExceptionToLogFile() throws IOException {
        // Arrange
        String message = "Test exception message";
        Exception exception = new RuntimeException("Test exception");

        // Act
        LogManager.logException(message, exception);

        // Assert
        List<String> lines = Files.readAllLines(logFile);
        assertFalse(lines.isEmpty());
        assertTrue(lines.get(0).contains("[ERROR]"));
        assertTrue(lines.get(0).contains(message));
        assertTrue(lines.get(0).contains(exception.getMessage()));
        assertTrue(lines.get(0).contains("Stack trace"));
    }

    @Test
    @DisplayName("run devrait créer un nouveau fichier de log")
    void runShouldCreateNewLogFile() {
        // Assert
        assertTrue(Files.exists(logFile));
    }

    @Test
    @DisplayName("stop devrait fermer le gestionnaire de logs")
    void stopShouldCloseLogManager() {
        // Act
        LogManager.stop();

        // Assert
        assertTrue(Files.exists(logFile));
    }
} 