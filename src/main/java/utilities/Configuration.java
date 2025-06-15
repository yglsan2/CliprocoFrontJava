package utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe de gestion de la configuration de l'application.
 * Charge les propriétés depuis le fichier application.properties.
 */
public class Configuration {
    private static final Logger LOGGER = Logger.getLogger(Configuration.class.getName());
    private static final Properties properties = new Properties();
    private static Configuration instance;

    private Configuration() {
        loadProperties();
    }

    public static synchronized Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    private void loadProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("Impossible de trouver application.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement de la configuration", e);
            throw new RuntimeException("Erreur lors du chargement de la configuration", e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public int getIntProperty(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    public int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Integer.parseInt(value) : defaultValue;
    }

    public boolean getBooleanProperty(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    public long getLongProperty(String key) {
        return Long.parseLong(properties.getProperty(key));
    }

    public long getLongProperty(String key, long defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Long.parseLong(value) : defaultValue;
    }
} 