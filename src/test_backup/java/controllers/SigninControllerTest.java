package controllers;

import dao.IDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SigninControllerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private IDAO<User, Integer> userDAO;

    private SigninController controller;

    @BeforeEach
    void setUp() {
        controller = new SigninController();
        when(request.getSession()).thenReturn(session);
    }

    @Test
    @DisplayName("execute devrait afficher le formulaire d'inscription si les paramètres sont manquants")
    void executeShouldDisplaySigninFormWhenParametersAreMissing() throws Exception {
        // Arrange
        when(request.getParameter("email")).thenReturn(null);
        when(request.getParameter("password")).thenReturn(null);
        when(request.getParameter("confirmPassword")).thenReturn(null);

        // Act
        String result = controller.execute(request, response);

        // Assert
        assertEquals("inscription.jsp", result);
    }

    @Test
    @DisplayName("execute devrait afficher une erreur si les mots de passe ne correspondent pas")
    void executeShouldDisplayErrorWhenPasswordsDoNotMatch() throws Exception {
        // Arrange
        when(request.getParameter("email")).thenReturn("test@test.com");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("confirmPassword")).thenReturn("password456");

        // Act
        String result = controller.execute(request, response);

        // Assert
        assertEquals("inscription.jsp", result);
        verify(request).setAttribute("error", "Les mots de passe ne correspondent pas");
    }

    @Test
    @DisplayName("execute devrait afficher une erreur si l'email est déjà utilisé")
    void executeShouldDisplayErrorWhenEmailIsAlreadyUsed() throws Exception {
        // Arrange
        when(request.getParameter("email")).thenReturn("test@test.com");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("confirmPassword")).thenReturn("password123");
        when(userDAO.findById(any())).thenReturn(Optional.of(new User()));

        // Act
        String result = controller.execute(request, response);

        // Assert
        assertEquals("inscription.jsp", result);
        verify(request).setAttribute("error", "Cet email est déjà utilisé");
    }

    @Test
    @DisplayName("execute devrait créer un nouvel utilisateur et rediriger vers l'index")
    void executeShouldCreateNewUserAndRedirectToIndex() throws Exception {
        // Arrange
        when(request.getParameter("email")).thenReturn("test@test.com");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("confirmPassword")).thenReturn("password123");
        when(userDAO.findById(any())).thenReturn(Optional.empty());

        // Act
        String result = controller.execute(request, response);

        // Assert
        assertEquals("index.jsp", result);
        verify(userDAO).save(any(User.class));
        verify(session).setAttribute("user", any(User.class));
        verify(session).setAttribute("role", "USER");
    }

    @Test
    @DisplayName("execute devrait gérer les erreurs lors de l'inscription")
    void executeShouldHandleErrorsDuringSignin() throws Exception {
        // Arrange
        when(request.getParameter("email")).thenReturn("test@test.com");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("confirmPassword")).thenReturn("password123");
        when(userDAO.findById(any())).thenThrow(new RuntimeException("Erreur de base de données"));

        // Act
        String result = controller.execute(request, response);

        // Assert
        assertEquals("inscription.jsp", result);
        verify(request).setAttribute("error", "Une erreur est survenue");
    }
} 