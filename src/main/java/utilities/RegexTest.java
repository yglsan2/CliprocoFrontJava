package utilities;

/**
 * Test simple pour vérifier les regexs de validation.
 */
public class RegexTest {
    
    public static void main(String[] args) {
        System.out.println("=== Test des regexs de validation ===");
        
        // Test codes postaux
        System.out.println("\n--- Codes postaux ---");
        testPostalCode("75001", true);   // Paris
        testPostalCode("69001", true);   // Lyon
        testPostalCode("2A000", true);   // Corse
        testPostalCode("2B000", true);   // Corse
        testPostalCode("20000", false);  // Invalide
        testPostalCode("97000", true);   // DOM-TOM
        testPostalCode("12345", false);  // Invalide
        
        // Test téléphones
        System.out.println("\n--- Téléphones ---");
        testPhone("0612345678", true);     // Mobile
        testPhone("0123456789", true);     // Fixe
        testPhone("+33612345678", true);   // International
        testPhone("0033612345678", true);  // International
        testPhone("123", false);           // Trop court
        testPhone("061234567", false);     // Trop court
        
        System.out.println("\n=== Fin des tests ===");
    }
    
    private static void testPostalCode(String code, boolean expected) {
        boolean result = ValidationManager.isValidPostalCode(code);
        String status = result == expected ? "✅" : "❌";
        System.out.printf("%s Code postal '%s': %s (attendu: %s)%n", 
                         status, code, result, expected);
    }
    
    private static void testPhone(String phone, boolean expected) {
        boolean result = ValidationManager.isValidPhone(phone);
        String status = result == expected ? "✅" : "❌";
        System.out.printf("%s Téléphone '%s': %s (attendu: %s)%n", 
                         status, phone, result, expected);
    }
} 