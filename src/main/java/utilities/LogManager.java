package utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Gestionnaire de logs pour l'application.
 * Cette classe permet de gérer les logs de l'application avec différents niveaux
 * (INFO, WARNING, ERROR) et de les enregistrer dans un fichier.
 */
public class LogManager {
    private static final Logger LOGGER = Logger.getLogger(LogManager.class.getName());
    private static String LOG_FILE;
    private static FileHandler fileHandler;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Initialise le gestionnaire de logs.
     * Crée le fichier de log s'il n'existe pas et configure le FileHandler.
     */
    public static void run() {
        try {
            // Déterminer le chemin du fichier de log
            String webappPath = System.getProperty("catalina.home") + "/webapps/CliprocoJEE";
            LOG_FILE = webappPath + "/application.log";
            
            // Créer le répertoire s'il n'existe pas
            File webappDir = new File(webappPath);
            if (!webappDir.exists()) {
                webappDir.mkdirs();
            }
            
            if (fileExists()) {
                deleteFile();
            }
            fileHandler = new FileHandler(LOG_FILE, true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
            logInfo("Initialisation du système de logs");
        } catch (IOException e) {
            System.err.println("Erreur lors de l'initialisation des logs : " + e.getMessage());
        }
    }

    /**
     * Arrête le gestionnaire de logs.
     * Ferme le FileHandler et libère les ressources.
     */
    public static void stop() {
        if (fileHandler != null) {
            fileHandler.close();
            LOGGER.removeHandler(fileHandler);
            logInfo("Arrêt du système de logs");
        }
    }

    /**
     * Enregistre un message de niveau INFO.
     * @param message Le message à enregistrer
     */
    public static void logInfo(String message) {
        String timestamp = LocalDateTime.now().format(DATE_FORMAT);
        String logMessage = String.format("[%s] [INFO] %s", timestamp, message);
        LOGGER.info(logMessage);
        writeToFile(logMessage);
    }

    /**
     * Enregistre un message de niveau WARNING.
     * @param message Le message à enregistrer
     */
    public static void logWarning(String message) {
        String timestamp = LocalDateTime.now().format(DATE_FORMAT);
        String logMessage = String.format("[%s] [WARNING] %s", timestamp, message);
        LOGGER.warning(logMessage);
        writeToFile(logMessage);
    }

    /**
     * Enregistre un message de niveau ERROR.
     * @param message Le message à enregistrer
     */
    public static void logError(String message) {
        String timestamp = LocalDateTime.now().format(DATE_FORMAT);
        String logMessage = String.format("[%s] [ERROR] %s", timestamp, message);
        LOGGER.severe(logMessage);
        writeToFile(logMessage);
    }

    /**
     * Enregistre une exception avec un message.
     * @param message Le message décrivant l'erreur
     * @param e L'exception à enregistrer
     */
    public static void logException(String message, Exception e) {
        String timestamp = LocalDateTime.now().format(DATE_FORMAT);
        String exceptionMessage = (e != null) ? e.getMessage() : "null";
        String stackTrace = (e != null) ? getStackTraceAsString(e) : "null";
        String logMessage = String.format("[%s] [ERROR] %s - Exception: %s - Stack trace: %s",
                timestamp, message, exceptionMessage, stackTrace);
        LOGGER.severe(logMessage);
        writeToFile(logMessage);
    }

    /**
     * Vérifie si le fichier de log existe.
     * @return true si le fichier existe, false sinon
     */
    private static boolean fileExists() {
        return LOG_FILE != null && Files.exists(Paths.get(LOG_FILE));
    }

    /**
     * Supprime le fichier de log existant.
     */
    private static void deleteFile() {
        try {
            if (LOG_FILE != null) {
                Files.deleteIfExists(Paths.get(LOG_FILE));
                System.out.println("Ancien fichier de log supprimé");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la suppression du fichier de log : " + e.getMessage());
        }
    }

    /**
     * Écrit un message dans le fichier de log.
     * @param message Le message à écrire
     */
    private static void writeToFile(String message) {
        try {
            if (LOG_FILE != null) {
                Path path = Paths.get(LOG_FILE);
                Files.write(path, (message + System.lineSeparator()).getBytes(),
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture dans le fichier de log : " + e.getMessage());
        }
    }

    /**
     * Convertit la stack trace d'une exception en chaîne de caractères.
     * @param e L'exception dont on veut la stack trace
     * @return La stack trace sous forme de chaîne de caractères
     */
    private static String getStackTraceAsString(Exception e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
} 