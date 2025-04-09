package logs;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe du gestionnaire de logs.
 */
public final class LogManager {

    /**
     * Constructeur privé pour empêcher l'instanciation de la classe utilitaire.
     */
    private LogManager() {
        throw new IllegalStateException("Classe utilitaire, ne pas instancier");
    }

    /**
     *
     */
    public static final Logger LOGS =
            Logger.getLogger(LogManager.class.getName());

    /**
     * Initialisation du gestionnaire de logs.
     */
    public static void init() throws IOException {
        LocalDate date = LocalDate.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        FileHandler fh = new FileHandler("logs/log"
                + date.format(df) + ".log", true);
        LOGS.setUseParentHandlers(false);
        LOGS.addHandler(fh);
        fh.setFormatter(new LogFormatter());
        LOGS.info(System.lineSeparator());
    }

    /**
     * Méthode de log du lancement de l'application.
     */
    public static void run() {
        LOGS.log(Level.INFO, "Software running...");
    }

    /**
     * Méthode de log de la fermeture de l'application.
     */
    public static void stop() {
        LOGS.log(Level.INFO, "Software stopped !");
    }
}
