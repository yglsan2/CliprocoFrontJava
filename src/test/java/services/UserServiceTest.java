package services;

import dao.IDAO;
import models.User;
import services.UserService;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour la classe UserService.
 * 
 * <p>Cette classe de test couvre tous les aspects de la classe UserService :
 * - Méthodes CRUD
 * - Gestion des exceptions
 * - Validation des données
 * - Cas limites et d'erreur
 * - Interaction avec le DAO
 * </p>
 * 
 * @author CliprocoJEE
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires pour la classe UserService")
class UserServiceTest {

    @Mock
    private IDAO<User, Integer> userDAO;

    private UserService userService;

    private User testUser;
    private static final Integer VALID_ID = 1;
    private static final String VALID_USERNAME = "testuser";
    private static final String VALID_PASSWORD = "password123";
    private static final String VALID_EMAIL = "test@example.com";
    private static final String VALID_ROLE = "USER";
    private static final String VALID_TOKEN = "token123";
    private static final LocalDate VALID_EXPIRE = LocalDate.now().plusDays(30);

    @BeforeEach
    void setUp() {
        userService = new UserService(userDAO);
        
        testUser = new User(VALID_ID, VALID_USERNAME, VALID_PASSWORD, VALID_TOKEN, VALID_EXPIRE, VALID_ROLE);
    }

    @Nested
    @DisplayName("Tests de la méthode findById")
    class FindByIdTests {

        @Test
        @DisplayName("findById avec ID valide doit retourner l'utilisateur")
        void testFindByIdWithValidId() throws Exception {
            // Arrange
            when(userDAO.findById(VALID_ID)).thenReturn(Optional.of(testUser));

            // Act
            Optional<User> result = userService.findById(VALID_ID);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(testUser, result.get());
            verify(userDAO).findById(VALID_ID);
        }

        @Test
        @DisplayName("findById avec ID inexistant doit retourner Optional vide")
        void testFindByIdWithNonExistentId() throws Exception {
            // Arrange
            when(userDAO.findById(999)).thenReturn(Optional.empty());

            // Act
            Optional<User> result = userService.findById(999);

            // Assert
            assertFalse(result.isPresent());
            verify(userDAO).findById(999);
        }

        @Test
        @DisplayName("findById avec ID null doit lever ValidationException")
        void testFindByIdWithNullId() throws Exception {
            // Act & Assert
            assertThrows(ValidationException.class, () -> userService.findById(null));
            verify(userDAO, never()).findById(any());
        }

        @Test
        @DisplayName("findById avec exception DAO doit lever DatabaseException")
        void testFindByIdWithDaoException() throws Exception {
            // Arrange
            when(userDAO.findById(VALID_ID)).thenThrow(new DatabaseException("Erreur DAO"));

            // Act & Assert
            assertThrows(DatabaseException.class, () -> userService.findById(VALID_ID));
            verify(userDAO).findById(VALID_ID);
        }
    }

    @Nested
    @DisplayName("Tests de la méthode findAll")
    class FindAllTests {

        @Test
        @DisplayName("findAll doit retourner la liste de tous les utilisateurs")
        void testFindAll() throws Exception {
            // Arrange
            List<User> users = Arrays.asList(testUser, new User(), new User());
            when(userDAO.findAll()).thenReturn(users);

            // Act
            List<User> result = userService.findAll();

            // Assert
            assertEquals(users, result);
            assertEquals(3, result.size());
            verify(userDAO).findAll();
        }

        @Test
        @DisplayName("findAll avec liste vide doit retourner liste vide")
        void testFindAllWithEmptyList() throws Exception {
            // Arrange
            when(userDAO.findAll()).thenReturn(Arrays.asList());

            // Act
            List<User> result = userService.findAll();

            // Assert
            assertTrue(result.isEmpty());
            verify(userDAO).findAll();
        }

        @Test
        @DisplayName("findAll avec exception DAO doit lever DatabaseException")
        void testFindAllWithDaoException() throws Exception {
            // Arrange
            when(userDAO.findAll()).thenThrow(new DatabaseException("Erreur DAO"));

            // Act & Assert
            assertThrows(DatabaseException.class, () -> userService.findAll());
            verify(userDAO).findAll();
        }
    }

