package models;

import models.Prospect;
import models.Adresse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe Prospect.
 * 
 * <p>Cette classe de test couvre tous les aspects de la classe Prospect :
 * - Constructeurs
 * - Getters et setters
 * - Validation des données métier
 * - Cas limites et d'erreur
 * - Héritage de Societe
 * </p>
 * 
 * @author CliprocoJEE
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Tests unitaires pour la classe Prospect")
class ProspectTest {

    private Prospect prospect;
    private Adresse adresse;
    
    private static final Integer VALID_ID = 1;
    private static final String VALID_RAISON_SOCIALE = "Prospect Test";
    private static final String VALID_NOM = "Martin";
    private static final String VALID_PRENOM = "Pierre";
    private static final String VALID_TELEPHONE = "0123456789";
    private static final String VALID_MAIL = "contact@prospect-test.com";
    private static final String VALID_COMMENTAIRES = "Prospect intéressé par nos services";
    private static final java.sql.Date VALID_DATE_PROSPECTION = java.sql.Date.valueOf("2024-01-15");
    private static final Boolean VALID_PROSPECT_INTERESSE = true;

    @BeforeEach
    void setUp() {
        prospect = new Prospect();
        adresse = new Adresse("123", "Rue de la Paix", "75001", "Paris");
        adresse.setPays("France");
    }

    @Nested
    @DisplayName("Tests des constructeurs")
    class ConstructorTests {

        @Test
        @DisplayName("Constructeur par défaut doit créer un prospect vide")
        void testDefaultConstructor() {
            Prospect emptyProspect = new Prospect();
            
            assertNotNull(emptyProspect);
            assertNull(emptyProspect.getIdentifiant());
            assertNull(emptyProspect.getRaisonSociale());
            assertNull(emptyProspect.getAdresse());
            assertNull(emptyProspect.getTelephone());
            assertNull(emptyProspect.getMail());
            assertNull(emptyProspect.getCommentaires());
            assertNull(emptyProspect.getDateProspection());
            assertNull(emptyProspect.getProspectInteresse());
        }

        @Test
        @DisplayName("Constructeur avec paramètres de base doit initialiser correctement")
        void testBasicConstructor() {
            Prospect basicProspect = new Prospect(VALID_RAISON_SOCIALE, VALID_NOM, VALID_PRENOM,
                                                 adresse, VALID_TELEPHONE, VALID_MAIL, 
                                                 VALID_COMMENTAIRES, VALID_DATE_PROSPECTION);
            
            assertNotNull(basicProspect);
            assertEquals(VALID_RAISON_SOCIALE, basicProspect.getRaisonSociale());
            assertEquals(VALID_NOM, basicProspect.getNom());
            assertEquals(VALID_PRENOM, basicProspect.getPrenom());
            assertEquals(adresse, basicProspect.getAdresse());
            assertEquals(VALID_TELEPHONE, basicProspect.getTelephone());
            assertEquals(VALID_MAIL, basicProspect.getMail());
            assertEquals(VALID_COMMENTAIRES, basicProspect.getCommentaires());
            assertEquals(VALID_DATE_PROSPECTION, basicProspect.getDateProspection());
            assertNull(basicProspect.getIdentifiant()); // Généré par la base de données
        }

        @Test
        @DisplayName("Constructeur avec paramètres null doit être accepté")
        void testConstructorWithNullValues() {
            Prospect nullProspect = new Prospect(null, null, null, null, null, null, null, null);
            
            assertNotNull(nullProspect);
            assertNull(nullProspect.getRaisonSociale());
            assertNull(nullProspect.getNom());
            assertNull(nullProspect.getPrenom());
            assertNull(nullProspect.getAdresse());
            assertNull(nullProspect.getTelephone());
            assertNull(nullProspect.getMail());
            assertNull(nullProspect.getCommentaires());
            assertNull(nullProspect.getDateProspection());
        }
    }

    @Nested
    @DisplayName("Tests des getters et setters")
    class GetterSetterTests {

