package utilities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classes des libellés servant au formattage des données.
 */
public final class Formatters {

    /**
     * Constructeur privé pour empêcher l'instanciation de la classe utilitaire.
     */
    private Formatters() {
        throw new IllegalStateException("Classe utilitaire, ne pas instancier");
    }

    /**
     * Format de date dd/MM/yyyy.
     */
    public static final DateTimeFormatter FORMAT_DDMMYYYY =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Parse une date au format dd/MM/yyyy ou yyyy-MM-dd.
     *
     * @param date La date à parser.
     * @return La date parsée.
     */
    public static LocalDate parseDate(String date) {
        if (date.contains("-")) {
            String[] dt = date.split("-");
            return LocalDate.parse(dt[2] + '/' + dt[1] + '/' + dt[0], FORMAT_DDMMYYYY);
        }
        return LocalDate.parse(date, FORMAT_DDMMYYYY);
    }
}
