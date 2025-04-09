package builders;

import models.SocieteEntityException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

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

    /**
     * Crée une instance d'un objet via réflexion.
     *
     * @param <U> Type de l'objet à créer.
     * @param clazz Classe de l'objet à créer.
     * @return nouvelle instance de l'objet
     * @throws SocieteEntityException si une erreur survient
     */
    protected static <U> U createInstance(Class<U> clazz) throws SocieteEntityException {
        try {
            Constructor<U> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new SocieteEntityException("Erreur lors de la création de l'instance de " + clazz.getSimpleName(), e);
        }
    }

    /**
     * Définit la valeur d'un champ via réflexion.
     *
     * @param fieldName Nom du champ.
     * @param value Valeur à définir.
     * @throws SocieteEntityException Si une erreur survient.
     */
    protected void setField(String fieldName, Object value) throws SocieteEntityException {
        try {
            Field field = entity.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(entity, value);
        } catch (Exception e) {
            throw new SocieteEntityException("Erreur lors de la définition du champ " + fieldName, e);
        }
    }

    /**
     * Définit la valeur d'un champ d'un objet imbriqué via réflexion.
     *
     * @param fieldName Nom du champ.
     * @param value Valeur à définir.
     * @param target Objet cible.
     * @throws SocieteEntityException Si une erreur survient.
     */
    protected void setField(String fieldName, Object value, Object target) throws SocieteEntityException {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new SocieteEntityException("Erreur lors de la définition du champ " + fieldName, e);
        }
    }

    /**
     * Récupère la valeur d'un champ via réflexion.
     *
     * @param fieldName Nom du champ.
     * @return Valeur du champ.
     * @throws SocieteEntityException Si une erreur survient.
     */
    protected Object getField(String fieldName) throws SocieteEntityException {
        try {
            Field field = entity.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(entity);
        } catch (Exception e) {
            throw new SocieteEntityException("Erreur lors de la récupération du champ " + fieldName, e);
        }
    }
}
