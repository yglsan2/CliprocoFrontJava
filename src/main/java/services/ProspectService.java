package services;

import dao.SocieteDatabaseException;
import dao.jpa.ProspectJpaDAO;
import models.Prospect;

/**
 * Service de gestion des prospects.
 */
public class ProspectService extends AbstractSocieteService<Prospect, ProspectJpaDAO> {
    
    public ProspectService() {
        super(new ProspectJpaDAO());
    }
} 