package services;

import dao.IDAO;
import exceptions.DatabaseException;
import models.Prospect;
import java.util.List;

/**
 * Service de gestion des prospects.
 */
public class ProspectService extends AbstractSocieteService<Prospect> {
    
    public ProspectService(IDAO<Prospect> dao) {
        super(dao);
    }

    @Override
    public List<Prospect> findBySiret(String siret) throws DatabaseException {
        return dao.findBySiret(siret);
    }

    @Override
    public List<Prospect> findByNom(String nom) throws DatabaseException {
        return dao.findByNom(nom);
    }

    @Override
    public List<Prospect> findByVille(String ville) throws DatabaseException {
        return dao.findByVille(ville);
    }

    @Override
    public List<Prospect> findByCodePostal(String codePostal) throws DatabaseException {
        return dao.findByCodePostal(codePostal);
    }
} 