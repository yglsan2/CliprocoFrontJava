package controllers.prospects;

import controllers.ICommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Prospect;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import services.ProspectService;
import utilities.Security;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListeProspectsControllerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private ProspectService prospectService;

    private ListeProspectsController controller;

    @BeforeEach
    void setUp() {
        controller = new ListeProspectsController(prospectService);
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
        assertEquals("index.jsp", result);
    }

    @Test
    @DisplayName("execute devrait afficher la liste des prospects pour un utilisateur connecté")
    void executeShouldDisplayProspectListForConnectedUser() throws Exception {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(new User());
        List<Prospect> prospects = Arrays.asList(new Prospect(), new Prospect());
        when(prospectService.findAll()).thenReturn(prospects);

        // Act
        String result = controller.execute(request, response);

        // Assert
        assertEquals("prospects/list.jsp", result);
        verify(request).setAttribute("titlePage", "Liste");
        verify(request).setAttribute("titleGroup", "Prospects");
        verify(request).setAttribute("prospects", prospects);
    }

    @Test
    @DisplayName("execute devrait gérer les erreurs lors de la récupération de la liste des prospects")
    void executeShouldHandleErrorsWhenRetrievingProspectList() throws Exception {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(new User());
        when(prospectService.findAll()).thenThrow(new RuntimeException("Test error"));

        // Act & Assert
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(
            RuntimeException.class,
            () -> controller.execute(request, response)
        );
        assertEquals("Test error", exception.getMessage());
    }
} 