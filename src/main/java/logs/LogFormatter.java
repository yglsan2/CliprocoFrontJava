package logs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Classe du formateur de logs.
 */
public class LogFormatter extends Formatter {
    /**
     * Méthode de formatage des logs.
     *
     * @param record L'enregistrement de log à formater
     * @return La ligne de log formatée
     */
    @Override
    public String format(final LogRecord record) {

        return "["
                + record.getLevel()
                + "] - "
                + getFormattedDate()
                + " | Class : " + record.getSourceClassName()
                + " | Method : " + record.getSourceMethodName()
                + "() | Message : " + record.getMessage()
                + "\n";
    }

    /**
     * Retourne la date actuelle formatée.
     *
     * @return La date formatée au format "yyyy-MM-dd HH:mm:ss"
     */
    private String getFormattedDate() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }
}
