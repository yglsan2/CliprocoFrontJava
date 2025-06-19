package services;

import dao.IDAO;
import models.User;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.InjectMocks;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private IDAO<User, Integer> userDAO;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Tests de findById")
    class FindByIdTests {
        
        @Test
        @DisplayName("Devrait retourner un utilisateur quand l'ID existe")
        void shouldReturnUserWhenIdExists() throws DatabaseException, ValidationException {
            // Arrange
            Integer userId = 1L;
            User expectedUser = new User();
            expectedUser.setIdentifiant(userId);
            when(userDAO.findById(userId)).thenReturn(Optional.of(expectedUser));

            // Act
            Optional<User> result = userService.findById(userId);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(expectedUser, result.get());
            verify(userDAO).findById(userId);
        }

        @Test
        @DisplayName("Devrait retourner Optional.empty quand l'ID n'existe pas")
        void shouldReturnEmptyWhenIdDoesNotExist() throws DatabaseException, ValidationException {
            // Arrange
            Integer userId = 999L;
            when(userDAO.findById(userId)).thenReturn(Optional.empty());

            // Act
            Optional<User> result = userService.findById(userId);

            // Assert
            assertFalse(result.isPresent());
            verify(userDAO).findById(userId);
        }

        @Test
        @DisplayName("Devrait lever une ValidationException quand l'ID est null")
        void shouldThrowValidationExceptionWhenIdIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> userService.findById(null));
            verify(userDAO, never()).findById(any());
        }
    }

    @Nested
    @DisplayName("Tests de findAll")
    class FindAllTests {
        
        @Test
        @DisplayName("Devrait retourner la liste complète des utilisateurs")
        void shouldReturnAllUsers() throws DatabaseException {
            // Arrange
            List<User> expectedUsers = Arrays.asList(
                new User(), new User(), new User()
            );
            when(userDAO.findAll()).thenReturn(expectedUsers);

            // Act
            List<User> result = userService.findAll();

            // Assert
            assertEquals(expectedUsers.size(), result.size());
            assertEquals(expectedUsers, result);
            verify(userDAO).findAll();
        }

        @Test
        @DisplayName("Devrait lever une DatabaseException en cas d'erreur")
        void shouldThrowDatabaseExceptionOnError() {
            // Arrange
            when(userDAO.findAll()).thenThrow(new RuntimeException("Erreur de base de données"));

            // Act & Assert
            assertThrows(DatabaseException.class, () -> userService.findAll());
            verify(userDAO).findAll();
        }
    }

    @Nested
    @DisplayName("Tests de save")
    class SaveTests {
        
        @Test
        @DisplayName("Devrait sauvegarder un nouvel utilisateur")
        void shouldSaveNewUser() throws DatabaseException, ValidationException {
            // Arrange
            User user = new User();
            when(userDAO.save(user)).thenReturn(user);

            // Act
            User result = userService.save(user);

            // Assert
            assertNotNull(result);
            verify(userDAO).save(user);
        }

        @Test
        @DisplayName("Devrait lever une ValidationException quand l'utilisateur est null")
        void shouldThrowValidationExceptionWhenUserIsNull() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> userService.save(null));
            verify(userDAO, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Tests de update")
    class UpdateTests {
        
        @Test
        @DisplayName("Devrait mettre à jour un utilisateur existant")
        void shouldUpdateExistingUser() throws DatabaseException, ValidationException, ResourceNotFoundException {
            // Arrange
            User user = new User();
            user.setIdentifiant(1L);
            when(userDAO.findById(user.getIdentifiant())).thenReturn(Optional.of(user));
            when(userDAO.update(user)).thenReturn(user);

            // Act
            User result = userService.update(user);

            // Assert
            assertNotNull(result);
            verify(userDAO).findById(user.getIdentifiant());
            verify(userDAO).update(user);
        }

        @Test
        @DisplayName("Devrait lever une ResourceNotFoundException quand l'utilisateur n'existe pas")
        void shouldThrowResourceNotFoundExceptionWhenUserDoesNotExist() {
            // Arrange
            User user = new User();
            user.setIdentifiant(999L);
            when(userDAO.findById(user.getIdentifiant())).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> userService.update(user));
            verify(userDAO).findById(user.getIdentifiant());
            verify(userDAO, never()).update(any());
        }
    }

    @Nested
    @DisplayName("Tests de delete")
    class DeleteTests {
        
        @Test
        @DisplayName("Devrait supprimer un utilisateur existant")
        void shouldDeleteExistingUser() throws DatabaseException, ValidationException, ResourceNotFoundException {
            // Arrange
            User user = new User();
            user.setIdentifiant(1L);
            when(userDAO.findById(user.getIdentifiant())).thenReturn(Optional.of(user));
            doNothing().when(userDAO).delete(user);

            // Act & Assert
            assertDoesNotThrow(() -> userService.delete(user));
            verify(userDAO).findById(user.getIdentifiant());
            verify(userDAO).delete(user);
        }

        @Test
        @DisplayName("Devrait lever une ResourceNotFoundException lors de la suppression d'un utilisateur inexistant")
        void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistentUser() {
            // Arrange
            User user = new User();
            user.setIdentifiant(999L);
            when(userDAO.findById(user.getIdentifiant())).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ResourceNotFoundException.class, () -> userService.delete(user));
            verify(userDAO).findById(user.getIdentifiant());
            verify(userDAO, never()).delete(any());
        }
    }

    @Nested
    @DisplayName("Tests de findByUsername")
    class FindByUsernameTests {
        
        @Test
        @DisplayName("Devrait trouver un utilisateur par son nom d'utilisateur")
        void shouldFindUserByUsername() throws DatabaseException, ValidationException {
            // Arrange
            String username = "testuser";
            User expectedUser = new User();
            expectedUser.setUsername(username);
            when(userDAO.findAll()).thenReturn(Arrays.asList(expectedUser));

            // Act
            Optional<User> result = userService.findByUsername(username);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(expectedUser, result.get());
            verify(userDAO).findAll();
        }

        @Test
        @DisplayName("Devrait lever une ValidationException quand le nom d'utilisateur est vide")
        void shouldThrowValidationExceptionWhenUsernameIsEmpty() {
            // Act & Assert
            assertThrows(ValidationException.class, () -> userService.findByUsername(""));
            verify(userDAO, never()).findAll();
        }
    }
} 