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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteProspectsControllerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private ProspectService prospectService;

    private DeleteProspectsController controller;

    @BeforeEach
    void setUp() {
        controller = new DeleteProspectsController(prospectService);
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
        assertEquals("prospects/delete.jsp", result);
    }

    @Test
    @DisplayName("execute devrait supprimer le prospect et rediriger vers la liste des prospects")
    void executeShouldDeleteProspectAndRedirectToList() throws Exception {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(new User());
        when(request.getParameter("id")).thenReturn("1");
        Prospect prospect = new Prospect();
        prospect.setIdentifiant(1L);
        when(prospectService.findById(1L)).thenReturn(prospect);

        // Act
        String result = controller.execute(request, response);

        // Assert
        assertEquals("/prospects", result);
        verify(prospectService).delete(prospect);
    }

    @Test
    @DisplayName("execute devrait gérer le cas où le prospect n'existe pas")
    void executeShouldHandleNonExistentProspect() throws Exception {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(new User());
        when(request.getParameter("id")).thenReturn("1");
        when(prospectService.findById(1L)).thenReturn(null);

        // Act
        String result = controller.execute(request, response);

        // Assert
        assertEquals("prospects/delete.jsp", result);
        verify(prospectService, never()).delete(any());
    }
} 