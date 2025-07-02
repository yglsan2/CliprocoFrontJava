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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteClientsControllerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private ClientService clientService;

    private DeleteClientsController controller;

    @BeforeEach
    void setUp() {
        controller = new DeleteClientsController(clientService);
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
    @DisplayName("execute devrait supprimer le client et rediriger vers la liste des clients")
    void executeShouldDeleteClientAndRedirectToList() throws Exception {
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
        assertEquals("clients/list.jsp", result);
        verify(clientService).delete(1L);
    }

    @Test
    @DisplayName("execute devrait gérer le cas où le client n'existe pas")
    void executeShouldHandleNonExistentClient() throws Exception {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(new User());
        when(request.getParameter("clientId")).thenReturn("1");
        when(clientService.findById(1L)).thenReturn(Optional.empty());

        // Act
        String result = controller.execute(request, response);

        // Assert
        assertEquals("clients/list.jsp", result);
        verify(clientService, never()).delete(any());
    }

    @Test
    @DisplayName("execute devrait gérer les erreurs lors de la suppression")
    void executeShouldHandleDeletionErrors() throws Exception {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(new User());
        when(request.getParameter("clientId")).thenReturn("1");
        Client client = new Client();
        client.setIdentifiant(1L);
        when(clientService.findById(1L)).thenReturn(Optional.of(client));
        doThrow(new RuntimeException("Erreur de suppression")).when(clientService).delete(1L);

        // Act
        String result = controller.execute(request, response);

        // Assert
        assertEquals("clients/list.jsp", result);
        verify(request).setAttribute("errorMessage", "Erreur lors de la suppression du client : Erreur de suppression");
    }
} 