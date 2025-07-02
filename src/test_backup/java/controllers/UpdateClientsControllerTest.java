package controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Client;
import models.Adresse;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import services.ClientService;
import utilities.Security;

import java.io.IOException;

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
    private RequestDispatcher requestDispatcher;

    @Mock
    private ClientService clientService;

    private UpdateClientsController controller;

    @BeforeEach
    void setUp() {
        controller = new UpdateClientsController();
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    @DisplayName("doPost devrait rediriger vers signin si l'utilisateur n'est pas connecté")
    void doPostShouldRedirectToSigninWhenUserNotConnected() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(null);

        // Act
        controller.doPost(request, response);

        // Assert
        verify(response).sendRedirect(request.getContextPath() + "/signin");
    }

    @Test
    @DisplayName("doPost devrait mettre à jour le client avec succès")
    void doPostShouldUpdateClientSuccessfully() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(new User());
        when(request.getParameter("clientId")).thenReturn("1");
        when(request.getParameter("raisonSociale")).thenReturn("Test Company");
        when(request.getParameter("telephone")).thenReturn("0123456789");
        when(request.getParameter("email")).thenReturn("test@example.com");
        when(request.getParameter("commentaires")).thenReturn("Test comments");
        when(request.getParameter("chiffreAffaires")).thenReturn("100000.0");
        when(request.getParameter("nombreEmployes")).thenReturn("50");
        when(request.getParameter("numeroRue")).thenReturn("123");
        when(request.getParameter("nomRue")).thenReturn("Test Street");
        when(request.getParameter("codePostal")).thenReturn("75000");
        when(request.getParameter("ville")).thenReturn("Paris");
        when(request.getParameter("pays")).thenReturn("France");

        // Act
        controller.doPost(request, response);

        // Assert
        verify(session).setAttribute("successMessage", "Client mis à jour avec succès");
        verify(response).sendRedirect(request.getContextPath() + "/clients");
    }

    @Test
    @DisplayName("doPost devrait gérer les erreurs lors de la mise à jour du client")
    void doPostShouldHandleErrorsWhenUpdatingClient() throws ServletException, IOException {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("currentUser")).thenReturn(new User());
        when(request.getParameter("clientId")).thenReturn("invalid");

        // Act
        controller.doPost(request, response);

        // Assert
        verify(session).setAttribute("errorMessage", "Erreur lors de la mise à jour du client");
        verify(response).sendRedirect(request.getContextPath() + "/clients");
    }
} 