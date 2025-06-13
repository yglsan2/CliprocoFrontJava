package utilities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import exceptions.ValidationException;

/**
 * Tests unitaires pour la classe ValidationManager.
 * 
 * @author Benja2
 * @version 1.0
 * @since 1.0
 */
class ValidationManagerTest {

    private static class TestObject {
        @NotBlank(message = "Le nom ne peut pas être vide")
        private String name;

        @Size(min = 3, max = 10, message = "La description doit faire entre 3 et 10 caractères")
        private String description;

        public TestObject(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }

    @Test
    void testValidObject() {
        TestObject validObject = new TestObject("Test", "Description");
        assertTrue(ValidationManager.isValid(validObject));
        assertNull(ValidationManager.getValidationMessages(validObject));
    }

    @Test
    void testInvalidObject() {
        TestObject invalidObject = new TestObject("", "D");
        assertFalse(ValidationManager.isValid(invalidObject));
        String messages = ValidationManager.getValidationMessages(invalidObject);
        assertNotNull(messages);
        assertTrue(messages.contains("Le nom ne peut pas être vide"));
        assertTrue(messages.contains("La description doit faire entre 3 et 10 caractères"));
    }

    @Test
    void testValidateAndThrowWithValidObject() {
        TestObject validObject = new TestObject("Test", "Description");
        assertDoesNotThrow(() -> ValidationManager.validateAndThrow(validObject));
    }

    @Test
    void testValidateAndThrowWithInvalidObject() {
        TestObject invalidObject = new TestObject("", "D");
        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> ValidationManager.validateAndThrow(invalidObject)
        );
        assertTrue(exception.getMessage().contains("Validation failed"));
    }
} 