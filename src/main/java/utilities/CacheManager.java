package utilities;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;

/**
 * Gestionnaire de cache pour l'application.
 * Utilise Caffeine pour la mise en cache des données.
 */
public class CacheManager {
    private static final Configuration config = Configuration.getInstance();
    private static CacheManager instance;
    private final Cache<String, Object> cache;

    private CacheManager() {
        this.cache = Caffeine.newBuilder()
                .maximumSize(config.getLongProperty("cache.maxSize", 1000))
                .expireAfterWrite(config.getLongProperty("cache.expiration", 3600), TimeUnit.SECONDS)
                .build();
    }

    public static synchronized CacheManager getInstance() {
        if (instance == null) {
            instance = new CacheManager();
        }
        return instance;
    }

    /**
     * Récupère une valeur du cache.
     * @param key La clé
     * @return La valeur, ou null si non trouvée
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) cache.getIfPresent(key);
    }

    /**
     * Stocke une valeur dans le cache.
     * @param key La clé
     * @param value La valeur
     */
    public void put(String key, Object value) {
        cache.put(key, value);
    }

    /**
     * Supprime une valeur du cache.
     * @param key La clé
     */
    public void remove(String key) {
        cache.invalidate(key);
    }

    /**
     * Vide le cache.
     */
    public void clear() {
        cache.invalidateAll();
    }

    /**
     * Récupère une valeur du cache ou la calcule si non présente.
     * @param key La clé
     * @param supplier Le fournisseur de valeur
     * @return La valeur
     */
    @SuppressWarnings("unchecked")
    public <T> T getOrCompute(String key, java.util.function.Supplier<T> supplier) {
        return (T) cache.get(key, k -> supplier.get());
    }
} 