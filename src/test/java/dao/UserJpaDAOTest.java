package dao;

import dao.jpa.UserJpaDAO;
import models.User;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;

public class UserJpaDAOTest {
    private UserJpaDAO dao;
    private User testUser;

    @BeforeEach
    public void setUp() throws ValidationException {
        dao = new UserJpaDAO();
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setEmail("test@user.com");
        testUser.setRole("USER");
    }

    @Test
    public void shouldCreateUser() throws DatabaseException, ValidationException {
        // When
        User created = dao.save(testUser);

        // Then
        assertNotNull(created);
        assertNotNull(created.getIdentifiant());
        assertEquals("testuser", created.getUsername());
    }

    @Test
    @DisplayName("Devrait sauvegarder un utilisateur et le retrouver par son ID")
    public void shouldSaveAndFindUserById() throws DatabaseException, ValidationException {
        // Given
        User created = dao.save(testUser);
        Integer id = created.getIdentifiant();

        // When
        Optional<User> found = dao.findById(id);

        // Then
        assertTrue(found.isPresent());
        assertEquals(id, found.get().getIdentifiant());
        assertEquals("testuser", found.get().getUsername());
    }

    @Test
    @DisplayName("Devrait retourner vide si l'utilisateur n'existe pas")
    public void shouldReturnEmptyIfUserNotFound() throws DatabaseException {
        // When
        Optional<User> found = dao.findById(9999L);

        // Then
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Devrait supprimer un utilisateur")
    public void shouldDeleteUser() throws DatabaseException, ValidationException, ResourceNotFoundException {
        // Given
        User created = dao.save(testUser);
        Integer id = created.getIdentifiant();

        // When
        dao.delete(created);

        // Then
        Optional<User> found = dao.findById(id);
        assertTrue(found.isEmpty());
    }

    @Test
    @DisplayName("Devrait mettre à jour un utilisateur")
    public void shouldUpdateUser() throws DatabaseException, ValidationException, ResourceNotFoundException {
        // Given
        User created = dao.save(testUser);
        Integer id = created.getIdentifiant();

        // When
        created.setUsername("updateduser");
        created.setEmail("updated@user.com");
        created.setRole("ADMIN");
        dao.update(created);

        // Then
        Optional<User> found = dao.findById(id);
        assertTrue(found.isPresent());
        assertEquals("updateduser", found.get().getUsername());
        assertEquals("updated@user.com", found.get().getMail());
        assertEquals("ADMIN", found.get().getRole());
    }

    @Test
    public void shouldFindAllUsers() throws DatabaseException, ValidationException {
        // Given
        dao.save(testUser);

        // When
        var users = dao.findAll();

        // Then
        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @Test
    public void shouldFindUserByUsername() throws DatabaseException, ValidationException {
        // Given
        dao.save(testUser);

        // When
        var found = dao.findByUsername("testuser");

        // Then
        assertTrue(found.isPresent());
        assertEquals("testuser", found.get().getUsername());
    }
} 