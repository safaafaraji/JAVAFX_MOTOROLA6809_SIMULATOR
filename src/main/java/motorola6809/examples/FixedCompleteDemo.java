package motorola6809.examples;

import motorola6809.assembler.Assembler;

/**
 * Démo corrigé sans étiquettes problématiques
 */
public class FixedCompleteDemo {
    
    public static void main(String[] args) {
        System.out.println("=== DÉMONSTRATION CORRIGÉE ===\n");
        
        // Programme corrigé sans étiquettes sur les mêmes lignes que les instructions
        String demoProgram = 
            "        ORG $C000\n" +
            "\n" +
            "; === INITIALISATION ===\n" +
            "        LDS #$1FFF      ; Initialiser stack\n" +
            "        LDU #$1E00      ; Initialiser user stack\n" +
            "        LDX #$2000      ; X = 2000\n" +
            "        LDY #$3000      ; Y = 3000\n" +
            "\n" +
            "; === TESTS ARITHMÉTIQUES ===\n" +
            "        LDA #$05        ; A = 05\n" +
            "        LDB #$04        ; B = 04\n" +
            "        MUL             ; D = 0014 (5*4=20)\n" +
            "        INCA            ; A = 06\n" +
            "        DECB            ; B = 03\n" +
            "        LDA #$99        ; Test DAA\n" +
            "        ADDA #$01       ; A = 9A\n" +
            "        DAA             ; Correction BCD\n" +
            "\n" +
            "; === TESTS LOGIQUES ===\n" +
            "        LDA #%10101010  ; A = AA\n" +
            "        COMA            ; A = 55\n" +
            "        LDB #%01010101  ; B = 55\n" +
            "        COMB            ; B = AA\n" +
            "        EORA #%11110000 ; A = 55 XOR F0 = A5\n" +
            "        EORB #%00001111 ; B = AA XOR 0F = A5\n" +
            "\n" +
            "; === TESTS TRANSFERT ===\n" +
            "        LDA #$DE        ; A = DE\n" +
            "        LDB #$AD        ; B = AD\n" +
            "        EXG             ; Échanger A et B\n" +
            "        LDD #$BEEF      ; D = BEEF\n" +
            "        STD $2100       ; Stocker D à 2100\n" +
            "\n" +
            "; === TESTS SAUT ===\n" +
            "        LDA #$01\n" +
            "        JSR $C100       ; Appel sous-routine\n" +
            "        LDB #$02\n" +
            "        JSR $C100\n" +
            "        JMP $C200\n" +
            "\n" +
            "; Sous-routine à C100\n" +
            "        ORG $C100\n" +
            "        NOP             ; 3 NOPs pour délai\n" +
            "        NOP\n" +
            "        NOP\n" +
            "        RTS             ; Retour\n" +
            "\n" +
            "; === FIN ===\n" +
            "        ORG $C200\n" +
            "        LDA #$FF        ; Signal de fin\n" +
            "        STA $4000       ; Écrire dans port de sortie\n" +
            "        CWAI #$FF       ; Attendre interruption\n" +
            "        JMP $C000       ; Redémarrer\n" +
            "\n" +
            "        END";
        
        System.out.println("Programme de démonstration corrigé:");
        System.out.println("=====================================");
        System.out.println(demoProgram);
        
        System.out.println("\n\nExécution de l'assemblage...");
        
        try {
            Assembler assembler = new Assembler();
            byte[] machineCode = assembler.assemble(demoProgram);
            
            System.out.printf("✓ Programme assemblé avec succès!%n");
            System.out.printf("  Taille: %d octets%n", machineCode.length);
            System.out.printf("  Adresse de début: $C000%n");
            System.out.printf("  Adresse de fin: $%04X%n", 0xC000 + machineCode.length - 1);
            
            // Afficher le code machine
            System.out.println("\nExtrait du code machine:");
            for (int i = 0; i < Math.min(machineCode.length, 32); i++) {
                if (i % 16 == 0) {
                    System.out.printf("%04X: ", 0xC000 + i);
                }
                System.out.printf("%02X ", machineCode[i] & 0xFF);
                if (i % 16 == 15) System.out.println();
            }
            if (machineCode.length > 32) System.out.println("...");
            
        } catch (Exception e) {
            System.out.println("✗ Erreur lors de l'assemblage: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== DÉMONSTRATION TERMINÉE ===");
    }
}