package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProspectTest {
    private Prospect prospect;
    private Adresse adresse;

    @BeforeEach
    void setUp() {
        adresse = new Adresse();
        adresse.setNumeroRue("123");
        adresse.setNomRue("Rue de la Paix");
        adresse.setCodePostal("75001");
        adresse.setVille("Paris");
        adresse.setPays("France");

        prospect = new Prospect(
            "Test Company",
            adresse,
            "+33123456789",
            "test@company.com",
            "Test comment",
            "2024-03-20"
        );
    }

    @Test
    void testConstructeurEtGettersSetters() {
        assertEquals("Test Company", prospect.getRaisonSociale());
        assertEquals(adresse, prospect.getAdresse());
        assertEquals("+33123456789", prospect.getTelephone());
        assertEquals("test@company.com", prospect.getMail());
        assertEquals("Test comment", prospect.getCommentaires());
        assertEquals("2024-03-20", prospect.getDateProspection());

        prospect.setDateProspection("2024-03-21");
        assertEquals("2024-03-21", prospect.getDateProspection());
    }

    @Test
    void testToString() {
        String expected = "Prospect{Societe{identifiant=null, raisonSociale='Test Company', " +
                         "adresse=Adresse{identifiant=null, numeroRue='123', nomRue='Rue de la Paix', " +
                         "codePostal='75001', ville='Paris', pays='France'}, telephone='+33123456789', " +
                         "mail='test@company.com', commentaires='Test comment'}, dateProspection='2024-03-20'}";
        assertEquals(expected, prospect.toString());
    }
} 