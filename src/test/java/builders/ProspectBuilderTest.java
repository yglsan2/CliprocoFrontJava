package builders;

import models.Prospect;
import models.Adresse;
import exceptions.ValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProspectBuilderTest {

    @Test
    void testBuildProspect() throws ValidationException {
        // Given
        Adresse adresse = new Adresse();
        adresse.setNumeroRue("123");
        adresse.setNomRue("Rue Test");
        adresse.setCodePostal("75000");
        adresse.setVille("Paris");
        adresse.setPays("France");

        // When
        Prospect prospect = new ProspectBuilder()
            .dIdentifiant(1L)
            .deRaisonSociale("Test Company")
            .deMail("test@company.com")
            .deTelephone("0123456789")
            .dAdresse(adresse)
            .deDateProspection("2024-03-20")
            .build();

        // Then
        assertNotNull(prospect);
        assertEquals(1L, prospect.getIdentifiant());
        assertEquals("Test Company", prospect.getRaisonSociale());
        assertEquals("test@company.com", prospect.getMail());
        assertEquals("0123456789", prospect.getTelephone());
        assertEquals("2024-03-20", prospect.getDateProspection());
        assertEquals(adresse, prospect.getAdresse());
    }

    @Test
    void testBuildProspectDeInvalidEmail() {
        // Given
        Adresse adresse = new Adresse();
        adresse.setNumeroRue("123");
        adresse.setNomRue("Rue Test");
        adresse.setCodePostal("75000");
        adresse.setVille("Paris");
        adresse.setPays("France");

        // When & Then
        assertThrows(ValidationException.class, () -> {
            new ProspectBuilder()
                .dIdentifiant(1L)
                .deRaisonSociale("Test Company")
                .deMail("invalid-email")
                .deTelephone("0123456789")
                .dAdresse(adresse)
                .deDateProspection("2024-03-20")
                .build();
        });
    }

    @Test
    void testBuildProspectDeInvalidTelephone() {
        // Given
        Adresse adresse = new Adresse();
        adresse.setNumeroRue("123");
        adresse.setNomRue("Rue Test");
        adresse.setCodePostal("75000");
        adresse.setVille("Paris");
        adresse.setPays("France");

        // When & Then
        assertThrows(ValidationException.class, () -> {
            new ProspectBuilder()
                .dIdentifiant(1L)
                .deRaisonSociale("Test Company")
                .deMail("test@company.com")
                .deTelephone("invalid-phone")
                .dAdresse(adresse)
                .deDateProspection("2024-03-20")
                .build();
        });
    }

    @Test
    void testBuildProspectDeFutureDate() {
        // Given
        Adresse adresse = new Adresse();
        adresse.setNumeroRue("123");
        adresse.setNomRue("Rue Test");
        adresse.setCodePostal("75000");
        adresse.setVille("Paris");
        adresse.setPays("France");

        // When & Then
        assertThrows(ValidationException.class, () -> {
            new ProspectBuilder()
                .dIdentifiant(1L)
                .deRaisonSociale("Test Company")
                .deMail("test@company.com")
                .deTelephone("0123456789")
                .dAdresse(adresse)
                .deDateProspection("2025-03-20") // Date future invalide
                .build();
        });
    }

    @Test
    void testBuildProspectDeInvalidDateFormat() {
        // Given
        Adresse adresse = new Adresse();
        adresse.setNumeroRue("123");
        adresse.setNomRue("Rue Test");
        adresse.setCodePostal("75000");
        adresse.setVille("Paris");
        adresse.setPays("France");

        // When & Then
        assertThrows(ValidationException.class, () -> {
            new ProspectBuilder()
                .dIdentifiant(1L)
                .deRaisonSociale("Test Company")
                .deMail("test@company.com")
                .deTelephone("0123456789")
                .dAdresse(adresse)
                .deDateProspection("20-03-2024") // Format de date invalide
                .build();
        });
    }

    @Test
    void testBuildProspectDeNullValues() throws ValidationException {
        // Given & When
        Prospect prospect = new ProspectBuilder()
            .dIdentifiant(1L)
            .build();

        // Then
        assertNotNull(prospect);
        assertEquals(1L, prospect.getIdentifiant());
        assertNull(prospect.getRaisonSociale());
        assertNull(prospect.getMail());
        assertNull(prospect.getTelephone());
        assertNull(prospect.getDateProspection());
        assertNull(prospect.getAdresse());
    }
} 