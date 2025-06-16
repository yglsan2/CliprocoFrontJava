package dao.jpa;

import models.Adresse;
import utilities.LogManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class AdresseJpaDAO extends GenericJpaDAO<Adresse, Long> {
    public AdresseJpaDAO() {
        super();
    }

            LogManager.logException("Erreur lors de la recherche de l'adresse par ID", e);
            return Optional.empty();
        }
    }

            LogManager.logException("Erreur lors de la récupération de toutes les adresses", e);
            return List.of();
        }
    }

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la sauvegarde de l'adresse", e);
            throw new RuntimeException("Erreur lors de la sauvegarde de l'adresse", e);
        }
    }

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la mise à jour de l'adresse", e);
            throw new RuntimeException("Erreur lors de la mise à jour de l'adresse", e);
        }
    }

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LogManager.logException("Erreur lors de la suppression de l'adresse", e);
            throw new RuntimeException("Erreur lors de la suppression de l'adresse", e);
        }
    }

    public void close() {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }
} 