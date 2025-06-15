package services;

import exceptions.DatabaseException;
import models.Client;
import dao.jpa.ClientJpaDAO;
import java.util.List;

public class ClientService {
    private final ClientJpaDAO clientDAO;
    
    public ClientService() {
        this.clientDAO = new ClientJpaDAO();
    }
    
    public Client findById(Long id) throws DatabaseException {
        return clientDAO.findById(id);
    }
    
    public List<Client> findAll() throws DatabaseException {
        return clientDAO.findAll();
    }
    
    public List<Client> findByRaisonSociale(String raisonSociale) throws DatabaseException {
        return clientDAO.findByRaisonSociale(raisonSociale);
    }
    
    public void save(Client client) throws DatabaseException {
        if (clientDAO.existsByRaisonSociale(client.getRaisonSociale())) {
            throw new DatabaseException("Un client avec cette raison sociale existe déjà");
        }
        clientDAO.save(client);
    }
    
    public void update(Client client) throws DatabaseException {
        clientDAO.update(client);
    }
    
    public void delete(Client client) throws DatabaseException {
        clientDAO.delete(client);
    }
} 