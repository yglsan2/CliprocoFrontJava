package dao.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConnection {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    private static EntityManagerFactory entityManagerFactory;

    static {
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("cliproco");
            logger.info("EntityManagerFactory créé avec succès");
        } catch (Exception e) {
            logger.error("Erreur lors de la création de l'EntityManagerFactory", e);
            throw new RuntimeException("Erreur lors de l'initialisation de la base de données", e);
        }
    }

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public static void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
            logger.info("EntityManagerFactory fermé avec succès");
        }
    }
} 