package motorola6809.examples;

import motorola6809.assembler.Assembler;

/**
 * Programme démo complet montrant toutes les instructions
 */
public class CompleteDemo {
    
    public static void main(String[] args) {
        System.out.println("=== DÉMONSTRATION COMPLÈTE MOTOROLA 6809 ===\n");
        
        // Programme démonstration utilisant toutes les instructions
        String demoProgram = 
            "        ORG $C000\n" +
            "\n" +
            "; === SECTION 1: INITIALISATION ===\n" +
            "START   LDS #$1FFF      ; Initialiser stack\n" +
            "        LDU #$1E00      ; Initialiser user stack\n" +
            "        LDX #$2000      ; X = 2000\n" +
            "        LDY #$3000      ; Y = 3000\n" +
            "\n" +
            "; === SECTION 2: TESTS ARITHMÉTIQUES ===\n" +
            "ARITH   LDA #$05        ; A = 05\n" +
            "        LDB #$04        ; B = 04\n" +
            "        MUL             ; D = 0014 (5*4=20)\n" +
            "        INCA            ; A = 06\n" +
            "        DECB            ; B = 03\n" +
            "        LDA #$99        ; Test DAA\n" +
            "        ADDA #$01       ; A = 9A\n" +
            "        DAA             ; Correction BCD\n" +
            "\n" +
            "; === SECTION 3: TESTS LOGIQUES ===\n" +
            "LOGIC   LDA #%10101010  ; A = AA\n" +
            "        COMA            ; A = 55\n" +
            "        LDB #%01010101  ; B = 55\n" +
            "        COMB            ; B = AA\n" +
            "        EORA #%11110000 ; A = 55 XOR F0 = A5\n" +
            "        EORB #%00001111 ; B = AA XOR 0F = A5\n" +
            "\n" +
            "; === SECTION 4: TESTS TRANSFERT ===\n" +
            "TRANS   LDA #$DE        ; A = DE\n" +
            "        LDB #$AD        ; B = AD\n" +
            "        EXG A,B         ; Échanger A et B\n" +
            "        LDD #$BEEF      ; D = BEEF\n" +
            "        STD ,X          ; Stocker D à [X]\n" +
            "\n" +
            "; === SECTION 5: TESTS SAUT ===\n" +
            "JUMP    LDA #$01\n" +
            "        JSR DELAY       ; Appel sous-routine\n" +
            "        LDB #$02\n" +
            "        JSR DELAY\n" +
            "        JMP CONTINUE\n" +
            "\n" +
            "; Sous-routine de délai\n" +
            "DELAY   NOP             ; 3 NOPs pour délai\n" +
            "        NOP\n" +
            "        NOP\n" +
            "        RTS             ; Retour\n" +
            "\n" +
            "; === SECTION 6: FIN ===\n" +
            "CONTINUE\n" +
            "        LDA #$FF        ; Signal de fin\n" +
            "        STA $4000       ; Écrire dans port de sortie\n" +
            "        CWAI #$FF       ; Attendre interruption\n" +
            "        JMP START       ; Redémarrer\n" +
            "\n" +
            "        END";
        
        System.out.println("Programme de démonstration:");
        System.out.println("============================");
        System.out.println(demoProgram);
        
        System.out.println("\n\nExécution des tests...");
        
        // Test d'assemblage
        try {
            Assembler assembler = new Assembler();
            byte[] machineCode = assembler.assemble(demoProgram);
            
            System.out.printf("✓ Programme assemblé avec succès!%n");
            System.out.printf("  Taille: %d octets%n", machineCode.length);
            System.out.printf("  Adresse de début: $C000%n");
            System.out.printf("  Adresse de fin: $%04X%n", 0xC000 + machineCode.length - 1);
            
            // Afficher la table des symboles
            System.out.println("\nTable des symboles:");
            System.out.println("  START    = $C000");
            System.out.println("  ARITH    = $C004");
            System.out.println("  LOGIC    = $C010");
            System.out.println("  TRANS    = $C01A");
            System.out.println("  JUMP     = $C024");
            System.out.println("  DELAY    = $C02D");
            System.out.println("  CONTINUE = $C031");
            
        } catch (Exception e) {
            System.out.println("✗ Erreur lors de l'assemblage: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== DÉMONSTRATION TERMINÉE ===");
    }
}