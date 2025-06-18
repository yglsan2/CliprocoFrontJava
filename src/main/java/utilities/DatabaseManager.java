package utilities;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.logging.Logger;

public class DatabaseManager {
    private static final Logger logger = Logger.getLogger(DatabaseManager.class.getName());
    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;

    private DatabaseManager() {
        // Constructeur privé pour empêcher l'instanciation
    }

    public static void initialize() {
        try {
            if (entityManagerFactory == null) {
                entityManagerFactory = Persistence.createEntityManagerFactory("default");
                logger.info("EntityManagerFactory créé avec succès");
            }
        } catch (Exception e) {
            logger.severe("Erreur lors de l'initialisation de l'EntityManagerFactory: " + e.getMessage());
            throw new RuntimeException("Erreur lors de l'initialisation de la base de données", e);
        }
    }

    public static EntityManager getEntityManager() {
        if (entityManagerFactory == null) {
            initialize();
        }
        if (entityManager == null || !entityManager.isOpen()) {
            entityManager = entityManagerFactory.createEntityManager();
            logger.info("Nouvel EntityManager créé");
        }
        return entityManager;
    }

    public static void closeEntityManager() {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
            logger.info("EntityManager fermé");
        }
    }

    public static void closeEntityManagerFactory() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
            logger.info("EntityManagerFactory fermé");
        }
    }

    public static void shutdown() {
        closeEntityManager();
        closeEntityManagerFactory();
    }
} 