package controllers.prospects;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Prospect;
import models.Adresse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import services.ProspectService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreationProspectsControllerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private ProspectService prospectService;

    private CreationProspectsController controller;

    @BeforeEach
    void setUp() {
        controller = new CreationProspectsController(prospectService);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(new Object());
    }

    @Test
    @DisplayName("execute devrait créer un prospect avec succès")
    void executeShouldCreateProspectSuccessfully() throws Exception {
        // Arrange
        when(request.getParameter("numeroRue")).thenReturn("123");
        when(request.getParameter("nomRue")).thenReturn("Rue Test");
        when(request.getParameter("codePostal")).thenReturn("75000");
        when(request.getParameter("ville")).thenReturn("Paris");
        when(request.getParameter("raisonSociale")).thenReturn("Test Company");
        when(request.getParameter("telephone")).thenReturn("0123456789");
        when(request.getParameter("mail")).thenReturn("test@test.com");
        when(request.getParameter("commentaires")).thenReturn("Test comments");
        when(request.getParameter("dateProspection")).thenReturn("2024-03-20");
        when(request.getParameter("prospectInteresse")).thenReturn("Oui");

        // Act
        String result = controller.execute(request, response);

        // Assert
        assertEquals("/prospects", result);
        verify(prospectService).save(any(Prospect.class));
        verify(request).setAttribute("titlePage", "Création");
        verify(request).setAttribute("titleGroup", "Prospects");
    }

    @Test
    @DisplayName("execute sans utilisateur connecté devrait retourner la page de création")
    void executeDeoutLoggedUserShouldReturnCreatePage() throws Exception {
        // Arrange
        when(session.getAttribute("user")).thenReturn(null);

        // Act
        String result = controller.execute(request, response);

        // Assert
        assertEquals("prospects/create.jsp", result);
    }

    @Test
    @DisplayName("execute avec données manquantes devrait retourner la page de création")
    void executeDeMissingDataShouldReturnCreatePage() throws Exception {
        // Arrange
        when(request.getParameter("numeroRue")).thenReturn(null);
        when(request.getParameter("nomRue")).thenReturn(null);
        when(request.getParameter("codePostal")).thenReturn(null);
        when(request.getParameter("ville")).thenReturn(null);
        when(request.getParameter("raisonSociale")).thenReturn(null);
        when(request.getParameter("telephone")).thenReturn(null);
        when(request.getParameter("mail")).thenReturn(null);
        when(request.getParameter("dateProspection")).thenReturn(null);
        when(request.getParameter("prospectInteresse")).thenReturn(null);

        // Act
        String result = controller.execute(request, response);

        // Assert
        assertEquals("prospects/create.jsp", result);
        verify(prospectService, never()).save(any(Prospect.class));
    }
} 