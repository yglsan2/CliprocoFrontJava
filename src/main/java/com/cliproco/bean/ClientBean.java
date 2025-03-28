package com.cliproco.bean;

import com.cliproco.dao.ClientDAO;
import com.cliproco.dao.ContratDAO;
import com.cliproco.model.Client;
import com.cliproco.model.Contrat;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("clientBean")
@SessionScoped
public class ClientBean implements Serializable {
    private final ClientDAO clientDAO;
    private final ContratDAO contratDAO;
    private Client client;
    private List<Client> clients;
    private List<Contrat> contrats;

    public ClientBean() {
        this.clientDAO = new ClientDAO();
        this.contratDAO = new ContratDAO();
        this.client = new Client();
    }

    public String initCreate() {
        this.client = new Client();
        return "client_form?faces-redirect=true";
    }

    public String initEdit(Integer id) {
        this.client = clientDAO.find(id);
        return "client_form?faces-redirect=true";
    }

    public String save() {
        clientDAO.save(client);
        return "client_list?faces-redirect=true";
    }

    public String delete(Integer id) {
        clientDAO.delete(id);
        return "client_list?faces-redirect=true";
    }

    public String viewClient(Integer id) {
        this.client = clientDAO.find(id);
        this.contrats = contratDAO.findByIdClient(id);
        return "client_view?faces-redirect=true";
    }

    public List<Client> getAllClients() {
        if (clients == null) {
            clients = clientDAO.findAll();
        }
        return clients;
    }

    // Getters et Setters
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Contrat> getContrats() {
        return contrats;
    }

    public void setContrats(List<Contrat> contrats) {
        this.contrats = contrats;
    }
} 