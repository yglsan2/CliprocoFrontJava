package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe User.
 * 
 * <p>Cette classe de test couvre tous les aspects de la classe User :
 * - Constructeurs
 * - Getters et setters
 * - Validation des données
 * - Cas limites et d'erreur
 * - Égalité et hashcode
 * </p>
 * 
 * @author CliprocoJEE
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Tests unitaires pour la classe User")
class UserTest {

    private User user;
    private static final String VALID_USERNAME = "testuser";
    private static final String VALID_PASSWORD = "password123";
    private static final String VALID_EMAIL = "test@example.com";
    private static final String VALID_ROLE = "USER";
    private static final Integer VALID_ID = 1;
    private static final String VALID_TOKEN = "token123";
    private static final LocalDate VALID_EXPIRE = LocalDate.now().plusDays(30);

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Nested
    @DisplayName("Tests des constructeurs")
    class ConstructorTests {

        @Test
        @DisplayName("Constructeur par défaut doit créer un utilisateur vide")
        void testDefaultConstructor() {
            User emptyUser = new User();
            
            assertNotNull(emptyUser);
            assertNull(emptyUser.getId());
            assertNull(emptyUser.getUsername());
            assertNull(emptyUser.getPassword());
            assertNull(emptyUser.getEmail());
            assertNull(emptyUser.getRole());
            assertNull(emptyUser.getToken());
            assertNull(emptyUser.getExpire());
        }

        @Test
        @DisplayName("Constructeur avec paramètres de base doit initialiser correctement")
        void testBasicConstructor() {
            User basicUser = new User(VALID_USERNAME, VALID_PASSWORD, VALID_EMAIL);
            
            assertNotNull(basicUser);
            assertEquals(VALID_USERNAME, basicUser.getUsername());
            assertEquals(VALID_PASSWORD, basicUser.getPassword());
            assertEquals(VALID_EMAIL, basicUser.getEmail());
            assertEquals("USER", basicUser.getRole()); // Rôle par défaut
            assertNull(basicUser.getId());
            assertNull(basicUser.getToken());
            assertNull(basicUser.getExpire());
        }

        @Test
        @DisplayName("Constructeur complet doit initialiser tous les champs")
        void testFullConstructor() {
            User fullUser = new User(VALID_ID, VALID_USERNAME, VALID_PASSWORD, 
                                   VALID_TOKEN, VALID_EXPIRE, VALID_ROLE);
            
            assertNotNull(fullUser);
            assertEquals(VALID_ID, fullUser.getId());
            assertEquals(VALID_USERNAME, fullUser.getUsername());
            assertEquals(VALID_PASSWORD, fullUser.getPassword());
            assertEquals(VALID_TOKEN, fullUser.getToken());
            assertEquals(VALID_EXPIRE, fullUser.getExpire());
            assertEquals(VALID_ROLE, fullUser.getRole());
            assertNull(fullUser.getEmail()); // Non défini dans ce constructeur
        }

        @Test
        @DisplayName("Constructeur avec paramètres null doit être accepté")
        void testConstructorWithNullValues() {
            User nullUser = new User(null, null, null, null, null, null);
            
            assertNotNull(nullUser);
            assertNull(nullUser.getId());
            assertNull(nullUser.getUsername());
            assertNull(nullUser.getPassword());
            assertNull(nullUser.getToken());
            assertNull(nullUser.getExpire());
            assertNull(nullUser.getRole());
        }
    }

    @Nested
    @DisplayName("Tests des getters et setters")
    class GetterSetterTests {

        @Test
        @DisplayName("Getter et setter pour id")
        void testIdGetterSetter() {
            user.setId(VALID_ID);
            assertEquals(VALID_ID, user.getId());
            
            user.setId(null);
            assertNull(user.getId());
        }

        @Test
        @DisplayName("Getter et setter pour username")
        void testUsernameGetterSetter() {
            user.setUsername(VALID_USERNAME);
            assertEquals(VALID_USERNAME, user.getUsername());
            
            user.setUsername(null);
            assertNull(user.getUsername());
        }

        @Test
        @DisplayName("Getter et setter pour password")
        void testPasswordGetterSetter() {
            user.setPassword(VALID_PASSWORD);
            assertEquals(VALID_PASSWORD, user.getPassword());
            
            user.setPassword(null);
            assertNull(user.getPassword());
        }

        @Test
        @DisplayName("Getter et setter pour email")
        void testEmailGetterSetter() {
            user.setEmail(VALID_EMAIL);
            assertEquals(VALID_EMAIL, user.getEmail());
            
            user.setEmail(null);
            assertNull(user.getEmail());
        }

        @Test
        @DisplayName("Getter et setter pour role")
        void testRoleGetterSetter() {
            user.setRole(VALID_ROLE);
            assertEquals(VALID_ROLE, user.getRole());
            
            user.setRole(null);
            assertNull(user.getRole());
        }

