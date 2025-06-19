package models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SocieteTest {
    @Test
    void testConstructeurEtGettersSetters() {
        // Societe est abstraite, on teste via une classe concrète
        class TestSociete extends Societe {}
        TestSociete societe = new TestSociete();
        societe.setIdentifiant(3L);
        societe.setRaisonSociale("Test Societe");
        Adresse adresse = new Adresse("56", "Avenue de la République", "75011", "Paris");
        societe.setAdresse(adresse);
        societe.setTelephone("0112233445");
        societe.setMail("societe@test.com");
        assertEquals(Integer.valueOf(3), societe.getIdentifiant());
        assertEquals("Test Societe", societe.getRaisonSociale());
        assertEquals(adresse, societe.getAdresse());
        assertEquals("0112233445", societe.getTelephone());
        assertEquals("societe@test.com", societe.getMail());
    }

    @Test
    void testToString() {
        class TestSociete extends Societe {}
        TestSociete societe = new TestSociete();
        societe.setRaisonSociale("Test Societe");
        String str = societe.toString();
        assertTrue(str.contains("Test Societe"));
    }
} 