package dao.jpa;

import models.Contrat;
import utilities.LogManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class ContratJpaDAO extends GenericJpaDAO<Contrat, Long> {
    public ContratJpaDAO() {
        super();
    }
} 