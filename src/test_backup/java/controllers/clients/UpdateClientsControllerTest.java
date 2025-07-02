package controllers.clients;

import builders.AdresseBuilder;
import builders.ClientBuilder;
import controllers.ICommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateClientsControllerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private ClientService clientService;

    private UpdateClientsController controller;

    @BeforeEach
    void setUp() {
        controller = new UpdateClientsController(clientService);
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
        assertEquals("clients/view.jsp", result);
    }

    @Test
    @DisplayName("execute devrait afficher le formulaire de mise à jour pour un client existant")
    void executeShouldDisplayUpdateFormForExistingClient() throws Exception {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(new User());
        when(request.getParameter("clientId")).thenReturn("1");
        Client client = new Client();
        client.setIdentifiant(1L);
        when(clientService.findById(1L)).thenReturn(Optional.of(client));

        // Act
        String result = controller.execute(request, response);

        // Assert
        assertEquals("clients/view.jsp", result);
        verify(request).setAttribute("titlePage", "Mise à jour");
        verify(request).setAttribute("titleGroup", "Clients");
        verify(request).setAttribute("client", client);
    }

    @Test
    @DisplayName("execute devrait mettre à jour le client et rediriger vers la liste des clients")
    void executeShouldUpdateClientAndRedirectToList() throws Exception {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(new User());
        when(request.getParameter("clientId")).thenReturn("1");
        Client client = new Client();
        client.setIdentifiant(1L);
        when(clientService.findById(1L)).thenReturn(Optional.of(client));

        // Mock request parameters for update
        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("raisonSociale", new String[]{"Test Company"});
        parameterMap.put("identifiant", new String[]{"1"});
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
        verify(clientService).update(any(Client.class));
    }

    @Test
    @DisplayName("execute devrait gérer les violations de contraintes")
    void executeShouldHandleConstraintViolations() throws Exception {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(new User());
        when(request.getParameter("clientId")).thenReturn("1");
        Client client = new Client();
        client.setIdentifiant(1L);
        when(clientService.findById(1L)).thenReturn(Optional.of(client));

        // Mock request parameters with invalid data
        Map<String, String[]> parameterMap = new HashMap<>();
        parameterMap.put("raisonSociale", new String[]{""}); // Invalid empty name
        parameterMap.put("identifiant", new String[]{"1"});
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
        assertEquals("clients/view.jsp", result);
        verify(request).setAttribute(eq("violations"), any(Set.class));
        verify(clientService, never()).update(any());
    }
} 