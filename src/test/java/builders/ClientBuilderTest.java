package builders;

import models.Client;
import models.Adresse;
import exceptions.ValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ClientBuilderTest {
    private ClientBuilder builder;

    @Test
    public void shouldBuildClientDeValidData() throws ValidationException {
        // Given
        builder = new ClientBuilder();
        Adresse adresse = new Adresse();
        adresse.setNumeroRue("123");
        adresse.setNomRue("Rue Test");
        adresse.setCodePostal("75000");
        adresse.setVille("Paris");
        adresse.setPays("France");

        // When
        Client client = builder
            .dIdentifiant(1L)
            .deRaisonSociale("Test Company")
            .deMail("test@company.com")
            .deTelephone("0123456789")
            .dAdresse(adresse)
            .deChiffreAffaires(1000.0)
            .deNombreEmployes(10)
            .build();

        // Then
        assertNotNull(client);
        assertEquals(1L, client.getIdentifiant());
        assertEquals("Test Company", client.getRaisonSociale());
        assertEquals("test@company.com", client.getMail());
        assertEquals("0123456789", client.getTelephone());
        assertEquals(1000.0, client.getChiffreAffaires());
        assertEquals(0, client.getNombreEmployes());
        assertEquals(adresse, client.getAdresse());
    }

    @Test
    public void shouldThrowExceptionDeInvalidEmail() {
        // Given
        builder = new ClientBuilder();
        Adresse adresse = new Adresse();
        adresse.setNumeroRue("123");
        adresse.setNomRue("Rue Test");
        adresse.setCodePostal("75000");
        adresse.setVille("Paris");
        adresse.setPays("France");

        // When & Then
        assertThrows(ValidationException.class, () -> {
            builder
                .dIdentifiant(1L)
                .deRaisonSociale("Test Company")
                .deMail("invalid-email")
                .deTelephone("0123456789")
                .dAdresse(adresse)
                .deChiffreAffaires(1000.0)
                .deNombreEmployes(10)
                .build();
        });
    }

    @Test
    public void shouldThrowExceptionDeInvalidTelephone() {
        // Given
        builder = new ClientBuilder();
        Adresse adresse = new Adresse();
        adresse.setNumeroRue("123");
        adresse.setNomRue("Rue Test");
        adresse.setCodePostal("75000");
        adresse.setVille("Paris");
        adresse.setPays("France");

        // When & Then
        assertThrows(ValidationException.class, () -> {
            builder
                .dIdentifiant(1L)
                .deRaisonSociale("Test Company")
                .deMail("test@company.com")
                .deTelephone("invalid-phone")
                .dAdresse(adresse)
                .deChiffreAffaires(1000.0)
                .deNombreEmployes(10)
                .build();
        });
    }

    @Test
    public void shouldThrowExceptionDeInvalidChiffreAffaires() {
        // Given
        builder = new ClientBuilder();
        Adresse adresse = new Adresse();
        adresse.setNumeroRue("123");
        adresse.setNomRue("Rue Test");
        adresse.setCodePostal("75000");
        adresse.setVille("Paris");
        adresse.setPays("France");

        // When & Then
        assertThrows(ValidationException.class, () -> {
            builder
                .dIdentifiant(1L)
                .deRaisonSociale("Test Company")
                .deMail("test@company.com")
                .deTelephone("0123456789")
                .dAdresse(adresse)
                .deChiffreAffaires(-1000.0)
                .deNombreEmployes(10)
                .build();
        });
    }

    @Test
    void testBuildClient() {
        // Given
        Client client = new Client.ClientBuilder()
            .dIdentifiant(1L)
            .deRaisonSociale("Test Company")
            .deMail("test@company.com")
            .deTelephone("0123456789")
            .build();

        // Then
        assertNotNull(client);
        assertEquals(1L, client.getIdentifiant());
        assertEquals("Test Company", client.getRaisonSociale());
        assertEquals("test@company.com", client.getMail());
        assertEquals("0123456789", client.getTelephone());
    }

    @Test
    void testBuildClientDeNullValues() {
        // Given & When
        Client client = new Client.ClientBuilder()
            .dIdentifiant(1L)
            .build();

        // Then
        assertNotNull(client);
        assertEquals(1L, client.getIdentifiant());
        assertNull(client.getRaisonSociale());
        assertNull(client.getMail());
        assertNull(client.getTelephone());
    }
} 