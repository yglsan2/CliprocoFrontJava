package builders;

import models.Contrat;
import models.Client;
import models.Adresse;
import exceptions.ValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.math.BigDecimal;

public class ContratBuilderTest {
    private ContratBuilder builder;
    private Client client;

    @Test
    public void shouldBuildContratDeValidData() throws ValidationException {
        // Given
        builder = new ContratBuilder();
        client = new Client();
        client.setIdentifiant(1L);
        client.setRaisonSociale("Test Company");
        client.setMail("test@company.com");
        client.setTelephone("0123456789");
        Adresse adresse = new Adresse();
        adresse.setNumeroRue("123");
        adresse.setNomRue("Rue Test");
        adresse.setCodePostal("75000");
        adresse.setVille("Paris");
        adresse.setPays("France");
        client.setAdresse(adresse);

        // When
        Contrat contrat = builder
            .dIdentifiant(Integer.valueOf(1))
            .deLibelle("Contrat Standard")
            .deMontant(new BigDecimal("1000.0"))
            .dIdClient(1L)
            .build();

        // Then
        assertNotNull(contrat);
        assertEquals(Integer.valueOf(1), contrat.getIdentifiant());
        assertEquals("Contrat Standard", contrat.getLibelle());
        assertEquals(new BigDecimal("1000.0"), contrat.getMontant());
        assertEquals(Integer.valueOf(1), contrat.getClient().getIdentifiant());
    }

    @Test
    public void shouldThrowExceptionDeInvalidDates() {
        // Given
        builder = new ContratBuilder();
        client = new Client();
        client.setIdentifiant(1L);
        client.setRaisonSociale("Test Company");
        client.setMail("test@company.com");
        client.setTelephone("0123456789");
        Adresse adresse = new Adresse();
        adresse.setNumeroRue("123");
        adresse.setNomRue("Rue Test");
        adresse.setCodePostal("75000");
        adresse.setVille("Paris");
        adresse.setPays("France");
        client.setAdresse(adresse);

        // When & Then
        assertThrows(ValidationException.class, () -> {
            builder
                .dIdentifiant(Integer.valueOf(1))
                .deLibelle("Contrat Standard")
                .deMontant(new BigDecimal("1000.0"))
                .dIdClient(1L)
                .build();
        });
    }

    @Test
    public void shouldThrowExceptionDeInvalidMontant() {
        // Given
        builder = new ContratBuilder();
        client = new Client();
        client.setIdentifiant(1L);
        client.setRaisonSociale("Test Company");
        client.setMail("test@company.com");
        client.setTelephone("0123456789");
        Adresse adresse = new Adresse();
        adresse.setNumeroRue("123");
        adresse.setNomRue("Rue Test");
        adresse.setCodePostal("75000");
        adresse.setVille("Paris");
        adresse.setPays("France");
        client.setAdresse(adresse);

        // When & Then
        assertThrows(ValidationException.class, () -> {
            builder
                .dIdentifiant(Integer.valueOf(1))
                .deLibelle("Contrat Standard")
                .deMontant(new BigDecimal("-1000.0")) // Montant négatif
                .dIdClient(1L)
                .build();
        });
    }

    @Test
    public void shouldThrowExceptionDeInvalidLibelle() {
        // Given
        builder = new ContratBuilder();
        client = new Client();
        client.setIdentifiant(1L);
        client.setRaisonSociale("Test Company");
        client.setMail("test@company.com");
        client.setTelephone("0123456789");
        Adresse adresse = new Adresse();
        adresse.setNumeroRue("123");
        adresse.setNomRue("Rue Test");
        adresse.setCodePostal("75000");
        adresse.setVille("Paris");
        adresse.setPays("France");
        client.setAdresse(adresse);

        // When & Then
        assertThrows(ValidationException.class, () -> {
            builder
                .dIdentifiant(Integer.valueOf(1))
                .deLibelle("") // Libellé vide
                .deMontant(new BigDecimal("1000.0"))
                .dIdClient(1L)
                .build();
        });
    }
} 