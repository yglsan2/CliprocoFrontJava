package builders;

import builders.ClientBuilder;
import builders.SocieteBuilder;
import models.Client;
import models.Adresse;
import exceptions.ValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SocieteBuilderTest {

    @Test
    void shouldBuildSocieteDeValidData() throws ValidationException {
        Client client = ClientBuilder.getNewClientBuilder()
            .dIdentifiant(1L)
            .deRaisonSociale("Entreprise Test")
            .deMail("test@example.com")
            .deTelephone("0123456789")
            .deCommentaires("Commentaire test")
            .avecAdresse("123 rue test", "75001", "Paris", "France", "0123456789")
            .build();

        assertNotNull(client);
        assertEquals(1L, client.getIdentifiant());
        assertEquals("Entreprise Test", client.getRaisonSociale());
        assertEquals("test@example.com", client.getMail());
        assertEquals("0123456789", client.getTelephone());
        assertEquals("Commentaire test", client.getCommentaires());
        
        Adresse adresse = client.getAdresse();
        assertNotNull(adresse);
        assertEquals("123 rue test", adresse.getNomRue());
        assertEquals("75001", adresse.getCodePostal());
        assertEquals("Paris", adresse.getVille());
        assertEquals("France", adresse.getPays());
    }

    @Test
    void shouldThrowExceptionForInvalidEmail() {
        assertThrows(ValidationException.class, () -> {
            ClientBuilder.getNewClientBuilder()
                .deRaisonSociale("Entreprise Test")
                .deMail("invalid-email")
                .build();
        });
    }

    @Test
    void shouldThrowExceptionForInvalidTelephone() {
        assertThrows(ValidationException.class, () -> {
            ClientBuilder.getNewClientBuilder()
                .deRaisonSociale("Entreprise Test")
                .deTelephone("invalid-phone")
                .build();
        });
    }

    @Test
    void shouldThrowExceptionForInvalidCodePostal() {
        assertThrows(ValidationException.class, () -> {
            ClientBuilder.getNewClientBuilder()
                .deRaisonSociale("Entreprise Test")
                .avecAdresse("123 rue test", "invalid", "Paris", "France", "0123456789")
                .build();
        });
    }
} 