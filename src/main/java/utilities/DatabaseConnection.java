package utilities;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe utilitaire pour la gestion de la connexion à la base de données via JPA
 * 
 * Cette classe fournit un accès centralisé à l'EntityManager et l'EntityManagerFactory
 * pour l'unité de persistance "cliprocoUP" (Cliproco Unit of Persistence).
 * 
 * L'unité de persistance "cliprocoUP" est définie dans le fichier persistence.xml
 * et gère la connexion à la base de données MySQL avec la configuration Hibernate.
 * 
 * @author Équipe CliprocoJEE
 * @version 1.0
 */
public class DatabaseConnection {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    
    // EntityManagerFactory pour l'unité de persistance "cliprocoUP"
    private static EntityManagerFactory entityManagerFactory;
    
    // EntityManager partagé pour les opérations de base de données
    private static EntityManager entityManager;

    /**
     * Bloc d'initialisation statique pour configurer la connexion JPA
     * 
     * Ce bloc s'exécute au chargement de la classe et initialise l'EntityManagerFactory
     * et l'EntityManager pour l'unité de persistance "cliprocoUP".
     * 
     * L'unité de persistance "cliprocoUP" (Cliproco Unit of Persistence) contient :
     * - La configuration de connexion MySQL
     * - Les classes d'entités JPA
     * - Les propriétés Hibernate
     */
    static {
        try {
            // Création de l'EntityManagerFactory pour l'unité de persistance "cliprocoUP"
            // UP = Unit of Persistence (Unité de Persistance)
            entityManagerFactory = Persistence.createEntityManagerFactory("cliprocoUP");
            
            // Création de l'EntityManager partagé
            entityManager = entityManagerFactory.createEntityManager();
            logger.info("Connexion à la base de données établie avec succès pour l'unité cliprocoUP");
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