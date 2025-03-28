package com.cliproco.dao;

import com.cliproco.model.Prospect;
import java.util.List;

public interface ProspectDAO {
    Prospect create(Prospect prospect);
    Prospect findById(Long id);
    List<Prospect> findAll();
    Prospect update(Prospect prospect);
    void delete(Long id);
    List<Prospect> findByNom(String nom);
} 