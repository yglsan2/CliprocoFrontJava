package com.cliproco.dao;

import com.cliproco.models.Client;
import java.util.List;

public interface ClientDao {
    void create(Client client);
    Client findById(Long id);
    List<Client> findAll();
    List<Client> findByNom(String nom);
    List<Client> findByEntreprise(String entreprise);
    void update(Client client);
    void delete(Long id);
    void close();
} 