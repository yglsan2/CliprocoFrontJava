package com.cliproco.dao;

import com.cliproco.models.Prospect;
import java.util.List;

public interface ProspectDao {
    void create(Prospect prospect);
    Prospect findById(Long id);
    List<Prospect> findAll();
    List<Prospect> findByNom(String nom);
    List<Prospect> findByEntreprise(String entreprise);
    void update(Prospect prospect);
    void delete(Long id);
    void close();
} 