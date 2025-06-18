package models;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class FactureTest {
    @Test
    void testConstructeurEtGettersSetters() {
        Client client = new Client();
        Facture facture = new Facture("FACT-001", LocalDate.now(), LocalDate.now().plusDays(30), client);
        facture.setIdentifiant(6L);
        assertEquals(6L, facture.getIdentifiant());
        assertEquals("FACT-001", facture.getNumero());
        assertEquals(LocalDate.now(), facture.getDateEmission());
        assertEquals(LocalDate.now().plusDays(30), facture.getDateEcheance());
        assertEquals(client, facture.getClient());
    }

    @Test
    void testToString() {
        Facture facture = new Facture();
        facture.setNumero("FACT-001");
        String str = facture.toString();
        assertTrue(str.contains("FACT-001"));
    }
} 