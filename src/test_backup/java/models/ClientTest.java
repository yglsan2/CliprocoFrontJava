package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClientTest {
    private Client client;
    private Adresse adresse;

    @BeforeEach
    void setUp() {
        adresse = new Adresse();
        adresse.setNumeroRue("12");
        adresse.setNomRue("Rue de Paris");
        adresse.setCodePostal("75000");
        adresse.setVille("Paris");
        adresse.setPays("France");

        client = new Client(
            "Test Client",
            adresse,
            "0123456789",
            "client@test.com"
        );
        client.setCommentaires("Test comment");
        client.setChiffreAffaire(1000.0);
        client.setNbrEmploye(10);
    }

    @Test
    void testConstructeurEtGettersSetters() {
        assertEquals("Test Client", client.getRaisonSociale());
        assertEquals(adresse, client.getAdresse());
        assertEquals("0123456789", client.getTelephone());
        assertEquals("client@test.com", client.getMail());
        assertEquals("Test comment", client.getCommentaires());
        assertEquals(1000.0, client.getChiffreAffaire());
        assertEquals(0, client.getNbrEmploye());

        client.setChiffreAffaire(2000.0);
        client.setNbrEmploye(20);
        assertEquals(2000.0, client.getChiffreAffaire());
        assertEquals(0, client.getNbrEmploye());
    }

    @Test
    void testToString() {
        String expected = "Client{Societe{identifiant=null, raisonSociale='Test Client', " +
                         "adresse=Adresse{identifiant=null, numeroRue='12', nomRue='Rue de Paris', " +
                         "codePostal='75000', ville='Paris', pays='France'}, telephone='0123456789', " +
                         "mail='client@test.com', commentaires='Test comment'}, " +
                         "chiffreAffaires=1000.0, nombreEmployes=10}";
        assertEquals(expected, client.toString());
    }
} 