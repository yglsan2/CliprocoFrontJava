package models;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ProduitTest {
    @Test
    void testConstructeurEtGettersSetters() {
        Produit produit = new Produit("Produit Test", "Description test", new BigDecimal("100.00"));
        produit.setIdentifiant(7L);
        produit.setNom("Produit Test Modifié");
        produit.setDescription("Description modifiée");
        produit.setPrixUnitaire(new BigDecimal("200.00"));
        assertEquals(Integer.valueOf(7), produit.getIdentifiant());
        assertEquals("Produit Test Modifié", produit.getNom());
        assertEquals("Description modifiée", produit.getDescription());
        assertEquals(new BigDecimal("200.00"), produit.getPrixUnitaire());
    }

    @Test
    void testToString() {
        Produit produit = new Produit();
        produit.setNom("Produit Test");
        String str = produit.toString();
        assertTrue(str.contains("Produit Test"));
    }
} 