        @Test
        @DisplayName("Getter et setter pour identifiant")
        void testIdentifiantGetterSetter() {
            prospect.setIdentifiant(VALID_ID);
            assertEquals(VALID_ID, prospect.getIdentifiant());
            
            prospect.setIdentifiant(null);
            assertNull(prospect.getIdentifiant());
        }

        @Test
        @DisplayName("Getter et setter pour raison sociale")
        void testRaisonSocialeGetterSetter() {
            prospect.setRaisonSociale(VALID_RAISON_SOCIALE);
            assertEquals(VALID_RAISON_SOCIALE, prospect.getRaisonSociale());
            
            prospect.setRaisonSociale(null);
            assertNull(prospect.getRaisonSociale());
        }

        @Test
        @DisplayName("Getter et setter pour adresse")
        void testAdresseGetterSetter() {
            prospect.setAdresse(adresse);
            assertEquals(adresse, prospect.getAdresse());
            
            prospect.setAdresse(null);
            assertNull(prospect.getAdresse());
        }

        @Test
        @DisplayName("Getter et setter pour téléphone")
        void testTelephoneGetterSetter() {
            prospect.setTelephone(VALID_TELEPHONE);
            assertEquals(VALID_TELEPHONE, prospect.getTelephone());
            
            prospect.setTelephone(null);
            assertNull(prospect.getTelephone());
        }

        @Test
        @DisplayName("Getter et setter pour mail")
        void testMailGetterSetter() {
            prospect.setMail(VALID_MAIL);
            assertEquals(VALID_MAIL, prospect.getMail());
            
            prospect.setMail(null);
            assertNull(prospect.getMail());
        }

        @Test
        @DisplayName("Getter et setter pour commentaires")
        void testCommentairesGetterSetter() {
            prospect.setCommentaires(VALID_COMMENTAIRES);
            assertEquals(VALID_COMMENTAIRES, prospect.getCommentaires());
            
            prospect.setCommentaires(null);
            assertNull(prospect.getCommentaires());
        }

        @Test
        @DisplayName("Getter et setter pour date de prospection")
        void testDateProspectionGetterSetter() {
            prospect.setDateProspection(VALID_DATE_PROSPECTION);
            assertEquals(VALID_DATE_PROSPECTION, prospect.getDateProspection());
            
            prospect.setDateProspection(null);
            assertNull(prospect.getDateProspection());
        }

        @Test
        @DisplayName("Getter et setter pour prospect intéressé")
        void testProspectInteresseGetterSetter() {
            prospect.setProspectInteresse(VALID_PROSPECT_INTERESSE);
            assertEquals(VALID_PROSPECT_INTERESSE, prospect.getProspectInteresse());
            
            prospect.setProspectInteresse(null);
            assertNull(prospect.getProspectInteresse());
        }
    }

