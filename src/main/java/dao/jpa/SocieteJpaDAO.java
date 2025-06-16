package dao.jpa;

import dao.IDAO;
import models.Societe;
import java.util.Optional;

/**
 * Implémentation JPA du DAO pour les sociétés.
 */
public class SocieteJpaDAO extends AbstractJpaDAO<Societe, Long> implements IDAO<Societe, Long> {
    
    public SocieteJpaDAO() {
        super(Societe.class);
    }

    @Override
    public Optional<Societe> findById(Long id) {
        return super.findById(id);
    }

    @Override
    public Societe save(Societe societe) {
        return super.save(societe);
    }

    @Override
    public Societe update(Societe societe) {
        return super.update(societe);
    }

    @Override
    public void delete(Societe societe) {
        super.delete(societe);
    }
} 