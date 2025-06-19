package controllers.clients;

import builders.AdresseBuilder;
import builders.ClientBuilder;
import controllers.ICommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import models.Adresse;
import models.Client;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import services.ClientService;
import utilities.Security;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreationClientsControllerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private ClientService clientService;

    private CreationClientsController controller;

    @BeforeEach
    void setUp() {
        controller = new CreationClientsController(clientService);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    @DisplayName("execute devrait rediriger vers la page de connexion si l'utilisateur n'est pas connecté")
    void executeShouldRedirectToLoginWhenUserNotConnected() throws Exception {
        // Arrange
        when(request.getSession(false)).thenReturn(null);

        // Act
        String result = controller.execute(request, response);

        // Assert
        assertEquals("clients/create.jsp", result);
    }

    @Test
    @DisplayName("execute devrait afficher le formulaire de création pour un utilisateur connecté")
    void executeShouldDisplayCreateFormForConnectedUser() throws Exception {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(new User());
        when(request.getMethod()).thenReturn("GET");

        // Act
        String result = controller.execute(request, response);

        // Assert
        assertEquals("clients/create.jsp", result);
        verify(request).setAttribute("titlePage", "Création");
        verify(request).setAttribute("titleGroup", "Clients");
    }

    @Test
    @DisplayName("execute devrait créer un nouveau client et rediriger vers la liste des clients")
    void executeShouldCreateClientAndRedirectToList() throws Exception {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(new User());
        when(request.getMethod()).thenReturn("POST");

        // Mock request parameters
        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("raisonSociale", new String[]{"Test Company"});
        parameterMap.put("numeroRue", new String[]{"123"});
        parameterMap.put("nomRue", new String[]{"Rue Test"});
        parameterMap.put("codePostal", new String[]{"75000"});
        parameterMap.put("ville", new String[]{"Paris"});
        parameterMap.put("telephone", new String[]{"0123456789"});
        parameterMap.put("adresseMail", new String[]{"test@test.com"});
        parameterMap.put("commentaires", new String[]{"Test comment"});
        parameterMap.put("chiffreAffaires", new String[]{"100000.0"});
        parameterMap.put("nbEmployes", new String[]{"50"});
        when(request.getParameterMap()).thenReturn(parameterMap);

        // Act
        String result = controller.execute(request, response);

        // Assert
        assertEquals("redirect:?cmd=clients", result);
        verify(clientService).create(any(Client.class));
    }

    @Test
    @DisplayName("execute devrait gérer les violations de contraintes")
    void executeShouldHandleConstraintViolations() throws Exception {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(new User());
        when(request.getMethod()).thenReturn("POST");

        // Mock request parameters with invalid data
        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("raisonSociale", new String[]{""}); // Invalid empty name
        parameterMap.put("numeroRue", new String[]{"123"});
        parameterMap.put("nomRue", new String[]{"Rue Test"});
        parameterMap.put("codePostal", new String[]{"75000"});
        parameterMap.put("ville", new String[]{"Paris"});
        parameterMap.put("telephone", new String[]{"0123456789"});
        parameterMap.put("adresseMail", new String[]{"invalid-email"}); // Invalid email
        parameterMap.put("commentaires", new String[]{"Test comment"});
        parameterMap.put("chiffreAffaires", new String[]{"-1000.0"}); // Invalid negative amount
        parameterMap.put("nbEmployes", new String[]{"-10"}); // Invalid negative number
        when(request.getParameterMap()).thenReturn(parameterMap);

        // Act
        String result = controller.execute(request, response);

        // Assert
        assertEquals("clients/create.jsp", result);
        verify(request).setAttribute(eq("violations"), any(Set.class));
        verify(clientService, never()).create(any());
    }
} 