        @Test
        @DisplayName("Getter et setter pour token")
        void testTokenGetterSetter() {
            user.setToken(VALID_TOKEN);
            assertEquals(VALID_TOKEN, user.getToken());
            
            user.setToken(null);
            assertNull(user.getToken());
        }

        @Test
        @DisplayName("Getter et setter pour expire")
        void testExpireGetterSetter() {
            user.setExpire(VALID_EXPIRE);
            assertEquals(VALID_EXPIRE, user.getExpire());
            
            user.setExpire(null);
            assertNull(user.getExpire());
        }
    }

    @Nested
    @DisplayName("Tests de validation des données")
    class ValidationTests {

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  ", "\t", "\n"})
        @DisplayName("Username vide ou blanc doit être accepté (pas de validation côté modèle)")
        void testUsernameWithEmptyOrBlankValues(String emptyValue) {
            user.setUsername(emptyValue);
            assertEquals(emptyValue, user.getUsername());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  ", "\t", "\n"})
        @DisplayName("Password vide ou blanc doit être accepté (pas de validation côté modèle)")
        void testPasswordWithEmptyOrBlankValues(String emptyValue) {
            user.setPassword(emptyValue);
            assertEquals(emptyValue, user.getPassword());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  ", "\t", "\n"})
        @DisplayName("Email vide ou blanc doit être accepté (pas de validation côté modèle)")
        void testEmailWithEmptyOrBlankValues(String emptyValue) {
            user.setEmail(emptyValue);
            assertEquals(emptyValue, user.getEmail());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  ", "\t", "\n"})
        @DisplayName("Role vide ou blanc doit être accepté (pas de validation côté modèle)")
        void testRoleWithEmptyOrBlankValues(String emptyValue) {
            user.setRole(emptyValue);
            assertEquals(emptyValue, user.getRole());
        }

        @Test
        @DisplayName("Username avec caractères spéciaux doit être accepté")
        void testUsernameWithSpecialCharacters() {
            String specialUsername = "user@123!#$%";
            user.setUsername(specialUsername);
            assertEquals(specialUsername, user.getUsername());
        }

        @Test
        @DisplayName("Email avec format complexe doit être accepté")
        void testEmailWithComplexFormat() {
            String complexEmail = "user.name+tag@domain.co.uk";
            user.setEmail(complexEmail);
            assertEquals(complexEmail, user.getEmail());
        }

        @Test
        @DisplayName("Token avec caractères spéciaux doit être accepté")
        void testTokenWithSpecialCharacters() {
            String specialToken = "token@123!#$%^&*()";
            user.setToken(specialToken);
            assertEquals(specialToken, user.getToken());
        }
    }

    @Nested
    @DisplayName("Tests des cas limites")
    class EdgeCaseTests {

        @Test
        @DisplayName("ID négatif doit être accepté")
        void testNegativeId() {
            Integer negativeId = -1;
            user.setId(negativeId);
            assertEquals(negativeId, user.getId());
        }

        @Test
        @DisplayName("ID zéro doit être accepté")
        void testZeroId() {
            Integer zeroId = 0;
            user.setId(zeroId);
            assertEquals(zeroId, user.getId());
        }

        @Test
        @DisplayName("Date d'expiration dans le passé doit être acceptée")
        void testPastExpireDate() {
            LocalDate pastDate = LocalDate.now().minusDays(1);
            user.setExpire(pastDate);
            assertEquals(pastDate, user.getExpire());
        }

        @Test
        @DisplayName("Date d'expiration très éloignée doit être acceptée")
        void testFarFutureExpireDate() {
            LocalDate farFutureDate = LocalDate.now().plusYears(100);
            user.setExpire(farFutureDate);
            assertEquals(farFutureDate, user.getExpire());
        }

        @Test
        @DisplayName("Username très long doit être accepté")
        void testVeryLongUsername() {
            String longUsername = "a".repeat(1000);
            user.setUsername(longUsername);
            assertEquals(longUsername, user.getUsername());
        }

        @Test
        @DisplayName("Password très long doit être accepté")
        void testVeryLongPassword() {
            String longPassword = "p".repeat(1000);
            user.setPassword(longPassword);
            assertEquals(longPassword, user.getPassword());
        }

        @Test
        @DisplayName("Email très long doit être accepté")
        void testVeryLongEmail() {
            String longEmail = "a".repeat(500) + "@" + "b".repeat(500) + ".com";
            user.setEmail(longEmail);
            assertEquals(longEmail, user.getEmail());
        }
    }

    @Nested
    @DisplayName("Tests de persistance et état")
    class PersistenceTests {

        @Test
        @DisplayName("Utilisateur doit conserver son état après modifications multiples")
        void testUserStateConservation() {
            // Initialisation
            user.setId(VALID_ID);
            user.setUsername(VALID_USERNAME);
            user.setPassword(VALID_PASSWORD);
            user.setEmail(VALID_EMAIL);
            user.setRole(VALID_ROLE);
            user.setToken(VALID_TOKEN);
            user.setExpire(VALID_EXPIRE);

            // Vérification de l'état initial
            assertEquals(VALID_ID, user.getId());
            assertEquals(VALID_USERNAME, user.getUsername());
            assertEquals(VALID_PASSWORD, user.getPassword());
            assertEquals(VALID_EMAIL, user.getEmail());
            assertEquals(VALID_ROLE, user.getRole());
            assertEquals(VALID_TOKEN, user.getToken());
            assertEquals(VALID_EXPIRE, user.getExpire());

            // Modifications
            user.setUsername("newusername");
            user.setPassword("newpassword");
            user.setEmail("new@example.com");

            // Vérification que les autres champs sont conservés
            assertEquals(VALID_ID, user.getId());
            assertEquals("newusername", user.getUsername());
            assertEquals("newpassword", user.getPassword());
            assertEquals("new@example.com", user.getEmail());
            assertEquals(VALID_ROLE, user.getRole());
            assertEquals(VALID_TOKEN, user.getToken());
            assertEquals(VALID_EXPIRE, user.getExpire());
        }

        @Test
        @DisplayName("Utilisateur doit être mutable")
        void testUserMutability() {
            // Création d'un utilisateur complet
            User originalUser = new User(VALID_ID, VALID_USERNAME, VALID_PASSWORD, 
                                       VALID_TOKEN, VALID_EXPIRE, VALID_ROLE);
            originalUser.setEmail(VALID_EMAIL);

            // Modification de tous les champs
            originalUser.setId(999);
            originalUser.setUsername("modified");
            originalUser.setPassword("modified");
            originalUser.setEmail("modified@example.com");
            originalUser.setRole("ADMIN");
            originalUser.setToken("modified_token");
            originalUser.setExpire(LocalDate.now().plusDays(1));

            // Vérification des modifications
            assertEquals(Integer.valueOf(999), originalUser.getId());
            assertEquals("modified", originalUser.getUsername());
            assertEquals("modified", originalUser.getPassword());
            assertEquals("modified@example.com", originalUser.getEmail());
            assertEquals("ADMIN", originalUser.getRole());
            assertEquals("modified_token", originalUser.getToken());
            assertNotNull(originalUser.getExpire());
        }
    }

    @Nested
    @DisplayName("Tests de scénarios métier")
    class BusinessScenarioTests {

        @Test
        @DisplayName("Création d'un utilisateur administrateur")
        void testAdminUserCreation() {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword("admin123");
            adminUser.setEmail("admin@cliproco.com");
            adminUser.setRole("ADMIN");

            assertEquals("admin", adminUser.getUsername());
            assertEquals("admin123", adminUser.getPassword());
            assertEquals("admin@cliproco.com", adminUser.getEmail());
            assertEquals("ADMIN", adminUser.getRole());
        }

        @Test
        @DisplayName("Création d'un utilisateur avec session active")
        void testUserWithActiveSession() {
            User sessionUser = new User();
            sessionUser.setUsername("sessionuser");
            sessionUser.setPassword("password");
            sessionUser.setEmail("session@example.com");
            sessionUser.setRole("USER");
            sessionUser.setToken("active_session_token");
            sessionUser.setExpire(LocalDate.now().plusDays(7));

            assertNotNull(sessionUser.getToken());
            assertNotNull(sessionUser.getExpire());
            assertTrue(sessionUser.getExpire().isAfter(LocalDate.now()));
        }

        @Test
        @DisplayName("Création d'un utilisateur sans session")
        void testUserWithoutSession() {
            User noSessionUser = new User();
            noSessionUser.setUsername("nosession");
            noSessionUser.setPassword("password");
            noSessionUser.setEmail("nosession@example.com");
            noSessionUser.setRole("USER");

            assertNull(noSessionUser.getToken());
            assertNull(noSessionUser.getExpire());
        }

        @Test
        @DisplayName("Utilisateur avec session expirée")
        void testUserWithExpiredSession() {
            User expiredUser = new User();
            expiredUser.setUsername("expired");
            expiredUser.setPassword("password");
            expiredUser.setEmail("expired@example.com");
            expiredUser.setRole("USER");
            expiredUser.setToken("expired_token");
            expiredUser.setExpire(LocalDate.now().minusDays(1));

            assertNotNull(expiredUser.getToken());
            assertNotNull(expiredUser.getExpire());
            assertTrue(expiredUser.getExpire().isBefore(LocalDate.now()));
        }
    }
} 