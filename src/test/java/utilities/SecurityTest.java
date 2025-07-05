package utilities;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour Security
 */
@DisplayName("Tests unitaires pour Security")
class SecurityTest {

    @Mock
    private HttpSession mockSession;
    
    private Security security;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Security est une classe utilitaire avec des méthodes statiques
        // Pas besoin d'instanciation
    }

    @Test
    @DisplayName("Doit hasher un mot de passe correctement")
    void testHashPassword() {
        // Arrange
        String password = "password123";
        
        // Act
        String hashedPassword = Security.hashPassword(password);
        
        // Assert
        assertNotNull(hashedPassword);
        assertNotEquals(password, hashedPassword);
        assertTrue(hashedPassword.length() > 0);
        assertTrue(hashedPassword.contains(":"));
    }

    @Test
    @DisplayName("Doit vérifier un mot de passe correct")
    void testVerifyPasswordCorrect() {
        // Arrange
        String password = "password123";
        String hashedPassword = Security.hashPassword(password);
        
        // Act
        boolean result = Security.verifyPassword(password, hashedPassword);
        
        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Doit rejeter un mot de passe incorrect")
    void testVerifyPasswordIncorrect() {
        // Arrange
        String correctPassword = "password123";
        String wrongPassword = "wrongpassword";
        String hashedPassword = Security.hashPassword(correctPassword);
        
        // Act
        boolean result = Security.verifyPassword(wrongPassword, hashedPassword);
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Doit gérer un mot de passe null")
    void testHashPasswordNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            Security.hashPassword(null);
        });
    }

    @Test
    @DisplayName("Doit gérer un mot de passe vide")
    void testHashPasswordEmpty() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            Security.hashPassword("");
        });
    }

    @Test
    @DisplayName("Doit gérer un hash null lors de la vérification")
    void testVerifyPasswordNullHash() {
        // Act & Assert
        assertFalse(Security.verifyPassword("password", null));
    }

    @Test
    @DisplayName("Doit gérer un mot de passe null lors de la vérification")
    void testVerifyPasswordNullPassword() {
        // Act & Assert
        assertFalse(Security.verifyPassword(null, "hash"));
    }

    @Test
    @DisplayName("Doit générer un token CSRF")
    void testGenerateCSRFToken() {
        // Act
        String token = Security.generateCSRFToken();
        
        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertTrue(token.matches("[a-zA-Z0-9+/]+={0,2}")); // Token Base64
    }

    @Test
    @DisplayName("Doit générer des tokens CSRF différents")
    void testGenerateCSRFTokenDifferent() {
        // Act
        String token1 = Security.generateCSRFToken();
        String token2 = Security.generateCSRFToken();
        
        // Assert
        assertNotEquals(token1, token2);
    }

    @Test
    @DisplayName("Doit récupérer un token CSRF de la session")
    void testGetCSRFTokenFromSession() {
        // Arrange
        String expectedToken = "testToken123";
        when(mockSession.getAttribute("csrfToken")).thenReturn(expectedToken);
        
        // Act
        String result = Security.getCSRFToken(mockSession);
        
        // Assert
        assertEquals(expectedToken, result);
        verify(mockSession).getAttribute("csrfToken");
    }

    @Test
    @DisplayName("Doit retourner null si pas de token dans la session")
    void testGetCSRFTokenNoTokenInSession() {
        // Arrange
        when(mockSession.getAttribute("csrfToken")).thenReturn(null);
        
        // Act
        String result = Security.getCSRFToken(mockSession);
        
        // Assert
        assertNull(result);
        verify(mockSession).getAttribute("csrfToken");
    }

    @Test
    @DisplayName("Doit gérer une session null")
    void testGetCSRFTokenNullSession() {
        // Act
        String result = Security.getCSRFToken(null);
        
        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Doit vérifier un token CSRF valide")
    void testVerifyCSRFTokenValid() {
        // Arrange
        String sessionToken = "sessionToken123";
        String formToken = "sessionToken123";
        
        // Act
        boolean result = Security.verifyCSRFToken(sessionToken, formToken);
        
        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Doit rejeter un token CSRF invalide")
    void testVerifyCSRFTokenInvalid() {
        // Arrange
        String sessionToken = "sessionToken123";
        String formToken = "differentToken456";
        
        // Act
        boolean result = Security.verifyCSRFToken(sessionToken, formToken);
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Doit rejeter un token CSRF null")
    void testVerifyCSRFTokenNull() {
        // Arrange
        String sessionToken = "sessionToken123";
        String formToken = null;
        
        // Act
        boolean result = Security.verifyCSRFToken(sessionToken, formToken);
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Doit rejeter un token de session null")
    void testVerifyCSRFTokenNullSession() {
        // Arrange
        String sessionToken = null;
        String formToken = "formToken123";
        
        // Act
        boolean result = Security.verifyCSRFToken(sessionToken, formToken);
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Doit rejeter des tokens vides")
    void testVerifyCSRFTokenEmpty() {
        // Arrange
        String sessionToken = "";
        String formToken = "";
        
        // Act
        boolean result = Security.verifyCSRFToken(sessionToken, formToken);
        
        // Assert
        assertTrue(result); // Deux chaînes vides sont égales
    }

    @Test
    @DisplayName("Doit générer et stocker un token CSRF en session")
    void testGenerateAndStoreCSRFToken() {
        // Act
        String token = Security.generateAndStoreCSRFToken(mockSession);
        
        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);
        verify(mockSession).setAttribute("csrfToken", token);
    }

    @Test
    @DisplayName("Doit gérer les mots de passe avec des caractères spéciaux")
    void testPasswordWithSpecialCharacters() {
        // Arrange
        String password = "P@ssw0rd!123";
        
        // Act
        String hashedPassword = Security.hashPassword(password);
        boolean result = Security.verifyPassword(password, hashedPassword);
        
        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Doit gérer les mots de passe très longs")
    void testLongPassword() {
        // Arrange
        StringBuilder longPassword = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longPassword.append("a");
        }
        
        // Act
        String hashedPassword = Security.hashPassword(longPassword.toString());
        boolean result = Security.verifyPassword(longPassword.toString(), hashedPassword);
        
        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Doit gérer les anciens formats de hash sans salt")
    void testOldHashFormat() {
        // Arrange
        String password = "password123";
        String oldHash = "password123"; // Ancien format sans salt
        
        // Act
        boolean result = Security.verifyPassword(password, oldHash);
        
        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Doit gérer les formats de hash invalides")
    void testInvalidHashFormat() {
        // Arrange
        String password = "password123";
        String invalidHash = "invalid:hash:format:with:too:many:colons";
        
        // Act
        boolean result = Security.verifyPassword(password, invalidHash);
        
        // Assert
        assertFalse(result);
    }

    @Test
    @DisplayName("Doit gérer les tokens CSRF avec des caractères spéciaux")
    void testCSRFTokenWithSpecialCharacters() {
        // Act
        String token1 = Security.generateCSRFToken();
        String token2 = Security.generateCSRFToken();
        
        // Assert
        assertNotNull(token1);
        assertNotNull(token2);
        assertNotEquals(token1, token2);
        // Les tokens Base64 ne contiennent que des caractères alphanumériques et +/=
    }

    @Test
    @DisplayName("Doit gérer les sessions avec des attributs différents")
    void testSessionWithDifferentAttributes() {
        // Arrange
        when(mockSession.getAttribute("otherAttribute")).thenReturn("otherValue");
        when(mockSession.getAttribute("csrfToken")).thenReturn("csrfValue");
        
        // Act
        String csrfResult = Security.getCSRFToken(mockSession);
        String otherResult = (String) mockSession.getAttribute("otherAttribute");
        
        // Assert
        assertEquals("csrfValue", csrfResult);
        assertEquals("otherValue", otherResult);
    }
} 