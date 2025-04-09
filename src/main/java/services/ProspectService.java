package services;

import dao.SocieteDatabaseException;
import dao.jpa.ProspectJpaDAO;
import models.Prospect;

import java.util.List;

public class ProspectService {
    private final ProspectJpaDAO prospectDao;
    
    public ProspectService() {
        this.prospectDao = new ProspectJpaDAO();
    }
    
    public Prospect findById(Long id) throws SocieteDatabaseException {
        return prospectDao.findById(id);
    }
    
    public List<Prospect> findAll() throws SocieteDatabaseException {
        return prospectDao.findAll();
    }
    
    public List<Prospect> findByRaisonSociale(String raisonSociale) throws SocieteDatabaseException {
        return prospectDao.findByRaisonSociale(raisonSociale);
    }
    
    public void save(Prospect prospect) throws SocieteDatabaseException {
        if (!prospectDao.existsByRaisonSociale(prospect.getRaisonSociale())) {
            prospectDao.save(prospect);
        } else {
            throw new SocieteDatabaseException("Un prospect avec cette raison sociale existe déjà");
        }
    }
    
    public void update(Prospect prospect) throws SocieteDatabaseException {
        prospectDao.update(prospect);
    }
    
    public void delete(Prospect prospect) throws SocieteDatabaseException {
        prospectDao.delete(prospect);
    }
} 