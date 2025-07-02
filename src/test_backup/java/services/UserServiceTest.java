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
    @Nested
    @DisplayName("Tests de performance")
    class PerformanceTests {
        private List<User> users;

        @BeforeEach
        void setUp() {
            users = new ArrayList<>();
            
            // Création de 1000 utilisateurs de test
            for (int i = 0; i < 1000; i++) {
                User user = new User();
                user.setUsername("user" + i);
                user.setEmail("user" + i + "@test.com");
                user.setPassword("Password" + i + "!");
                users.add(user);
            }
        }

        @Test
        @DisplayName("Devrait gérer efficacement la création de 1000 utilisateurs")
        void shouldHandleBulkUserCreation() throws ValidationException, DatabaseException {
            for (int i = 0; i < 1000; i++) {
                when(userDAO.save(any(User.class))).thenReturn(users.get(i));
                userService.create(
                    "user" + i,
                    "user" + i + "@test.com",
                    "Password" + i + "!"
                );
            }
            verify(userDAO, times(1000)).save(any(User.class));
        }

        @Test
        @DisplayName("Devrait gérer efficacement la recherche d'utilisateurs")
        void shouldHandleEfficientUserSearch() throws ValidationException, DatabaseException {
            when(userDAO.findByUsername(any())).thenReturn(Optional.of(users.get(0)));
            
            for (int i = 0; i < 1000; i++) {
                userService.findByUsername("user" + i);
            }
            
            verify(userDAO, times(1000)).findByUsername(any());
        }
    }

    @Nested
    @DisplayName("Tests de concurrence")
    class ConcurrencyTests {
        private ExecutorService executorService;
        private CountDownLatch latch;

        @BeforeEach
        void setUp() {
            executorService = Executors.newFixedThreadPool(10);
            latch = new CountDownLatch(10);
        }

        @AfterEach
        void tearDown() {
            executorService.shutdown();
        }

        @Test
        @DisplayName("Devrait gérer correctement les accès concurrents à la création d'utilisateurs")
        void shouldHandleConcurrentUserCreation() throws InterruptedException {
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failureCount = new AtomicInteger(0);

            for (int i = 0; i < 10; i++) {
                final int index = i;
                executorService.submit(() -> {
                    try {
                        when(userDAO.save(any(User.class))).thenReturn(
                            new User("user" + index, "user" + index + "@test.com", "Password" + index + "!")
                        );
                        
                        userService.create(
                            "user" + index,
                            "user" + index + "@test.com",
                            "Password" + index + "!"
                        );
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(5, TimeUnit.SECONDS);
            assertEquals(10, successCount.get() + failureCount.get());
        }

        @Test
        @DisplayName("Devrait gérer correctement les mises à jour concurrentes")
        void shouldHandleConcurrentUpdates() throws InterruptedException, ValidationException, DatabaseException {
            User user = new User("testuser", "test@example.com", "Password123!");
            when(userDAO.findById(any())).thenReturn(Optional.of(user));
            when(userDAO.save(any(User.class))).thenReturn(user);
            
            AtomicInteger successCount = new AtomicInteger(0);
            AtomicInteger failureCount = new AtomicInteger(0);

            for (int i = 0; i < 10; i++) {
                executorService.submit(() -> {
                    try {
                        user.setEmail("updated" + i + "@example.com");
                        userService.update(user);
                        successCount.incrementAndGet();
                    } catch (Exception e) {
                        failureCount.incrementAndGet();
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(5, TimeUnit.SECONDS);
            assertEquals(10, successCount.get() + failureCount.get());
        }
    }
    @Nested
    @DisplayName("Tests de performance")
    class PerformanceTests {
    @Nested
    @DisplayName("Tests de sécurité")
    class SecurityTests {
        @Test
        @DisplayName("Devrait valider la force du mot de passe")
        void shouldValidatePasswordStrength() throws ValidationException, DatabaseException {
            User user = new User();
            user.setUsername("testuser");
            user.setEmail("test@example.com");
            user.setPassword("weak");
            assertThrows(ValidationException.class, () -> userService.save(user));
        }

        @Test
        @DisplayName("Devrait empêcher les injections SQL")
        void shouldPreventSqlInjection() throws ValidationException, DatabaseException {
            User user = new User();
            user.setUsername("test"; DROP TABLE users; --");
            user.setEmail("test@example.com");
            user.setPassword("password123");
            assertThrows(ValidationException.class, () -> userService.save(user));
        }

        @Test
        @DisplayName("Devrait valider le format de l'email")
        void shouldValidateEmailFormat() throws ValidationException, DatabaseException {
            User user = new User();
            user.setUsername("testuser");
            user.setEmail("invalid-email");
            user.setPassword("password123");
            assertThrows(ValidationException.class, () -> userService.save(user));
        }
    }
        @Test
        @DisplayName("Devrait gérer efficacement un grand nombre d'utilisateurs")
        void shouldHandleLargeNumberOfUsers() throws ValidationException, DatabaseException {
            List<User> users = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                User user = new User();
                user.setUsername("user" + i);
                user.setEmail("user" + i + "@test.com");
                user.setPassword("password" + i);
                users.add(user);
            }
            when(userDAO.findAll()).thenReturn(users);
            List<User> result = userService.findAll();
            assertEquals(1000, result.size());
        }
    }
    @Nested
    @DisplayName("Tests de validation métier")
    class ValidationMetierTests {
        private User user;

        @BeforeEach
        void setUp() {
            user = new User();
            user.setUsername("testuser");
            user.setEmail("test@example.com");
            user.setPassword("password123");
        }

        @Test
        @DisplayName("Devrait lancer ValidationException quand le nom d'utilisateur est vide")
        void shouldThrowValidationExceptionWhenUsernameIsEmpty() throws ValidationException, DatabaseException {
            user.setUsername("");
            assertThrows(ValidationException.class, () -> userService.save(user));
        }

        @Test
        @DisplayName("Devrait lancer ValidationException quand l'email est invalide")
        void shouldThrowValidationExceptionWhenEmailIsInvalid() throws ValidationException, DatabaseException {
            user.setEmail("invalid-email");
            assertThrows(ValidationException.class, () -> userService.save(user));
        }

        @Test
        @DisplayName("Devrait lancer ValidationException quand le mot de passe est trop court")
        void shouldThrowValidationExceptionWhenPasswordIsTooShort() throws ValidationException, DatabaseException {
            user.setPassword("123");
            assertThrows(ValidationException.class, () -> userService.save(user));
        }
    }

    @Nested
    @DisplayName("Tests de recherche")
    class RechercheTests {
        private List<User> users;

        @BeforeEach
        void setUp() {
            users = Arrays.asList(
                new User("user1", "user1@test.com", "password1"),
                new User("user2", "user2@test.com", "password2")
            );
        }

        @Test
        @DisplayName("Devrait trouver un utilisateur par nom d'utilisateur")
        void shouldFindUserByUsername() throws ValidationException, DatabaseException {
            String username = "user1";
            when(userDAO.findAll()).thenReturn(users);
            Optional<User> result = userService.findByUsername(username);
            assertTrue(result.isPresent());
            assertEquals(username, result.get().getUsername());
        }

        @Test
        @DisplayName("Devrait retourner Optional.empty quand l'utilisateur n'est pas trouvé")
        void shouldReturnEmptyWhenUserNotFound() throws ValidationException, DatabaseException {
            when(userDAO.findAll()).thenReturn(users);
            Optional<User> result = userService.findByUsername("nonexistent");
            assertFalse(result.isPresent());
        }
    }

    @Nested
    @DisplayName("Tests de cas limites")
    class CasLimitesTests {
        private User user;

        @BeforeEach
        void setUp() {
            user = new User();
            user.setUsername("testuser");
            user.setEmail("test@example.com");
            user.setPassword("password123");
        }

        @Test
        @DisplayName("Devrait gérer correctement les caractères spéciaux dans le nom d'utilisateur")
        void shouldHandleSpecialCharactersInUsername() throws ValidationException, DatabaseException {
            user.setUsername("test.user-123");
            when(userDAO.save(any(User.class))).thenReturn(user);
            assertDoesNotThrow(() -> userService.save(user));
        }

        @Test
        @DisplayName("Devrait gérer correctement les emails avec sous-domaines")
        void shouldHandleEmailsWithSubdomains() throws ValidationException, DatabaseException {
            user.setEmail("test.sub@domain.co.uk");
            when(userDAO.save(any(User.class))).thenReturn(user);
            assertDoesNotThrow(() -> userService.save(user));
        }

        @Test
        @DisplayName("Devrait gérer correctement les mises à jour partielles")
        void shouldHandlePartialUpdates() throws ValidationException, DatabaseException, ResourceNotFoundException {
            User existingUser = new User("existing", "existing@test.com", "password");
            existingUser.setId(1L);
            when(userDAO.findById(1L)).thenReturn(Optional.of(existingUser));
            when(userDAO.update(any(User.class))).thenReturn(existingUser);

            existingUser.setEmail("new@test.com");
            assertDoesNotThrow(() -> userService.update(existingUser));
        }
    }

    @Mock
    private IDAO<User, Long> userDAO;

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
            Long userId = 1L;
            User expectedUser = new User();
            expectedUser.setId(userId);
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
            Long userId = 999L;
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
            ValidationException exception = assertThrows(ValidationException.class, () -> {
                userService.findById(null);
            });
            verify(userDAO, never()).findById(any());
        }

        @Test
        @DisplayName("Devrait lever une DatabaseException en cas d'erreur de base de données")
        void shouldThrowDatabaseExceptionOnDatabaseError() {
            // Arrange
            Long userId = 1L;
            when(userDAO.findById(userId)).thenThrow(new RuntimeException("Erreur de base de données"));

            // Act & Assert
            DatabaseException exception = assertThrows(DatabaseException.class, () -> {
                userService.findById(userId);
            });
            verify(userDAO).findById(userId);
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
            DatabaseException exception = assertThrows(DatabaseException.class, () -> {
                userService.findAll();
            });
            verify(userDAO).findAll();
        }

        @Test
        @DisplayName("Devrait retourner une liste vide quand aucun utilisateur n'existe")
        void shouldReturnEmptyListWhenNoUsersExist() throws DatabaseException {
            // Arrange
            when(userDAO.findAll()).thenReturn(Arrays.asList());

            // Act
            List<User> result = userService.findAll();

            // Assert
            assertTrue(result.isEmpty());
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
            ValidationException exception = assertThrows(ValidationException.class, () -> {
                userService.save(null);
            });
            verify(userDAO, never()).save(any());
        }

        @Test
        @DisplayName("Devrait lever une DatabaseException en cas d'erreur de base de données")
        void shouldThrowDatabaseExceptionOnDatabaseError() {
            // Arrange
            User user = new User();
            when(userDAO.save(user)).thenThrow(new RuntimeException("Erreur de base de données"));

            // Act & Assert
            DatabaseException exception = assertThrows(DatabaseException.class, () -> {
                userService.save(user);
            });
            verify(userDAO).save(user);
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
            user.setId(1L);
            when(userDAO.findById(user.getId())).thenReturn(Optional.of(user));
            when(userDAO.update(user)).thenReturn(user);

            // Act
            User result = userService.update(user);

            // Assert
            assertNotNull(result);
            verify(userDAO).findById(user.getId());
            verify(userDAO).update(user);
        }

        @Test
        @DisplayName("Devrait lever une ResourceNotFoundException quand l'utilisateur n'existe pas")
        void shouldThrowResourceNotFoundExceptionWhenUserDoesNotExist() {
            // Arrange
            User user = new User();
            user.setId(999L);
            when(userDAO.findById(user.getId())).thenReturn(Optional.empty());

            // Act & Assert
            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                userService.update(user);
            });
            verify(userDAO).findById(user.getId());
            verify(userDAO, never()).update(any());
        }

        @Test
        @DisplayName("Devrait lever une ValidationException quand l'utilisateur est null")
        void shouldThrowValidationExceptionWhenUserIsNull() {
            // Act & Assert
            ValidationException exception = assertThrows(ValidationException.class, () -> {
                userService.update(null);
            });
            verify(userDAO, never()).findById(any());
            verify(userDAO, never()).update(any());
        }

        @Test
        @DisplayName("Devrait lever une ValidationException quand l'ID de l'utilisateur est null")
        void shouldThrowValidationExceptionWhenUserIdIsNull() {
            // Arrange
            User user = new User();

            // Act & Assert
            ValidationException exception = assertThrows(ValidationException.class, () -> {
                userService.update(user);
            });
            verify(userDAO, never()).findById(any());
            verify(userDAO, never()).update(any());
        }

        @Test
        @DisplayName("Devrait lever une DatabaseException en cas d'erreur de base de données")
        void shouldThrowDatabaseExceptionOnDatabaseError() {
            // Arrange
            User user = new User();
            user.setId(1L);
            when(userDAO.findById(user.getId())).thenReturn(Optional.of(user));
            when(userDAO.update(user)).thenThrow(new RuntimeException("Erreur de base de données"));

            // Act & Assert
            DatabaseException exception = assertThrows(DatabaseException.class, () -> {
                userService.update(user);
            });
            verify(userDAO).findById(user.getId());
            verify(userDAO).update(user);
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
            user.setId(1L);
            when(userDAO.findById(user.getId())).thenReturn(Optional.of(user));
            doNothing().when(userDAO).delete(user);

            // Act & Assert
            assertDoesNotThrow(() -> userService.delete(user));
            verify(userDAO).findById(user.getId());
            verify(userDAO).delete(user);
        }

        @Test
        @DisplayName("Devrait lever une ResourceNotFoundException lors de la suppression d'un utilisateur inexistant")
        void shouldThrowResourceNotFoundExceptionWhenDeletingNonExistentUser() {
            // Arrange
            User user = new User();
            user.setId(999L);
            when(userDAO.findById(user.getId())).thenReturn(Optional.empty());

            // Act & Assert
            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
                userService.delete(user);
            });
            verify(userDAO).findById(user.getId());
            verify(userDAO, never()).delete(any());
        }

        @Test
        @DisplayName("Devrait lever une ValidationException quand l'utilisateur est null")
        void shouldThrowValidationExceptionWhenUserIsNull() {
            // Act & Assert
            ValidationException exception = assertThrows(ValidationException.class, () -> {
                userService.delete(null);
            });
            verify(userDAO, never()).findById(any());
            verify(userDAO, never()).delete(any());
        }

        @Test
        @DisplayName("Devrait lever une ValidationException quand l'ID de l'utilisateur est null")
        void shouldThrowValidationExceptionWhenUserIdIsNull() {
            // Arrange
            User user = new User();

            // Act & Assert
            ValidationException exception = assertThrows(ValidationException.class, () -> {
                userService.delete(user);
            });
            verify(userDAO, never()).findById(any());
            verify(userDAO, never()).delete(any());
        }

        @Test
        @DisplayName("Devrait lever une DatabaseException en cas d'erreur de base de données")
        void shouldThrowDatabaseExceptionOnDatabaseError() {
            // Arrange
            User user = new User();
            user.setId(1L);
            when(userDAO.findById(user.getId())).thenReturn(Optional.of(user));
            doThrow(new RuntimeException("Erreur de base de données")).when(userDAO).delete(user);

            // Act & Assert
            DatabaseException exception = assertThrows(DatabaseException.class, () -> {
                userService.delete(user);
            });
            verify(userDAO).findById(user.getId());
            verify(userDAO).delete(user);
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
            ValidationException exception = assertThrows(ValidationException.class, () -> {
                userService.findByUsername("");
            });
            verify(userDAO, never()).findAll();
        }

        @Test
        @DisplayName("Devrait lever une ValidationException quand le nom d'utilisateur est null")
        void shouldThrowValidationExceptionWhenUsernameIsNull() {
            // Act & Assert
            ValidationException exception = assertThrows(ValidationException.class, () -> {
                userService.findByUsername(null);
            });
            verify(userDAO, never()).findAll();
        }

        @Test
        @DisplayName("Devrait retourner Optional.empty quand aucun utilisateur n'est trouvé")
        void shouldReturnEmptyWhenNoUserFound() throws DatabaseException, ValidationException {
            // Arrange
            String username = "nonexistent";
            when(userDAO.findAll()).thenReturn(Arrays.asList());

            // Act
            Optional<User> result = userService.findByUsername(username);

            // Assert
            assertFalse(result.isPresent());
            verify(userDAO).findAll();
        }

        @Test
        @DisplayName("Devrait lever une DatabaseException en cas d'erreur de base de données")
        void shouldThrowDatabaseExceptionOnDatabaseError() {
            // Arrange
            String username = "testuser";
            when(userDAO.findAll()).thenThrow(new RuntimeException("Erreur de base de données"));

            // Act & Assert
            DatabaseException exception = assertThrows(DatabaseException.class, () -> {
                userService.findByUsername(username);
            });
            verify(userDAO).findAll();
        }
    }
} 