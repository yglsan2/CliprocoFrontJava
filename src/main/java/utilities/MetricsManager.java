package utilities;

import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire des métriques de l'application.
 */
public class MetricsManager {
    private static final Map<String, Integer> metrics = new HashMap<>();

    public static void incrementMetric(String metricName) {
        metrics.merge(metricName, 1, Integer::sum);
        LogManager.logInfo("Métrique incrémentée : " + metricName);
    }

    public static void decrementMetric(String metricName) {
        metrics.merge(metricName, -1, Integer::sum);
        LogManager.logInfo("Métrique décrémentée : " + metricName);
    }

    public static void setMetric(String metricName, Integer value) {
        metrics.put(metricName, value);
        LogManager.logInfo("Métrique définie : " + metricName + " = " + value);
    }

    public static Integer getMetric(String metricName) {
        return metrics.getOrDefault(metricName, 0);
    }

    public static void resetMetric(String metricName) {
        metrics.remove(metricName);
        LogManager.logInfo("Métrique réinitialisée : " + metricName);
    }

    public static void resetAllMetrics() {
        metrics.clear();
        LogManager.logInfo("Toutes les métriques ont été réinitialisées");
    }

    public static Map<String, Integer> getAllMetrics() {
        return new HashMap<>(metrics);
    }
} 