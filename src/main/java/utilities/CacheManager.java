package utilities;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;

/**
 * Gestionnaire de cache pour l'application.
 * Utilise Caffeine pour fournir un cache en mémoire performant.
 * 
 * @author Benja2
 * @version 1.0
 * @since 1.0
 */
public final class CacheManager {
    
    private static final int MAXIMUM_SIZE = 10_000;
    private static final int EXPIRE_AFTER_WRITE_MINUTES = 30;
    private static final Cache<String, Object> cache;

    static {
        cache = Caffeine.newBuilder()
                .maximumSize(MAXIMUM_SIZE)
                .expireAfterWrite(EXPIRE_AFTER_WRITE_MINUTES, TimeUnit.MINUTES)
                .build();
    }

    /**
     * Constructeur privé pour empêcher l'instanciation de la classe utilitaire.
     */
    private CacheManager() {
        throw new IllegalStateException("Classe utilitaire, ne pas instancier");
    }

    /**
     * Récupère une valeur du cache.
     *
     * @param key la clé
     * @return la valeur associée à la clé, ou null si non trouvée
     */
    public static Object get(String key) {
        return cache.getIfPresent(key);
    }

    /**
     * Stocke une valeur dans le cache.
     *
     * @param key la clé
     * @param value la valeur à stocker
     */
    public static void put(String key, Object value) {
        cache.put(key, value);
    }

    /**
     * Supprime une valeur du cache.
     *
     * @param key la clé à supprimer
     */
    public static void remove(String key) {
        cache.invalidate(key);
    }

    /**
     * Vide le cache.
     */
    public static void clear() {
        cache.invalidateAll();
    }

    /**
     * Vérifie si une clé existe dans le cache.
     *
     * @param key la clé à vérifier
     * @return true si la clé existe, false sinon
     */
    public static boolean containsKey(String key) {
        return cache.getIfPresent(key) != null;
    }

    /**
     * Récupère la taille actuelle du cache.
     *
     * @return le nombre d'éléments dans le cache
     */
    public static long size() {
        return cache.estimatedSize();
    }
} 