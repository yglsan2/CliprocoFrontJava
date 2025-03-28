package com.cliproco.bean;

import com.cliproco.dao.ContratDAO;
import com.cliproco.model.Contrat;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named("contratBean")
@SessionScoped
public class ContratBean implements Serializable {
    private final ContratDAO contratDAO;
    private Contrat contrat;
    private Integer clientId;

    public ContratBean() {
        this.contratDAO = new ContratDAO();
        this.contrat = new Contrat();
    }

    public String initCreate() {
        this.contrat = new Contrat();
        return "contrat_form?faces-redirect=true";
    }

    public String initEdit(Integer id) {
        this.contrat = contratDAO.find(id);
        return "contrat_form?faces-redirect=true";
    }

    public String save() {
        contratDAO.save(contrat);
        return "client_view?faces-redirect=true&id=" + clientId;
    }

    public String delete(Integer id) {
        contratDAO.delete(id);
        return "client_view?faces-redirect=true&id=" + clientId;
    }

    public List<Contrat> getContratsByClient(Integer clientId) {
        return contratDAO.findByIdClient(clientId);
    }

    // Getters et Setters
    public Contrat getContrat() {
        return contrat;
    }

    public void setContrat(Contrat contrat) {
        this.contrat = contrat;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }
} 