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
    @DisplayName("isValidEmail doit accepter les emails valides")
    void testValidEmails() {
        assertTrue(ValidationManager.isValidEmail("test@example.com"));
        assertTrue(ValidationManager.isValidEmail("user.name@domain.co.uk"));
        assertTrue(ValidationManager.isValidEmail("test+tag@example.org"));
        assertTrue(ValidationManager.isValidEmail("user123@company.fr"));
        assertTrue(ValidationManager.isValidEmail("test.email+tag@subdomain.example.com"));
    }

    @Test
    @DisplayName("isValidEmail doit rejeter les emails invalides")
    void testInvalidEmails() {
        assertFalse(ValidationManager.isValidEmail("invalid-email"));
        assertFalse(ValidationManager.isValidEmail("test@"));
        assertFalse(ValidationManager.isValidEmail("@example.com"));
        assertFalse(ValidationManager.isValidEmail("test @example.com"));
        assertFalse(ValidationManager.isValidEmail("test@.com"));
        assertFalse(ValidationManager.isValidEmail("test@example."));
    }

    @Test
    @DisplayName("isValidEmail doit rejeter les valeurs null et vides")
    void testEmailNullAndEmpty() {
        assertFalse(ValidationManager.isValidEmail(null));
        assertFalse(ValidationManager.isValidEmail(""));
        assertFalse(ValidationManager.isValidEmail("   "));
    }

    @Test
    @DisplayName("isValidPhone doit accepter les numéros de téléphone valides")
    void testValidPhones() {
        // Formats nationaux
        assertTrue(ValidationManager.isValidPhone("0612345678"));
        assertTrue(ValidationManager.isValidPhone("0123456789"));
        assertTrue(ValidationManager.isValidPhone("0987654321"));
        
        // Formats internationaux
        assertTrue(ValidationManager.isValidPhone("+33612345678"));
        assertTrue(ValidationManager.isValidPhone("0033612345678"));
        assertTrue(ValidationManager.isValidPhone("+33123456789"));
        assertTrue(ValidationManager.isValidPhone("0033123456789"));
    }

    @Test
    @DisplayName("isValidPhone doit rejeter les numéros de téléphone invalides")
    void testInvalidPhones() {
        // Trop courts
        assertFalse(ValidationManager.isValidPhone("123"));
        assertFalse(ValidationManager.isValidPhone("061234567"));
        
        // Trop longs
        assertFalse(ValidationManager.isValidPhone("06123456789"));
        assertFalse(ValidationManager.isValidPhone("+336123456789"));
        
        // Formats invalides
        assertFalse(ValidationManager.isValidPhone("061234abcd"));
        assertFalse(ValidationManager.isValidPhone("abc123def"));
        assertFalse(ValidationManager.isValidPhone("061234 5678"));
        assertFalse(ValidationManager.isValidPhone("06-12-34-56-78"));
        
        // Numéros spéciaux (non acceptés)
        assertFalse(ValidationManager.isValidPhone("118218"));
        assertFalse(ValidationManager.isValidPhone("3631"));
    }

    @Test
    @DisplayName("isValidPhone doit rejeter les valeurs null et vides")
    void testPhoneNullAndEmpty() {
        assertFalse(ValidationManager.isValidPhone(null));
        assertFalse(ValidationManager.isValidPhone(""));
        assertFalse(ValidationManager.isValidPhone("   "));
    }

    @Test
    @DisplayName("isValidPostalCode doit accepter les codes postaux valides")
    void testValidPostalCodes() {
        // Codes métropolitains
        assertTrue(ValidationManager.isValidPostalCode("75001"));
        assertTrue(ValidationManager.isValidPostalCode("13001"));
        assertTrue(ValidationManager.isValidPostalCode("69001"));
        assertTrue(ValidationManager.isValidPostalCode("95000"));
        
        // Corse
        assertTrue(ValidationManager.isValidPostalCode("2A001"));
        assertTrue(ValidationManager.isValidPostalCode("2B001"));
        
        // DOM-TOM
        assertTrue(ValidationManager.isValidPostalCode("97100"));
        assertTrue(ValidationManager.isValidPostalCode("97200"));
        assertTrue(ValidationManager.isValidPostalCode("97300"));
        assertTrue(ValidationManager.isValidPostalCode("97400"));
        assertTrue(ValidationManager.isValidPostalCode("97500"));
        assertTrue(ValidationManager.isValidPostalCode("97600"));
        assertTrue(ValidationManager.isValidPostalCode("97700"));
        assertTrue(ValidationManager.isValidPostalCode("97800"));
        assertTrue(ValidationManager.isValidPostalCode("98000"));
        assertTrue(ValidationManager.isValidPostalCode("99000"));
    }

    @Test
    @DisplayName("isValidPostalCode doit rejeter les codes postaux invalides")
    void testInvalidPostalCodes() {
        // Trop courts
        assertFalse(ValidationManager.isValidPostalCode("123"));
        assertFalse(ValidationManager.isValidPostalCode("7500"));
        
        // Trop longs
        assertFalse(ValidationManager.isValidPostalCode("123456"));
        
        // Codes inexistants
        assertFalse(ValidationManager.isValidPostalCode("00000"));
        assertFalse(ValidationManager.isValidPostalCode("96000"));
        assertFalse(ValidationManager.isValidPostalCode("20000")); // Corse sans A/B
        
        // Avec lettres (sauf 2A/2B)
        assertFalse(ValidationManager.isValidPostalCode("7500A"));
        assertFalse(ValidationManager.isValidPostalCode("ABC12"));
        assertFalse(ValidationManager.isValidPostalCode("2C001"));
    }

    @Test
    @DisplayName("isValidPostalCode doit rejeter les valeurs null et vides")
    void testPostalCodeNullAndEmpty() {
        assertFalse(ValidationManager.isValidPostalCode(null));
        assertFalse(ValidationManager.isValidPostalCode(""));
        assertFalse(ValidationManager.isValidPostalCode("   "));
    }

    @Test
    @DisplayName("isValidName doit accepter les noms valides")
    void testValidNames() {
        assertTrue(ValidationManager.isValidName("Jean Dupont"));
        assertTrue(ValidationManager.isValidName("Marie-Claire"));
        assertTrue(ValidationManager.isValidName("O'Connor"));
        assertTrue(ValidationManager.isValidName("François"));
        assertTrue(ValidationManager.isValidName("José"));
        assertTrue(ValidationManager.isValidName("Jean-Pierre"));
    }

    @Test
    @DisplayName("isValidName doit rejeter les noms invalides")
    void testInvalidNames() {
        assertFalse(ValidationManager.isValidName("Test@Company"));
        assertFalse(ValidationManager.isValidName("User123"));
        assertFalse(ValidationManager.isValidName("Test_Company"));
        assertFalse(ValidationManager.isValidName("Test.Company"));
        assertFalse(ValidationManager.isValidName("Test&Company"));
    }

    @Test
    @DisplayName("isValidName doit rejeter les valeurs null et vides")
    void testNameNullAndEmpty() {
        assertFalse(ValidationManager.isValidName(null));
        assertFalse(ValidationManager.isValidName(""));
        assertFalse(ValidationManager.isValidName("   "));
    }

    @Test
    @DisplayName("isValidCompanyName doit accepter les raisons sociales valides")
    void testValidCompanyNames() {
        assertTrue(ValidationManager.isValidCompanyName("Entreprise SARL"));
        assertTrue(ValidationManager.isValidCompanyName("Test & Co."));
        assertTrue(ValidationManager.isValidCompanyName("Company (France)"));
        assertTrue(ValidationManager.isValidCompanyName("Tech Solutions, Inc."));
        assertTrue(ValidationManager.isValidCompanyName("123 Company"));
        assertTrue(ValidationManager.isValidCompanyName("Test-Company"));
    }

    @Test
    @DisplayName("isValidCompanyName doit rejeter les raisons sociales invalides")
    void testInvalidCompanyNames() {
        assertFalse(ValidationManager.isValidCompanyName("Test@Company"));
        assertFalse(ValidationManager.isValidCompanyName("Test_Company"));
        assertFalse(ValidationManager.isValidCompanyName("Test#Company"));
        assertFalse(ValidationManager.isValidCompanyName("Test$Company"));
    }

    @Test
    @DisplayName("isValidAddress doit accepter les adresses valides")
    void testValidAddresses() {
        assertTrue(ValidationManager.isValidAddress("123 Rue de la Paix"));
        assertTrue(ValidationManager.isValidAddress("Avenue des Champs-Élysées"));
        assertTrue(ValidationManager.isValidAddress("Place de la Concorde"));
        assertTrue(ValidationManager.isValidAddress("Boulevard Saint-Germain"));
        assertTrue(ValidationManager.isValidAddress("Rue du 8 Mai 1945"));
    }

    @Test
    @DisplayName("isValidCity doit accepter les villes valides")
    void testValidCities() {
        assertTrue(ValidationManager.isValidCity("Paris"));
        assertTrue(ValidationManager.isValidCity("Lyon"));
        assertTrue(ValidationManager.isValidCity("Marseille"));
        assertTrue(ValidationManager.isValidCity("Saint-Étienne"));
        assertTrue(ValidationManager.isValidCity("Le Havre"));
    }

    @Test
    @DisplayName("isValidCountry doit accepter les pays valides")
    void testValidCountries() {
        assertTrue(ValidationManager.isValidCountry("France"));
        assertTrue(ValidationManager.isValidCountry("États-Unis"));
        assertTrue(ValidationManager.isValidCountry("Royaume-Uni"));
        assertTrue(ValidationManager.isValidCountry("Côte d'Ivoire"));
    }

    @Test
    @DisplayName("isValidStreetNumber doit accepter les numéros de rue valides")
    void testValidStreetNumbers() {
        assertTrue(ValidationManager.isValidStreetNumber("123"));
        assertTrue(ValidationManager.isValidStreetNumber("12B"));
        assertTrue(ValidationManager.isValidStreetNumber("1A"));
        assertTrue(ValidationManager.isValidStreetNumber("42"));
        assertTrue(ValidationManager.isValidStreetNumber("7C"));
    }

    @Test
    @DisplayName("isValidStreetNumber doit rejeter les numéros de rue invalides")
    void testInvalidStreetNumbers() {
        assertFalse(ValidationManager.isValidStreetNumber("12@"));
        assertFalse(ValidationManager.isValidStreetNumber("A#"));
        assertFalse(ValidationManager.isValidStreetNumber("1-2"));
        assertFalse(ValidationManager.isValidStreetNumber("12 34"));
        assertFalse(ValidationManager.isValidStreetNumber("A")); // Lettre seule
        assertFalse(ValidationManager.isValidStreetNumber("A1")); // Lettre + chiffres
        assertFalse(ValidationManager.isValidStreetNumber("")); // Vide
        assertFalse(ValidationManager.isValidStreetNumber(null)); // Null
    }

    @Test
    @DisplayName("isPositiveAmount doit accepter les montants positifs")
    void testPositiveAmounts() {
        assertTrue(ValidationManager.isPositiveAmount(100));
        assertTrue(ValidationManager.isPositiveAmount(0.5));
        assertTrue(ValidationManager.isPositiveAmount(1000L));
        assertTrue(ValidationManager.isPositiveAmount(1.0));
    }

    @Test
    @DisplayName("isPositiveAmount doit rejeter les montants non positifs")
    void testNonPositiveAmounts() {
        assertFalse(ValidationManager.isPositiveAmount(-100));
        assertFalse(ValidationManager.isPositiveAmount(0));
        assertFalse(ValidationManager.isPositiveAmount(null));
    }

    @Test
    @DisplayName("isAlphanumeric doit accepter les chaînes alphanumériques")
    void testAlphanumeric() {
        assertTrue(ValidationManager.isAlphanumeric("abc123"));
        assertTrue(ValidationManager.isAlphanumeric("ABC123"));
        assertTrue(ValidationManager.isAlphanumeric("123456"));
    }

    @Test
    @DisplayName("isAlphanumeric doit rejeter les chaînes non alphanumériques")
    void testNonAlphanumeric() {
        assertFalse(ValidationManager.isAlphanumeric("abc-123"));
        assertFalse(ValidationManager.isAlphanumeric("abc 123"));
        assertFalse(ValidationManager.isAlphanumeric("abc@123"));
        assertFalse(ValidationManager.isAlphanumeric(""));
        assertFalse(ValidationManager.isAlphanumeric(null));
    }

    @Test
    @DisplayName("isNotNull doit fonctionner correctement")
    void testIsNotNull() {
        assertTrue(ValidationManager.isNotNull("test"));
        assertTrue(ValidationManager.isNotNull(123));
        assertTrue(ValidationManager.isNotNull(new Object()));
        assertFalse(ValidationManager.isNotNull(null));
    }

    @Test
    @DisplayName("isNotEmpty doit fonctionner correctement")
    void testIsNotEmpty() {
        assertTrue(ValidationManager.isNotEmpty("test"));
        assertTrue(ValidationManager.isNotEmpty("  test  "));
        assertTrue(ValidationManager.isNotEmpty(123));
        assertFalse(ValidationManager.isNotEmpty(""));
        assertFalse(ValidationManager.isNotEmpty("   "));
        assertFalse(ValidationManager.isNotEmpty(null));
    }

    @Test
    @DisplayName("isEmpty doit fonctionner correctement")
    void testIsEmpty() {
        assertTrue(ValidationManager.isEmpty(""));
        assertTrue(ValidationManager.isEmpty("   "));
        assertTrue(ValidationManager.isEmpty(null));
        assertFalse(ValidationManager.isEmpty("test"));
        assertFalse(ValidationManager.isEmpty("  test  "));
    }
} 