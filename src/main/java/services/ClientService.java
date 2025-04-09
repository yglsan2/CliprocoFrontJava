package services;

import dao.SocieteDatabaseException;
import dao.jpa.ClientJpaDAO;
import models.Client;

/**
 * Service de gestion des clients.
 */
public class ClientService extends AbstractSocieteService<Client, ClientJpaDAO> {
    
    public ClientService() {
        super(new ClientJpaDAO());
    }
} 