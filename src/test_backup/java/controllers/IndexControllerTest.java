package controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IndexControllerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private IndexController controller;

    @BeforeEach
    void setUp() {
        controller = new IndexController();
    }

    @Test
    @DisplayName("execute devrait définir les attributs de la page et retourner index.jsp")
    void executeShouldSetPageAttributesAndReturnIndexJsp() throws Exception {
        // Act
        String result = controller.execute(request, response);

        // Assert
        assertEquals("index.jsp", result);
        verify(request).setAttribute("titlePage", "Accueil");
        verify(request).setAttribute("titleGroup", "Général");
    }
} 