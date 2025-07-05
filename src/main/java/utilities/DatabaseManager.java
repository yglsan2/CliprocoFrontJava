package utilities;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.logging.Logger;

/**
 * Gestionnaire de base de données pour l'application.
 * Fournit les méthodes pour initialiser et gérer la connexion à la base de données.
 */
public final class DatabaseManager {
    
    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());
    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;
    private static boolean isInitialized = false;
    private static boolean isAvailable = true;
    private static final String PERSISTENCE_UNIT_NAME = "cliprocoUP";
    private static final String ERROR_MSG = "Erreur lors de l'initialisation de la base de données";
    private static final String SUCCESS_MSG = "EntityManagerFactory créé avec succès";
    
    /**
     * Constructeur privé pour empêcher l'instanciation.
     */
    private DatabaseManager() {
        // Classe utilitaire, pas d'instanciation
    }
    
    /**
     * Initialise la connexion à la base de données.
     * 
     * @return EntityManagerFactory configuré
     * @throws RuntimeException si l'initialisation échoue
     */
    public static EntityManagerFactory initialize() {
        if (isInitialized) {
            return entityManagerFactory;
        }
        
        try {
            if (entityManagerFactory == null || !entityManagerFactory.isOpen()) {
                entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
                LOGGER.info(SUCCESS_MSG);
                isInitialized = true;
            }
            return entityManagerFactory;
        } catch (Exception e) {
            LOGGER.severe(ERROR_MSG + ": " + e.getMessage());
            isAvailable = false;
            throw new RuntimeException(ERROR_MSG, e);
        }
    }
    
    /**
     * Récupère l'EntityManagerFactory actuel.
     * 
     * @return EntityManagerFactory ou null si non initialisé
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
    
    /**
     * Récupère un EntityManager.
     * 
     * @return EntityManager ou null si non disponible
     */
    public static EntityManager getEntityManager() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            if (entityManager == null || !entityManager.isOpen()) {
                entityManager = entityManagerFactory.createEntityManager();
            }
            return entityManager;
        }
        return null;
    }
    
    /**
     * Ferme l'EntityManager.
     */
    public static void closeEntityManager() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
            LOGGER.info("EntityManager fermé");
        }
    }
    
    /**
     * Ferme l'EntityManagerFactory.
     */
    public static void closeEntityManagerFactory() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
            LOGGER.info("EntityManagerFactory fermé");
        }
        entityManagerFactory = null;
        entityManager = null;
        isInitialized = false;
    }
    
    /**
     * Ferme la connexion à la base de données.
     */
    public static void close() {
        closeEntityManager();
        closeEntityManagerFactory();
    }
    
    /**
     * Arrête complètement le gestionnaire de base de données.
     */
    public static void shutdown() {
        closeEntityManager();
        closeEntityManagerFactory();
        isAvailable = false;
        isInitialized = false;
    }
    
    /**
     * Vérifie si la connexion est ouverte.
     * 
     * @return true si la connexion est ouverte, false sinon
     */
    public static boolean isOpen() {
        return entityManagerFactory != null && entityManagerFactory.isOpen();
    }
    
    /**
     * Vérifie si la base de données est disponible.
     * 
     * @return true si disponible, false sinon
     */
    public static boolean isAvailable() {
        return isAvailable;
    }
    
    /**
     * Vérifie si le gestionnaire est initialisé.
     * 
     * @return true si initialisé, false sinon
     */
    public static boolean isInitialized() {
        return isInitialized;
    }
    
    /**
     * Nettoie les ressources de la base de données.
     */
    public static void cleanup() {
        close();
    }
} 