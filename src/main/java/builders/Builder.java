package builders;

import utilities.LogManager;

/**
 * Classe constructrice générique.
 *
 * @param <T> Type d'entité à construire
 */
public abstract class Builder<T> {

    /**
     * L'objet en cours de construction.
     */
    private final T entity;

    /**
     * Constructor.
     *
     * @param entity Entité à construire
     */
    public Builder(T entity) {
        LogManager.logInfo("Initialisation d'un nouveau Builder pour " + entity.getClass().getSimpleName());
        this.entity = entity;
    }

    /**
     * Getter entité en cours de construction.
     *
     * @return Entité en cours de construction
     */
    protected T getEntity() {
        LogManager.logInfo("Récupération de l'entité en cours de construction: " + entity);
        return entity;
    }

    /**
     * Méthode de construction de l'objet.
     *
     * @return Objet construit.
     */
    public abstract T build();
}
