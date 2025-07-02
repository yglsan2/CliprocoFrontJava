package utilities;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConnection {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;

    static {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("cliproco");
            entityManager = entityManagerFactory.createEntityManager();
            logger.info("Connexion à la base de données établie avec succès");
        } catch (Exception e) {
            logger.error("Erreur lors de l'initialisation de la connexion à la base de données", e);
            throw new RuntimeException("Erreur lors de l'initialisation de la connexion à la base de données", e);
        }
    }

    public static EntityManager getEntityManager() {
        return entityManager;
    }

    public static void close() {
        try {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
            if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
                entityManagerFactory.close();
            }
            logger.info("Connexion à la base de données fermée avec succès");
        } catch (Exception e) {
            logger.error("Erreur lors de la fermeture de la connexion à la base de données", e);
            throw new RuntimeException("Erreur lors de la fermeture de la connexion à la base de données", e);
        }
    }
} 