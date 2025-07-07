package models;

import models.Client;
import models.Adresse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe Client.
 * 
 * <p>Cette classe de test couvre tous les aspects de la classe Client :
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
@DisplayName("Tests unitaires pour la classe Client")
class ClientTest {

    private Client client;
    private Adresse adresse;
    
    private static final Integer VALID_ID = 1;
    private static final String VALID_RAISON_SOCIALE = "Entreprise Test";
    private static final String VALID_NOM = "Dupont";
    private static final String VALID_PRENOM = "Jean";
    private static final String VALID_TELEPHONE = "0123456789";
    private static final String VALID_MAIL = "contact@entreprise-test.com";
    private static final String VALID_COMMENTAIRES = "Client fidèle depuis 5 ans";
    private static final Double VALID_CHIFFRE_AFFAIRES = 500000.0;
    private static final Integer VALID_NB_EMPLOYES = 25;

    @BeforeEach
    void setUp() {
        client = new Client();
        adresse = new Adresse("123", "Rue de la Paix", "75001", "Paris");
        adresse.setPays("France");
    }

    @Nested
    @DisplayName("Tests des constructeurs")
    class ConstructorTests {

        @Test
        @DisplayName("Constructeur par défaut doit créer un client vide")
        void testDefaultConstructor() {
            Client emptyClient = new Client();
            
            assertNotNull(emptyClient);
            assertNull(emptyClient.getIdentifiant());
            assertNull(emptyClient.getRaisonSociale());
            assertNull(emptyClient.getAdresse());
            assertNull(emptyClient.getTelephone());
            assertNull(emptyClient.getMail());
            assertNull(emptyClient.getCommentaires());
            assertNull(emptyClient.getChiffreAffaires());
            assertNull(emptyClient.getNbEmployes());
        }

        @Test
        @DisplayName("Constructeur avec paramètres de base doit initialiser correctement")
        void testBasicConstructor() {
            Client basicClient = new Client(adresse, VALID_MAIL, VALID_COMMENTAIRES, 
                                          VALID_RAISON_SOCIALE, VALID_NOM, VALID_PRENOM,
                                          VALID_TELEPHONE, VALID_CHIFFRE_AFFAIRES, VALID_NB_EMPLOYES);
            
            assertNotNull(basicClient);
            assertEquals(adresse, basicClient.getAdresse());
            assertEquals(VALID_MAIL, basicClient.getMail());
            assertEquals(VALID_COMMENTAIRES, basicClient.getCommentaires());
            assertEquals(VALID_RAISON_SOCIALE, basicClient.getRaisonSociale());
            assertEquals(VALID_NOM, basicClient.getNom());
            assertEquals(VALID_PRENOM, basicClient.getPrenom());
            assertEquals(VALID_TELEPHONE, basicClient.getTelephone());
            assertEquals(VALID_CHIFFRE_AFFAIRES, basicClient.getChiffreAffaires());
            assertEquals(VALID_NB_EMPLOYES, basicClient.getNbEmployes());
            assertNull(basicClient.getIdentifiant()); // Généré par la base de données
        }

        @Test
        @DisplayName("Constructeur complet doit initialiser tous les champs")
        void testFullConstructor() {
            Client fullClient = new Client(VALID_ID, adresse, VALID_MAIL, 
                                         VALID_COMMENTAIRES, VALID_RAISON_SOCIALE, 
                                         VALID_NOM, VALID_PRENOM, VALID_TELEPHONE, 
                                         VALID_CHIFFRE_AFFAIRES, VALID_NB_EMPLOYES);
            
            assertNotNull(fullClient);
            assertEquals(VALID_ID, fullClient.getIdentifiant());
            assertEquals(adresse, fullClient.getAdresse());
            assertEquals(VALID_MAIL, fullClient.getMail());
            assertEquals(VALID_COMMENTAIRES, fullClient.getCommentaires());
            assertEquals(VALID_RAISON_SOCIALE, fullClient.getRaisonSociale());
            assertEquals(VALID_NOM, fullClient.getNom());
            assertEquals(VALID_PRENOM, fullClient.getPrenom());
            assertEquals(VALID_TELEPHONE, fullClient.getTelephone());
            assertEquals(VALID_CHIFFRE_AFFAIRES, fullClient.getChiffreAffaires());
            assertEquals(VALID_NB_EMPLOYES, fullClient.getNbEmployes());
        }

