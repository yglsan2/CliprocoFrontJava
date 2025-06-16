package dao;

import java.util.List;
import java.util.Optional;

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
     */
    T save(T entity);

    /**
     * Met à jour une entité.
     * @param entity L'entité à mettre à jour
     * @return L'entité mise à jour
     */
    T update(T entity);

    /**
     * Supprime une entité.
     * @param entity L'entité à supprimer
     */
    void delete(T entity);

    /**
     * Trouve une entité par son identifiant.
     * @param id L'identifiant de l'entité
     * @return Un Optional contenant l'entité si trouvée
     */
    Optional<T> findById(ID id);

    /**
     * Récupère toutes les entités.
     * @return La liste de toutes les entités
     */
    List<T> findAll();
} 