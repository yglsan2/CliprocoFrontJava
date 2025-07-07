package utilities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
 * Tests unitaires pour toutes les regexs de validation métier (2024)
 */
@DisplayName("Tests des regexs de validation métier")
class RegexTest {

    // --- Téléphone ---
    @Test
    void testPhoneValid() {
        assertTrue(RegexUtils.isValidPhone("0123456789"));
        assertTrue(RegexUtils.isValidPhone("01 23 45 67 89"));
        assertTrue(RegexUtils.isValidPhone("01.23.45.67.89"));
        assertTrue(RegexUtils.isValidPhone("+33123456789"));
        assertTrue(RegexUtils.isValidPhone("+33 1 23 45 67 89"));
    }
    @Test
    void testPhoneInvalid() {
        assertFalse(RegexUtils.isValidPhone("012345678"));
        assertFalse(RegexUtils.isValidPhone("01234567890"));
        assertFalse(RegexUtils.isValidPhone("012345678a"));
        assertFalse(RegexUtils.isValidPhone(""));
        assertFalse(RegexUtils.isValidPhone("abc"));
    }

    // --- Email ---
    @Test
    void testEmailValid() {
        assertTrue(RegexUtils.isValidEmail("user@domain.com"));
        assertTrue(RegexUtils.isValidEmail("user.name@domain.com"));
        assertTrue(RegexUtils.isValidEmail("user+tag@domain.com"));
        assertTrue(RegexUtils.isValidEmail("user@domain.co.uk"));
    }
    @Test
    void testEmailInvalid() {
        assertFalse(RegexUtils.isValidEmail("@domain.com"));
        assertFalse(RegexUtils.isValidEmail("user@"));
        assertFalse(RegexUtils.isValidEmail("user@domain"));
        assertFalse(RegexUtils.isValidEmail("user domain.com"));
        assertFalse(RegexUtils.isValidEmail("user@domain..com"));
        assertFalse(RegexUtils.isValidEmail(".user@domain.com"));
        assertFalse(RegexUtils.isValidEmail("user@domain.com@"));
        // Note: user@domain.com. est techniquement valide selon les standards RFC
    }

    // --- Code postal ---
    @Test
    void testPostalCodeValid() {
        assertTrue(RegexUtils.isValidPostalCode("75001"));
        assertTrue(RegexUtils.isValidPostalCode("2A123"));
        assertTrue(RegexUtils.isValidPostalCode("97100"));
    }
    @Test
    void testPostalCodeInvalid() {
        assertFalse(RegexUtils.isValidPostalCode("7500"));
        assertFalse(RegexUtils.isValidPostalCode("2C123"));
        assertFalse(RegexUtils.isValidPostalCode("9710A"));
        assertFalse(RegexUtils.isValidPostalCode("75001A"));
        assertFalse(RegexUtils.isValidPostalCode(""));
    }

    // --- Nom/prénom ---
    @Test
    void testNameValid() {
        assertTrue("Jean-Pierre".matches(RegexPatterns.NAME_PATTERN));
        assertTrue("Marie-Claire".matches(RegexPatterns.NAME_PATTERN));
        assertTrue("O'Connor".matches(RegexPatterns.NAME_PATTERN));
        assertTrue("François".matches(RegexPatterns.NAME_PATTERN));
    }
    @Test
    void testNameInvalid() {
        assertFalse("Jean123".matches(RegexPatterns.NAME_PATTERN));
        assertFalse("Jean@".matches(RegexPatterns.NAME_PATTERN));
        assertFalse("A".matches(RegexPatterns.NAME_PATTERN));
        assertFalse("".matches(RegexPatterns.NAME_PATTERN));
    }

    // --- Ville ---
    @Test
    void testCityValid() {
        assertTrue("Paris".matches(RegexPatterns.CITY_PATTERN));
        assertTrue("Saint-Étienne".matches(RegexPatterns.CITY_PATTERN));
        assertTrue("L'Hôpital".matches(RegexPatterns.CITY_PATTERN));
    }
    @Test
    void testCityInvalid() {
        assertFalse("Paris123".matches(RegexPatterns.CITY_PATTERN));
        assertFalse("Paris@".matches(RegexPatterns.CITY_PATTERN));
        assertFalse("A".matches(RegexPatterns.CITY_PATTERN));
        assertFalse("".matches(RegexPatterns.CITY_PATTERN));
    }

    // --- Pays ---
    @Test
    void testCountryValid() {
        assertTrue("France".matches(RegexPatterns.COUNTRY_PATTERN));
        assertTrue("États-Unis".matches(RegexPatterns.COUNTRY_PATTERN));
        assertTrue("Côte d'Ivoire".matches(RegexPatterns.COUNTRY_PATTERN));
    }
    @Test
    void testCountryInvalid() {
        assertFalse("France123".matches(RegexPatterns.COUNTRY_PATTERN));
        assertFalse("France@".matches(RegexPatterns.COUNTRY_PATTERN));
        assertFalse("A".matches(RegexPatterns.COUNTRY_PATTERN));
        assertFalse("".matches(RegexPatterns.COUNTRY_PATTERN));
    }

