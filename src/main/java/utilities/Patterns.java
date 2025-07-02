package utilities;

import java.util.regex.Pattern;

/**
 * Classe des patterns à utiliser pour les expressions régulières.
 */
public final class Patterns {

    /**
     * Constructeur privé pour empêcher l'instanciation de la classe utilitaire.
     */
    private Patterns() {
        throw new IllegalStateException("Classe utilitaire, ne pas instancier");
    }

    /**
     *
     */
    public static final Pattern PATTERN_NUMERO_RUE =
            Pattern.compile("(?:\\d{0,3} +"
                    + "(bis|ter|quat)|\\G(?<!^))|(?:\\b\\d{0,3}(?:a|b)*\\b)");
    /**
     *
     */
    public static final Pattern PATTERN_NOM_RUE =
            Pattern.compile("\\b([a-zA"
                    + "-Z\\u0080-\\u024F]+(?:. |-| |'))"
                    + "*[a-zA-Z\\u0080-\\u024F]*"
                    + "(?:[0-9]+)*([a-zA-Z\\u0080-\\u024F])*\\b");
    /**
     *
     */
    public static final Pattern PATTERN_CODE_POSTAL =
            Pattern.compile("^(?:0[1-9]|[1-8][0-9]|9[0-8])\\d{3}$|^97[1-8]\\d{2}$|^98[46-8]\\d{2}$");
    /**
     *
     */
    public static final Pattern PATTERN_VILLE =
            Pattern.compile("\\b([a-zA-Z\\u0080-\\u024F]+"
                    + "(?:. |-| |'))*[a-zA-Z\\u0080-\\u024F]*\\b");

    /**
     * Numéro de téléphone français (national, international, DOM/TOM, Corse)
     */
    public static final Pattern PATTERN_TELEPHONE =
            Pattern.compile("^(?:(?:\\+33|0033)[1-9]|0[1-9])(?:[ .-]?\\d{2}){4}$");

    /**
     *
     */
    public static final Pattern PATTERN_MAIL =
            Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+"
                    + "@[a-zA-Z0-9.-]+$");
}
