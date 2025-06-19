package dao;

import dao.jpa.AdresseJpaDAO;
import models.Adresse;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AdresseJpaDAOTest {
    private AdresseJpaDAO dao;
    private Adresse testAdresse;

    @BeforeEach
    void setUp() throws ValidationException {
        dao = new AdresseJpaDAO();
        testAdresse = new Adresse("123", "Rue de Paris", "75000", "Paris");
        testAdresse.setPays("France");
    }

    @Test
    @DisplayName("Devrait sauvegarder une adresse et la retrouver par son ID")
    void shouldSaveAndFindAdresseById() throws DatabaseException, ValidationException, ResourceNotFoundException {
        dao.save(testAdresse);
        Optional<Adresse> found = dao.findById(testAdresse.getIdentifiant());
        assertTrue(found.isPresent());
        assertEquals("123", found.get().getNumeroRue());
        assertEquals("Rue de Paris", found.get().getNomRue());
        assertEquals("75000", found.get().getCodePostal());
        assertEquals("Paris", found.get().getVille());
        assertEquals("France", found.get().getPays());
    }

    @Test
    @DisplayName("Devrait retourner vide si l'adresse n'existe pas")
    void shouldReturnEmptyIfAdresseNotFound() throws DatabaseException, ValidationException, ResourceNotFoundException {
        Optional<Adresse> found = dao.findById(9999);
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Devrait supprimer une adresse")
    void shouldDeleteAdresse() throws DatabaseException, ValidationException, ResourceNotFoundException {
        dao.save(testAdresse);
        dao.delete(testAdresse);
        Optional<Adresse> found = dao.findById(testAdresse.getIdentifiant());
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Devrait mettre à jour une adresse")
    void shouldUpdateAdresse() throws DatabaseException, ValidationException, ResourceNotFoundException {
        dao.save(testAdresse);
        testAdresse.setNumeroRue("456");
        testAdresse.setNomRue("Avenue des Champs-Élysées");
        testAdresse.setCodePostal("75008");
        testAdresse.setVille("Paris");
        testAdresse.setPays("France");
        dao.update(testAdresse);
        Optional<Adresse> found = dao.findById(testAdresse.getIdentifiant());
        assertTrue(found.isPresent());
        assertEquals("456", found.get().getNumeroRue());
        assertEquals("Avenue des Champs-Élysées", found.get().getNomRue());
        assertEquals("75008", found.get().getCodePostal());
        assertEquals("Paris", found.get().getVille());
        assertEquals("France", found.get().getPays());
    }
} 