package utilities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour ValidationManager.
 */
@DisplayName("ValidationManager Tests")
class ValidationManagerTest {

    @Test
    @DisplayName("Doit valider un email valide")
    void testIsValidEmailValid() {
        assertTrue(ValidationManager.isValidEmail("test@example.com"));
        assertTrue(ValidationManager.isValidEmail("user.name@domain.co.uk"));
        assertTrue(ValidationManager.isValidEmail("test+tag@example.org"));
    }

    @Test
    @DisplayName("Doit rejeter un email invalide")
    void testIsValidEmailInvalid() {
        assertFalse(ValidationManager.isValidEmail("invalid-email"));
        assertFalse(ValidationManager.isValidEmail("test@"));
        assertFalse(ValidationManager.isValidEmail("@example.com"));
    }

    @Test
    @DisplayName("Doit rejeter un email null")
    void testIsValidEmailNull() {
        assertFalse(ValidationManager.isValidEmail(null));
    }

    @Test
    @DisplayName("Doit rejeter un email vide")
    void testIsValidEmailEmpty() {
        assertFalse(ValidationManager.isValidEmail(""));
        assertFalse(ValidationManager.isValidEmail("   "));
    }

    @Test
    @DisplayName("Doit rejeter un email avec espaces")
    void testIsValidEmailWithSpaces() {
        assertFalse(ValidationManager.isValidEmail("test @example.com"));
        assertTrue(ValidationManager.isValidEmail(" test@example.com"));
        assertTrue(ValidationManager.isValidEmail("test@example.com "));
    }

    @Test
    @DisplayName("Doit valider un numéro de téléphone valide")
    void testIsValidPhoneValid() {
        assertTrue(ValidationManager.isValidPhone("0612345678"));
        assertTrue(ValidationManager.isValidPhone("0123456789"));
    }

    @Test
    @DisplayName("Doit rejeter un numéro de téléphone invalide")
    void testIsValidPhoneInvalid() {
        assertFalse(ValidationManager.isValidPhone("123"));
        assertFalse(ValidationManager.isValidPhone("06123456789"));
        assertFalse(ValidationManager.isValidPhone("061234567"));
    }

    @Test
    @DisplayName("Doit rejeter un numéro de téléphone null")
    void testIsValidPhoneNull() {
        assertFalse(ValidationManager.isValidPhone(null));
    }

    @Test
    @DisplayName("Doit rejeter un numéro de téléphone vide")
    void testIsValidPhoneEmpty() {
        assertFalse(ValidationManager.isValidPhone(""));
        assertFalse(ValidationManager.isValidPhone("   "));
    }

    @Test
    @DisplayName("Doit rejeter un numéro de téléphone avec lettres")
    void testIsValidPhoneWithLetters() {
        assertFalse(ValidationManager.isValidPhone("061234abcd"));
        assertFalse(ValidationManager.isValidPhone("abc123def"));
    }

    @Test
    @DisplayName("Doit valider différents formats de téléphone")
    void testIsValidPhoneVariousFormats() {
        assertTrue(ValidationManager.isValidPhone("0612345678"));
        assertTrue(ValidationManager.isValidPhone("0123456789"));
        assertFalse(ValidationManager.isValidPhone("+33612345678")); // Format international non supporté
        assertFalse(ValidationManager.isValidPhone("0033612345678")); // Format international non supporté
    }

    @Test
    @DisplayName("Doit valider un code postal valide")
    void testIsValidPostalCodeValid() {
        assertTrue(ValidationManager.isValidPostalCode("75001"));
        assertTrue(ValidationManager.isValidPostalCode("20000"));
        assertTrue(ValidationManager.isValidPostalCode("97100"));
    }

    @Test
    @DisplayName("Doit rejeter un code postal invalide")
    void testIsValidPostalCodeInvalid() {
        assertFalse(ValidationManager.isValidPostalCode("123"));
        assertFalse(ValidationManager.isValidPostalCode("123456"));
        assertFalse(ValidationManager.isValidPostalCode("7500"));
    }

    @Test
    @DisplayName("Doit rejeter un code postal null")
    void testIsValidPostalCodeNull() {
        assertFalse(ValidationManager.isValidPostalCode(null));
    }

    @Test
    @DisplayName("Doit rejeter un code postal vide")
    void testIsValidPostalCodeEmpty() {
        assertFalse(ValidationManager.isValidPostalCode(""));
        assertFalse(ValidationManager.isValidPostalCode("   "));
    }

