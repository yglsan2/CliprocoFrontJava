package builders;

/**
 * Classe générique builder.
 *
 * @param <T> Classe de l'objet à construire.
 */
public abstract class Builder<T> {

    /**
     * L'objet en cours de construction.
     */
    private final T entity;

    /**
     * Constructor.
     *
     * @param obj Objet à construire.
     */
    public Builder(final T obj) {
        this.entity = obj;
    }

    /**
     * Getter objet à construire.
     *
     * @return Objet à construire.
     */
    public T getEntity() {
        return entity;
    }

    /**
     * Méthode de construction de l'objet.
     *
     * @return Objet construit.
     */
    public abstract T build();
}
