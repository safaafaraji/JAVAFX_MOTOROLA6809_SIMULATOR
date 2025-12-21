package motorola6809.examples;
import motorola6809.assembler.Assembler;

/**
 * Tests corrigés
 */
public class FixedInstructionTests {
    
    public static void testFixedArithmetic() {
        System.out.println("\n=== TEST ARITHMÉTIQUE CORRIGÉ ===");
        
        String program = 
            "        ORG $2000\n" +
            "        LDA #$09        ; A = 09\n" +
            "        INCA            ; A = 0A\n" +
            "        LDB #$10        ; B = 10\n" +
            "        DECB            ; B = 0F\n" +
            "        LDA #$99        ; Test DAA\n" +
            "        ADDA #$01       ; A = 9A (BCD incorrect)\n" +
            "        DAA             ; A = 00, C=1 (correction BCD)\n" +
            "        LDA #$05        ; A = 05\n" +
            "        LDB #$04        ; B = 04\n" +
            "        MUL             ; D = 0014 (5*4=20)\n" +
            "        END";
        
        runTest("Arithmétique corrigée", program);
    }
    
    public static void testFixedTransfer() {
        System.out.println("\n=== TEST TRANSFERT CORRIGÉ ===");
        
        String program = 
            "        ORG $4000\n" +
            "        LDA #$AA        ; A = AA\n" +
            "        LDB #$55        ; B = 55\n" +
            "        EXG             ; Échanger A et B (sans opérande)\n" +
            "        END";
        
        runTest("Transfert corrigé", program);
    }
    
    public static void testFixedJump() {
        System.out.println("\n=== TEST SAUT CORRIGÉ ===");
        
        String program = 
            "        ORG $6000\n" +
            "        LDA #$10\n" +
            "        JSR $6100       ; Appel sous-routine\n" +
            "        LDB #$20\n" +
            "        JMP $6200\n" +
            "        RTS             ; Retour\n" +
            "        END";
        
        runTest("Saut corrigé", program);
    }
    
    private static void runTest(String testName, String program) {
        System.out.println("\nTest: " + testName);
        System.out.println("Programme:");
        System.out.println(program);
        
        try {
            Assembler assembler = new Assembler();
            byte[] code = assembler.assemble(program);
            
            System.out.printf("✓ Assemblage réussi (%d octets)%n", code.length);
            System.out.println("Code machine:");
            
            for (int i = 0; i < code.length; i++) {
                if (i % 8 == 0) System.out.print("  ");
                System.out.printf("%02X ", code[i] & 0xFF);
            }
            System.out.println();
            
        } catch (Exception e) {
            System.out.println("✗ Erreur: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== TESTS CORRIGÉS ===");
        
        testFixedArithmetic();
        testFixedTransfer();
        testFixedJump();
        
        System.out.println("\n=== TESTS TERMINÉS ===");
    }
}