    @Test
    @DisplayName("Doit rejeter un code postal avec lettres")
    void testIsValidPostalCodeWithLetters() {
        assertFalse(ValidationManager.isValidPostalCode("7500A"));
        assertFalse(ValidationManager.isValidPostalCode("ABC12"));
    }

    @Test
    @DisplayName("Doit valider un nom valide")
    void testIsValidNameValid() {
        assertTrue(ValidationManager.isValidName("Jean Dupont"));
        assertTrue(ValidationManager.isValidName("Marie-Claire"));
        assertTrue(ValidationManager.isValidName("O'Connor"));
    }

    @Test
    @DisplayName("Doit rejeter un nom avec caractères spéciaux")
    void testIsValidNameWithSpecialCharacters() {
        assertFalse(ValidationManager.isValidName("Test@Company"));
        assertFalse(ValidationManager.isValidName("User123"));
        assertFalse(ValidationManager.isValidName("Test_Company"));
    }

    @Test
    @DisplayName("Doit rejeter un nom null")
    void testIsValidNameNull() {
        assertFalse(ValidationManager.isValidName(null));
    }

    @Test
    @DisplayName("Doit rejeter un nom vide")
    void testIsValidNameEmpty() {
        assertFalse(ValidationManager.isValidName(""));
        assertFalse(ValidationManager.isValidName("   "));
    }

    @Test
    @DisplayName("Doit valider un montant positif")
    void testIsPositiveAmount() {
        assertTrue(ValidationManager.isPositiveAmount(100));
        assertTrue(ValidationManager.isPositiveAmount(0.5));
        assertTrue(ValidationManager.isPositiveAmount(1000L));
    }

    @Test
    @DisplayName("Doit rejeter un montant négatif ou nul")
    void testIsPositiveAmountNegative() {
        assertFalse(ValidationManager.isPositiveAmount(-100));
        assertFalse(ValidationManager.isPositiveAmount(0));
        assertFalse(ValidationManager.isPositiveAmount(null));
    }

    @Test
    @DisplayName("Doit valider une chaîne alphanumérique")
    void testIsAlphanumeric() {
        assertTrue(ValidationManager.isAlphanumeric("abc123"));
        assertTrue(ValidationManager.isAlphanumeric("ABC123"));
        assertTrue(ValidationManager.isAlphanumeric("123456"));
    }

    @Test
    @DisplayName("Doit rejeter une chaîne non alphanumérique")
    void testIsAlphanumericInvalid() {
        assertFalse(ValidationManager.isAlphanumeric("abc-123"));
        assertFalse(ValidationManager.isAlphanumeric("abc 123"));
        assertFalse(ValidationManager.isAlphanumeric("abc@123"));
        assertFalse(ValidationManager.isAlphanumeric(""));
        assertFalse(ValidationManager.isAlphanumeric(null));
    }

    @Test
    @DisplayName("Doit valider un objet non null")
    void testIsNotNull() {
        assertTrue(ValidationManager.isNotNull("test"));
        assertTrue(ValidationManager.isNotNull(123));
        assertTrue(ValidationManager.isNotNull(new Object()));
    }

    @Test
    @DisplayName("Doit rejeter un objet null")
    void testIsNotNullNull() {
        assertFalse(ValidationManager.isNotNull(null));
    }

    @Test
    @DisplayName("Doit valider une chaîne non vide")
    void testIsNotEmpty() {
        assertTrue(ValidationManager.isNotEmpty("test"));
        assertTrue(ValidationManager.isNotEmpty("  test  "));
        assertTrue(ValidationManager.isNotEmpty(123));
    }

    @Test
    @DisplayName("Doit rejeter une chaîne vide")
    void testIsNotEmptyEmpty() {
        assertFalse(ValidationManager.isNotEmpty(""));
        assertFalse(ValidationManager.isNotEmpty("   "));
        assertFalse(ValidationManager.isNotEmpty(null));
    }

    @Test
    @DisplayName("Doit valider une chaîne vide")
    void testIsEmpty() {
        assertTrue(ValidationManager.isEmpty(""));
        assertTrue(ValidationManager.isEmpty("   "));
        assertTrue(ValidationManager.isEmpty(null));
    }

    @Test
    @DisplayName("Doit rejeter une chaîne non vide")
    void testIsEmptyNotEmpty() {
        assertFalse(ValidationManager.isEmpty("test"));
        assertFalse(ValidationManager.isEmpty("  test  "));
    }
} 