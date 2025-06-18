package utilities;

import exceptions.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ValidationManagerTest {

    @Test
    @DisplayName("isValidEmail devrait accepter une adresse email valide")
    void isValidEmailShouldAcceptValidEmail() {
        // Arrange
        String email = "test@example.com";

        // Act & Assert
        assertDoesNotThrow(() -> assertTrue(ValidationManager.isValidEmail(email)));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "invalid-email",
        "test@",
        "@example.com",
        "test@example",
        "test@.com"
    })
    @DisplayName("isValidEmail devrait rejeter une adresse email invalide")
    void isValidEmailShouldRejectInvalidEmail(String email) {
        // Act & Assert
        assertThrows(ValidationException.class, () -> ValidationManager.isValidEmail(email));
    }

    @Test
    @DisplayName("isValidPhone devrait accepter un numéro de téléphone valide")
    void isValidPhoneShouldAcceptValidPhone() {
        // Arrange
        String phone = "0123456789";

        // Act & Assert
        assertDoesNotThrow(() -> assertTrue(ValidationManager.isValidPhone(phone)));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "123456789",
        "012345678",
        "01234567890",
        "abc1234567",
        "+3312345678"
    })
    @DisplayName("isValidPhone devrait rejeter un numéro de téléphone invalide")
    void isValidPhoneShouldRejectInvalidPhone(String phone) {
        // Act & Assert
        assertThrows(ValidationException.class, () -> ValidationManager.isValidPhone(phone));
    }

    @Test
    @DisplayName("isValidPostalCode devrait accepter un code postal valide")
    void isValidPostalCodeShouldAcceptValidPostalCode() {
        // Arrange
        String postalCode = "75000";

        // Act & Assert
        assertDoesNotThrow(() -> assertTrue(ValidationManager.isValidPostalCode(postalCode)));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "1234",
        "123456",
        "abcde",
        "1234a",
        "1234-5"
    })
    @DisplayName("isValidPostalCode devrait rejeter un code postal invalide")
    void isValidPostalCodeShouldRejectInvalidPostalCode(String postalCode) {
        // Act & Assert
        assertThrows(ValidationException.class, () -> ValidationManager.isValidPostalCode(postalCode));
    }

    @Test
    @DisplayName("isValidAmount devrait accepter un montant valide")
    void isValidAmountShouldAcceptValidAmount() {
        // Arrange
        String amount = "123.45";

        // Act & Assert
        assertDoesNotThrow(() -> assertTrue(ValidationManager.isValidAmount(amount)));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "123.456",
        "abc",
        "123,45",
        "-123.45",
        "123.4.5"
    })
    @DisplayName("isValidAmount devrait rejeter un montant invalide")
    void isValidAmountShouldRejectInvalidAmount(String amount) {
        // Act & Assert
        assertThrows(ValidationException.class, () -> ValidationManager.isValidAmount(amount));
    }

    @Test
    @DisplayName("isValidName devrait accepter un nom valide")
    void isValidNameShouldAcceptValidName() {
        // Arrange
        String name = "Test Company";

        // Act & Assert
        assertDoesNotThrow(() -> assertTrue(ValidationManager.isValidName(name)));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "a",
        "a".repeat(51L),
        "Test@Company",
        "Test!Company",
        "Test Company!"
    })
    @DisplayName("isValidName devrait rejeter un nom invalide")
    void isValidNameShouldRejectInvalidName(String name) {
        // Act & Assert
        assertThrows(ValidationException.class, () -> ValidationManager.isValidName(name));
    }

    @Test
    @DisplayName("isValidString devrait accepter une chaîne valide")
    void isValidStringShouldAcceptValidString() {
        // Arrange
        String str = "Test string";

        // Act & Assert
        assertDoesNotThrow(() -> assertTrue(ValidationManager.isValidString(str)));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "   ",
        "\t",
        "\n"
    })
    @DisplayName("isValidString devrait rejeter une chaîne invalide")
    void isValidStringShouldRejectInvalidString(String str) {
        // Act & Assert
        assertThrows(ValidationException.class, () -> ValidationManager.isValidString(str));
    }

    @Test
    @DisplayName("Les méthodes devraient lever une IllegalArgumentException pour un paramètre null")
    void methodsShouldThrowIllegalArgumentExceptionForNullParameter() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> ValidationManager.isValidEmail(null));
        assertThrows(IllegalArgumentException.class, () -> ValidationManager.isValidPhone(null));
        assertThrows(IllegalArgumentException.class, () -> ValidationManager.isValidPostalCode(null));
        assertThrows(IllegalArgumentException.class, () -> ValidationManager.isValidAmount(null));
        assertThrows(IllegalArgumentException.class, () -> ValidationManager.isValidName(null));
        assertThrows(IllegalArgumentException.class, () -> ValidationManager.isValidString(null));
    }
} 