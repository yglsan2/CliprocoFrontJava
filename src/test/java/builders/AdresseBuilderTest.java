package builders;

import builders.AdresseBuilder;
import models.Adresse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AdresseBuilderTest {

    @Test
    void shouldBuildAdresseDeValidData() {
        Adresse adresse = new AdresseBuilder()
            .dIdentifiant(1L)
            .deNomRue("123 Rue Test")
            .deVille("Paris")
            .deCodePostal("75001")
            .build();

        assertNotNull(adresse);
        assertEquals(1L, adresse.getIdentifiant());
        assertEquals("123 Rue Test", adresse.getNomRue());
        assertEquals("Paris", adresse.getVille());
        assertEquals("75001", adresse.getCodePostal());
    }

    @Test
    void shouldBuildAdresseDeMinimalData() {
        Adresse adresse = new AdresseBuilder()
            .deNomRue("123 Rue Test")
            .build();

        assertNotNull(adresse);
        assertEquals("123 Rue Test", adresse.getNomRue());
        assertNull(adresse.getVille());
        assertNull(adresse.getCodePostal());
    }
} 