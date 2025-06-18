package dao;

import java.util.List;
import java.util.Optional;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;

/**
 * Interface générique pour les opérations CRUD sur les entités.
 * @param <T> Le type d'entité
 * @param <ID> Le type de l'identifiant de l'entité
 */
public interface IDAO<T, ID> {
    /**
     * Sauvegarde une entité.
     * @param entity L'entité à sauvegarder
     * @return L'entité sauvegardée
     * @throws ValidationException si l'entité n'est pas valide
     * @throws DatabaseException si une erreur de base de données survient
     */
    T save(T entity) throws ValidationException, DatabaseException;

    /**
     * Met à jour une entité.
     * @param entity L'entité à mettre à jour
     * @return L'entité mise à jour
     * @throws ValidationException si l'entité n'est pas valide
     * @throws ResourceNotFoundException si l'entité n'existe pas
     * @throws DatabaseException si une erreur de base de données survient
     */
    T update(T entity) throws ValidationException, ResourceNotFoundException, DatabaseException;

    /**
     * Supprime une entité.
     * @param entity L'entité à supprimer
     * @throws ValidationException si l'entité n'est pas valide
     * @throws ResourceNotFoundException si l'entité n'existe pas
     * @throws DatabaseException si une erreur de base de données survient
     */
    void delete(T entity) throws ValidationException, ResourceNotFoundException, DatabaseException;

    /**
     * Trouve une entité par son identifiant.
     * @param id L'identifiant de l'entité
     * @return Un Optional contenant l'entité si trouvée
     * @throws ValidationException si l'identifiant n'est pas valide
     * @throws DatabaseException si une erreur de base de données survient
     */
    Optional<T> findById(ID id) throws ValidationException, DatabaseException;

    /**
     * Récupère toutes les entités.
     * @return La liste de toutes les entités
     * @throws DatabaseException si une erreur de base de données survient
     */
    List<T> findAll() throws DatabaseException;

    /**
     * Vérifie si une entité existe avec l'identifiant donné.
     * @param id L'identifiant de l'entité
     * @return true si l'entité existe, false sinon
     * @throws ValidationException si l'identifiant n'est pas valide
     * @throws DatabaseException si une erreur de base de données survient
     */
    boolean existsById(ID id) throws ValidationException, DatabaseException;

    /**
     * Ferme les ressources utilisées par le DAO.
     * @throws DatabaseException si une erreur survient lors de la fermeture des ressources
     */
    void close() throws DatabaseException;
} 