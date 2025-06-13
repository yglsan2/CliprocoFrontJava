package logs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Level;

/**
 * Formateur personnalisé pour les logs de l'application.
 * Ce formateur crée des entrées de log structurées avec des informations
 * détaillées sur l'événement, incluant la date, l'heure, le niveau de log,
 * la classe source, la méthode et le message.
 * 
 * @author Benja2
 * @version 1.0
 * @since 1.0
 */
public class LogFormatter extends Formatter {
    
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String LOG_SEPARATOR = " | ";
    private static final String THREAD_INFO = "Thread: %s";
    private static final String EXCEPTION_FORMAT = "%nException: %s%nStack Trace:%n%s";
    private static final String METHOD_FORMAT = "%s()";
    private static final String CLASS_METHOD_FORMAT = "%s.%s";
    private static final String MESSAGE_FORMAT = "Message: %s";

    /**
     * Formate un enregistrement de log avec des informations détaillées.
     *
     * @param record L'enregistrement de log à formater
     * @return La ligne de log formatée avec toutes les informations pertinentes
     */
    @Override
    public String format(final LogRecord record) {
        StringBuilder logEntry = new StringBuilder();
        
        // Ajout du niveau de log avec indication visuelle
        logEntry.append(getFormattedLevel(record.getLevel()))
                .append(LOG_SEPARATOR)
                .append(getFormattedDate())
                .append(LOG_SEPARATOR)
                .append(String.format(THREAD_INFO, Thread.currentThread().getName()))
                .append(LOG_SEPARATOR)
                .append(formatSourceInfo(record))
                .append(LOG_SEPARATOR)
                .append(String.format(MESSAGE_FORMAT, record.getMessage()));

        // Ajout des informations sur l'exception si présente
        if (record.getThrown() != null) {
            logEntry.append(formatException(record.getThrown()));
        }

        logEntry.append("\n");
        return logEntry.toString();
    }

    /**
     * Retourne la date actuelle formatée avec précision milliseconde.
     *
     * @return La date formatée au format "yyyy-MM-dd HH:mm:ss.SSS"
     */
    private String getFormattedDate() {
        DateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return format.format(new Date());
    }

    /**
     * Retourne le niveau de log avec une indication visuelle de sa gravité.
     *
     * @param level Le niveau de log
     * @return Le niveau de log formaté
     */
    private String getFormattedLevel(Level level) {
        String levelName = level.getName();
        if (level == Level.SEVERE) {
            return "[ERROR]";
        } else if (level == Level.WARNING) {
            return "[WARN]";
        } else if (level == Level.INFO) {
            return "[INFO]";
        } else if (level == Level.FINE || level == Level.FINER || level == Level.FINEST) {
            return "[DEBUG]";
        }
        return "[" + levelName + "]";
    }

    /**
     * Formate les informations sur la source du log.
     *
     * @param record L'enregistrement de log
     * @return Les informations sur la source formatées
     */
    private String formatSourceInfo(LogRecord record) {
        String className = record.getSourceClassName();
        String methodName = record.getSourceMethodName();
        return String.format(CLASS_METHOD_FORMAT, className, 
            String.format(METHOD_FORMAT, methodName));
    }

    /**
     * Formate les informations sur une exception.
     *
     * @param throwable L'exception
     * @return Les informations sur l'exception formatées
     */
    private String formatException(Throwable throwable) {
        return String.format(EXCEPTION_FORMAT,
                throwable.getMessage(),
                getStackTrace(throwable));
    }

    /**
     * Récupère la stack trace d'une exception sous forme de chaîne.
     *
     * @param throwable L'exception
     * @return La stack trace formatée
     */
    private String getStackTrace(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append("\tat ").append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
