package utilities;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Test
    @DisplayName("estConnecte devrait retourner index.jsp quand l'utilisateur n'est pas connecté")
    void estConnecteShouldReturnIndexJspWhenUserNotConnected() {
        // Arrange
        when(request.getSession(false)).thenReturn(null);

        // Act
        String result = Security.estConnecte(request, "test.jsp");

        // Assert
        assertEquals("index.jsp", result);
    }

    @Test
    @DisplayName("estConnecte devrait retourner index.jsp quand la session n'a pas d'utilisateur")
    void estConnecteShouldReturnIndexJspWhenSessionHasNoUser() {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(null);

        // Act
        String result = Security.estConnecte(request, "test.jsp");

        // Assert
        assertEquals("index.jsp", result);
    }

    @Test
    @DisplayName("estConnecte devrait retourner la JSP fournie quand l'utilisateur est connecté")
    void estConnecteShouldReturnProvidedJspWhenUserConnected() {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(new Object());

        // Act
        String result = Security.estConnecte(request, "test.jsp");

        // Assert
        assertEquals("test.jsp", result);
    }

    @Test
    @DisplayName("hashPassword devrait retourner un hash différent pour chaque appel")
    void hashPasswordShouldReturnDifferentHashForEachCall() {
        // Arrange
        String password = "testPassword";

        // Act
        String hash1 = Security.hashPassword(password);
        String hash2 = Security.hashPassword(password);

        // Assert
        assertNotNull(hash1L);
        assertNotNull(hash2);
        assertNotEquals(hash1L, hash2);
    }

    @Test
    @DisplayName("verifyPassword devrait retourner true pour un mot de passe valide")
    void verifyPasswordShouldReturnTrueForValidPassword() {
        // Arrange
        String password = "testPassword";
        String hash = Security.hashPassword(password);

        // Act
        boolean result = Security.verifyPassword(password, hash);

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("verifyPassword devrait retourner false pour un mot de passe invalide")
    void verifyPasswordShouldReturnFalseForInvalidPassword() {
        // Arrange
        String password = "testPassword";
        String hash = Security.hashPassword(password);

        // Act
        boolean result = Security.verifyPassword("wrongPassword", hash);

        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("verifyPassword devrait retourner false pour un hash invalide")
    void verifyPasswordShouldReturnFalseForInvalidHash() {
        // Arrange
        String password = "testPassword";
        String invalidHash = "invalidHash";

        // Act
        boolean result = Security.verifyPassword(password, invalidHash);

        // Assert
        assertFalse(result);
    }
} 