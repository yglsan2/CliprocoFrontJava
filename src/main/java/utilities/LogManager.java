package utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogManager {
    private static final Logger logger = LoggerFactory.getLogger(LogManager.class);

    public static void info(String message) {
        logger.info(message);
    }

    public static void warning(String message) {
        logger.warn(message);
    }

    public static void error(String message) {
        logger.error(message);
    }

    public static void debug(String message) {
        logger.debug(message);
    }

    public static void logException(String message, Throwable throwable) {
        logger.error(message, throwable);
    }
} 