    // --- Raison sociale ---
    @Test
    void testCompanyNameValid() {
        assertTrue("SARL Dupont & Fils".matches(RegexPatterns.COMPANY_NAME_PATTERN));
        assertTrue("EURL Martin (SAS)".matches(RegexPatterns.COMPANY_NAME_PATTERN));
        assertTrue("Tech-Solutions 2.0".matches(RegexPatterns.COMPANY_NAME_PATTERN));
    }
    @Test
    void testCompanyNameInvalid() {
        assertFalse("SARL@Dupont".matches(RegexPatterns.COMPANY_NAME_PATTERN));
        assertFalse("Tech#Solutions".matches(RegexPatterns.COMPANY_NAME_PATTERN));
        assertFalse("A".matches(RegexPatterns.COMPANY_NAME_PATTERN));
        assertFalse("".matches(RegexPatterns.COMPANY_NAME_PATTERN));
    }

    // --- Adresse ---
    @Test
    void testAddressValid() {
        assertTrue("123 Rue de la Paix".matches(RegexPatterns.ADDRESS_PATTERN));
        assertTrue("42 Avenue Jean-Jaurès".matches(RegexPatterns.ADDRESS_PATTERN));
        assertTrue("15 bis Boulevard Saint-Germain".matches(RegexPatterns.ADDRESS_PATTERN));
    }
    @Test
    void testAddressInvalid() {
        assertFalse("123 Rue@de la Paix".matches(RegexPatterns.ADDRESS_PATTERN));
        assertFalse("A".matches(RegexPatterns.ADDRESS_PATTERN));
        assertFalse("".matches(RegexPatterns.ADDRESS_PATTERN));
    }

    // --- Numéro de rue ---
    @Test
    void testStreetNumberValid() {
        assertTrue("123".matches(RegexPatterns.STREET_NUMBER_PATTERN));
        assertTrue("123A".matches(RegexPatterns.STREET_NUMBER_PATTERN));
        assertTrue("1".matches(RegexPatterns.STREET_NUMBER_PATTERN));
        assertTrue("1 bis".matches(RegexPatterns.STREET_NUMBER_PATTERN));
        assertTrue("23 ter".matches(RegexPatterns.STREET_NUMBER_PATTERN));
        assertTrue("42 bis".matches(RegexPatterns.STREET_NUMBER_PATTERN));
    }
    @Test
    void testStreetNumberInvalid() {
        assertFalse("A123".matches(RegexPatterns.STREET_NUMBER_PATTERN));
        assertFalse("A".matches(RegexPatterns.STREET_NUMBER_PATTERN));
        assertFalse("".matches(RegexPatterns.STREET_NUMBER_PATTERN));
        assertFalse("1 bisA".matches(RegexPatterns.STREET_NUMBER_PATTERN));
        assertFalse("bis".matches(RegexPatterns.STREET_NUMBER_PATTERN));
        assertFalse("1 quater".matches(RegexPatterns.STREET_NUMBER_PATTERN));
    }

    // --- Nom de rue ---
    @Test
    void testStreetNameValid() {
        assertTrue("Rue de la Paix".matches(RegexPatterns.STREET_NAME_PATTERN));
        assertTrue("Avenue des Champs-Élysées".matches(RegexPatterns.STREET_NAME_PATTERN));
        assertTrue("Boulevard Saint-Germain".matches(RegexPatterns.STREET_NAME_PATTERN));
    }
    @Test
    void testStreetNameInvalid() {
        assertFalse("Rue@de la Paix".matches(RegexPatterns.STREET_NAME_PATTERN));
        assertFalse("A".matches(RegexPatterns.STREET_NAME_PATTERN));
        assertFalse("".matches(RegexPatterns.STREET_NAME_PATTERN));
    }

    // --- Montant ---
    @Test
    void testAmountValid() {
        assertTrue("1000".matches(RegexPatterns.AMOUNT_PATTERN));
        assertTrue("1000,50".matches(RegexPatterns.AMOUNT_PATTERN));
        assertTrue("1000.50".matches(RegexPatterns.AMOUNT_PATTERN));
    }
    @Test
    void testAmountInvalid() {
        assertFalse("-1000".matches(RegexPatterns.AMOUNT_PATTERN));
        assertFalse("1000,123".matches(RegexPatterns.AMOUNT_PATTERN));
        assertFalse("abc".matches(RegexPatterns.AMOUNT_PATTERN));
        assertFalse("".matches(RegexPatterns.AMOUNT_PATTERN));
    }

    // --- Entier ---
    @Test
    void testIntegerValid() {
        assertTrue("0".matches(RegexPatterns.INTEGER_PATTERN));
        assertTrue("123456".matches(RegexPatterns.INTEGER_PATTERN));
    }
    @Test
    void testIntegerInvalid() {
        assertFalse("-1".matches(RegexPatterns.INTEGER_PATTERN));
        assertFalse("1.5".matches(RegexPatterns.INTEGER_PATTERN));
        assertFalse("abc".matches(RegexPatterns.INTEGER_PATTERN));
        assertFalse("".matches(RegexPatterns.INTEGER_PATTERN));
    }

    // --- Commentaire ---
    @Test
    void testCommentValid() {
        assertTrue("".matches(RegexPatterns.COMMENT_PATTERN));
        assertTrue("Client fidèle depuis 5 ans".matches(RegexPatterns.COMMENT_PATTERN));
        assertTrue("A".repeat(500).matches(RegexPatterns.COMMENT_PATTERN));
    }
    @Test
    void testCommentInvalid() {
        assertFalse("A".repeat(501).matches(RegexPatterns.COMMENT_PATTERN));
    }
} 