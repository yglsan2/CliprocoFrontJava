package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import exceptions.DatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;

/**
 * Gestionnaire de connexion à la base de données.
 * Implémente le pattern Singleton pour garantir une seule instance de connexion.
 * 
 * @author Benja2
 * @version 1.0
 * @since 1.0
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    
    private static final String URL = "jdbc:mysql://localhost:3306/cliproco";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    private static EntityManagerFactory emf;
    private static EntityManager em;
    
    /**
     * Constructeur privé pour empêcher l'instanciation directe.
     * Initialise le driver MySQL.
     * 
     * @throws DatabaseException si le driver MySQL n'est pas trouvé
     */
    private DatabaseConnection() throws DatabaseException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            LogManager.info("Driver MySQL chargé avec succès");
        } catch (ClassNotFoundException e) {
            LogManager.logException("Driver MySQL introuvable", e);
            throw new DatabaseException("Driver MySQL introuvable", e);
        }
    }
    
    /**
     * Récupère l'instance unique de DatabaseConnection.
     * Crée une nouvelle instance si elle n'existe pas.
     * 
     * @return l'instance de DatabaseConnection
     * @throws DatabaseException si une erreur survient lors de la création de l'instance
     */
    public static synchronized DatabaseConnection getInstance() throws DatabaseException {
        if (instance == null) {
            try {
                instance = new DatabaseConnection();
            } catch (DatabaseException e) {
                LogManager.logException("Erreur lors de la création de l'instance DatabaseConnection", e);
                throw e;
            }
        }
        return instance;
    }
    
    /**
     * Récupère une connexion à la base de données.
     * Crée une nouvelle connexion si elle n'existe pas ou si elle est fermée.
     *
     * @return la connexion à la base de données
     * @throws DatabaseException si une erreur survient lors de la connexion
     */
    public Connection getConnection() throws DatabaseException {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                LogManager.info("Connexion à la base de données établie");
            }
            return connection;
        } catch (SQLException e) {
            LogManager.logException("Erreur lors de la connexion à la base de données", e);
            throw new DatabaseException("Erreur lors de la connexion à la base de données", e);
        }
    }
    
    /**
     * Ferme la connexion à la base de données.
     *
     * @throws DatabaseException si une erreur survient lors de la fermeture
     */
    public void closeConnection() throws DatabaseException {
        if (connection != null) {
            try {
                connection.close();
                LogManager.info("Connexion à la base de données fermée");
            } catch (SQLException e) {
                LogManager.logException("Erreur lors de la fermeture de la connexion à la base de données", e);
                throw new DatabaseException("Erreur lors de la fermeture de la connexion à la base de données", e);
            } finally {
                connection = null;
            }
        }
    }

    /**
     * Vérifie si la connexion est active.
     *
     * @return true si la connexion est active, false sinon
     */
    public boolean isConnectionActive() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            LogManager.logException("Erreur lors de la vérification de l'état de la connexion", e);
            return false;
        }
    }

    public static EntityManager getEntityManager() {
        try {
            if (emf == null) {
                LogManager.info("Initialisation de l'EntityManagerFactory");
                emf = Persistence.createEntityManagerFactory("cliproco");
            }
            if (em == null || !em.isOpen()) {
                LogManager.info("Création d'un nouvel EntityManager");
                em = emf.createEntityManager();
            }
            return em;
        } catch (PersistenceException e) {
            LogManager.logException("Erreur lors de la création de l'EntityManager", e);
            throw e;
        }
    }

    public static void closeEntityManager() {
        try {
            if (em != null && em.isOpen()) {
                LogManager.info("Fermeture de l'EntityManager");
                em.close();
            }
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la fermeture de l'EntityManager", e);
        }
    }

    public static void closeEntityManagerFactory() {
        try {
            if (emf != null && emf.isOpen()) {
                LogManager.info("Fermeture de l'EntityManagerFactory");
                emf.close();
            }
        } catch (Exception e) {
            LogManager.logException("Erreur lors de la fermeture de l'EntityManagerFactory", e);
        }
    }

    public static void beginTransaction() {
        try {
            EntityManager em = getEntityManager();
            if (!em.getTransaction().isActive()) {
                LogManager.info("Début de la transaction");
                em.getTransaction().begin();
            }
        } catch (Exception e) {
            LogManager.logException("Erreur lors du début de la transaction", e);
            throw e;
        }
    }

    public static void commitTransaction() {
        try {
            EntityManager em = getEntityManager();
            if (em.getTransaction().isActive()) {
                LogManager.info("Commit de la transaction");
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            LogManager.logException("Erreur lors du commit de la transaction", e);
            throw e;
        }
    }

    public static void rollbackTransaction() {
        try {
            EntityManager em = getEntityManager();
            if (em.getTransaction().isActive()) {
                LogManager.info("Rollback de la transaction");
                em.getTransaction().rollback();
            }
        } catch (Exception e) {
            LogManager.logException("Erreur lors du rollback de la transaction", e);
            throw e;
        }
    }
} 