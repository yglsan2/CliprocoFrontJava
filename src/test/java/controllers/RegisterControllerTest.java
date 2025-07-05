package controllers;

import models.Client;
import models.Prospect;
import models.Adresse;
import services.ClientService;
import services.ProspectService;
import utilities.Security;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.InjectMocks;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour RegisterController
 */
@DisplayName("Tests unitaires pour RegisterController")
class RegisterControllerTest {

    @Mock
    private HttpServletRequest mockRequest;
    
    @Mock
    private HttpServletResponse mockResponse;
    
    @Mock
    private HttpSession mockSession;
    
    @Mock
    private RequestDispatcher mockDispatcher;
    
    @Mock
    private ClientService mockClientService;
    
    @Mock
    private ProspectService mockProspectService;

    @InjectMocks
    private RegisterController registerController;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        registerController = new RegisterController();
        
        // Injection des mocks des services via réflexion
        Field clientServiceField = RegisterController.class.getDeclaredField("clientService");
        clientServiceField.setAccessible(true);
        clientServiceField.set(registerController, mockClientService);
        
        Field prospectServiceField = RegisterController.class.getDeclaredField("prospectService");
        prospectServiceField.setAccessible(true);
        prospectServiceField.set(registerController, mockProspectService);
        
        // Configuration des mocks de base
        when(mockRequest.getSession(false)).thenReturn(mockSession);
        when(mockRequest.getRequestDispatcher(anyString())).thenReturn(mockDispatcher);
        when(mockSession.getAttribute("csrfToken")).thenReturn("validToken");
    }

    @Test
    @DisplayName("doGet doit rediriger vers la page d'inscription")
    void testDoGet() throws Exception {
        // Arrange
        when(mockRequest.getRequestDispatcher("/WEB-INF/jsp/signin.jsp")).thenReturn(mockDispatcher);
        
        // Act
        registerController.doGet(mockRequest, mockResponse);
        
        // Assert
        verify(mockRequest).getRequestDispatcher("/WEB-INF/jsp/signin.jsp");
        verify(mockDispatcher).forward(mockRequest, mockResponse);
    }

    @Test
    @DisplayName("doPost doit créer un client avec succès")
    void testDoPostClientRegistrationSuccess() throws Exception {
        // Arrange
        when(mockRequest.getParameter("csrfToken")).thenReturn("validToken");
        when(mockRequest.getParameter("userType")).thenReturn("client");
        when(mockRequest.getParameter("username")).thenReturn("testuser");
        when(mockRequest.getParameter("email")).thenReturn("test@example.com");
        when(mockRequest.getParameter("password")).thenReturn("password123");
        when(mockRequest.getParameter("nom")).thenReturn("Test");
        when(mockRequest.getParameter("prenom")).thenReturn("User");
        when(mockRequest.getParameter("telephone")).thenReturn("0123456789");
        when(mockRequest.getParameter("adresse")).thenReturn("123 Rue Test");
        when(mockRequest.getParameter("codePostal")).thenReturn("75001");
        when(mockRequest.getParameter("ville")).thenReturn("Paris");
        when(mockRequest.getParameter("raisonSociale")).thenReturn("Test Company");
        when(mockRequest.getParameter("chiffreAffaire")).thenReturn("100000");
        when(mockRequest.getParameter("nbrEmploye")).thenReturn("10");
        
        // Pas besoin de mocker explicitement create() : le mock ne fait rien par défaut
        
        try (MockedStatic<Security> mockedSecurity = Mockito.mockStatic(Security.class)) {
            mockedSecurity.when(() -> Security.getCSRFToken(mockSession)).thenReturn("validToken");
            mockedSecurity.when(() -> Security.verifyCSRFToken("validToken", "validToken")).thenReturn(true);
            
            // Act
            registerController.doPost(mockRequest, mockResponse);
            
            // Assert
            verify(mockRequest).setAttribute("success", "Compte créé avec succès. Vous pouvez vous connecter.");
            verify(mockRequest).getRequestDispatcher("/WEB-INF/jsp/connexion.jsp");
            verify(mockDispatcher).forward(mockRequest, mockResponse);
            verify(mockClientService).create(any(Client.class));
        }
    }

    @Test
    @DisplayName("doPost doit créer un prospect avec succès")
    void testDoPostProspectRegistrationSuccess() throws Exception {
        // Arrange
        when(mockRequest.getParameter("csrfToken")).thenReturn("validToken");
        when(mockRequest.getParameter("userType")).thenReturn("prospect");
        when(mockRequest.getParameter("username")).thenReturn("testuser");
        when(mockRequest.getParameter("email")).thenReturn("test@example.com");
        when(mockRequest.getParameter("password")).thenReturn("password123");
        when(mockRequest.getParameter("nom")).thenReturn("Test");
        when(mockRequest.getParameter("prenom")).thenReturn("User");
        when(mockRequest.getParameter("telephone")).thenReturn("0123456789");
        when(mockRequest.getParameter("adresse")).thenReturn("123 Rue Test");
        when(mockRequest.getParameter("codePostal")).thenReturn("75001");
        when(mockRequest.getParameter("ville")).thenReturn("Paris");
        when(mockRequest.getParameter("raisonSociale")).thenReturn("Test Company");
        when(mockRequest.getParameter("dateProspection")).thenReturn("2024-01-01");
        when(mockRequest.getParameter("prospectInteresse")).thenReturn("true");
        
        // Pas besoin de mocker explicitement create() : le mock ne fait rien par défaut
        
        try (MockedStatic<Security> mockedSecurity = Mockito.mockStatic(Security.class)) {
            mockedSecurity.when(() -> Security.getCSRFToken(mockSession)).thenReturn("validToken");
            mockedSecurity.when(() -> Security.verifyCSRFToken("validToken", "validToken")).thenReturn(true);
            
            // Act
            registerController.doPost(mockRequest, mockResponse);
            
            // Assert
            verify(mockRequest).setAttribute("success", "Compte créé avec succès. Vous pouvez vous connecter.");
            verify(mockRequest).getRequestDispatcher("/WEB-INF/jsp/connexion.jsp");
            verify(mockDispatcher).forward(mockRequest, mockResponse);
            verify(mockProspectService).create(any(Prospect.class));
        }
    }

    @Test
    @DisplayName("doPost doit gérer les données invalides")
    void testDoPostInvalidData() throws Exception {
        // Arrange
        when(mockRequest.getParameter("csrfToken")).thenReturn("validToken");
        when(mockRequest.getParameter("userType")).thenReturn("client");
        when(mockRequest.getParameter("username")).thenReturn("testuser");
        when(mockRequest.getParameter("email")).thenReturn("invalid-email");
        when(mockRequest.getParameter("password")).thenReturn("password123");
        when(mockRequest.getParameter("nom")).thenReturn("Test");
        when(mockRequest.getParameter("prenom")).thenReturn("User");
        when(mockRequest.getParameter("telephone")).thenReturn("0123456789");
        when(mockRequest.getParameter("adresse")).thenReturn("123 Rue Test");
        when(mockRequest.getParameter("codePostal")).thenReturn("75001");
        when(mockRequest.getParameter("ville")).thenReturn("Paris");
        when(mockRequest.getParameter("raisonSociale")).thenReturn("Test Company");
        when(mockRequest.getParameter("chiffreAffaire")).thenReturn("100000");
        when(mockRequest.getParameter("nbrEmploye")).thenReturn("10");
        
        try (MockedStatic<Security> mockedSecurity = Mockito.mockStatic(Security.class)) {
            mockedSecurity.when(() -> Security.getCSRFToken(mockSession)).thenReturn("validToken");
            mockedSecurity.when(() -> Security.verifyCSRFToken("validToken", "validToken")).thenReturn(true);
            
            // Act
            registerController.doPost(mockRequest, mockResponse);
            
            // Assert
            verify(mockRequest).setAttribute("errorValidation", "L'adresse email n'est pas valide");
            verify(mockRequest).getRequestDispatcher("/WEB-INF/jsp/signin.jsp");
            verify(mockDispatcher).forward(mockRequest, mockResponse);
        }
    }

    @Test
    @DisplayName("doPost doit gérer le type d'utilisateur invalide")
    void testDoPostInvalidUserType() throws Exception {
        // Arrange
        when(mockRequest.getParameter("csrfToken")).thenReturn("validToken");
        when(mockRequest.getParameter("userType")).thenReturn("invalid");
        when(mockRequest.getParameter("username")).thenReturn("testuser");
        when(mockRequest.getParameter("email")).thenReturn("test@example.com");
        when(mockRequest.getParameter("password")).thenReturn("password123");
        when(mockRequest.getParameter("nom")).thenReturn("Test");
        when(mockRequest.getParameter("prenom")).thenReturn("User");
        when(mockRequest.getParameter("telephone")).thenReturn("0123456789");
        when(mockRequest.getParameter("adresse")).thenReturn("123 Rue Test");
        when(mockRequest.getParameter("codePostal")).thenReturn("75001");
        when(mockRequest.getParameter("ville")).thenReturn("Paris");
        when(mockRequest.getParameter("raisonSociale")).thenReturn("Test Company");
        when(mockRequest.getParameter("dateProspection")).thenReturn("2024-01-01");
        when(mockRequest.getParameter("prospectInteresse")).thenReturn("true");
        
        // Mock des services
        doNothing().when(mockProspectService).create(any(Prospect.class));
        
        try (MockedStatic<Security> mockedSecurity = Mockito.mockStatic(Security.class)) {
            mockedSecurity.when(() -> Security.getCSRFToken(mockSession)).thenReturn("validToken");
            mockedSecurity.when(() -> Security.verifyCSRFToken("validToken", "validToken")).thenReturn(true);
            
            // Act
            registerController.doPost(mockRequest, mockResponse);
            
            // Assert
            // Le contrôleur traite tout ce qui n'est pas "client" comme un prospect
            verify(mockRequest).setAttribute("success", "Compte créé avec succès. Vous pouvez vous connecter.");
            verify(mockRequest).getRequestDispatcher("/WEB-INF/jsp/connexion.jsp");
            verify(mockDispatcher).forward(mockRequest, mockResponse);
            verify(mockProspectService).create(any(Prospect.class));
        }
    }

    @Test
    @DisplayName("doPost doit gérer les exceptions de validation")
    void testDoPostValidationException() throws Exception {
        // Arrange
        when(mockRequest.getParameter("csrfToken")).thenReturn("validToken");
        when(mockRequest.getParameter("userType")).thenReturn("client");
        when(mockRequest.getParameter("username")).thenReturn("testuser");
        when(mockRequest.getParameter("email")).thenReturn("invalid-email");
        when(mockRequest.getParameter("password")).thenReturn("password123");
        when(mockRequest.getParameter("nom")).thenReturn("Test");
        when(mockRequest.getParameter("prenom")).thenReturn("User");
        when(mockRequest.getParameter("telephone")).thenReturn("0123456789");
        when(mockRequest.getParameter("adresse")).thenReturn("123 Rue Test");
        when(mockRequest.getParameter("codePostal")).thenReturn("75001");
        when(mockRequest.getParameter("ville")).thenReturn("Paris");
        when(mockRequest.getParameter("raisonSociale")).thenReturn("Test Company");
        when(mockRequest.getParameter("chiffreAffaire")).thenReturn("100000");
        when(mockRequest.getParameter("nbrEmploye")).thenReturn("10");
        
        try (MockedStatic<Security> mockedSecurity = Mockito.mockStatic(Security.class)) {
            mockedSecurity.when(() -> Security.getCSRFToken(mockSession)).thenReturn("validToken");
            mockedSecurity.when(() -> Security.verifyCSRFToken("validToken", "validToken")).thenReturn(true);
            
            // Act
            registerController.doPost(mockRequest, mockResponse);
            
            // Assert
            verify(mockRequest).setAttribute("errorValidation", "L'adresse email n'est pas valide");
            verify(mockRequest).getRequestDispatcher("/WEB-INF/jsp/signin.jsp");
            verify(mockDispatcher).forward(mockRequest, mockResponse);
        }
    }

    @Test
    @DisplayName("doPost doit gérer les exceptions de format numérique")
    void testDoPostNumberFormatException() throws Exception {
        // Arrange
        when(mockRequest.getParameter("csrfToken")).thenReturn("validToken");
        when(mockRequest.getParameter("userType")).thenReturn("client");
        when(mockRequest.getParameter("username")).thenReturn("testuser");
        when(mockRequest.getParameter("email")).thenReturn("test@example.com");
        when(mockRequest.getParameter("password")).thenReturn("password123");
        when(mockRequest.getParameter("nom")).thenReturn("Test");
        when(mockRequest.getParameter("prenom")).thenReturn("User");
        when(mockRequest.getParameter("telephone")).thenReturn("0123456789");
        when(mockRequest.getParameter("adresse")).thenReturn("123 Rue Test");
        when(mockRequest.getParameter("codePostal")).thenReturn("75001");
        when(mockRequest.getParameter("ville")).thenReturn("Paris");
        when(mockRequest.getParameter("raisonSociale")).thenReturn("Test Company");
        when(mockRequest.getParameter("chiffreAffaire")).thenReturn("invalid-number");
        when(mockRequest.getParameter("nbrEmploye")).thenReturn("10");
        
        try (MockedStatic<Security> mockedSecurity = Mockito.mockStatic(Security.class)) {
            mockedSecurity.when(() -> Security.getCSRFToken(mockSession)).thenReturn("validToken");
            mockedSecurity.when(() -> Security.verifyCSRFToken("validToken", "validToken")).thenReturn(true);
            
            // Act
            registerController.doPost(mockRequest, mockResponse);
            
            // Assert
            verify(mockRequest).setAttribute(eq("errorFormat"), contains("Format numérique invalide"));
            verify(mockRequest).getRequestDispatcher("/WEB-INF/jsp/signin.jsp");
            verify(mockDispatcher).forward(mockRequest, mockResponse);
        }
    }

    @Test
    @DisplayName("doPost doit gérer les paramètres numériques invalides")
    void testDoPostInvalidNumericParameters() throws Exception {
        // Arrange
        when(mockRequest.getParameter("csrfToken")).thenReturn("validToken");
        when(mockRequest.getParameter("userType")).thenReturn("client");
        when(mockRequest.getParameter("username")).thenReturn("testuser");
        when(mockRequest.getParameter("email")).thenReturn("test@example.com");
        when(mockRequest.getParameter("password")).thenReturn("password123");
        when(mockRequest.getParameter("nom")).thenReturn("Test");
        when(mockRequest.getParameter("prenom")).thenReturn("User");
        when(mockRequest.getParameter("telephone")).thenReturn("0123456789");
        when(mockRequest.getParameter("adresse")).thenReturn("123 Rue Test");
        when(mockRequest.getParameter("codePostal")).thenReturn("75001");
        when(mockRequest.getParameter("ville")).thenReturn("Paris");
        when(mockRequest.getParameter("raisonSociale")).thenReturn("Test Company");
        when(mockRequest.getParameter("chiffreAffaire")).thenReturn("100000");
        when(mockRequest.getParameter("nbrEmploye")).thenReturn("invalid");
        
        try (MockedStatic<Security> mockedSecurity = Mockito.mockStatic(Security.class)) {
            mockedSecurity.when(() -> Security.getCSRFToken(mockSession)).thenReturn("validToken");
            mockedSecurity.when(() -> Security.verifyCSRFToken("validToken", "validToken")).thenReturn(true);
            
            // Act
            registerController.doPost(mockRequest, mockResponse);
            
            // Assert
            verify(mockRequest).setAttribute(eq("errorFormat"), contains("Format numérique invalide"));
            verify(mockRequest).getRequestDispatcher("/WEB-INF/jsp/signin.jsp");
            verify(mockDispatcher).forward(mockRequest, mockResponse);
        }
    }

    @Test
    @DisplayName("doPost doit valider le format d'email")
    void testValidateEmailFormat() throws Exception {
        // Arrange
        when(mockRequest.getParameter("csrfToken")).thenReturn("validToken");
        when(mockRequest.getParameter("userType")).thenReturn("client");
        when(mockRequest.getParameter("username")).thenReturn("testuser");
        when(mockRequest.getParameter("email")).thenReturn("invalid-email");
        when(mockRequest.getParameter("password")).thenReturn("password123");
        when(mockRequest.getParameter("nom")).thenReturn("Test");
        when(mockRequest.getParameter("prenom")).thenReturn("User");
        when(mockRequest.getParameter("telephone")).thenReturn("0123456789");
        when(mockRequest.getParameter("adresse")).thenReturn("123 Rue Test");
        when(mockRequest.getParameter("codePostal")).thenReturn("75001");
        when(mockRequest.getParameter("ville")).thenReturn("Paris");
        when(mockRequest.getParameter("raisonSociale")).thenReturn("Test Company");
        when(mockRequest.getParameter("chiffreAffaire")).thenReturn("100000");
        when(mockRequest.getParameter("nbrEmploye")).thenReturn("10");
        
        try (MockedStatic<Security> mockedSecurity = Mockito.mockStatic(Security.class)) {
            mockedSecurity.when(() -> Security.getCSRFToken(mockSession)).thenReturn("validToken");
            mockedSecurity.when(() -> Security.verifyCSRFToken("validToken", "validToken")).thenReturn(true);
            
            // Act
            registerController.doPost(mockRequest, mockResponse);
            
            // Assert
            verify(mockRequest).setAttribute("errorValidation", "L'adresse email n'est pas valide");
            verify(mockRequest).getRequestDispatcher("/WEB-INF/jsp/signin.jsp");
            verify(mockDispatcher).forward(mockRequest, mockResponse);
        }
    }

    @Test
    @DisplayName("doPost doit valider le format de téléphone")
    void testValidatePhoneFormat() throws Exception {
        // Arrange
        when(mockRequest.getParameter("csrfToken")).thenReturn("validToken");
        when(mockRequest.getParameter("userType")).thenReturn("client");
        when(mockRequest.getParameter("username")).thenReturn("testuser");
        when(mockRequest.getParameter("email")).thenReturn("test@example.com");
        when(mockRequest.getParameter("password")).thenReturn("password123");
        when(mockRequest.getParameter("nom")).thenReturn("Test");
        when(mockRequest.getParameter("prenom")).thenReturn("User");
        when(mockRequest.getParameter("telephone")).thenReturn("invalid");
        when(mockRequest.getParameter("adresse")).thenReturn("123 Rue Test");
        when(mockRequest.getParameter("codePostal")).thenReturn("75001");
        when(mockRequest.getParameter("ville")).thenReturn("Paris");
        when(mockRequest.getParameter("raisonSociale")).thenReturn("Test Company");
        when(mockRequest.getParameter("chiffreAffaire")).thenReturn("100000");
        when(mockRequest.getParameter("nbrEmploye")).thenReturn("10");
        
        try (MockedStatic<Security> mockedSecurity = Mockito.mockStatic(Security.class)) {
            mockedSecurity.when(() -> Security.getCSRFToken(mockSession)).thenReturn("validToken");
            mockedSecurity.when(() -> Security.verifyCSRFToken("validToken", "validToken")).thenReturn(true);
            
            // Act
            registerController.doPost(mockRequest, mockResponse);
            
            // Assert
            verify(mockRequest).setAttribute(eq("errorValidation"), eq("Le numéro de téléphone n'est pas valide. Exemple : 0612345678, +33612345678, 0033612345678"));
            verify(mockRequest).getRequestDispatcher("/WEB-INF/jsp/signin.jsp");
            verify(mockDispatcher).forward(mockRequest, mockResponse);
        }
    }

    @Test
    @DisplayName("doPost doit gérer les exceptions inattendues")
    void testDoPostUnexpectedException() throws Exception {
        // Arrange
        when(mockRequest.getParameter("csrfToken")).thenReturn("validToken");
        when(mockRequest.getParameter("userType")).thenReturn("client");
        when(mockRequest.getParameter("username")).thenReturn("testuser");
        when(mockRequest.getParameter("email")).thenReturn("test@example.com");
        when(mockRequest.getParameter("password")).thenReturn("password123");
        when(mockRequest.getParameter("nom")).thenReturn("Test");
        when(mockRequest.getParameter("prenom")).thenReturn("User");
        when(mockRequest.getParameter("telephone")).thenReturn("0123456789");
        when(mockRequest.getParameter("adresse")).thenReturn("123 Rue Test");
        when(mockRequest.getParameter("codePostal")).thenReturn("75001");
        when(mockRequest.getParameter("ville")).thenReturn("Paris");
        when(mockRequest.getParameter("raisonSociale")).thenReturn("Test Company");
        when(mockRequest.getParameter("chiffreAffaire")).thenReturn("100000");
        when(mockRequest.getParameter("nbrEmploye")).thenReturn("10");
        
        // Mock des services pour lever une exception
        doThrow(new RuntimeException("Erreur de base de données")).when(mockClientService).create(any(Client.class));
        
        try (MockedStatic<Security> mockedSecurity = Mockito.mockStatic(Security.class)) {
            mockedSecurity.when(() -> Security.getCSRFToken(mockSession)).thenReturn("validToken");
            mockedSecurity.when(() -> Security.verifyCSRFToken("validToken", "validToken")).thenReturn(true);
            
            // Act
            registerController.doPost(mockRequest, mockResponse);
            
            // Assert
            verify(mockRequest).setAttribute("errorGlobal", "Une erreur inattendue est survenue. Merci de réessayer.");
            verify(mockRequest).getRequestDispatcher("/WEB-INF/jsp/signin.jsp");
            verify(mockDispatcher).forward(mockRequest, mockResponse);
        }
    }
} 