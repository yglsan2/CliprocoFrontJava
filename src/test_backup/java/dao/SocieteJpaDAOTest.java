package dao;

import dao.jpa.SocieteJpaDAO;
import models.Societe;
import models.Adresse;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SocieteJpaDAOTest {
    private SocieteJpaDAO dao;
    private Societe testSociete;
    private Adresse testAdresse;

    // Classe concrète pour les tests
    private static class TestSociete extends Societe {
        public TestSociete(String raisonSociale, Adresse adresse, String telephone, String mail, String commentaire) {
            super(raisonSociale, adresse, telephone, mail, commentaire);
        }
    }

    @BeforeEach
    void setUp() {
        dao = new SocieteJpaDAO();
        testAdresse = new Adresse("123", "Rue Test", "75000", "Paris");
        testSociete = new TestSociete("Test Societe", testAdresse, "0123456789", "societe@test.com", "commentaire");
    }

    @Test
    @DisplayName("Devrait sauvegarder une société et la retrouver par son ID")
    void shouldSaveAndFindSocieteById() throws DatabaseException, ValidationException, ResourceNotFoundException {
        dao.save(testSociete);
        Optional<Societe> found = dao.findById(testSociete.getIdentifiant());
        assertTrue(found.isPresent());
        assertEquals("Test Societe", found.get().getRaisonSociale());
    }

    @Test
    @DisplayName("Devrait retourner vide si la société n'existe pas")
    void shouldReturnEmptyIfSocieteNotFound() throws DatabaseException, ValidationException, ResourceNotFoundException {
        Optional<Societe> found = dao.findById(9999L);
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Devrait supprimer une société")
    void shouldDeleteSociete() throws DatabaseException, ValidationException, ResourceNotFoundException {
        dao.save(testSociete);
        dao.delete(testSociete);
        Optional<Societe> found = dao.findById(testSociete.getIdentifiant());
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Devrait mettre à jour une société")
    void shouldUpdateSociete() throws DatabaseException, ValidationException, ResourceNotFoundException {
        dao.save(testSociete);
        testSociete.setRaisonSociale("Societe Updated");
        dao.update(testSociete);
        Optional<Societe> found = dao.findById(testSociete.getIdentifiant());
        assertTrue(found.isPresent());
        assertEquals("Societe Updated", found.get().getRaisonSociale());
    }
} 