package models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdresseTest {
    @Test
    void testConstructeurEtGettersSetters() {
        Adresse adresse = new Adresse();
        adresse.setIdentifiant(4L);
        adresse.setNumeroRue("12");
        adresse.setNomRue("Rue de Paris");
        adresse.setCodePostal("75000");
        adresse.setVille("Paris");
        adresse.setPays("France");
        assertEquals(4L, adresse.getIdentifiant());
        assertEquals("12", adresse.getNumeroRue());
        assertEquals("Rue de Paris", adresse.getNomRue());
        assertEquals("75000", adresse.getCodePostal());
        assertEquals("Paris", adresse.getVille());
        assertEquals("France", adresse.getPays());
    }

    @Test
    void testToString() {
        Adresse adresse = new Adresse("12", "Rue de Paris", "75000", "Paris");
        adresse.setPays("France");
        String str = adresse.toString();
        assertTrue(str.contains("Rue de Paris"));
    }
} 