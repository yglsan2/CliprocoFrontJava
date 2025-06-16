package services;

import exceptions.DatabaseException;
import dao.jpa.ProspectJpaDAO;
import models.Prospect;

import java.util.List;

public class ProspectService {
    private final ProspectJpaDAO prospectDAO;
    
    public ProspectService() {
        this.prospectDAO = new ProspectJpaDAO();
    }
    
    public Prospect findById(Long id) throws DatabaseException {
        return prospectDAO.findById(id);
    }
    
    public List<Prospect> findAll() throws DatabaseException {
        return prospectDAO.findAll();
    }
    
    public List<Prospect> findByRaisonSociale(String raisonSociale) throws DatabaseException {
        return prospectDAO.findByRaisonSociale(raisonSociale);
    }
    
    public void save(Prospect prospect) throws DatabaseException {
        if (prospectDAO.existsByRaisonSociale(prospect.getRaisonSociale())) {
            throw new DatabaseException("Un prospect avec cette raison sociale existe déjà");
        }
        prospectDAO.save(prospect);
    }
    
    public void update(Prospect prospect) throws DatabaseException {
        prospectDAO.update(prospect);
    }
    
    public void delete(Prospect prospect) throws DatabaseException {
        prospectDAO.delete(prospect);
    }
} 