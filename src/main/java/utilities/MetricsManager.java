package utilities;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Gestionnaire de métriques pour l'application.
 * Fournit des méthodes pour collecter et analyser les métriques de performance.
 */
public final class MetricsManager {
    
    private static final ConcurrentHashMap<String, AtomicLong> METRICS = new ConcurrentHashMap<>();
    
    /**
     * Constructeur privé pour empêcher l'instanciation.
     */
    private MetricsManager() {
        // Classe utilitaire, pas d'instanciation
    }
    
    /**
     * Incrémente un compteur de métrique.
     * 
     * @param metricName Nom de la métrique
     */
    public static void incrementCounter(final String metricName) {
        METRICS.computeIfAbsent(metricName, k -> new AtomicLong()).incrementAndGet();
    }
    
    /**
     * Définit la valeur d'une métrique.
     * 
     * @param metricName Nom de la métrique
     * @param value Valeur à définir
     */
    public static void setValue(final String metricName, final long value) {
        METRICS.computeIfAbsent(metricName, k -> new AtomicLong()).set(value);
    }
    
    /**
     * Récupère la valeur d'une métrique.
     * 
     * @param metricName Nom de la métrique
     * @return Valeur de la métrique ou 0 si non trouvée
     */
    public static long getValue(final String metricName) {
        AtomicLong metric = METRICS.get(metricName);
        return metric != null ? metric.get() : 0;
    }
    
    /**
     * Réinitialise toutes les métriques.
     */
    public static void reset() {
        METRICS.clear();
    }
    
    /**
     * Récupère toutes les métriques.
     * 
     * @return Copie des métriques actuelles
     */
    public static ConcurrentHashMap<String, AtomicLong> getAllMetrics() {
        return new ConcurrentHashMap<>(METRICS);
    }
} 