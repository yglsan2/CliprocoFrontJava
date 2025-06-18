package services;

import models.Adresse;
import dao.jpa.AdresseJpaDAO;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class AdresseService {
    private static final Logger logger = LoggerFactory.getLogger(AdresseService.class);
    private final AdresseJpaDAO adresseDao;

    public AdresseService() {
        this.adresseDao = new AdresseJpaDAO();
        logger.info("AdresseService initialisé avec le DAO par défaut");
    }

    public AdresseService(AdresseJpaDAO adresseDao) {
        this.adresseDao = adresseDao;
        logger.info("AdresseService initialisé avec un DAO personnalisé");
    }

    public Adresse findById(Integer id) throws DatabaseException, ValidationException, ResourceNotFoundException {
        logger.debug("Recherche de l'adresse avec l'ID: {}", id);
        Optional<Adresse> adresse = adresseDao.findById(id);
        if (adresse.isPresent()) {
            return adresse.get();
        }
        logger.warn("Aucune adresse trouvée avec l'ID: {}", id);
        throw new ResourceNotFoundException("Adresse non trouvée avec l'ID: " + id);
    }

    public List<Adresse> findAll() throws DatabaseException {
        logger.debug("Récupération de toutes les adresses");
        return adresseDao.findAll();
    }

    public List<Adresse> findByVille(String ville) throws DatabaseException, ValidationException {
        logger.debug("Recherche des adresses dans la ville: {}", ville);
        return adresseDao.findByVille(ville);
    }

    public void save(Adresse adresse) throws DatabaseException, ValidationException {
        logger.debug("Sauvegarde d'une nouvelle adresse");
        adresseDao.save(adresse);
        logger.info("Nouvelle adresse sauvegardée avec succès");
    }

    public void update(Adresse adresse) throws DatabaseException, ValidationException, ResourceNotFoundException {
        logger.debug("Mise à jour de l'adresse avec l'ID: {}", adresse.getIdentifiant());
        if (!adresseDao.existsById(adresse.getIdentifiant())) {
            logger.warn("Tentative de mise à jour d'une adresse inexistante avec l'ID: {}", adresse.getIdentifiant());
            throw new ResourceNotFoundException("Adresse non trouvée avec l'ID: " + adresse.getIdentifiant());
        }
        adresseDao.update(adresse);
        logger.info("Adresse mise à jour avec succès");
    }

    public void delete(Adresse adresse) throws DatabaseException, ValidationException, ResourceNotFoundException {
        logger.debug("Suppression de l'adresse avec l'ID: {}", adresse.getIdentifiant());
        if (!adresseDao.existsById(adresse.getIdentifiant())) {
            logger.warn("Tentative de suppression d'une adresse inexistante avec l'ID: {}", adresse.getIdentifiant());
            throw new ResourceNotFoundException("Adresse non trouvée avec l'ID: " + adresse.getIdentifiant());
        }
        adresseDao.delete(adresse);
        logger.info("Adresse supprimée avec succès");
    }
} 