        @Test
        @DisplayName("Constructeur avec paramètres null doit être accepté")
        void testConstructorWithNullValues() {
            Client nullClient = new Client(null, null, null, null, null, null, null, null, null);
            
            assertNotNull(nullClient);
            assertNull(nullClient.getAdresse());
            assertNull(nullClient.getMail());
            assertNull(nullClient.getCommentaires());
            assertNull(nullClient.getRaisonSociale());
            assertNull(nullClient.getNom());
            assertNull(nullClient.getPrenom());
            assertNull(nullClient.getTelephone());
            assertNull(nullClient.getChiffreAffaires());
            assertNull(nullClient.getNbEmployes());
        }
    }

    @Nested
    @DisplayName("Tests des getters et setters")
    class GetterSetterTests {

        @Test
        @DisplayName("Getter et setter pour identifiant")
        void testIdentifiantGetterSetter() {
            client.setIdentifiant(VALID_ID);
            assertEquals(VALID_ID, client.getIdentifiant());
            
            client.setIdentifiant(null);
            assertNull(client.getIdentifiant());
        }

        @Test
        @DisplayName("Getter et setter pour raison sociale")
        void testRaisonSocialeGetterSetter() {
            client.setRaisonSociale(VALID_RAISON_SOCIALE);
            assertEquals(VALID_RAISON_SOCIALE, client.getRaisonSociale());
            
            client.setRaisonSociale(null);
            assertNull(client.getRaisonSociale());
        }

        @Test
        @DisplayName("Getter et setter pour adresse")
        void testAdresseGetterSetter() {
            client.setAdresse(adresse);
            assertEquals(adresse, client.getAdresse());
            
            client.setAdresse(null);
            assertNull(client.getAdresse());
        }

        @Test
        @DisplayName("Getter et setter pour téléphone")
        void testTelephoneGetterSetter() {
            client.setTelephone(VALID_TELEPHONE);
            assertEquals(VALID_TELEPHONE, client.getTelephone());
            
            client.setTelephone(null);
            assertNull(client.getTelephone());
        }

        @Test
        @DisplayName("Getter et setter pour mail")
        void testMailGetterSetter() {
            client.setMail(VALID_MAIL);
            assertEquals(VALID_MAIL, client.getMail());
            
            client.setMail(null);
            assertNull(client.getMail());
        }

        @Test
        @DisplayName("Getter et setter pour commentaires")
        void testCommentairesGetterSetter() {
            client.setCommentaires(VALID_COMMENTAIRES);
            assertEquals(VALID_COMMENTAIRES, client.getCommentaires());
            
            client.setCommentaires(null);
            assertNull(client.getCommentaires());
        }

        @Test
        @DisplayName("Getter et setter pour chiffre d'affaires")
        void testChiffreAffairesGetterSetter() {
            client.setChiffreAffaires(VALID_CHIFFRE_AFFAIRES);
            assertEquals(VALID_CHIFFRE_AFFAIRES, client.getChiffreAffaires());
            
            client.setChiffreAffaires(null);
            assertNull(client.getChiffreAffaires());
        }

        @Test
        @DisplayName("Getter et setter pour nombre d'employés")
        void testNbEmployesGetterSetter() {
            client.setNbEmployes(VALID_NB_EMPLOYES);
            assertEquals(VALID_NB_EMPLOYES, client.getNbEmployes());
            
            client.setNbEmployes(null);
            assertNull(client.getNbEmployes());
        }
    }

