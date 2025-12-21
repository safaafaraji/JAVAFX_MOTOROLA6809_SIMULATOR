package motorola6809.examples;
import motorola6809.assembler.Assembler;
/**
 * Tests unitaires détaillés par instruction
 */
public class InstructionTests {
    
    /**
     * Test 1: Instructions de complément
     */
    public static void testComplementInstructions() {
        System.out.println("\n=== TEST COMPLÉMENT (COMA/COMB/COM) ===");
        
        // Programme de test
        String program = 
            "        ORG $1000\n" +
            "        LDA #%10101010   ; A = 10101010 (0xAA)\n" +
            "        COMA             ; A = 01010101 (0x55)\n" +
            "        LDB #%01010101   ; B = 01010101 (0x55)\n" +
            "        COMB             ; B = 10101010 (0xAA)\n" +
            "        LDA #%11110000   ; A = 11110000 (0xF0)\n" +
            "        COM $80          ; Complément mémoire à $80\n" +
            "        END";
        
        runTest("Complément", program);
    }
    
    /**
     * Test 2: Instructions arithmétiques
     */
    public static void testArithmeticInstructions() {
        System.out.println("\n=== TEST ARITHMÉTIQUE (INC/DEC/DAA/MUL) ===");
        
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
        
        runTest("Arithmétique", program);
    }
    
    /**
     * Test 3: Instructions logiques
     */
    public static void testLogicalInstructions() {
        System.out.println("\n=== TEST LOGIQUE (EORA/EORB) ===");
        
        String program = 
            "        ORG $3000\n" +
            "        LDA #%11001100  ; A = CC\n" +
            "        EORA #%10101010 ; A = CC XOR AA = 66\n" +
            "        LDB #%00110011  ; B = 33\n" +
            "        EORB #%01010101 ; B = 33 XOR 55 = 66\n" +
            "        END";
        
        runTest("Logique", program);
    }
    
    /**
     * Test 4: Instructions de transfert
     */
    public static void testTransferInstructions() {
        System.out.println("\n=== TEST TRANSFERT (EXG) ===");
        
        String program = 
            "        ORG $4000\n" +
            "        LDA #$AA        ; A = AA\n" +
            "        LDB #$55        ; B = 55\n" +
            "        EXG A,B         ; A = 55, B = AA\n" +
            "        END";
        
        runTest("Transfert", program);
    }
    
    /**
     * Test 5: Instructions de contrôle
     */
    public static void testControlInstructions() {
        System.out.println("\n=== TEST CONTRÔLE (NOP/CWAI) ===");
        
        String program = 
            "        ORG $5000\n" +
            "        LDA #$01\n" +
            "        NOP             ; Ne rien faire\n" +
            "        NOP             ; Attente\n" +
            "        NOP\n" +
            "        CWAI #$FF       ; Attendre interruption\n" +
            "        END";
        
        runTest("Contrôle", program);
    }
    
    /**
     * Test 6: Instructions de saut
     */
    public static void testJumpInstructions() {
        System.out.println("\n=== TEST SAUT (JMP/JSR) ===");
        
        String program = 
            "        ORG $6000\n" +
            "MAIN    LDA #$10\n" +
            "        JSR SUBROUTINE  ; Appel sous-routine\n" +
            "        LDB #$20\n" +
            "        JMP END\n" +
            "SUBROUTINE\n" +
            "        INCA            ; Sous-routine\n" +
            "        RTS             ; Retour\n" +
            "END     NOP\n" +
            "        END";
        
        runTest("Saut", program);
    }
    
    /**
     * Test 7: Instructions LD/ST complètes
     */
    public static void testLoadStoreInstructions() {
        System.out.println("\n=== TEST CHARGEMENT/STOCKAGE COMPLET ===");
        
        String program = 
            "        ORG $7000\n" +
            "        LDA #$11        ; A = 11\n" +
            "        LDB #$22        ; B = 22\n" +
            "        LDD #$3344      ; D = 33:44\n" +
            "        LDX #$1234      ; X = 1234\n" +
            "        STA $8000       ; Stocker A à 8000\n" +
            "        STB $8001       ; Stocker B à 8001\n" +
            "        STD $8002       ; Stocker D à 8002-8003\n" +
            "        STX $8004       ; Stocker X à 8004-8005\n" +
            "        END";
        
        runTest("Load/Store", program);
    }
    
    /**
     * Méthode pour exécuter un test
     */
    private static void runTest(String testName, String program) {
        System.out.println("\nTest: " + testName);
        System.out.println("Programme:");
        System.out.println(program);
        
        try {
            Assembler assembler = new Assembler();
            byte[] code = assembler.assemble(program);
            
            System.out.printf("✓ Assemblage réussi (%d octets)%n", code.length);
            System.out.println("Code machine:");
            
            for (int i = 0; i < Math.min(code.length, 16); i++) {
                if (i % 8 == 0) System.out.print("  ");
                System.out.printf("%02X ", code[i] & 0xFF);
            }
            if (code.length > 16) System.out.print("...");
            System.out.println();
            
        } catch (Exception e) {
            System.out.println("✗ Erreur: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== TESTS UNITAIRES PAR INSTRUCTION ===");
        
        testComplementInstructions();
        testArithmeticInstructions();
        testLogicalInstructions();
        testTransferInstructions();
        testControlInstructions();
        testJumpInstructions();
        testLoadStoreInstructions();
        
        System.out.println("\n=== TOUS LES TESTS TERMINÉS ===");
    }
}