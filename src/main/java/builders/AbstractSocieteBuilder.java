package builders;

import models.Adresse;
import models.Societe;
import models.SocieteEntityException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractSocieteBuilder<T extends Societe> {
    
    protected final Map<String, Object> fields = new HashMap<>();
    protected final T entity;
    
    protected AbstractSocieteBuilder(T entity) {
        this.entity = entity;
    }
    
    protected static <T extends Societe> T createInstance(Class<T> clazz) throws SocieteEntityException {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new SocieteEntityException("Erreur lors de la création de l'instance", e);
        }
    }
    
    protected void setField(String fieldName, Object value) {
        fields.put(fieldName, value);
    }
    
    protected void setField(String fieldName, String subField, Object value) {
        if (value instanceof Adresse) {
            Adresse adresse = (Adresse) value;
            setField(fieldName + "." + subField, adresse);
        } else {
            setField(fieldName + "." + subField, value);
        }
    }
    
    protected Object getField(String fieldName) {
        return fields.get(fieldName);
    }
    
    protected T getEntity() {
        return entity;
    }
    
    public AbstractSocieteBuilder<T> dIdentifiant(Long identifiant) throws SocieteEntityException {
        setField("id", identifiant);
        return this;
    }
    
    public AbstractSocieteBuilder<T> deRaisonSociale(String raisonSociale) throws SocieteEntityException {
        if (raisonSociale == null || raisonSociale.trim().isEmpty()) {
            throw new SocieteEntityException("La raison sociale ne peut pas être vide");
        }
        setField("raisonSociale", raisonSociale);
        return this;
    }

    public AbstractSocieteBuilder<T> deTelephone(String telephone) throws SocieteEntityException {
        if (telephone == null || telephone.trim().isEmpty()) {
            throw new SocieteEntityException("Le numéro de téléphone ne peut pas être vide");
        }
        setField("telephone", telephone);
        return this;
    }

    public AbstractSocieteBuilder<T> dEmail(String email) throws SocieteEntityException {
        if (email == null || email.trim().isEmpty()) {
            throw new SocieteEntityException("L'adresse email ne peut pas être vide");
        }
        setField("email", email);
        return this;
    }

    public AbstractSocieteBuilder<T> deCommentaire(String commentaire) throws SocieteEntityException {
        setField("commentaire", commentaire);
        return this;
    }

    public AbstractSocieteBuilder<T> dAdresse(Adresse adresse) throws SocieteEntityException {
        if (adresse == null) {
            throw new SocieteEntityException("L'adresse ne peut pas être nulle");
        }
        setField("adresse", adresse);
        return this;
    }
    
    public T build() {
        fields.forEach((field, value) -> {
            try {
                String[] parts = field.split("\\.");
                if (parts.length > 1) {
                    Object obj = entity;
                    for (int i = 0; i < parts.length - 1; i++) {
                        obj = obj.getClass().getMethod("get" + capitalize(parts[i])).invoke(obj);
                    }
                    obj.getClass().getMethod("set" + capitalize(parts[parts.length - 1]), value.getClass())
                       .invoke(obj, value);
                } else {
                    entity.getClass().getMethod("set" + capitalize(field), value.getClass())
                          .invoke(entity, value);
                }
            } catch (Exception e) {
                throw new RuntimeException("Erreur lors de la construction de l'entité", e);
            }
        });
        return entity;
    }
    
    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
} 