    @Nested
    @DisplayName("Tests de la méthode save")
    class SaveTests {

        @Test
        @DisplayName("save avec utilisateur valide doit sauvegarder et retourner l'utilisateur")
        void testSaveWithValidUser() throws Exception {
            // Arrange
            User userToSave = new User(VALID_USERNAME, VALID_PASSWORD, VALID_EMAIL);
            when(userDAO.save(userToSave)).thenReturn(userToSave);

            // Act
            User result = userService.save(userToSave);

            // Assert
            assertEquals(userToSave, result);
            verify(userDAO).save(userToSave);
        }

        @Test
        @DisplayName("save avec utilisateur null doit lever ValidationException")
        void testSaveWithNullUser() throws Exception {
            // Act & Assert
            assertThrows(ValidationException.class, () -> userService.save(null));
            verify(userDAO, never()).save(any());
        }

        @Test
        @DisplayName("save avec exception DAO doit lever DatabaseException")
        void testSaveWithDaoException() throws Exception {
            // Arrange
            when(userDAO.save(testUser)).thenThrow(new DatabaseException("Erreur DAO"));

            // Act & Assert
            assertThrows(DatabaseException.class, () -> userService.save(testUser));
            verify(userDAO).save(testUser);
        }

        @Test
        @DisplayName("save avec ValidationException du DAO doit la propager")
        void testSaveWithDaoValidationException() throws Exception {
            // Arrange
            when(userDAO.save(testUser)).thenThrow(new ValidationException("Erreur validation"));

            // Act & Assert
            assertThrows(ValidationException.class, () -> userService.save(testUser));
            verify(userDAO).save(testUser);
        }
    }

    @Nested
    @DisplayName("Tests de la méthode update")
    class UpdateTests {

        @Test
        @DisplayName("update avec utilisateur valide doit mettre à jour et retourner l'utilisateur")
        void testUpdateWithValidUser() throws Exception {
            // Arrange
            when(userDAO.findById(VALID_ID)).thenReturn(Optional.of(testUser));
            when(userDAO.update(testUser)).thenReturn(testUser);

            // Act
            User result = userService.update(testUser);

            // Assert
            assertEquals(testUser, result);
            verify(userDAO).findById(VALID_ID);
            verify(userDAO).update(testUser);
        }

        @Test
        @DisplayName("update avec utilisateur null doit lever ValidationException")
        void testUpdateWithNullUser() throws Exception {
            // Act & Assert
            assertThrows(ValidationException.class, () -> userService.update(null));
            verify(userDAO, never()).findById(any());
            verify(userDAO, never()).update(any());
        }

        @Test
        @DisplayName("update avec utilisateur sans ID doit lever ValidationException")
        void testUpdateWithUserWithoutId() throws Exception {
            // Arrange
            User userWithoutId = new User();
            userWithoutId.setUsername(VALID_USERNAME);
            userWithoutId.setPassword(VALID_PASSWORD);
            userWithoutId.setEmail(VALID_EMAIL);

            // Act & Assert
            assertThrows(ValidationException.class, () -> userService.update(userWithoutId));
            verify(userDAO, never()).findById(any());
            verify(userDAO, never()).update(any());
        }

