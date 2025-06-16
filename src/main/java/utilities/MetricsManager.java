package utilities;

import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire des métriques de l'application.
 */
public class MetricsManager {
    private static final Map<String, Long> metrics = new HashMap<>();

    public static void incrementMetric(String metricName) {
        metrics.merge(metricName, 1L, Long::sum);
        LogManager.logInfo("Métrique incrémentée : " + metricName);
    }

    public static void decrementMetric(String metricName) {
        metrics.merge(metricName, -1L, Long::sum);
        LogManager.logInfo("Métrique décrémentée : " + metricName);
    }

    public static void setMetric(String metricName, long value) {
        metrics.put(metricName, value);
        LogManager.logInfo("Métrique définie : " + metricName + " = " + value);
    }

    public static long getMetric(String metricName) {
        return metrics.getOrDefault(metricName, 0L);
    }

    public static void resetMetric(String metricName) {
        metrics.remove(metricName);
        LogManager.logInfo("Métrique réinitialisée : " + metricName);
    }

    public static void resetAllMetrics() {
        metrics.clear();
        LogManager.logInfo("Toutes les métriques ont été réinitialisées");
    }

    public static Map<String, Long> getAllMetrics() {
        return new HashMap<>(metrics);
    }
} 