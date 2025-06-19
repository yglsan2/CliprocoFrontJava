package controllers;

import dao.IDAO;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
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
import utilities.Security;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConnexionControllerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private IDAO<User, Integer> userDAO;

    private ConnexionController controller;

    @BeforeEach
    void setUp() {
        controller = new ConnexionController();
        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    @DisplayName("doGet devrait rediriger vers la page de connexion")
    void doGetShouldForwardToLoginPage() throws ServletException, IOException {
        // Act
        controller.doGet(request, response);

        // Assert
        verify(request).getRequestDispatcher("/WEB-INF/views/login.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doPost devrait rediriger vers la page de connexion si email ou mot de passe manquant")
    void doPostShouldForwardToLoginPageWhenCredentialsMissing() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("email")).thenReturn(null);
        when(request.getParameter("password")).thenReturn(null);

        // Act
        controller.doPost(request, response);

        // Assert
        verify(request).getRequestDispatcher("/WEB-INF/views/login.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doPost devrait rediriger vers la page d'accueil si les identifiants sont corrects")
    void doPostShouldRedirectToHomePageWhenCredentialsValid() throws ServletException, IOException, ValidationException, DatabaseException {
        // Arrange
        String email = "1";
        String password = "password";
        User user = new User();
        user.setEmail(email);
        user.setPassword(Security.hashPassword(password));

        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(userDAO.findById(1L)).thenReturn(Optional.of(user));
        when(request.getContextPath()).thenReturn("/context");

        // Act
        controller.doPost(request, response);

        // Assert
        verify(session).setAttribute("user", user);
        verify(session).setAttribute("role", user.getRole());
        verify(response).sendRedirect("/context/home");
    }

    @Test
    @DisplayName("doPost devrait afficher une erreur si les identifiants sont incorrects")
    void doPostShouldShowErrorWhenCredentialsInvalid() throws ServletException, IOException, ValidationException, DatabaseException {
        // Arrange
        String email = "1";
        String password = "wrongpassword";
        User user = new User();
        user.setEmail(email);
        user.setPassword(Security.hashPassword("correctpassword"));

        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(userDAO.findById(1L)).thenReturn(Optional.of(user));

        // Act
        controller.doPost(request, response);

        // Assert
        verify(request).setAttribute("error", "Email ou mot de passe incorrect");
        verify(request).getRequestDispatcher("/WEB-INF/views/login.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("doPost devrait afficher une erreur si l'utilisateur n'existe pas")
    void doPostShouldShowErrorWhenUserNotFound() throws ServletException, IOException, ValidationException, DatabaseException {
        // Arrange
        String email = "1";
        String password = "password";

        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(userDAO.findById(1L)).thenReturn(Optional.empty());

        // Act
        controller.doPost(request, response);

        // Assert
        verify(request).setAttribute("error", "Email ou mot de passe incorrect");
        verify(request).getRequestDispatcher("/WEB-INF/views/login.jsp");
        verify(requestDispatcher).forward(request, response);
    }
} 