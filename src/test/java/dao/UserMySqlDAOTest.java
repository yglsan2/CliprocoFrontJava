package dao;

import models.User;
import exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utilities.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserMySqlDAOTest {

    @Mock
    private DatabaseConnection mockDatabaseConnection;
    
    @Mock
    private Connection mockConnection;
    
    @Mock
    private PreparedStatement mockPreparedStatement;
    
    @Mock
    private ResultSet mockResultSet;
    
    private UserMySqlDAO userDAO;
    private User testUser;

    @BeforeEach
    void setUp() {
        userDAO = new UserMySqlDAO();
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("password");
        testUser.setToken("token123");
    }

    @Test
    void findByUsername_WhenUserExists_ReturnsUser() throws SQLException, DatabaseException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("id")).thenReturn(1L);
        when(mockResultSet.getString("username")).thenReturn("testuser");
        when(mockResultSet.getString("password")).thenReturn("password");
        when(mockResultSet.getString("token")).thenReturn("token123");
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockDatabaseConnection.getConnection()).thenReturn(mockConnection);

        // Act
        User result = userDAO.findByUsername("testuser");

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("password", result.getPassword());
        assertEquals("token123", result.getToken());
    }

    @Test
    void findByUsername_WhenDatabaseError_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.findByUsername("testuser"));
    }

    @Test
    void save_WhenSuccessful_SavesUser() throws SQLException, DatabaseException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(1L);
        when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockPreparedStatement);

        // Act
        userDAO.save(testUser);

        // Assert
        verify(mockPreparedStatement).setString(1, "testuser");
        verify(mockPreparedStatement).setString(2, "password");
        verify(mockPreparedStatement).setString(3, "token123");
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    void save_WhenDatabaseError_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString(), anyInt()))
            .thenThrow(new SQLException("Database error"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.save(testUser));
    }

    @Test
    void update_WhenSuccessful_UpdatesUser() throws SQLException, DatabaseException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Act
        userDAO.update(testUser);

        // Assert
        verify(mockPreparedStatement).setString(1, "testuser");
        verify(mockPreparedStatement).setString(2, "password");
        verify(mockPreparedStatement).setString(3, "token123");
        verify(mockPreparedStatement).setLong(4, 1L);
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    void update_WhenDatabaseError_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString()))
            .thenThrow(new SQLException("Database error"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.update(testUser));
    }

    @Test
    void delete_WhenSuccessful_DeletesUser() throws SQLException, DatabaseException {
        // Arrange
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Act
        userDAO.delete(testUser);

        // Assert
        verify(mockPreparedStatement).setLong(1, 1L);
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    void delete_WhenDatabaseError_ThrowsDatabaseException() throws SQLException {
        // Arrange
        when(mockConnection.prepareStatement(anyString()))
            .thenThrow(new SQLException("Database error"));

        // Act & Assert
        assertThrows(DatabaseException.class, () -> userDAO.delete(testUser));
    }
} 