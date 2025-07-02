package controllers.prospects;

import controllers.ICommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Adresse;
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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProspectsControllerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private ProspectService prospectService;

    private UpdateProspectsController controller;

    @BeforeEach
    void setUp() {
        controller = new UpdateProspectsController(prospectService);
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
        assertEquals("prospects/update.jsp", result);
        verify(request).setAttribute("titlePage", "Modification");
        verify(request).setAttribute("titleGroup", "Prospects");
    }

    @Test
    @DisplayName("execute devrait mettre à jour le prospect et rediriger vers la liste des prospects")
    void executeShouldUpdateProspectAndRedirectToList() throws Exception {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(new User());
        when(request.getParameter("id")).thenReturn("1");
        Prospect prospect = new Prospect();
        prospect.setIdentifiant(1L);
        when(prospectService.findById(1L)).thenReturn(prospect);

        // Mock request parameters
        when(request.getParameter("numeroRue")).thenReturn("123");
        when(request.getParameter("nomRue")).thenReturn("Rue Test");
        when(request.getParameter("codePostal")).thenReturn("75000");
        when(request.getParameter("ville")).thenReturn("Paris");
        when(request.getParameter("raisonSociale")).thenReturn("Test Company");
        when(request.getParameter("telephone")).thenReturn("0123456789");
        when(request.getParameter("mail")).thenReturn("test@test.com");
        when(request.getParameter("commentaires")).thenReturn("Test comment");
        when(request.getParameter("dateProspection")).thenReturn("2024-03-20");
        when(request.getParameter("prospectInteresse")).thenReturn("Oui");

        // Act
        String result = controller.execute(request, response);

        // Assert
        assertEquals("/prospects", result);
        verify(prospectService).update(any(Prospect.class));
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
        assertEquals("prospects/update.jsp", result);
        verify(prospectService, never()).update(any());
    }
} 