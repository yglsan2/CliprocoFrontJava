package builders;

import builders.AdresseBuilder;
import models.Adresse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BuilderTest {

    @Test
    void shouldCreateBuilderInstance() {
        AdresseBuilder builder = new AdresseBuilder();
        assertNotNull(builder);
    }

    @Test
    void shouldBuildAdresse() {
        AdresseBuilder builder = new AdresseBuilder();
        Adresse adresse = builder
            .deNomRue("123 Rue Test")
            .deVille("Paris")
            .deCodePostal("75001")
            .build();
        assertNotNull(adresse);
        assertEquals("123 Rue Test", adresse.getNomRue());
        assertEquals("Paris", adresse.getVille());
        assertEquals("75001", adresse.getCodePostal());
    }
} 