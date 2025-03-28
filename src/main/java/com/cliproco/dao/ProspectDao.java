package com.cliproco.dao;

import com.cliproco.model.Prospect;
import java.util.List;

public interface ProspectDao extends HibernateDao<Prospect> {
    List<Prospect> findByStatut(Prospect.Statut statut);
    Prospect findByEmail(String email);
    List<Prospect> findBySecteurActivite(String secteurActivite);
} 