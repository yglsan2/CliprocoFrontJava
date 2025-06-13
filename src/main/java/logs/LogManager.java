package logs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import exceptions.ValidationException;
import exceptions.DatabaseException;
import exceptions.ApplicationException;

/**
 * Gestionnaire de logs de l'application.
 * Cette classe centralise la gestion des logs avec les fonctionnalités suivantes :
 * - Rotation des fichiers de log
 * - Formatage des messages
 * - Niveaux de log configurables
 * - Gestion des exceptions
 *
 * @author Benja2
 * @version 1.0
 * @since 1.0
 */
public final class LogManager {
    // Constantes pour la configuration
    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE = "application.log";
    private static final int MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 Mo
    private static final int MAX_FILES = 5;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Logger singleton
    private static final Logger LOGGER = Logger.getLogger(LogManager.class.getName());
    private static boolean initialized = false;

    /**
     * Constructeur privé pour empêcher l'instanciation.
     */
    private LogManager() {
        throw new AssertionError("Cette classe ne doit pas être instanciée");
    }

    /**
     * Initialise le système de logs.
     * Cette méthode doit être appelée une seule fois au démarrage de l'application.
     *
     * @throws IOException si une erreur survient lors de la création des fichiers de log
     */
    public static synchronized void initialize() throws IOException {
        if (!initialized) {
            try {
                // Création du répertoire de logs si nécessaire
                Path logDir = Paths.get(LOG_DIR);
                if (!Files.exists(logDir)) {
                    Files.createDirectories(logDir);
                }

                // Configuration du handler de fichier
                FileHandler fileHandler = new FileHandler(
                    LOG_DIR + "/" + LOG_FILE,
                    MAX_FILE_SIZE,
                    MAX_FILES,
                    true
                );
                fileHandler.setFormatter(new SimpleFormatter());
                LOGGER.addHandler(fileHandler);

                // Configuration du niveau de log
                LOGGER.setLevel(Level.INFO);

                initialized = true;
                info("Système de logs initialisé avec succès");

            } catch (IOException e) {
                warning("Erreur lors de l'initialisation du système de logs: " + e.getMessage());
                throw e;
            }
        }
    }

    /**
     * Enregistre un message de niveau INFO.
     * Utilisé pour les informations normales sur le fonctionnement de l'application.
     *
     * @param message le message à enregistrer
     */
    public static void info(String message) {
        try {
            if (!initialized) {
                initialize();
            }
            LOGGER.info(formatMessage(message));
        } catch (IOException e) {
            System.err.println("Erreur lors de l'initialisation des logs: " + e.getMessage());
        }
    }

    /**
     * Enregistre un message de niveau WARNING.
     * Utilisé pour les situations anormales mais non critiques.
     *
     * @param message le message à enregistrer
     */
    public static void warning(String message) {
        try {
            if (!initialized) {
                initialize();
            }
            LOGGER.warning(formatMessage(message));
        } catch (IOException e) {
            System.err.println("Erreur lors de l'initialisation des logs: " + e.getMessage());
        }
    }

    /**
     * Enregistre un message de niveau FINE pour le débogage.
     * Utilisé pour les informations détaillées de débogage.
     *
     * @param message le message à enregistrer
     */
    public static void debug(String message) {
        try {
            if (!initialized) {
                initialize();
            }
            LOGGER.fine(formatMessage(message));
        } catch (IOException e) {
            System.err.println("Erreur lors de l'initialisation des logs: " + e.getMessage());
        }
    }

    /**
     * Enregistre une exception avec un message.
     * Utilisé pour tracer les exceptions de manière appropriée.
     *
     * @param message le message à enregistrer
     * @param exception l'exception à enregistrer
     */
    public static void logException(String message, Throwable exception) {
        try {
            if (!initialized) {
                initialize();
            }
            // On utilise WARNING pour les exceptions attendues
            if (exception instanceof ValidationException || 
                exception instanceof DatabaseException || 
                exception instanceof ApplicationException) {
                LOGGER.log(Level.WARNING, formatMessage(message), exception);
            } else {
                // On utilise INFO pour les autres exceptions
                LOGGER.log(Level.INFO, formatMessage(message), exception);
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'initialisation des logs: " + e.getMessage());
        }
    }

    /**
     * Formate un message avec la date et l'heure.
     *
     * @param message le message à formater
     * @return le message formaté
     */
    private static String formatMessage(String message) {
        return String.format("[%s] %s",
            LocalDateTime.now().format(DATE_FORMAT),
            message
        );
    }
}
