package services;

import dao.IDAO;
import exceptions.DatabaseException;
import models.Client;
import java.util.List;

/**
 * Service de gestion des clients.
 */
public class ClientService extends AbstractSocieteService<Client> {
    
    public ClientService(IDAO<Client> dao) {
        super(dao);
    }

    @Override
    public List<Client> findBySiret(String siret) throws DatabaseException {
        return dao.findBySiret(siret);
    }

    @Override
    public List<Client> findByNom(String nom) throws DatabaseException {
        return dao.findByNom(nom);
    }

    @Override
    public List<Client> findByVille(String ville) throws DatabaseException {
        return dao.findByVille(ville);
    }

    @Override
    public List<Client> findByCodePostal(String codePostal) throws DatabaseException {
        return dao.findByCodePostal(codePostal);
    }
} 