package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import exceptions.DatabaseException;
import logs.LogManager;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    
    private static final String URL = "jdbc:mysql://localhost:3306/cliproco";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LogManager.logException("Driver MySQL introuvable", e);
            throw new DatabaseException("Driver MySQL introuvable", e);
        }
    }
    
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    /**
     * Récupère une connexion à la base de données.
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
            }
        }
    }
} 