        @Test
        @DisplayName("update avec utilisateur inexistant doit lever ResourceNotFoundException")
        void testUpdateWithNonExistentUser() throws Exception {
            // Arrange
            when(userDAO.findById(VALID_ID)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> {
                try {
                    userService.update(testUser);
                } catch (ResourceNotFoundException e) {
                    throw e;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            verify(userDAO).findById(VALID_ID);
            verify(userDAO, never()).update(any());
        }

        @Test
        @DisplayName("update avec exception DAO doit lever DatabaseException")
        void testUpdateWithDaoException() throws Exception {
            // Arrange
            when(userDAO.findById(VALID_ID)).thenThrow(new DatabaseException("Erreur DAO"));

            // Act & Assert
            assertThrows(DatabaseException.class, () -> userService.update(testUser));
            verify(userDAO).findById(VALID_ID);
            verify(userDAO, never()).update(any());
        }
    }

    @Nested
    @DisplayName("Tests de la méthode delete")
    class DeleteTests {

        @Test
        @DisplayName("delete avec utilisateur valide doit supprimer l'utilisateur")
        void testDeleteWithValidUser() throws Exception {
            // Arrange
            when(userDAO.findById(VALID_ID)).thenReturn(Optional.of(testUser));
            doNothing().when(userDAO).delete(testUser);

            // Act
            userService.delete(testUser);

            // Assert
            verify(userDAO).findById(VALID_ID);
            verify(userDAO).delete(testUser);
        }

        @Test
        @DisplayName("delete avec utilisateur null doit lever ValidationException")
        void testDeleteWithNullUser() throws Exception {
            // Act & Assert
            assertThrows(ValidationException.class, () -> userService.delete(null));
            verify(userDAO, never()).findById(any());
            verify(userDAO, never()).delete(any());
        }

        @Test
        @DisplayName("delete avec utilisateur sans ID doit lever ValidationException")
        void testDeleteWithUserWithoutId() throws Exception {
            // Arrange
            User userWithoutId = new User();
            userWithoutId.setUsername(VALID_USERNAME);
            userWithoutId.setPassword(VALID_PASSWORD);
            userWithoutId.setEmail(VALID_EMAIL);

            // Act & Assert
            assertThrows(ValidationException.class, () -> userService.delete(userWithoutId));
            verify(userDAO, never()).findById(any());
            verify(userDAO, never()).delete(any());
        }

        @Test
        @DisplayName("delete avec utilisateur inexistant doit lever ResourceNotFoundException")
        void testDeleteWithNonExistentUser() throws Exception {
            // Arrange
            when(userDAO.findById(VALID_ID)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> {
                try {
                    userService.delete(testUser);
                } catch (ResourceNotFoundException e) {
                    throw e;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            verify(userDAO).findById(VALID_ID);
            verify(userDAO, never()).delete(any());
        }

        @Test
        @DisplayName("delete avec exception DAO doit lever DatabaseException")
        void testDeleteWithDaoException() throws Exception {
            // Arrange
            when(userDAO.findById(VALID_ID)).thenThrow(new DatabaseException("Erreur DAO"));

            // Act & Assert
            assertThrows(DatabaseException.class, () -> userService.delete(testUser));
            verify(userDAO).findById(VALID_ID);
            verify(userDAO, never()).delete(any());
        }
    }

    @Nested
    @DisplayName("Tests de la méthode findByUsername")
    class FindByUsernameTests {

        @Test
        @DisplayName("findByUsername avec username valide doit retourner l'utilisateur")
        void testFindByUsernameWithValidUsername() throws Exception {
            // Arrange
            List<User> users = Arrays.asList(testUser);
            when(userDAO.findAll()).thenReturn(users);

            // Act
            Optional<User> result = userService.findByUsername(VALID_USERNAME);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(testUser, result.get());
            verify(userDAO).findAll();
        }

        @Test
        @DisplayName("findByUsername avec username inexistant doit retourner Optional vide")
        void testFindByUsernameWithNonExistentUsername() throws Exception {
            // Arrange
            List<User> users = Arrays.asList(testUser);
            when(userDAO.findAll()).thenReturn(users);

            // Act
            Optional<User> result = userService.findByUsername("nonexistent");

            // Assert
            assertFalse(result.isPresent());
            verify(userDAO).findAll();
        }

        @Test
        @DisplayName("findByUsername avec username null doit lever ValidationException")
        void testFindByUsernameWithNullUsername() throws Exception {
            // Act & Assert
            assertThrows(ValidationException.class, () -> userService.findByUsername(null));
            verify(userDAO, never()).findAll();
        }

        @Test
        @DisplayName("findByUsername avec username vide doit lever ValidationException")
        void testFindByUsernameWithEmptyUsername() throws Exception {
            // Act & Assert
            assertThrows(ValidationException.class, () -> userService.findByUsername(""));
            verify(userDAO, never()).findAll();
        }

        @Test
        @DisplayName("findByUsername avec username blanc doit lever ValidationException")
        void testFindByUsernameWithBlankUsername() throws Exception {
            // Act & Assert
            assertThrows(ValidationException.class, () -> userService.findByUsername("   "));
            verify(userDAO, never()).findAll();
        }

        @Test
        @DisplayName("findByUsername avec exception DAO doit lever DatabaseException")
        void testFindByUsernameWithDaoException() throws Exception {
            // Arrange
            when(userDAO.findAll()).thenThrow(new DatabaseException("Erreur DAO"));

            // Act & Assert
            assertThrows(DatabaseException.class, () -> userService.findByUsername(VALID_USERNAME));
            verify(userDAO).findAll();
        }
    }

    @Nested
    @DisplayName("Tests de scénarios métier")
    class BusinessScenarioTests {

        @Test
        @DisplayName("Création et récupération d'un utilisateur administrateur")
        void testCreateAndRetrieveAdminUser() throws Exception {
            // Arrange
            User adminUser = new User("admin", "adminpass", "admin@example.com");
            adminUser.setRole("ADMIN");
            when(userDAO.save(adminUser)).thenReturn(adminUser);
            when(userDAO.findAll()).thenReturn(Arrays.asList(adminUser));

            // Act
            User savedUser = userService.save(adminUser);
            Optional<User> retrievedUser = userService.findByUsername("admin");

            // Assert
            assertEquals("ADMIN", savedUser.getRole());
            assertTrue(retrievedUser.isPresent());
            assertEquals("ADMIN", retrievedUser.get().getRole());
        }

        @Test
        @DisplayName("Mise à jour du rôle d'un utilisateur")
        void testUpdateUserRole() throws Exception {
            // Arrange
            when(userDAO.findById(VALID_ID)).thenReturn(Optional.of(testUser));
            when(userDAO.update(testUser)).thenReturn(testUser);
            testUser.setRole("ADMIN");

            // Act
            User updatedUser = userService.update(testUser);

            // Assert
            assertEquals("ADMIN", updatedUser.getRole());
            verify(userDAO).update(testUser);
        }

        @Test
        @DisplayName("Suppression d'un utilisateur et vérification de sa non-existence")
        void testDeleteUserAndVerifyNonExistence() throws Exception {
            // Arrange
            when(userDAO.findById(VALID_ID)).thenReturn(Optional.of(testUser));
            doNothing().when(userDAO).delete(testUser);
            when(userDAO.findAll()).thenReturn(Arrays.asList());

            // Act
            userService.delete(testUser);
            List<User> remainingUsers = userService.findAll();

            // Assert
            assertTrue(remainingUsers.isEmpty());
            verify(userDAO).delete(testUser);
        }

        @Test
        @DisplayName("Recherche d'utilisateur par nom d'utilisateur avec plusieurs utilisateurs")
        void testFindByUsernameWithMultipleUsers() throws Exception {
            // Arrange
            User user1 = new User("user1", "pass1", "user1@example.com");
            User user2 = new User("user2", "pass2", "user2@example.com");
            User user3 = new User("user3", "pass3", "user3@example.com");
            List<User> users = Arrays.asList(user1, user2, user3);
            when(userDAO.findAll()).thenReturn(users);

            // Act
            Optional<User> result = userService.findByUsername("user2");

            // Assert
            assertTrue(result.isPresent());
            assertEquals("user2", result.get().getUsername());
        }
    }

    @Nested
    @DisplayName("Tests de gestion d'erreurs")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Gestion d'erreur de base de données lors de la sauvegarde")
        void testDatabaseErrorHandlingOnSave() throws Exception {
            // Arrange
            when(userDAO.save(testUser)).thenThrow(new DatabaseException("Erreur de base de données"));

            // Act & Assert
            assertThrows(DatabaseException.class, () -> userService.save(testUser));
        }

        @Test
        @DisplayName("Gestion d'erreur de validation lors de la mise à jour")
        void testValidationErrorHandlingOnUpdate() throws Exception {
            // Arrange
            when(userDAO.findById(VALID_ID)).thenReturn(Optional.of(testUser));
            when(userDAO.update(testUser)).thenThrow(new ValidationException("Erreur de validation"));

            // Act & Assert
            assertThrows(ValidationException.class, () -> userService.update(testUser));
        }

        @Test
        @DisplayName("Gestion d'erreur de ressource non trouvée lors de la suppression")
        void testResourceNotFoundErrorHandlingOnDelete() throws Exception {
            // Arrange
            when(userDAO.findById(VALID_ID)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> {
                try {
                    userService.delete(testUser);
                } catch (ResourceNotFoundException e) {
                    throw e;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
} 