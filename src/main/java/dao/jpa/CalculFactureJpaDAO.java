package dao.jpa;

import dao.IDAO;
import models.CalculFacture;
import java.util.Optional;

/**
 * Implémentation JPA du DAO pour les calculs de factures.
 */
public class CalculFactureJpaDAO extends AbstractJpaDAO<CalculFacture, Long> implements IDAO<CalculFacture, Long> {
    
    public CalculFactureJpaDAO() {
        super(CalculFacture.class);
    }

    @Override
    public Optional<CalculFacture> findById(Long id) {
        return super.findById(id);
    }

    @Override
    public CalculFacture save(CalculFacture entity) {
        return super.save(entity);
    }

    @Override
    public CalculFacture update(CalculFacture entity) {
        return super.update(entity);
    }

    @Override
    public void delete(CalculFacture entity) {
        super.delete(entity);
    }
} 