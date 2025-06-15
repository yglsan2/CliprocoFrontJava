package dao.jpa;

import models.Adresse;

/**
 * Implémentation JPA du DAO Adresse.
 */
public class AdresseJpaDAO extends AbstractJpaDAO<Adresse> {
    public AdresseJpaDAO() {
        super(Adresse.class);
    }
} 