package utilities;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import exceptions.MetricsException;
import logs.LogManager;

/**
 * Gestionnaire de métriques pour l'application.
 * Utilise Micrometer avec Prometheus pour collecter et exposer les métriques.
 * 
 * @author Benja2
 * @version 1.0
 * @since 1.0
 */
public final class MetricsManager {
    
    private static final MeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    
    /**
     * Constructeur privé pour empêcher l'instanciation de la classe utilitaire.
     */
    private MetricsManager() {
        throw new IllegalStateException("Classe utilitaire, ne pas instancier");
    }

    /**
     * Crée ou récupère un compteur.
     *
     * @param name le nom du compteur
     * @param description la description du compteur
     * @return le compteur
     * @throws MetricsException si une erreur survient lors de la création du compteur
     */
    public static Counter counter(String name, String description) throws MetricsException {
        try {
            return Counter.builder(name)
                    .description(description)
                    .register(registry);
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la création du compteur: " + name, e);
            throw new MetricsException("Erreur lors de la création du compteur: " + name, e);
        }
    }

    /**
     * Crée ou récupère un timer.
     *
     * @param name le nom du timer
     * @param description la description du timer
     * @return le timer
     * @throws MetricsException si une erreur survient lors de la création du timer
     */
    public static Timer timer(String name, String description) throws MetricsException {
        try {
            return Timer.builder(name)
                    .description(description)
                    .register(registry);
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la création du timer: " + name, e);
            throw new MetricsException("Erreur lors de la création du timer: " + name, e);
        }
    }

    /**
     * Récupère le registre de métriques.
     *
     * @return le registre de métriques
     */
    public static MeterRegistry getRegistry() {
        return registry;
    }

    /**
     * Mesure le temps d'exécution d'une opération.
     *
     * @param timer le timer à utiliser
     * @param operation l'opération à mesurer
     * @return le résultat de l'opération
     * @throws MetricsException si une erreur survient lors de la mesure
     */
    public static <T> T measureExecutionTime(Timer timer, ThrowingSupplier<T> operation) throws MetricsException {
        try {
            return timer.record(operation::get);
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la mesure du temps d'exécution", e);
            throw new MetricsException("Erreur lors de la mesure du temps d'exécution", e);
        }
    }

    /**
     * Interface fonctionnelle pour les opérations qui peuvent lever des exceptions.
     *
     * @param <T> le type de retour
     */
    @FunctionalInterface
    public interface ThrowingSupplier<T> {
        T get() throws Exception;
    }
} 