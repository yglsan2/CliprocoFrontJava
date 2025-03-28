package com.cliproco.dao;

import com.cliproco.model.Client;
import java.util.List;

public interface ClientDao extends HibernateDao<Client> {
    List<Client> findBySecteurActivite(String secteurActivite);
    Client findByEmail(String email);
    List<Client> findByChiffreAffairesMin(double chiffreAffaires);
} 