    @Nested
    @DisplayName("Tests de validation des données")
    class ValidationTests {

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  ", "\t", "\n"})
        @DisplayName("Raison sociale vide ou blanc doit être accepté (pas de validation côté modèle)")
        void testRaisonSocialeWithEmptyOrBlankValues(String emptyValue) {
            prospect.setRaisonSociale(emptyValue);
            assertEquals(emptyValue, prospect.getRaisonSociale());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  ", "\t", "\n"})
        @DisplayName("Téléphone vide ou blanc doit être accepté (pas de validation côté modèle)")
        void testTelephoneWithEmptyOrBlankValues(String emptyValue) {
            prospect.setTelephone(emptyValue);
            assertEquals(emptyValue, prospect.getTelephone());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  ", "\t", "\n"})
        @DisplayName("Mail vide ou blanc doit être accepté (pas de validation côté modèle)")
        void testMailWithEmptyOrBlankValues(String emptyValue) {
            prospect.setMail(emptyValue);
            assertEquals(emptyValue, prospect.getMail());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  ", "\t", "\n"})
        @DisplayName("Commentaires vide ou blanc doit être accepté (pas de validation côté modèle)")
        void testCommentairesWithEmptyOrBlankValues(String emptyValue) {
            prospect.setCommentaires(emptyValue);
            assertEquals(emptyValue, prospect.getCommentaires());
        }

        @Test
        @DisplayName("Raison sociale avec caractères spéciaux doit être acceptée")
        void testRaisonSocialeWithSpecialCharacters() {
            String specialRaisonSociale = "Prospect & Fils SARL - 123!@#";
            prospect.setRaisonSociale(specialRaisonSociale);
            assertEquals(specialRaisonSociale, prospect.getRaisonSociale());
        }

        @Test
        @DisplayName("Téléphone avec format international doit être accepté")
        void testTelephoneWithInternationalFormat() {
            String internationalPhone = "+33 1 23 45 67 89";
            prospect.setTelephone(internationalPhone);
            assertEquals(internationalPhone, prospect.getTelephone());
        }

        @Test
        @DisplayName("Mail avec format complexe doit être accepté")
        void testMailWithComplexFormat() {
            String complexMail = "contact+prospect@entreprise-test.co.uk";
            prospect.setMail(complexMail);
            assertEquals(complexMail, prospect.getMail());
        }

        @Test
        @DisplayName("Commentaires avec caractères spéciaux doit être accepté")
        void testCommentairesWithSpecialCharacters() {
            String specialCommentaires = "Prospect intéressé depuis 3 mois - Contact: Jean Dupont (01.23.45.67.89)";
            prospect.setCommentaires(specialCommentaires);
            assertEquals(specialCommentaires, prospect.getCommentaires());
        }
    }

    @Nested
    @DisplayName("Tests des cas limites")
    class EdgeCaseTests {

        @Test
        @DisplayName("ID négatif doit être accepté")
        void testNegativeId() {
            Integer negativeId = -1;
            prospect.setIdentifiant(negativeId);
            assertEquals(negativeId, prospect.getIdentifiant());
        }

        @Test
        @DisplayName("ID zéro doit être accepté")
        void testZeroId() {
            Integer zeroId = 0;
            prospect.setIdentifiant(zeroId);
            assertEquals(zeroId, prospect.getIdentifiant());
        }

        @Test
        @DisplayName("Date de prospection dans le passé doit être acceptée")
        void testPastDateProspection() {
            Date pastDate = Date.valueOf(LocalDate.now().minusDays(30));
            prospect.setDateProspection(pastDate);
            assertEquals(pastDate, prospect.getDateProspection());
        }

        @Test
        @DisplayName("Date de prospection dans le futur doit être acceptée")
        void testFutureDateProspection() {
            Date futureDate = Date.valueOf(LocalDate.now().plusDays(30));
            prospect.setDateProspection(futureDate);
            assertEquals(futureDate, prospect.getDateProspection());
        }

        @Test
        @DisplayName("Date de prospection très éloignée doit être acceptée")
        void testFarFutureDateProspection() {
            Date farFutureDate = Date.valueOf(LocalDate.now().plusYears(10));
            prospect.setDateProspection(farFutureDate);
            assertEquals(farFutureDate, prospect.getDateProspection());
        }

        @Test
        @DisplayName("Prospect intéressé avec valeur false doit être accepté")
        void testProspectInteresseFalse() {
            prospect.setProspectInteresse(false);
            assertEquals(false, prospect.getProspectInteresse());
        }

        @Test
        @DisplayName("Raison sociale très longue doit être acceptée")
        void testVeryLongRaisonSociale() {
            String longRaisonSociale = "p".repeat(1000);
            prospect.setRaisonSociale(longRaisonSociale);
            assertEquals(longRaisonSociale, prospect.getRaisonSociale());
        }

        @Test
        @DisplayName("Commentaires très longs doivent être acceptés")
        void testVeryLongCommentaires() {
            String longCommentaires = "c".repeat(10000);
            prospect.setCommentaires(longCommentaires);
            assertEquals(longCommentaires, prospect.getCommentaires());
        }
    }

    @Nested
    @DisplayName("Tests de persistance et état")
    class PersistenceTests {

        @Test
        @DisplayName("Prospect doit conserver son état après modifications multiples")
        void testProspectStateConservation() {
            // Initialisation
            prospect.setIdentifiant(VALID_ID);
            prospect.setRaisonSociale(VALID_RAISON_SOCIALE);
            prospect.setAdresse(adresse);
            prospect.setTelephone(VALID_TELEPHONE);
            prospect.setMail(VALID_MAIL);
            prospect.setCommentaires(VALID_COMMENTAIRES);
            prospect.setDateProspection(VALID_DATE_PROSPECTION);
            prospect.setProspectInteresse(VALID_PROSPECT_INTERESSE);

            // Vérification de l'état initial
            assertEquals(VALID_ID, prospect.getIdentifiant());
            assertEquals(VALID_RAISON_SOCIALE, prospect.getRaisonSociale());
            assertEquals(adresse, prospect.getAdresse());
            assertEquals(VALID_TELEPHONE, prospect.getTelephone());
            assertEquals(VALID_MAIL, prospect.getMail());
            assertEquals(VALID_COMMENTAIRES, prospect.getCommentaires());
            assertEquals(VALID_DATE_PROSPECTION, prospect.getDateProspection());
            assertEquals(VALID_PROSPECT_INTERESSE, prospect.getProspectInteresse());

            // Modifications
            prospect.setRaisonSociale("Nouveau Prospect");
            prospect.setDateProspection(Date.valueOf(LocalDate.now().plusDays(7)));
            prospect.setProspectInteresse(false);

            // Vérification que les autres champs sont conservés
            assertEquals(VALID_ID, prospect.getIdentifiant());
            assertEquals("Nouveau Prospect", prospect.getRaisonSociale());
            assertEquals(adresse, prospect.getAdresse());
            assertEquals(VALID_TELEPHONE, prospect.getTelephone());
            assertEquals(VALID_MAIL, prospect.getMail());
            assertEquals(VALID_COMMENTAIRES, prospect.getCommentaires());
            assertNotNull(prospect.getDateProspection());
            assertEquals(false, prospect.getProspectInteresse());
        }

        @Test
        @DisplayName("Prospect doit être mutable")
        void testProspectMutability() {
            // Création d'un prospect complet
            Prospect originalProspect = new Prospect(VALID_RAISON_SOCIALE, VALID_NOM, VALID_PRENOM,
                                                   adresse, VALID_TELEPHONE, VALID_MAIL, 
                                                   VALID_COMMENTAIRES, VALID_DATE_PROSPECTION);

            // Modification de tous les champs
            Adresse newAdresse = new Adresse("456", "Avenue des Champs", "69000", "Lyon");
            newAdresse.setPays("France");
            originalProspect.setIdentifiant(999);
            originalProspect.setRaisonSociale("Prospect Modifié");
            originalProspect.setNom("Nouveau");
            originalProspect.setPrenom("Contact");
            originalProspect.setAdresse(newAdresse);
            originalProspect.setTelephone("0987654321");
            originalProspect.setMail("nouveau@prospect.com");
            originalProspect.setCommentaires("Nouveaux commentaires prospect");
            originalProspect.setDateProspection(Date.valueOf(LocalDate.now().plusDays(14)));
            originalProspect.setProspectInteresse(true);

            // Vérification des modifications
            assertEquals(Integer.valueOf(999), originalProspect.getIdentifiant());
            assertEquals("Prospect Modifié", originalProspect.getRaisonSociale());
            assertEquals("Nouveau", originalProspect.getNom());
            assertEquals("Contact", originalProspect.getPrenom());
            assertEquals(newAdresse, originalProspect.getAdresse());
            assertEquals("0987654321", originalProspect.getTelephone());
            assertEquals("nouveau@prospect.com", originalProspect.getMail());
            assertEquals("Nouveaux commentaires prospect", originalProspect.getCommentaires());
            assertNotNull(originalProspect.getDateProspection());
            assertEquals(true, originalProspect.getProspectInteresse());
        }
    }

    @Nested
    @DisplayName("Tests de scénarios métier")
    class BusinessScenarioTests {

        @Test
        @DisplayName("Création d'un prospect intéressé")
        void testInterestedProspectCreation() {
            Prospect interestedProspect = new Prospect();
            interestedProspect.setRaisonSociale("Prospect Intéressé SARL");
            Adresse interestedAdresse = new Adresse("10", "Rue du Commerce", "31000", "Toulouse");
            interestedAdresse.setPays("France");
            interestedProspect.setAdresse(interestedAdresse);
            interestedProspect.setTelephone("05.61.23.45.67");
            interestedProspect.setMail("contact@prospect-interesse.fr");
            interestedProspect.setCommentaires("Très intéressé par nos services - RDV prévu");
            interestedProspect.setDateProspection(Date.valueOf(LocalDate.now()));
            interestedProspect.setProspectInteresse(true);

            assertEquals("Prospect Intéressé SARL", interestedProspect.getRaisonSociale());
            assertEquals("05.61.23.45.67", interestedProspect.getTelephone());
            assertEquals("contact@prospect-interesse.fr", interestedProspect.getMail());
            assertEquals("Très intéressé par nos services - RDV prévu", interestedProspect.getCommentaires());
            assertEquals(Date.valueOf(LocalDate.now()), interestedProspect.getDateProspection());
            assertEquals(true, interestedProspect.getProspectInteresse());
        }

        @Test
        @DisplayName("Création d'un prospect non intéressé")
        void testNotInterestedProspectCreation() {
            Prospect notInterestedProspect = new Prospect();
            notInterestedProspect.setRaisonSociale("Prospect Non Intéressé");
            Adresse notInterestedAdresse = new Adresse("20", "Rue de la Paix", "75001", "Paris");
            notInterestedAdresse.setPays("France");
            notInterestedProspect.setAdresse(notInterestedAdresse);
            notInterestedProspect.setTelephone("01.42.34.56.78");
            notInterestedProspect.setMail("contact@prospect-non-interesse.fr");
            notInterestedProspect.setCommentaires("Pas intéressé pour le moment - à recontacter dans 6 mois");
            notInterestedProspect.setDateProspection(Date.valueOf(LocalDate.now().minusDays(7)));
            notInterestedProspect.setProspectInteresse(false);

            assertEquals("Prospect Non Intéressé", notInterestedProspect.getRaisonSociale());
            assertEquals("01.42.34.56.78", notInterestedProspect.getTelephone());
            assertEquals("contact@prospect-non-interesse.fr", notInterestedProspect.getMail());
            assertEquals("Pas intéressé pour le moment - à recontacter dans 6 mois", notInterestedProspect.getCommentaires());
            assertEquals(Date.valueOf(LocalDate.now().minusDays(7)), notInterestedProspect.getDateProspection());
            assertEquals(false, notInterestedProspect.getProspectInteresse());
        }

        @Test
        @DisplayName("Prospect sans adresse")
        void testProspectWithoutAddress() {
            Prospect noAddressProspect = new Prospect();
            noAddressProspect.setRaisonSociale("Prospect Sans Adresse");
            noAddressProspect.setTelephone("01.23.45.67.89");
            noAddressProspect.setMail("contact@sans-adresse-prospect.com");
            noAddressProspect.setCommentaires("Prospect contacté par téléphone");
            noAddressProspect.setDateProspection(Date.valueOf(LocalDate.now()));
            noAddressProspect.setProspectInteresse(true);

            assertNull(noAddressProspect.getAdresse());
            assertEquals("Prospect Sans Adresse", noAddressProspect.getRaisonSociale());
            assertEquals("01.23.45.67.89", noAddressProspect.getTelephone());
            assertEquals("contact@sans-adresse-prospect.com", noAddressProspect.getMail());
            assertEquals("Prospect contacté par téléphone", noAddressProspect.getCommentaires());
            assertEquals(Date.valueOf(LocalDate.now()), noAddressProspect.getDateProspection());
            assertEquals(true, noAddressProspect.getProspectInteresse());
        }

        @Test
        @DisplayName("Prospect sans commentaires")
        void testProspectWithoutComments() {
            Prospect noCommentsProspect = new Prospect();
            noCommentsProspect.setRaisonSociale("Prospect Sans Commentaires");
            noCommentsProspect.setAdresse(adresse);
            noCommentsProspect.setTelephone("01.23.45.67.89");
            noCommentsProspect.setMail("contact@sans-commentaires-prospect.com");
            noCommentsProspect.setDateProspection(Date.valueOf(LocalDate.now()));
            noCommentsProspect.setProspectInteresse(false);

            assertNull(noCommentsProspect.getCommentaires());
            assertEquals("Prospect Sans Commentaires", noCommentsProspect.getRaisonSociale());
            assertEquals(adresse, noCommentsProspect.getAdresse());
            assertEquals("01.23.45.67.89", noCommentsProspect.getTelephone());
            assertEquals("contact@sans-commentaires-prospect.com", noCommentsProspect.getMail());
            assertEquals(Date.valueOf(LocalDate.now()), noCommentsProspect.getDateProspection());
            assertEquals(false, noCommentsProspect.getProspectInteresse());
        }

        @Test
        @DisplayName("Prospect avec statut non défini")
        void testProspectWithUndefinedStatus() {
            Prospect undefinedProspect = new Prospect();
            undefinedProspect.setRaisonSociale("Prospect Statut Non Défini");
            undefinedProspect.setAdresse(adresse);
            undefinedProspect.setTelephone("01.23.45.67.89");
            undefinedProspect.setMail("contact@statut-non-defini.com");
            undefinedProspect.setCommentaires("Premier contact - statut à déterminer");
            undefinedProspect.setDateProspection(Date.valueOf(LocalDate.now()));
            // Pas de setProspectInteresse

            assertNull(undefinedProspect.getProspectInteresse());
            assertEquals("Prospect Statut Non Défini", undefinedProspect.getRaisonSociale());
            assertEquals(adresse, undefinedProspect.getAdresse());
            assertEquals("01.23.45.67.89", undefinedProspect.getTelephone());
            assertEquals("contact@statut-non-defini.com", undefinedProspect.getMail());
            assertEquals("Premier contact - statut à déterminer", undefinedProspect.getCommentaires());
            assertEquals(Date.valueOf(LocalDate.now()), undefinedProspect.getDateProspection());
        }
    }

    @Nested
    @DisplayName("Tests d'héritage")
    class InheritanceTests {

        @Test
        @DisplayName("Prospect doit hériter de Societe")
        void testProspectInheritsFromSociete() {
            Prospect prospect = new Prospect();
            
            // Vérification que Prospect est une instance de Societe
            assertTrue(prospect instanceof Societe);
            
            // Vérification que les méthodes de Societe sont accessibles
            prospect.setRaisonSociale("Test");
            assertEquals("Test", prospect.getRaisonSociale());
        }

        @Test
        @DisplayName("Prospect doit avoir ses propres propriétés en plus de celles de Societe")
        void testProspectHasOwnProperties() {
            Prospect prospect = new Prospect();
            
            // Propriétés héritées de Societe
            prospect.setRaisonSociale("Société Prospect Test");
            prospect.setTelephone("01.23.45.67.89");
            prospect.setMail("test@prospect-societe.com");
            prospect.setCommentaires("Commentaires test prospect");
            
            // Propriétés spécifiques à Prospect
            prospect.setDateProspection(Date.valueOf(LocalDate.now()));
            prospect.setProspectInteresse(true);
            
            // Vérification des propriétés héritées
            assertEquals("Société Prospect Test", prospect.getRaisonSociale());
            assertEquals("01.23.45.67.89", prospect.getTelephone());
            assertEquals("test@prospect-societe.com", prospect.getMail());
            assertEquals("Commentaires test prospect", prospect.getCommentaires());
            
            // Vérification des propriétés spécifiques
            assertEquals(Date.valueOf(LocalDate.now()), prospect.getDateProspection());
            assertEquals(true, prospect.getProspectInteresse());
        }
    }
} 