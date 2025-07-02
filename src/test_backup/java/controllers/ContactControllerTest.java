package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ContactControllerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private ContactController controller;

    @BeforeEach
    void setUp() {
        controller = new ContactController();
    }

    @Test
    @DisplayName("execute devrait définir les attributs de la page et retourner contact.jsp")
    void executeShouldSetPageAttributesAndReturnContactJsp() throws ServletException, IOException {
        // Act
        String result = controller.execute(request, response);

        // Assert
        assertEquals("contact.jsp", result);
        verify(request).setAttribute("titlePage", "Contact");
        verify(request).setAttribute("titleGroup", "Général");
    }
} 