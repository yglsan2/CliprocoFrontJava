package services;

import dao.SocieteDatabaseException;
import dao.jpa.ClientJpaDAO;
import models.Client;

import java.util.List;

public class ClientService {
    private final ClientJpaDAO clientDao;
    
    public ClientService() {
        this.clientDao = new ClientJpaDAO();
    }
    
    public Client findById(Long id) throws SocieteDatabaseException {
        return clientDao.findById(id);
    }
    
    public List<Client> findAll() throws SocieteDatabaseException {
        return clientDao.findAll();
    }
    
    public List<Client> findByRaisonSociale(String raisonSociale) throws SocieteDatabaseException {
        return clientDao.findByRaisonSociale(raisonSociale);
    }
    
    public void save(Client client) throws SocieteDatabaseException {
        if (!clientDao.existsByRaisonSociale(client.getRaisonSociale())) {
            clientDao.save(client);
        } else {
            throw new SocieteDatabaseException("Un client avec cette raison sociale existe déjà");
        }
    }
    
    public void update(Client client) throws SocieteDatabaseException {
        clientDao.update(client);
    }
    
    public void delete(Client client) throws SocieteDatabaseException {
        clientDao.delete(client);
    }
} 