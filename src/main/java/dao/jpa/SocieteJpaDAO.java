package dao.jpa;

import models.Societe;
import dao.SocieteDatabaseException;
import dao.jpa.GenericJpaDAO;
import java.util.List;

public class SocieteJpaDAO extends GenericJpaDAO<Societe> {
    
    public SocieteJpaDAO() {
        super();
    }
    
    @Override
    public Societe findById(Long id) throws SocieteDatabaseException {
        return super.findById(id);
    }
    
    @Override
    public List<Societe> findAll() throws SocieteDatabaseException {
        return super.findAll();
    }
    
    @Override
    public void save(Societe societe) throws SocieteDatabaseException {
        super.save(societe);
    }
    
    @Override
    public void update(Societe societe) throws SocieteDatabaseException {
        super.update(societe);
    }
    
    @Override
    public void delete(Societe societe) throws SocieteDatabaseException {
        super.delete(societe);
    }
} 