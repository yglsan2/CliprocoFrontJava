package dao;

import dao.jpa.ProspectJpaDAO;
import models.Prospect;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ProspectJpaDAOTest {
    private ProspectJpaDAO dao;
    private Prospect testProspect;

    @BeforeEach
    void setUp() throws ValidationException {
        dao = new ProspectJpaDAO();
        testProspect = new Prospect("Test Prospect", "12 Rue de Paris, 75000 Paris, France", "0123456789", "prospect@test.com", "Commentaire test", "2023-01-01");
    }

    @Test
    @DisplayName("Devrait sauvegarder un prospect et le retrouver par son ID")
    void shouldSaveAndFindProspectById() throws DatabaseException, ValidationException, ResourceNotFoundException {
        dao.save(testProspect);
        Optional<Prospect> found = dao.findById(testProspect.getIdentifiant());
        assertTrue(found.isPresent());
        assertEquals("Test Prospect", found.get().getRaisonSociale());
        assertEquals("12 Rue de Paris, 75000 Paris, France", found.get().getAdresse());
        assertEquals("0123456789", found.get().getTelephone());
        assertEquals("prospect@test.com", found.get().getMail());
        assertEquals("2023-01-01", found.get().getDateProspection());
        assertEquals("Commentaire test", found.get().getCommentaires());
    }

    @Test
    @DisplayName("Devrait retourner vide si le prospect n'existe pas")
    void shouldReturnEmptyIfProspectNotFound() throws DatabaseException, ValidationException, ResourceNotFoundException {
        Optional<Prospect> found = dao.findById(9999L);
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Devrait supprimer un prospect")
    void shouldDeleteProspect() throws DatabaseException, ValidationException, ResourceNotFoundException {
        dao.save(testProspect);
        dao.delete(testProspect);
        Optional<Prospect> found = dao.findById(testProspect.getIdentifiant());
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Devrait mettre à jour un prospect")
    void shouldUpdateProspect() throws DatabaseException, ValidationException, ResourceNotFoundException {
        dao.save(testProspect);
        testProspect.setRaisonSociale("Prospect Updated");
        testProspect.setAdresse("15 Rue de Lyon, 69000 Lyon, France");
        testProspect.setTelephone("0987654321");
        testProspect.setMail("updated@prospect.com");
        testProspect.setDateProspection("2023-02-01");
        testProspect.setCommentaires("Commentaire mis à jour");
        dao.update(testProspect);
        Optional<Prospect> found = dao.findById(testProspect.getIdentifiant());
        assertTrue(found.isPresent());
        assertEquals("Prospect Updated", found.get().getRaisonSociale());
        assertEquals("15 Rue de Lyon, 69000 Lyon, France", found.get().getAdresse());
        assertEquals("0987654321", found.get().getTelephone());
        assertEquals("updated@prospect.com", found.get().getMail());
        assertEquals("2023-02-01", found.get().getDateProspection());
        assertEquals("Commentaire mis à jour", found.get().getCommentaires());
    }

    @Test
    public void shouldFindAllProspects() throws DatabaseException {
        // Given
        dao.save(testProspect);

        // When
        var prospects = dao.findAll();

        // Then
        assertNotNull(prospects);
        assertFalse(prospects.isEmpty());
    }
} 