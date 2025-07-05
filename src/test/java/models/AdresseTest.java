package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe Adresse.
 * 
 * <p>Cette classe de test couvre tous les aspects de la classe Adresse :
 * - Constructeurs
 * - Getters et setters
 * - Validation des données
 * - Cas limites et d'erreur
 * - Méthode toString
 * </p>
 * 
 * @author CliprocoJEE
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Tests unitaires pour la classe Adresse")
class AdresseTest {

    private Adresse adresse;
    
    private static final Integer VALID_ID = 1;
    private static final String VALID_NUMERO = "123";
    private static final String VALID_NOM_RUE = "Rue de la Paix";
    private static final String VALID_CODE_POSTAL = "75001";
    private static final String VALID_VILLE = "Paris";
    private static final String VALID_PAYS = "France";

    @BeforeEach
    void setUp() {
        adresse = new Adresse();
    }

    @Nested
    @DisplayName("Tests des constructeurs")
    class ConstructorTests {

        @Test
        @DisplayName("Constructeur par défaut doit créer une adresse vide")
        void testDefaultConstructor() {
            Adresse emptyAdresse = new Adresse();
            
            assertNotNull(emptyAdresse);
            assertNull(emptyAdresse.getIdentifiant());
            assertNull(emptyAdresse.getNumeroRue());
            assertNull(emptyAdresse.getNomRue());
            assertNull(emptyAdresse.getCodePostal());
            assertNull(emptyAdresse.getVille());
            assertNull(emptyAdresse.getPays());
        }

        @Test
        @DisplayName("Constructeur avec paramètres de base doit initialiser correctement")
        void testBasicConstructor() {
            Adresse basicAdresse = new Adresse(VALID_NUMERO, VALID_NOM_RUE, 
                                             VALID_CODE_POSTAL, VALID_VILLE);
            
            assertNotNull(basicAdresse);
            assertEquals(VALID_NUMERO, basicAdresse.getNumeroRue());
            assertEquals(VALID_NOM_RUE, basicAdresse.getNomRue());
            assertEquals(VALID_CODE_POSTAL, basicAdresse.getCodePostal());
            assertEquals(VALID_VILLE, basicAdresse.getVille());
            assertNull(basicAdresse.getPays()); // Non défini dans ce constructeur
            assertNull(basicAdresse.getIdentifiant()); // Généré par la base de données
        }

        @Test
        @DisplayName("Constructeur avec paramètres null doit être accepté")
        void testConstructorWithNullValues() {
            Adresse nullAdresse = new Adresse(null, null, null, null);
            
            assertNotNull(nullAdresse);
            assertNull(nullAdresse.getNumeroRue());
            assertNull(nullAdresse.getNomRue());
            assertNull(nullAdresse.getCodePostal());
            assertNull(nullAdresse.getVille());
            assertNull(nullAdresse.getPays());
        }
    }

    @Nested
    @DisplayName("Tests des getters et setters")
    class GetterSetterTests {

        @Test
        @DisplayName("Getter et setter pour identifiant")
        void testIdentifiantGetterSetter() {
            adresse.setIdentifiant(VALID_ID);
            assertEquals(VALID_ID, adresse.getIdentifiant());
            
            adresse.setIdentifiant(null);
            assertNull(adresse.getIdentifiant());
        }

        @Test
        @DisplayName("Getter et setter pour numéro de rue")
        void testNumeroRueGetterSetter() {
            adresse.setNumeroRue(VALID_NUMERO);
            assertEquals(VALID_NUMERO, adresse.getNumeroRue());
            
            adresse.setNumeroRue(null);
            assertNull(adresse.getNumeroRue());
        }

        @Test
        @DisplayName("Getter et setter pour nom de rue")
        void testNomRueGetterSetter() {
            adresse.setNomRue(VALID_NOM_RUE);
            assertEquals(VALID_NOM_RUE, adresse.getNomRue());
            
            adresse.setNomRue(null);
            assertNull(adresse.getNomRue());
        }

