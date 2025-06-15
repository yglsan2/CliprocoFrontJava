package utilities;

import exceptions.DatabaseException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.function.Supplier;

/**
 * Gestionnaire de transactions pour l'application.
 * Permet d'exécuter des opérations dans une transaction avec gestion automatique
 * du commit/rollback.
 */
public class TransactionManager {
    private static final Configuration config = Configuration.getInstance();

    /**
     * Exécute une opération dans une transaction.
     * @param em L'EntityManager à utiliser
     * @param operation L'opération à exécuter
     * @return Le résultat de l'opération
     * @throws DatabaseException Si une erreur survient
     */
    public static <T> T executeInTransaction(EntityManager em, Supplier<T> operation) throws DatabaseException {
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            
            T result = operation.get();
            
            if (transaction.isActive()) {
                transaction.commit();
            }
            
            return result;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new DatabaseException("Erreur lors de l'exécution de la transaction", e);
        }
    }

    /**
     * Exécute une opération dans une transaction sans retour.
     * @param em L'EntityManager à utiliser
     * @param operation L'opération à exécuter
     * @throws DatabaseException Si une erreur survient
     */
    public static void executeInTransaction(EntityManager em, Runnable operation) throws DatabaseException {
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();
            
            operation.run();
            
            if (transaction.isActive()) {
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new DatabaseException("Erreur lors de l'exécution de la transaction", e);
        }
    }

    /**
     * Exécute une opération dans une transaction avec timeout.
     * @param em L'EntityManager à utiliser
     * @param operation L'opération à exécuter
     * @param timeout Le timeout en secondes
     * @return Le résultat de l'opération
     * @throws DatabaseException Si une erreur survient
     */
    public static <T> T executeInTransactionWithTimeout(EntityManager em, Supplier<T> operation, int timeout) throws DatabaseException {
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.setTimeout(timeout);
            transaction.begin();
            
            T result = operation.get();
            
            if (transaction.isActive()) {
                transaction.commit();
            }
            
            return result;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new DatabaseException("Erreur lors de l'exécution de la transaction", e);
        }
    }
} 