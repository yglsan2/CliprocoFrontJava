package models;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class CalculFactureTest {
    @Test
    void testConstructeurEtGettersSetters() {
        CalculFacture calcul = new CalculFacture(new BigDecimal("1000.00"), new BigDecimal("20.00"), new BigDecimal("200.00"), new BigDecimal("1200.00"));
        calcul.setIdentifiant(8L);
        assertEquals(8L, calcul.getIdentifiant());
        assertEquals(new BigDecimal("1000.00"), calcul.getMontantHT());
        assertEquals(new BigDecimal("20.00"), calcul.getTauxTVA());
        assertEquals(new BigDecimal("200.00"), calcul.getMontantTVA());
        assertEquals(new BigDecimal("1200.00"), calcul.getMontantTTC());
    }
} 