    @Nested
    @DisplayName("Tests de validation des données")
    class ValidationTests {

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  ", "\t", "\n"})
        @DisplayName("Raison sociale vide ou blanc doit être accepté (pas de validation côté modèle)")
        void testRaisonSocialeWithEmptyOrBlankValues(String emptyValue) {
            client.setRaisonSociale(emptyValue);
            assertEquals(emptyValue, client.getRaisonSociale());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  ", "\t", "\n"})
        @DisplayName("Téléphone vide ou blanc doit être accepté (pas de validation côté modèle)")
        void testTelephoneWithEmptyOrBlankValues(String emptyValue) {
            client.setTelephone(emptyValue);
            assertEquals(emptyValue, client.getTelephone());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  ", "\t", "\n"})
        @DisplayName("Mail vide ou blanc doit être accepté (pas de validation côté modèle)")
        void testMailWithEmptyOrBlankValues(String emptyValue) {
            client.setMail(emptyValue);
            assertEquals(emptyValue, client.getMail());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  ", "\t", "\n"})
        @DisplayName("Commentaires vide ou blanc doit être accepté (pas de validation côté modèle)")
        void testCommentairesWithEmptyOrBlankValues(String emptyValue) {
            client.setCommentaires(emptyValue);
            assertEquals(emptyValue, client.getCommentaires());
        }

        @Test
        @DisplayName("Raison sociale avec caractères spéciaux doit être acceptée")
        void testRaisonSocialeWithSpecialCharacters() {
            String specialRaisonSociale = "Entreprise & Fils SARL - 123!@#";
            client.setRaisonSociale(specialRaisonSociale);
            assertEquals(specialRaisonSociale, client.getRaisonSociale());
        }

        @Test
        @DisplayName("Téléphone avec format international doit être accepté")
        void testTelephoneWithInternationalFormat() {
            String internationalPhone = "+33 1 23 45 67 89";
            client.setTelephone(internationalPhone);
            assertEquals(internationalPhone, client.getTelephone());
        }

        @Test
        @DisplayName("Mail avec format complexe doit être accepté")
        void testMailWithComplexFormat() {
            String complexMail = "contact+test@entreprise-test.co.uk";
            client.setMail(complexMail);
            assertEquals(complexMail, client.getMail());
        }

        @Test
        @DisplayName("Commentaires avec caractères spéciaux doit être accepté")
        void testCommentairesWithSpecialCharacters() {
            String specialCommentaires = "Client fidèle depuis 5 ans - Contact: Jean Dupont (01.23.45.67.89)";
            client.setCommentaires(specialCommentaires);
            assertEquals(specialCommentaires, client.getCommentaires());
        }
    }

    @Nested
    @DisplayName("Tests des cas limites")
    class EdgeCaseTests {

        @Test
        @DisplayName("ID négatif doit être accepté")
        void testNegativeId() {
            Integer negativeId = -1;
            client.setIdentifiant(negativeId);
            assertEquals(negativeId, client.getIdentifiant());
        }

        @Test
        @DisplayName("ID zéro doit être accepté")
        void testZeroId() {
            Integer zeroId = 0;
            client.setIdentifiant(zeroId);
            assertEquals(zeroId, client.getIdentifiant());
        }

        @Test
        @DisplayName("Chiffre d'affaires négatif doit être accepté")
        void testNegativeChiffreAffaires() {
            Double negativeCA = -1000.0;
            client.setChiffreAffaires(negativeCA);
            assertEquals(negativeCA, client.getChiffreAffaires());
        }

        @Test
        @DisplayName("Chiffre d'affaires zéro doit être accepté")
        void testZeroChiffreAffaires() {
            Double zeroCA = 0.0;
            client.setChiffreAffaires(zeroCA);
            assertEquals(zeroCA, client.getChiffreAffaires());
        }

        @Test
        @DisplayName("Nombre d'employés négatif doit être accepté")
        void testNegativeNbEmployes() {
            Integer negativeNb = -5;
            client.setNbEmployes(negativeNb);
            assertEquals(negativeNb, client.getNbEmployes());
        }

        @Test
        @DisplayName("Nombre d'employés zéro doit être accepté")
        void testZeroNbEmployes() {
            Integer zeroNb = 0;
            client.setNbEmployes(zeroNb);
            assertEquals(zeroNb, client.getNbEmployes());
        }

        @Test
        @DisplayName("Chiffre d'affaires très élevé doit être accepté")
        void testVeryHighChiffreAffaires() {
            Double veryHighCA = Double.MAX_VALUE;
            client.setChiffreAffaires(veryHighCA);
            assertEquals(veryHighCA, client.getChiffreAffaires());
        }

        @Test
        @DisplayName("Nombre d'employés très élevé doit être accepté")
        void testVeryHighNbEmployes() {
            Integer veryHighNb = Integer.MAX_VALUE;
            client.setNbEmployes(veryHighNb);
            assertEquals(veryHighNb, client.getNbEmployes());
        }

        @Test
        @DisplayName("Raison sociale très longue doit être acceptée")
        void testVeryLongRaisonSociale() {
            String longRaisonSociale = "a".repeat(1000);
            client.setRaisonSociale(longRaisonSociale);
            assertEquals(longRaisonSociale, client.getRaisonSociale());
        }

        @Test
        @DisplayName("Commentaires très longs doivent être acceptés")
        void testVeryLongCommentaires() {
            String longCommentaires = "c".repeat(10000);
            client.setCommentaires(longCommentaires);
            assertEquals(longCommentaires, client.getCommentaires());
        }
    }

    @Nested
    @DisplayName("Tests de persistance et état")
    class PersistenceTests {

        @Test
        @DisplayName("Client doit conserver son état après modifications multiples")
        void testClientStateConservation() {
            // Initialisation
            client.setIdentifiant(VALID_ID);
            client.setRaisonSociale(VALID_RAISON_SOCIALE);
            client.setAdresse(adresse);
            client.setTelephone(VALID_TELEPHONE);
            client.setMail(VALID_MAIL);
            client.setCommentaires(VALID_COMMENTAIRES);
            client.setChiffreAffaires(VALID_CHIFFRE_AFFAIRES);
            client.setNbEmployes(VALID_NB_EMPLOYES);

            // Vérification de l'état initial
            assertEquals(VALID_ID, client.getIdentifiant());
            assertEquals(VALID_RAISON_SOCIALE, client.getRaisonSociale());
            assertEquals(adresse, client.getAdresse());
            assertEquals(VALID_TELEPHONE, client.getTelephone());
            assertEquals(VALID_MAIL, client.getMail());
            assertEquals(VALID_COMMENTAIRES, client.getCommentaires());
            assertEquals(VALID_CHIFFRE_AFFAIRES, client.getChiffreAffaires());
            assertEquals(VALID_NB_EMPLOYES, client.getNbEmployes());

            // Modifications
            client.setRaisonSociale("Nouvelle Raison Sociale");
            client.setChiffreAffaires(750000.0);
            client.setNbEmployes(50);

            // Vérification que les autres champs sont conservés
            assertEquals(VALID_ID, client.getIdentifiant());
            assertEquals("Nouvelle Raison Sociale", client.getRaisonSociale());
            assertEquals(adresse, client.getAdresse());
            assertEquals(VALID_TELEPHONE, client.getTelephone());
            assertEquals(VALID_MAIL, client.getMail());
            assertEquals(VALID_COMMENTAIRES, client.getCommentaires());
            assertEquals(750000.0, client.getChiffreAffaires());
            assertEquals(Integer.valueOf(50), client.getNbEmployes());
        }

        @Test
        @DisplayName("Client doit être mutable")
        void testClientMutability() {
            // Création d'un client complet
            Client originalClient = new Client(VALID_ID, adresse, VALID_MAIL, 
                                             VALID_COMMENTAIRES, VALID_RAISON_SOCIALE, 
                                             VALID_NOM, VALID_PRENOM, VALID_TELEPHONE, 
                                             VALID_CHIFFRE_AFFAIRES, VALID_NB_EMPLOYES);

            // Modification de tous les champs
            Adresse newAdresse = new Adresse("456", "Avenue des Champs", "69000", "Lyon");
            newAdresse.setPays("France");
            originalClient.setIdentifiant(999);
            originalClient.setRaisonSociale("Entreprise Modifiée");
            originalClient.setAdresse(newAdresse);
            originalClient.setTelephone("0987654321");
            originalClient.setMail("nouveau@entreprise.com");
            originalClient.setCommentaires("Nouveaux commentaires");
            originalClient.setChiffreAffaires(1000000.0);
            originalClient.setNbEmployes(100);

            // Vérification des modifications
            assertEquals(Integer.valueOf(999), originalClient.getIdentifiant());
            assertEquals("Entreprise Modifiée", originalClient.getRaisonSociale());
            assertEquals(newAdresse, originalClient.getAdresse());
            assertEquals("0987654321", originalClient.getTelephone());
            assertEquals("nouveau@entreprise.com", originalClient.getMail());
            assertEquals("Nouveaux commentaires", originalClient.getCommentaires());
            assertEquals(1000000.0, originalClient.getChiffreAffaires());
            assertEquals(Integer.valueOf(100), originalClient.getNbEmployes());
        }
    }

    @Nested
    @DisplayName("Tests de scénarios métier")
    class BusinessScenarioTests {

        @Test
        @DisplayName("Création d'un client PME")
        void testPMEClientCreation() {
            Client pmeClient = new Client();
            pmeClient.setRaisonSociale("PME Test SARL");
            Adresse pmeAdresse = new Adresse("10", "Rue du Commerce", "31000", "Toulouse");
            pmeAdresse.setPays("France");
            pmeClient.setAdresse(pmeAdresse);
            pmeClient.setTelephone("05.61.23.45.67");
            pmeClient.setMail("contact@pme-test.fr");
            pmeClient.setCommentaires("PME en croissance");
            pmeClient.setChiffreAffaires(250000.0);
            pmeClient.setNbEmployes(15);

            assertEquals("PME Test SARL", pmeClient.getRaisonSociale());
            assertEquals("05.61.23.45.67", pmeClient.getTelephone());
            assertEquals("contact@pme-test.fr", pmeClient.getMail());
            assertEquals("PME en croissance", pmeClient.getCommentaires());
            assertEquals(250000.0, pmeClient.getChiffreAffaires());
            assertEquals(Integer.valueOf(15), pmeClient.getNbEmployes());
        }

        @Test
        @DisplayName("Création d'un grand compte")
        void testLargeAccountCreation() {
            Client largeClient = new Client();
            largeClient.setRaisonSociale("Grande Entreprise SA");
            Adresse largeAdresse = new Adresse("1", "Place de la Bourse", "75002", "Paris");
            largeAdresse.setPays("France");
            largeClient.setAdresse(largeAdresse);
            largeClient.setTelephone("01.42.34.56.78");
            largeClient.setMail("direction@grande-entreprise.fr");
            largeClient.setCommentaires("Grand compte - Contact principal: Marie Martin");
            largeClient.setChiffreAffaires(5000000.0);
            largeClient.setNbEmployes(500);

            assertEquals("Grande Entreprise SA", largeClient.getRaisonSociale());
            assertEquals("01.42.34.56.78", largeClient.getTelephone());
            assertEquals("direction@grande-entreprise.fr", largeClient.getMail());
            assertEquals("Grand compte - Contact principal: Marie Martin", largeClient.getCommentaires());
            assertEquals(5000000.0, largeClient.getChiffreAffaires());
            assertEquals(Integer.valueOf(500), largeClient.getNbEmployes());
        }

        @Test
        @DisplayName("Client sans adresse")
        void testClientWithoutAddress() {
            Client noAddressClient = new Client();
            noAddressClient.setRaisonSociale("Client Sans Adresse");
            noAddressClient.setTelephone("01.23.45.67.89");
            noAddressClient.setMail("contact@sans-adresse.com");
            noAddressClient.setChiffreAffaires(100000.0);
            noAddressClient.setNbEmployes(5);

            assertNull(noAddressClient.getAdresse());
            assertEquals("Client Sans Adresse", noAddressClient.getRaisonSociale());
            assertEquals("01.23.45.67.89", noAddressClient.getTelephone());
            assertEquals("contact@sans-adresse.com", noAddressClient.getMail());
            assertEquals(100000.0, noAddressClient.getChiffreAffaires());
            assertEquals(Integer.valueOf(5), noAddressClient.getNbEmployes());
        }

        @Test
        @DisplayName("Client sans commentaires")
        void testClientWithoutComments() {
            Client noCommentsClient = new Client();
            noCommentsClient.setRaisonSociale("Client Sans Commentaires");
            noCommentsClient.setAdresse(adresse);
            noCommentsClient.setTelephone("01.23.45.67.89");
            noCommentsClient.setMail("contact@sans-commentaires.com");
            noCommentsClient.setChiffreAffaires(300000.0);
            noCommentsClient.setNbEmployes(20);

            assertNull(noCommentsClient.getCommentaires());
            assertEquals("Client Sans Commentaires", noCommentsClient.getRaisonSociale());
            assertEquals(adresse, noCommentsClient.getAdresse());
            assertEquals("01.23.45.67.89", noCommentsClient.getTelephone());
            assertEquals("contact@sans-commentaires.com", noCommentsClient.getMail());
            assertEquals(300000.0, noCommentsClient.getChiffreAffaires());
            assertEquals(Integer.valueOf(20), noCommentsClient.getNbEmployes());
        }
    }

    @Nested
    @DisplayName("Tests d'héritage")
    class InheritanceTests {

        @Test
        @DisplayName("Client doit hériter de Societe")
        void testClientInheritsFromSociete() {
            Client client = new Client();
            
            // Vérification que Client est une instance de Societe
            assertTrue(client instanceof Societe);
            
            // Vérification que les méthodes de Societe sont accessibles
            client.setRaisonSociale("Test");
            assertEquals("Test", client.getRaisonSociale());
        }

        @Test
        @DisplayName("Client doit avoir ses propres propriétés en plus de celles de Societe")
        void testClientHasOwnProperties() {
            Client client = new Client();
            
            // Propriétés héritées de Societe
            client.setRaisonSociale("Société Test");
            client.setTelephone("01.23.45.67.89");
            client.setMail("test@societe.com");
            client.setCommentaires("Commentaires test");
            
            // Propriétés spécifiques à Client
            client.setChiffreAffaires(500000.0);
            client.setNbEmployes(25);
            
            // Vérification des propriétés héritées
            assertEquals("Société Test", client.getRaisonSociale());
            assertEquals("01.23.45.67.89", client.getTelephone());
            assertEquals("test@societe.com", client.getMail());
            assertEquals("Commentaires test", client.getCommentaires());
            
            // Vérification des propriétés spécifiques
            assertEquals(500000.0, client.getChiffreAffaires());
            assertEquals(Integer.valueOf(25), client.getNbEmployes());
        }
    }
} 