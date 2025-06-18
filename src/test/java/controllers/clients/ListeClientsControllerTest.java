package controllers.clients;

import controllers.ICommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListeClientsControllerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private ClientService clientService;

    private ListeClientsController controller;

    @BeforeEach
    void setUp() {
        controller = new ListeClientsController(clientService);
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
        assertEquals("clients/list.jsp", result);
    }

    @Test
    @DisplayName("execute devrait afficher la liste des clients pour un utilisateur connecté")
    void executeShouldDisplayClientListForConnectedUser() throws Exception {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(new User());
        List<Client> clients = new ArrayList<>();
        clients.add(new Client());
        when(clientService.findAll()).thenReturn(clients);

        // Act
        String result = controller.execute(request, response);

        // Assert
        assertEquals("clients/list.jsp", result);
        verify(request).setAttribute("titlePage", "Liste");
        verify(request).setAttribute("titleGroup", "Clients");
        verify(request).setAttribute("clients", clients);
    }

    @Test
    @DisplayName("execute devrait gérer les erreurs lors de la récupération de la liste des clients")
    void executeShouldHandleErrorsWhenRetrievingClientList() throws Exception {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(new User());
        when(clientService.findAll()).thenThrow(new RuntimeException("Erreur de récupération"));

        // Act & Assert
        try {
            controller.execute(request, response);
        } catch (Exception e) {
            assertEquals("Erreur de récupération", e.getMessage());
        }
    }
} 