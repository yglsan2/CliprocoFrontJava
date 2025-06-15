package dao;

import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import utilities.Security;
import exceptions.DatabaseException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour UserMySqlDAO.
 * Ces tests vérifient le bon fonctionnement des opérations CRUD sur les utilisateurs.
 */
class UserMySqlDAOTest {
    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;
    @Mock
    private Statement mockStatement;

    private UserMySqlDAO userDAO;
    private User testUser;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        userDAO = new UserMySqlDAO(mockConnection);
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword(Security.hashPassword("password"));
        testUser.setRole("USER");
        testUser.setToken(UUID.randomUUID().toString());
        testUser.setExpire(LocalDate.now().plusDays(7));
    }

    @Test
    void findByEmail_WhenUserExists_ReturnsUser() throws SQLException, DatabaseException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("id")).thenReturn(testUser.getId());
        when(mockResultSet.getString("email")).thenReturn(testUser.getEmail());
        when(mockResultSet.getString("password")).thenReturn(testUser.getPassword());
        when(mockResultSet.getString("role")).thenReturn(testUser.getRole());
        when(mockResultSet.getString("token")).thenReturn(testUser.getToken());
        when(mockResultSet.getDate("expire")).thenReturn(java.sql.Date.valueOf(testUser.getExpire()));
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Act
        User result = userDAO.findByEmail(testUser.getEmail());

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(testUser.getRole(), result.getRole());
        assertEquals(testUser.getToken(), result.getToken());
    }

    @Test
    void findByEmail_WhenUserDoesNotExist_ReturnsNull() throws SQLException, DatabaseException {
        // Arrange
        when(mockResultSet.next()).thenReturn(false);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Act
        User result = userDAO.findByEmail("nonexistent@example.com");

        // Assert
        assertNull(result);
    }

    @Test
    void findByEmail_WhenEmailIsNull_ThrowsIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.findByEmail(null));
    }

    @Test
    void findByEmail_WhenDatabaseError_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.findByEmail("test@example.com"));
    }

    @Test
    void findByToken_WhenUserExists_ReturnsUser() throws SQLException, DatabaseException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("id")).thenReturn(testUser.getId());
        when(mockResultSet.getString("email")).thenReturn(testUser.getEmail());
        when(mockResultSet.getString("password")).thenReturn(testUser.getPassword());
        when(mockResultSet.getString("role")).thenReturn(testUser.getRole());
        when(mockResultSet.getString("token")).thenReturn(testUser.getToken());
        when(mockResultSet.getDate("expire")).thenReturn(java.sql.Date.valueOf(testUser.getExpire()));
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Act
        User result = userDAO.findByToken(testUser.getToken());

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getToken(), result.getToken());
        assertEquals(testUser.getEmail(), result.getEmail());
    }

    @Test
    void findByToken_WhenUserDoesNotExist_ReturnsNull() throws SQLException, DatabaseException {
        // Arrange
        when(mockResultSet.next()).thenReturn(false);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Act
        User result = userDAO.findByToken("nonexistent-token");

        // Assert
        assertNull(result);
    }

    @Test
    void findByToken_WhenTokenIsNull_ThrowsIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.findByToken(null));
    }

    @Test
    void findById_WhenUserExists_ReturnsUser() throws SQLException, DatabaseException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("id")).thenReturn(testUser.getId());
        when(mockResultSet.getString("email")).thenReturn(testUser.getEmail());
        when(mockResultSet.getString("password")).thenReturn(testUser.getPassword());
        when(mockResultSet.getString("role")).thenReturn(testUser.getRole());
        when(mockResultSet.getString("token")).thenReturn(testUser.getToken());
        when(mockResultSet.getDate("expire")).thenReturn(java.sql.Date.valueOf(testUser.getExpire()));
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Act
        User result = userDAO.findById(testUser.getId());

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getEmail(), result.getEmail());
    }

    @Test
    void findById_WhenUserDoesNotExist_ReturnsNull() throws SQLException, DatabaseException {
        // Arrange
        when(mockResultSet.next()).thenReturn(false);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Act
        User result = userDAO.findById(999L);

        // Assert
        assertNull(result);
    }

    @Test
    void findById_WhenIdIsNull_ThrowsIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.findById(null));
    }

    @Test
    void findAll_WhenUsersExist_ReturnsListOfUsers() throws SQLException, DatabaseException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getLong("id")).thenReturn(1L, 2L);
        when(mockResultSet.getString("email")).thenReturn("user1@example.com", "user2@example.com");
        when(mockResultSet.getString("password")).thenReturn("pass1", "pass2");
        when(mockResultSet.getString("role")).thenReturn("USER", "ADMIN");
        when(mockResultSet.getString("token")).thenReturn("token1", "token2");
        when(mockResultSet.getDate("expire")).thenReturn(
            java.sql.Date.valueOf(LocalDate.now().plusDays(7)),
            java.sql.Date.valueOf(LocalDate.now().plusDays(7))
        );
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        // Act
        List<User> results = userDAO.findAll();

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals("user1@example.com", results.get(0).getEmail());
        assertEquals("user2@example.com", results.get(1).getEmail());
    }

    @Test
    void findAll_WhenNoUsersExist_ReturnsEmptyList() throws SQLException, DatabaseException {
        // Arrange
        when(mockResultSet.next()).thenReturn(false);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        // Act
        List<User> results = userDAO.findAll();

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void save_WhenSuccessful_SavesUser() throws SQLException, DatabaseException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Act
        userDAO.save(testUser);

        // Assert
        verify(mockPreparedStatement).setString(1, testUser.getEmail());
        verify(mockPreparedStatement).setString(2, testUser.getPassword());
        verify(mockPreparedStatement).setString(3, testUser.getRole());
        verify(mockPreparedStatement).setString(4, testUser.getToken());
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    void save_WhenUserIsNull_ThrowsIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(null));
    }

    @Test
    void save_WhenDatabaseError_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.save(testUser));
    }

    @Test
    void save_WhenEmailIsInvalid_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setEmail("invalid-email");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void save_WhenPasswordIsTooShort_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setPassword("short");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void save_WhenRoleIsInvalid_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setRole("INVALID_ROLE");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void save_WhenEmailIsEmpty_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setEmail("");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void save_WhenPasswordIsEmpty_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setPassword("");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void save_WhenRoleIsEmpty_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setRole("");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void save_WhenTokenIsEmpty_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setToken("");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void save_WhenUserHasNullFields_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setEmail(null);
        testUser.setPassword(null);
        testUser.setRole(null);
        testUser.setToken(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void update_WhenSuccessful_UpdatesUser() throws SQLException, DatabaseException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Act
        userDAO.update(testUser);

        // Assert
        verify(mockPreparedStatement).setString(1, testUser.getEmail());
        verify(mockPreparedStatement).setString(2, testUser.getPassword());
        verify(mockPreparedStatement).setString(3, testUser.getRole());
        verify(mockPreparedStatement).setString(4, testUser.getToken());
        verify(mockPreparedStatement).setLong(5, testUser.getId());
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    void update_WhenUserIsNull_ThrowsIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(null));
    }

    @Test
    void update_WhenDatabaseError_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.update(testUser));
    }

    @Test
    void update_WhenEmailIsInvalid_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setEmail("invalid-email");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void update_WhenPasswordIsTooShort_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setPassword("short");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void update_WhenRoleIsInvalid_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setRole("INVALID_ROLE");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void update_WhenEmailIsEmpty_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setEmail("");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void update_WhenPasswordIsEmpty_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setPassword("");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void update_WhenRoleIsEmpty_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setRole("");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void update_WhenTokenIsEmpty_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setToken("");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void update_WhenUserHasNullFields_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setEmail(null);
        testUser.setPassword(null);
        testUser.setRole(null);
        testUser.setToken(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void update_WhenUserHasExpiredDate_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setExpire(LocalDate.now().minusDays(1));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void update_WhenUserHasNullExpireDate_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setExpire(null);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void update_WhenUserHasInvalidEmailFormat_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setEmail("invalid.email@");
        testUser.setEmail("invalid@.com");
        testUser.setEmail("@invalid.com");
        testUser.setEmail("invalid@com");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void update_WhenUserHasInvalidRoleFormat_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setRole("user"); // Doit être en majuscules
        testUser.setRole("ADMIN "); // Contient un espace
        testUser.setRole("USER_"); // Contient un underscore

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void update_WhenUserHasInvalidTokenFormat_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setToken("invalid-token"); // Doit être un UUID valide
        testUser.setToken("12345678-1234-1234-1234-123456789012"); // UUID invalide

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void update_WhenUserHasInvalidPasswordFormat_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setPassword("password"); // Doit être hashé
        testUser.setPassword("123456"); // Trop court
        testUser.setPassword("abcdefghijklmnopqrstuvwxyz"); // Trop long

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void update_WhenUserHasInvalidId_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setId(-1L);
        testUser.setId(0L);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void update_WhenUserHasInvalidExpireDate_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setExpire(LocalDate.now().minusDays(1)); // Date dans le passé
        testUser.setExpire(LocalDate.now().plusYears(100)); // Date trop lointaine

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void update_WhenUserHasInvalidEmailLength_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setEmail("a".repeat(256) + "@example.com"); // Email trop long

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void update_WhenUserHasInvalidPasswordLength_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setPassword("a".repeat(256)); // Mot de passe trop long

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void update_WhenUserHasInvalidRoleLength_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setRole("a".repeat(256)); // Rôle trop long

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void update_WhenUserHasInvalidTokenLength_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setToken("a".repeat(256)); // Token trop long

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void update_WhenUserHasInvalidEmailCharacters_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setEmail("test@example.com<script>alert('xss')</script>"); // Contient des caractères HTML
        testUser.setEmail("test@example.com' OR '1'='1"); // Injection SQL
        testUser.setEmail("test@example.com; DROP TABLE users;"); // Injection SQL

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void update_WhenUserHasInvalidPasswordCharacters_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setPassword("<script>alert('xss')</script>"); // Contient des caractères HTML
        testUser.setPassword("' OR '1'='1"); // Injection SQL
        testUser.setPassword("; DROP TABLE users;"); // Injection SQL

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void update_WhenUserHasInvalidRoleCharacters_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setRole("<script>alert('xss')</script>"); // Contient des caractères HTML
        testUser.setRole("' OR '1'='1"); // Injection SQL
        testUser.setRole("; DROP TABLE users;"); // Injection SQL

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void update_WhenUserHasInvalidTokenCharacters_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setToken("<script>alert('xss')</script>"); // Contient des caractères HTML
        testUser.setToken("' OR '1'='1"); // Injection SQL
        testUser.setToken("; DROP TABLE users;"); // Injection SQL

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void delete_WhenSuccessful_DeletesUser() throws SQLException, DatabaseException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Act
        userDAO.delete(testUser);

        // Assert
        verify(mockPreparedStatement).setLong(1, testUser.getId());
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    void delete_WhenUserIsNull_ThrowsIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.delete(null));
    }

    @Test
    void delete_WhenDatabaseError_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.delete(testUser));
    }

    @Test
    void findByEmail_WhenEmailIsInvalid_ThrowsIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.findByEmail("invalid-email"));
    }

    @Test
    void findByEmail_WhenEmailIsEmpty_ThrowsIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.findByEmail(""));
    }

    @Test
    void findByToken_WhenTokenIsEmpty_ThrowsIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.findByToken(""));
    }

    @Test
    void findById_WhenIdIsZero_ThrowsIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.findById(0L));
    }

    @Test
    void findById_WhenIdIsNegative_ThrowsIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.findById(-1L));
    }

    @Test
    void delete_WhenIdIsZero_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setId(0L);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.delete(testUser));
    }

    @Test
    void delete_WhenIdIsNegative_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setId(-1L);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.delete(testUser));
    }

    @Test
    void save_WhenUserHasExpiredDate_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setExpire(LocalDate.now().minusDays(1));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void save_WhenUserHasInvalidEmailFormat_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setEmail("invalid.email@");
        testUser.setEmail("invalid@.com");
        testUser.setEmail("@invalid.com");
        testUser.setEmail("invalid@com");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void save_WhenUserHasInvalidRoleFormat_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setRole("user"); // Doit être en majuscules
        testUser.setRole("ADMIN "); // Contient un espace
        testUser.setRole("USER_"); // Contient un underscore

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void save_WhenUserHasInvalidTokenFormat_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setToken("invalid-token"); // Doit être un UUID valide
        testUser.setToken("12345678-1234-1234-1234-123456789012"); // UUID invalide

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void save_WhenUserHasInvalidPasswordFormat_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setPassword("password"); // Doit être hashé
        testUser.setPassword("123456"); // Trop court
        testUser.setPassword("abcdefghijklmnopqrstuvwxyz"); // Trop long

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void save_WhenUserHasInvalidId_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setId(-1L);
        testUser.setId(0L);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void save_WhenUserHasInvalidExpireDate_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setExpire(LocalDate.now().minusDays(1)); // Date dans le passé
        testUser.setExpire(LocalDate.now().plusYears(100)); // Date trop lointaine

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void save_WhenUserHasInvalidEmailLength_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setEmail("a".repeat(256) + "@example.com"); // Email trop long

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void save_WhenUserHasInvalidPasswordLength_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setPassword("a".repeat(256)); // Mot de passe trop long

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void save_WhenUserHasInvalidRoleLength_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setRole("a".repeat(256)); // Rôle trop long

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void save_WhenUserHasInvalidTokenLength_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setToken("a".repeat(256)); // Token trop long

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void save_WhenUserHasInvalidEmailCharacters_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setEmail("test@example.com<script>alert('xss')</script>"); // Contient des caractères HTML
        testUser.setEmail("test@example.com' OR '1'='1"); // Injection SQL
        testUser.setEmail("test@example.com; DROP TABLE users;"); // Injection SQL

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void save_WhenUserHasInvalidPasswordCharacters_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setPassword("<script>alert('xss')</script>"); // Contient des caractères HTML
        testUser.setPassword("' OR '1'='1"); // Injection SQL
        testUser.setPassword("; DROP TABLE users;"); // Injection SQL

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void save_WhenUserHasInvalidRoleCharacters_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setRole("<script>alert('xss')</script>"); // Contient des caractères HTML
        testUser.setRole("' OR '1'='1"); // Injection SQL
        testUser.setRole("; DROP TABLE users;"); // Injection SQL

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void save_WhenUserHasInvalidTokenCharacters_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setToken("<script>alert('xss')</script>"); // Contient des caractères HTML
        testUser.setToken("' OR '1'='1"); // Injection SQL
        testUser.setToken("; DROP TABLE users;"); // Injection SQL

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void save_WhenConcurrentUpdate_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Deadlock detected"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.save(testUser));
    }

    @Test
    void update_WhenConcurrentUpdate_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Deadlock detected"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.update(testUser));
    }

    @Test
    void delete_WhenConcurrentDelete_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Deadlock detected"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.delete(testUser));
    }

    @Test
    void findAll_WhenConcurrentRead_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockStatement.executeQuery(anyString())).thenThrow(new SQLException("Lock timeout"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.findAll());
    }

    @Test
    void save_WhenDatabaseConnectionLost_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Connection lost"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.save(testUser));
    }

    @Test
    void update_WhenDatabaseConnectionLost_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Connection lost"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.update(testUser));
    }

    @Test
    void delete_WhenDatabaseConnectionLost_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Connection lost"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.delete(testUser));
    }

    @Test
    void findAll_WhenDatabaseConnectionLost_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.createStatement()).thenThrow(new SQLException("Connection lost"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.findAll());
    }

    @Test
    void save_WhenTransactionRollback_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Transaction rollback"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.save(testUser));
    }

    @Test
    void update_WhenTransactionRollback_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Transaction rollback"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.update(testUser));
    }

    @Test
    void delete_WhenTransactionRollback_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Transaction rollback"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.delete(testUser));
    }

    @Test
    void save_WhenDatabaseTimeout_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Query timeout"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.save(testUser));
    }

    @Test
    void update_WhenDatabaseTimeout_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Query timeout"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.update(testUser));
    }

    @Test
    void delete_WhenDatabaseTimeout_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Query timeout"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.delete(testUser));
    }

    @Test
    void findAll_WhenDatabaseTimeout_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.createStatement()).thenThrow(new SQLException("Query timeout"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.findAll());
    }

    @Test
    void save_WhenDatabaseConstraintViolation_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Unique constraint violation"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.save(testUser));
    }

    @Test
    void update_WhenDatabaseConstraintViolation_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Unique constraint violation"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.update(testUser));
    }

    @Test
    void save_WhenDatabaseForeignKeyViolation_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Foreign key violation"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.save(testUser));
    }

    @Test
    void update_WhenDatabaseForeignKeyViolation_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Foreign key violation"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.update(testUser));
    }

    @Test
    void delete_WhenDatabaseForeignKeyViolation_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Foreign key violation"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.delete(testUser));
    }

    @Test
    void save_WhenDatabaseConnectionPoolExhausted_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Connection pool exhausted"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.save(testUser));
    }

    @Test
    void update_WhenDatabaseConnectionPoolExhausted_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Connection pool exhausted"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.update(testUser));
    }

    @Test
    void delete_WhenDatabaseConnectionPoolExhausted_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Connection pool exhausted"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.delete(testUser));
    }

    @Test
    void findAll_WhenDatabaseConnectionPoolExhausted_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.createStatement()).thenThrow(new SQLException("Connection pool exhausted"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.findAll());
    }

    @Test
    void save_WhenLargeDataSet_CompletesWithinTimeout() throws SQLException, DatabaseException {
        // Arrange
        int numberOfUsers = 1000;
        List<User> users = new ArrayList<>();
        for (int i = 0; i < numberOfUsers; i++) {
            User user = new User();
            user.setEmail("user" + i + "@example.com");
            user.setPassword(Security.hashPassword("password" + i));
            user.setRole("USER");
            user.setToken(UUID.randomUUID().toString());
            user.setExpire(LocalDate.now().plusDays(7));
            users.add(user);
        }

        // Act & Assert
        long startTime = System.currentTimeMillis();
        for (User user : users) {
            userDAO.save(user);
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        assertTrue(duration < 5000, "Operation took too long: " + duration + "ms");
    }

    @Test
    void findAll_WhenLargeDataSet_CompletesWithinTimeout() throws SQLException, DatabaseException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true, true, true, false);
        when(mockResultSet.getLong("id")).thenReturn(1L, 2L, 3L);
        when(mockResultSet.getString("email")).thenReturn("user1@example.com", "user2@example.com", "user3@example.com");
        when(mockResultSet.getString("password")).thenReturn("pass1", "pass2", "pass3");
        when(mockResultSet.getString("role")).thenReturn("USER", "USER", "USER");
        when(mockResultSet.getString("token")).thenReturn("token1", "token2", "token3");
        when(mockResultSet.getDate("expire")).thenReturn(
            java.sql.Date.valueOf(LocalDate.now().plusDays(7)),
            java.sql.Date.valueOf(LocalDate.now().plusDays(7)),
            java.sql.Date.valueOf(LocalDate.now().plusDays(7))
        );
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        // Act & Assert
        long startTime = System.currentTimeMillis();
        List<User> results = userDAO.findAll();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        assertTrue(duration < 1000, "Operation took too long: " + duration + "ms");
        assertEquals(3, results.size());
    }

    @Test
    void save_WhenMemoryPressure_HandlesGracefully() throws SQLException, DatabaseException {
        // Arrange
        int largeStringSize = 1024 * 1024; // 1MB
        String largeString = "a".repeat(largeStringSize);
        testUser.setEmail(largeString + "@example.com");
        testUser.setPassword(largeString);
        testUser.setRole(largeString);
        testUser.setToken(largeString);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void findAll_WhenMemoryPressure_HandlesGracefully() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getLong("id")).thenReturn(1L);
        when(mockResultSet.getString("email")).thenReturn("a".repeat(1024 * 1024));
        when(mockResultSet.getString("password")).thenReturn("a".repeat(1024 * 1024));
        when(mockResultSet.getString("role")).thenReturn("a".repeat(1024 * 1024));
        when(mockResultSet.getString("token")).thenReturn("a".repeat(1024 * 1024));
        when(mockResultSet.getDate("expire")).thenReturn(java.sql.Date.valueOf(LocalDate.now().plusDays(7)));
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.findAll());
    }

    @Test
    void save_WhenDatabaseVersionMismatch_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Incompatible database version"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.save(testUser));
    }

    @Test
    void update_WhenDatabaseVersionMismatch_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Incompatible database version"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.update(testUser));
    }

    @Test
    void delete_WhenDatabaseVersionMismatch_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Incompatible database version"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.delete(testUser));
    }

    @Test
    void findAll_WhenDatabaseVersionMismatch_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.createStatement()).thenThrow(new SQLException("Incompatible database version"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.findAll());
    }

    @Test
    void save_WhenSchemaMigrationRequired_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Schema migration required"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.save(testUser));
    }

    @Test
    void update_WhenSchemaMigrationRequired_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Schema migration required"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.update(testUser));
    }

    @Test
    void delete_WhenSchemaMigrationRequired_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Schema migration required"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.delete(testUser));
    }

    @Test
    void findAll_WhenSchemaMigrationRequired_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.createStatement()).thenThrow(new SQLException("Schema migration required"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.findAll());
    }

    @Test
    void save_WhenSQLInjectionAttempt_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setEmail("test@example.com' OR '1'='1");
        testUser.setPassword("' OR '1'='1");
        testUser.setRole("' OR '1'='1");
        testUser.setToken("' OR '1'='1");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void update_WhenSQLInjectionAttempt_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setEmail("test@example.com' OR '1'='1");
        testUser.setPassword("' OR '1'='1");
        testUser.setRole("' OR '1'='1");
        testUser.setToken("' OR '1'='1");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void save_WhenXSSAttempt_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setEmail("test@example.com<script>alert('xss')</script>");
        testUser.setPassword("<script>alert('xss')</script>");
        testUser.setRole("<script>alert('xss')</script>");
        testUser.setToken("<script>alert('xss')</script>");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void update_WhenXSSAttempt_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setEmail("test@example.com<script>alert('xss')</script>");
        testUser.setPassword("<script>alert('xss')</script>");
        testUser.setRole("<script>alert('xss')</script>");
        testUser.setToken("<script>alert('xss')</script>");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void save_WhenPasswordNotHashed_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setPassword("plaintextpassword");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void update_WhenPasswordNotHashed_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setPassword("plaintextpassword");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void save_WhenTokenNotUUID_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setToken("not-a-uuid");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(testUser));
    }

    @Test
    void update_WhenTokenNotUUID_ThrowsIllegalArgumentException() {
        // Arrange
        testUser.setToken("not-a-uuid");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(testUser));
    }

    @Test
    void save_WhenIntegrationWithSecurityService_ValidatesPassword() throws SQLException, DatabaseException {
        // Arrange
        String hashedPassword = Security.hashPassword("password123");
        testUser.setPassword(hashedPassword);

        // Act & Assert
        assertDoesNotThrow(() -> userDAO.save(testUser));
        verify(mockPreparedStatement).setString(2, hashedPassword);
    }

    @Test
    void save_WhenIntegrationWithEmailService_ValidatesEmail() throws SQLException, DatabaseException {
        // Arrange
        String validEmail = "test@example.com";
        testUser.setEmail(validEmail);

        // Act & Assert
        assertDoesNotThrow(() -> userDAO.save(testUser));
        verify(mockPreparedStatement).setString(1, validEmail);
    }

    @Test
    void save_WhenIntegrationWithTokenService_ValidatesToken() throws SQLException, DatabaseException {
        // Arrange
        String validToken = UUID.randomUUID().toString();
        testUser.setToken(validToken);

        // Act & Assert
        assertDoesNotThrow(() -> userDAO.save(testUser));
        verify(mockPreparedStatement).setString(4, validToken);
    }

    @Test
    void save_WhenLoadTest_HandlesConcurrentRequests() throws SQLException, DatabaseException, InterruptedException {
        // Arrange
        int numberOfThreads = 10;
        int requestsPerThread = 100;
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        // Act
        for (int i = 0; i < numberOfThreads; i++) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < requestsPerThread; j++) {
                        User user = new User();
                        user.setEmail("user" + j + "@example.com");
                        user.setPassword(Security.hashPassword("password" + j));
                        user.setRole("USER");
                        user.setToken(UUID.randomUUID().toString());
                        user.setExpire(LocalDate.now().plusDays(7));
                        try {
                            userDAO.save(user);
                            successCount.incrementAndGet();
                        } catch (Exception e) {
                            failureCount.incrementAndGet();
                        }
                    }
                } finally {
                    latch.countDown();
                }
            }).start();
        }

        // Assert
        assertTrue(latch.await(30, TimeUnit.SECONDS), "Load test timed out");
        assertTrue(successCount.get() > 0, "No successful requests");
        assertTrue(failureCount.get() < numberOfThreads * requestsPerThread, "Too many failures");
    }

    @Test
    void findAll_WhenLoadTest_HandlesConcurrentRequests() throws SQLException, DatabaseException, InterruptedException {
        // Arrange
        int numberOfThreads = 10;
        int requestsPerThread = 100;
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        // Act
        for (int i = 0; i < numberOfThreads; i++) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < requestsPerThread; j++) {
                        try {
                            userDAO.findAll();
                            successCount.incrementAndGet();
                        } catch (Exception e) {
                            failureCount.incrementAndGet();
                        }
                    }
                } finally {
                    latch.countDown();
                }
            }).start();
        }

        // Assert
        assertTrue(latch.await(30, TimeUnit.SECONDS), "Load test timed out");
        assertTrue(successCount.get() > 0, "No successful requests");
        assertTrue(failureCount.get() < numberOfThreads * requestsPerThread, "Too many failures");
    }

    @Test
    void save_WhenDatabaseRecovery_HandlesGracefully() throws SQLException, DatabaseException {
        // Arrange
        when(mockConnection.prepareStatement(anyString()))
            .thenThrow(new SQLException("Database unavailable"))
            .thenReturn(mockPreparedStatement);

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.save(testUser));
        assertDoesNotThrow(() -> userDAO.save(testUser));
    }

    @Test
    void update_WhenDatabaseRecovery_HandlesGracefully() throws SQLException, DatabaseException {
        // Arrange
        when(mockConnection.prepareStatement(anyString()))
            .thenThrow(new SQLException("Database unavailable"))
            .thenReturn(mockPreparedStatement);

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.update(testUser));
        assertDoesNotThrow(() -> userDAO.update(testUser));
    }

    @Test
    void delete_WhenDatabaseRecovery_HandlesGracefully() throws SQLException, DatabaseException {
        // Arrange
        when(mockConnection.prepareStatement(anyString()))
            .thenThrow(new SQLException("Database unavailable"))
            .thenReturn(mockPreparedStatement);

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.delete(testUser));
        assertDoesNotThrow(() -> userDAO.delete(testUser));
    }

    @Test
    void findAll_WhenDatabaseRecovery_HandlesGracefully() throws SQLException, DatabaseException {
        // Arrange
        when(mockConnection.createStatement())
            .thenThrow(new SQLException("Database unavailable"))
            .thenReturn(mockStatement);

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.findAll());
        assertDoesNotThrow(() -> userDAO.findAll());
    }

    @Test
    void save_WhenOperationSucceeds_LogsInfo() throws SQLException, DatabaseException {
        // Arrange
        Logger logger = mock(Logger.class);
        when(logger.isInfoEnabled()).thenReturn(true);

        // Act
        userDAO.save(testUser);

        // Assert
        verify(logger).info(anyString());
    }

    @Test
    void update_WhenOperationSucceeds_LogsInfo() throws SQLException, DatabaseException {
        // Arrange
        Logger logger = mock(Logger.class);
        when(logger.isInfoEnabled()).thenReturn(true);

        // Act
        userDAO.update(testUser);

        // Assert
        verify(logger).info(anyString());
    }

    @Test
    void delete_WhenOperationSucceeds_LogsInfo() throws SQLException, DatabaseException {
        // Arrange
        Logger logger = mock(Logger.class);
        when(logger.isInfoEnabled()).thenReturn(true);

        // Act
        userDAO.delete(testUser);

        // Assert
        verify(logger).info(anyString());
    }

    @Test
    void findAll_WhenOperationSucceeds_LogsInfo() throws SQLException, DatabaseException {
        // Arrange
        Logger logger = mock(Logger.class);
        when(logger.isInfoEnabled()).thenReturn(true);

        // Act
        userDAO.findAll();

        // Assert
        verify(logger).info(anyString());
    }

    @Test
    void save_WhenErrorOccurs_LogsError() throws SQLException {
        // Arrange
        Logger logger = mock(Logger.class);
        when(logger.isErrorEnabled()).thenReturn(true);
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.save(testUser));
        verify(logger).error(anyString());
    }

    @Test
    void update_WhenErrorOccurs_LogsError() throws SQLException {
        // Arrange
        Logger logger = mock(Logger.class);
        when(logger.isErrorEnabled()).thenReturn(true);
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.update(testUser));
        verify(logger).error(anyString());
    }

    @Test
    void delete_WhenErrorOccurs_LogsError() throws SQLException {
        // Arrange
        Logger logger = mock(Logger.class);
        when(logger.isErrorEnabled()).thenReturn(true);
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.delete(testUser));
        verify(logger).error(anyString());
    }

    @Test
    void findAll_WhenErrorOccurs_LogsError() throws SQLException {
        // Arrange
        Logger logger = mock(Logger.class);
        when(logger.isErrorEnabled()).thenReturn(true);
        when(mockConnection.createStatement()).thenThrow(new SQLException("Database error"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.findAll());
        verify(logger).error(anyString());
    }
} 