package controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeconnexionControllerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    private DeconnexionController controller;

    @BeforeEach
    void setUp() {
        controller = new DeconnexionController();
        when(request.getSession()).thenReturn(session);
    }

    @Test
    @DisplayName("execute devrait invalider la session et rediriger vers la page d'accueil")
    void executeShouldInvalidateSessionAndRedirectToIndex() throws Exception {
        // Act
        String result = controller.execute(request, response);

        // Assert
        assertEquals("index.jsp", result);
        verify(session).invalidate();
    }
} 