        @Test
        @DisplayName("Getter et setter pour code postal")
        void testCodePostalGetterSetter() {
            adresse.setCodePostal(VALID_CODE_POSTAL);
            assertEquals(VALID_CODE_POSTAL, adresse.getCodePostal());
            
            adresse.setCodePostal(null);
            assertNull(adresse.getCodePostal());
        }

        @Test
        @DisplayName("Getter et setter pour ville")
        void testVilleGetterSetter() {
            adresse.setVille(VALID_VILLE);
            assertEquals(VALID_VILLE, adresse.getVille());
            
            adresse.setVille(null);
            assertNull(adresse.getVille());
        }

        @Test
        @DisplayName("Getter et setter pour pays")
        void testPaysGetterSetter() {
            adresse.setPays(VALID_PAYS);
            assertEquals(VALID_PAYS, adresse.getPays());
            
            adresse.setPays(null);
            assertNull(adresse.getPays());
        }
    }

    @Nested
    @DisplayName("Tests de validation des données")
    class ValidationTests {

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  ", "\t", "\n"})
        @DisplayName("Numéro de rue vide ou blanc doit être accepté (pas de validation côté modèle)")
        void testNumeroRueWithEmptyOrBlankValues(String emptyValue) {
            adresse.setNumeroRue(emptyValue);
            assertEquals(emptyValue, adresse.getNumeroRue());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  ", "\t", "\n"})
        @DisplayName("Nom de rue vide ou blanc doit être accepté (pas de validation côté modèle)")
        void testNomRueWithEmptyOrBlankValues(String emptyValue) {
            adresse.setNomRue(emptyValue);
            assertEquals(emptyValue, adresse.getNomRue());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  ", "\t", "\n"})
        @DisplayName("Code postal vide ou blanc doit être accepté (pas de validation côté modèle)")
        void testCodePostalWithEmptyOrBlankValues(String emptyValue) {
            adresse.setCodePostal(emptyValue);
            assertEquals(emptyValue, adresse.getCodePostal());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  ", "\t", "\n"})
        @DisplayName("Ville vide ou blanc doit être accepté (pas de validation côté modèle)")
        void testVilleWithEmptyOrBlankValues(String emptyValue) {
            adresse.setVille(emptyValue);
            assertEquals(emptyValue, adresse.getVille());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  ", "\t", "\n"})
        @DisplayName("Pays vide ou blanc doit être accepté (pas de validation côté modèle)")
        void testPaysWithEmptyOrBlankValues(String emptyValue) {
            adresse.setPays(emptyValue);
            assertEquals(emptyValue, adresse.getPays());
        }

        @Test
        @DisplayName("Numéro de rue avec caractères spéciaux doit être accepté")
        void testNumeroRueWithSpecialCharacters() {
            String specialNumero = "123 bis A";
            adresse.setNumeroRue(specialNumero);
            assertEquals(specialNumero, adresse.getNumeroRue());
        }

        @Test
        @DisplayName("Nom de rue avec caractères spéciaux doit être accepté")
        void testNomRueWithSpecialCharacters() {
            String specialNomRue = "Rue de l'Église-Saint-Michel";
            adresse.setNomRue(specialNomRue);
            assertEquals(specialNomRue, adresse.getNomRue());
        }

        @Test
        @DisplayName("Code postal avec format international doit être accepté")
        void testCodePostalWithInternationalFormat() {
            String internationalCode = "SW1A 1AA"; // Format UK
            adresse.setCodePostal(internationalCode);
            assertEquals(internationalCode, adresse.getCodePostal());
        }

        @Test
        @DisplayName("Ville avec caractères spéciaux doit être acceptée")
        void testVilleWithSpecialCharacters() {
            String specialVille = "Saint-Étienne-du-Rouvray";
            adresse.setVille(specialVille);
            assertEquals(specialVille, adresse.getVille());
        }

        @Test
        @DisplayName("Pays avec caractères spéciaux doit être accepté")
        void testPaysWithSpecialCharacters() {
            String specialPays = "Côte d'Ivoire";
            adresse.setPays(specialPays);
            assertEquals(specialPays, adresse.getPays());
        }
    }

    @Nested
    @DisplayName("Tests des cas limites")
    class EdgeCaseTests {

        @Test
        @DisplayName("ID négatif doit être accepté")
        void testNegativeId() {
            Integer negativeId = -1;
            adresse.setIdentifiant(negativeId);
            assertEquals(negativeId, adresse.getIdentifiant());
        }

        @Test
        @DisplayName("ID zéro doit être accepté")
        void testZeroId() {
            Integer zeroId = 0;
            adresse.setIdentifiant(zeroId);
            assertEquals(zeroId, adresse.getIdentifiant());
        }

        @Test
        @DisplayName("Numéro de rue très long doit être accepté")
        void testVeryLongNumeroRue() {
            String longNumero = "n".repeat(1000);
            adresse.setNumeroRue(longNumero);
            assertEquals(longNumero, adresse.getNumeroRue());
        }

        @Test
        @DisplayName("Nom de rue très long doit être accepté")
        void testVeryLongNomRue() {
            String longNomRue = "r".repeat(1000);
            adresse.setNomRue(longNomRue);
            assertEquals(longNomRue, adresse.getNomRue());
        }

        @Test
        @DisplayName("Code postal très long doit être accepté")
        void testVeryLongCodePostal() {
            String longCodePostal = "c".repeat(100);
            adresse.setCodePostal(longCodePostal);
            assertEquals(longCodePostal, adresse.getCodePostal());
        }

        @Test
        @DisplayName("Ville très longue doit être acceptée")
        void testVeryLongVille() {
            String longVille = "v".repeat(1000);
            adresse.setVille(longVille);
            assertEquals(longVille, adresse.getVille());
        }

        @Test
        @DisplayName("Pays très long doit être accepté")
        void testVeryLongPays() {
            String longPays = "p".repeat(1000);
            adresse.setPays(longPays);
            assertEquals(longPays, adresse.getPays());
        }

        @Test
        @DisplayName("Numéro de rue avec zéros en tête doit être accepté")
        void testNumeroRueWithLeadingZeros() {
            String numeroWithZeros = "001";
            adresse.setNumeroRue(numeroWithZeros);
            assertEquals(numeroWithZeros, adresse.getNumeroRue());
        }

        @Test
        @DisplayName("Code postal avec zéros en tête doit être accepté")
        void testCodePostalWithLeadingZeros() {
            String codeWithZeros = "00100";
            adresse.setCodePostal(codeWithZeros);
            assertEquals(codeWithZeros, adresse.getCodePostal());
        }
    }

    @Nested
    @DisplayName("Tests de persistance et état")
    class PersistenceTests {

        @Test
        @DisplayName("Adresse doit conserver son état après modifications multiples")
        void testAdresseStateConservation() {
            // Initialisation
            adresse.setIdentifiant(VALID_ID);
            adresse.setNumeroRue(VALID_NUMERO);
            adresse.setNomRue(VALID_NOM_RUE);
            adresse.setCodePostal(VALID_CODE_POSTAL);
            adresse.setVille(VALID_VILLE);
            adresse.setPays(VALID_PAYS);

            // Vérification de l'état initial
            assertEquals(VALID_ID, adresse.getIdentifiant());
            assertEquals(VALID_NUMERO, adresse.getNumeroRue());
            assertEquals(VALID_NOM_RUE, adresse.getNomRue());
            assertEquals(VALID_CODE_POSTAL, adresse.getCodePostal());
            assertEquals(VALID_VILLE, adresse.getVille());
            assertEquals(VALID_PAYS, adresse.getPays());

            // Modifications
            adresse.setNumeroRue("456");
            adresse.setNomRue("Avenue des Champs");
            adresse.setCodePostal("69000");

            // Vérification que les autres champs sont conservés
            assertEquals(VALID_ID, adresse.getIdentifiant());
            assertEquals("456", adresse.getNumeroRue());
            assertEquals("Avenue des Champs", adresse.getNomRue());
            assertEquals("69000", adresse.getCodePostal());
            assertEquals(VALID_VILLE, adresse.getVille());
            assertEquals(VALID_PAYS, adresse.getPays());
        }

        @Test
        @DisplayName("Adresse doit être mutable")
        void testAdresseMutability() {
            // Création d'une adresse complète
            Adresse originalAdresse = new Adresse(VALID_NUMERO, VALID_NOM_RUE, 
                                                VALID_CODE_POSTAL, VALID_VILLE);
            originalAdresse.setIdentifiant(VALID_ID);
            originalAdresse.setPays(VALID_PAYS);

            // Modification de tous les champs
            originalAdresse.setIdentifiant(999);
            originalAdresse.setNumeroRue("789");
            originalAdresse.setNomRue("Boulevard de la République");
            originalAdresse.setCodePostal("13000");
            originalAdresse.setVille("Marseille");
            originalAdresse.setPays("France");

            // Vérification des modifications
            assertEquals(Integer.valueOf(999), originalAdresse.getIdentifiant());
            assertEquals("789", originalAdresse.getNumeroRue());
            assertEquals("Boulevard de la République", originalAdresse.getNomRue());
            assertEquals("13000", originalAdresse.getCodePostal());
            assertEquals("Marseille", originalAdresse.getVille());
            assertEquals("France", originalAdresse.getPays());
        }
    }

    @Nested
    @DisplayName("Tests de scénarios métier")
    class BusinessScenarioTests {

        @Test
        @DisplayName("Création d'une adresse française complète")
        void testFrenchAddressCreation() {
            Adresse frenchAdresse = new Adresse();
            frenchAdresse.setNumeroRue("15");
            frenchAdresse.setNomRue("Rue de Rivoli");
            frenchAdresse.setCodePostal("75001");
            frenchAdresse.setVille("Paris");
            frenchAdresse.setPays("France");

            assertEquals("15", frenchAdresse.getNumeroRue());
            assertEquals("Rue de Rivoli", frenchAdresse.getNomRue());
            assertEquals("75001", frenchAdresse.getCodePostal());
            assertEquals("Paris", frenchAdresse.getVille());
            assertEquals("France", frenchAdresse.getPays());
        }

        @Test
        @DisplayName("Création d'une adresse avec numéro complexe")
        void testAddressWithComplexNumber() {
            Adresse complexAdresse = new Adresse();
            complexAdresse.setNumeroRue("2 bis A");
            complexAdresse.setNomRue("Rue du Commerce");
            complexAdresse.setCodePostal("44000");
            complexAdresse.setVille("Nantes");
            complexAdresse.setPays("France");

            assertEquals("2 bis A", complexAdresse.getNumeroRue());
            assertEquals("Rue du Commerce", complexAdresse.getNomRue());
            assertEquals("44000", complexAdresse.getCodePostal());
            assertEquals("Nantes", complexAdresse.getVille());
            assertEquals("France", complexAdresse.getPays());
        }

        @Test
        @DisplayName("Création d'une adresse internationale")
        void testInternationalAddressCreation() {
            Adresse internationalAdresse = new Adresse();
            internationalAdresse.setNumeroRue("221B");
            internationalAdresse.setNomRue("Baker Street");
            internationalAdresse.setCodePostal("NW1 6XE");
            internationalAdresse.setVille("London");
            internationalAdresse.setPays("United Kingdom");

            assertEquals("221B", internationalAdresse.getNumeroRue());
            assertEquals("Baker Street", internationalAdresse.getNomRue());
            assertEquals("NW1 6XE", internationalAdresse.getCodePostal());
            assertEquals("London", internationalAdresse.getVille());
            assertEquals("United Kingdom", internationalAdresse.getPays());
        }

        @Test
        @DisplayName("Adresse sans pays")
        void testAddressWithoutCountry() {
            Adresse noCountryAdresse = new Adresse();
            noCountryAdresse.setNumeroRue("10");
            noCountryAdresse.setNomRue("Rue de la Liberté");
            noCountryAdresse.setCodePostal("67000");
            noCountryAdresse.setVille("Strasbourg");

            assertNull(noCountryAdresse.getPays());
            assertEquals("10", noCountryAdresse.getNumeroRue());
            assertEquals("Rue de la Liberté", noCountryAdresse.getNomRue());
            assertEquals("67000", noCountryAdresse.getCodePostal());
            assertEquals("Strasbourg", noCountryAdresse.getVille());
        }

        @Test
        @DisplayName("Adresse avec caractères accentués")
        void testAddressWithAccentedCharacters() {
            Adresse accentedAdresse = new Adresse();
            accentedAdresse.setNumeroRue("1");
            accentedAdresse.setNomRue("Rue de l'Église");
            accentedAdresse.setCodePostal("69000");
            accentedAdresse.setVille("Lyon");
            accentedAdresse.setPays("France");

            assertEquals("1", accentedAdresse.getNumeroRue());
            assertEquals("Rue de l'Église", accentedAdresse.getNomRue());
            assertEquals("69000", accentedAdresse.getCodePostal());
            assertEquals("Lyon", accentedAdresse.getVille());
            assertEquals("France", accentedAdresse.getPays());
        }
    }

    @Nested
    @DisplayName("Tests de la méthode toString")
    class ToStringTests {

        @Test
        @DisplayName("toString avec adresse complète")
        void testToStringWithCompleteAddress() {
            adresse.setIdentifiant(VALID_ID);
            adresse.setNumeroRue(VALID_NUMERO);
            adresse.setNomRue(VALID_NOM_RUE);
            adresse.setCodePostal(VALID_CODE_POSTAL);
            adresse.setVille(VALID_VILLE);
            adresse.setPays(VALID_PAYS);

            String result = adresse.toString();
            
            assertNotNull(result);
            assertTrue(result.contains(VALID_NUMERO));
            assertTrue(result.contains(VALID_NOM_RUE));
            assertTrue(result.contains(VALID_CODE_POSTAL));
            assertTrue(result.contains(VALID_VILLE));
            // Note: toString() ne contient pas le pays dans sa représentation
            // Format attendu: "numeroRue nomRue, codePostal ville"
            assertEquals(VALID_NUMERO + " " + VALID_NOM_RUE + ", " + VALID_CODE_POSTAL + " " + VALID_VILLE, result);
        }

        @Test
        @DisplayName("toString avec adresse partielle")
        void testToStringWithPartialAddress() {
            adresse.setNumeroRue(VALID_NUMERO);
            adresse.setNomRue(VALID_NOM_RUE);
            adresse.setCodePostal(VALID_CODE_POSTAL);
            adresse.setVille(VALID_VILLE);
            // Pas de pays défini

            String result = adresse.toString();
            
            assertNotNull(result);
            assertTrue(result.contains(VALID_NUMERO));
            assertTrue(result.contains(VALID_NOM_RUE));
            assertTrue(result.contains(VALID_CODE_POSTAL));
            assertTrue(result.contains(VALID_VILLE));
        }

        @Test
        @DisplayName("toString avec adresse vide")
        void testToStringWithEmptyAddress() {
            String result = adresse.toString();
            
            assertNotNull(result);
            // Vérification que la méthode ne génère pas d'erreur avec des valeurs null
        }

        @Test
        @DisplayName("toString avec valeurs nulles")
        void testToStringWithNullValues() {
            adresse.setIdentifiant(null);
            adresse.setNumeroRue(null);
            adresse.setNomRue(null);
            adresse.setCodePostal(null);
            adresse.setVille(null);
            adresse.setPays(null);

            String result = adresse.toString();
            
            assertNotNull(result);
            // Vérification que la méthode gère correctement les valeurs null
        }
    }
} 