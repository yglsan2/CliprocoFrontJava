package utilities;

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
     *
     */
    public static final DateTimeFormatter FORMAT_DDMMYYYY =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");
}
