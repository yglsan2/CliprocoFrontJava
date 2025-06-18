package models;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ContratTest {
    @Test
    void testConstructeurEtGettersSetters() {
        Client client = new Client();
        Contrat contrat = new Contrat(client, "Contrat Test", new BigDecimal("1000.00"));
        contrat.setIdentifiant(5L);
        contrat.setDateDebut(LocalDate.now());
        contrat.setDateFin(LocalDate.now().plusDays(10));
        assertEquals(5L, contrat.getIdentifiant());
        assertEquals(client, contrat.getClient());
        assertEquals("Contrat Test", contrat.getLibelle());
        assertEquals(new BigDecimal("1000.00"), contrat.getMontant());
        assertEquals(LocalDate.now(), contrat.getDateDebut());
        assertEquals(LocalDate.now().plusDays(10), contrat.getDateFin());
    }

    @Test
    void testToString() {
        Contrat contrat = new Contrat();
        contrat.setLibelle("Contrat Test");
        String str = contrat.toString();
        assertTrue(str.contains("Contrat Test"));
    }
} 