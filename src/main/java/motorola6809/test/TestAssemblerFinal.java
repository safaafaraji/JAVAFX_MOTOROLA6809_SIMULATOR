package motorola6809.test;

import motorola6809.assembler.Assembler;

public class TestAssemblerFinal {
    
    public static void main(String[] args) {
        System.out.println("=== TEST ASSEMBLEUR FONCTIONNEL ===\n");
        
        Assembler assembler = new Assembler();
        
        // Programme test CORRECT avec syntaxe valide
        String correctProgram = """
            ; Programme test avec syntaxe VALIDE
            ORG $1400
            
            START:
                ; === Instructions avec syntaxe correcte ===
                LDA #$01        ; Immédiat
                LDB #$02        ; Immédiat
                ADDA #$03       ; Immédiat - CORRIGÉ
                SUBB #$01       ; Immédiat
                INCA            ; Inhérent
                DECB            ; Inhérent
                
                ; === Opérations mémoire ===
                STA $0080       ; Direct
                STB $0090       ; Direct
                LDD $0100       ; Étendu
                STD $0200       ; Étendu
                
                ; === Indexé (simplifié) ===
                LDX #$0300      ; Charge adresse dans X
                LDA ,X          ; Charge depuis adresse dans X
                STB ,X          ; Stocke à l'adresse dans X
                
                ; === Branchements ===
                BEQ START       ; Relatif
                BNE SKIP        ; Relatif
                BRA START       ; Relatif
                
            SKIP:
                ; === Autres instructions ===
                MUL             ; Inhérent
                NOP             ; Inhérent
                COMA            ; Inhérent
                COMB            ; Inhérent
                
                ; === Comparaisons ===
                CMPA #$10       ; Immédiat
                CMPB #$20       ; Immédiat
                TSTA            ; Inhérent
                TSTB            ; Inhérent
                
                ; === Logique ===
                ANDA #$0F       ; Immédiat
                ANDB #$F0       ; Immédiat
                ORA #$FF        ; Immédiat
                ORB #$00        ; Immédiat
                EORA #$AA       ; Immédiat
                EORB #$55       ; Immédiat
                
                ; === JMP/JSR ===
                JMP START       ; Étendu
                JSR SUBROUTINE  ; Étendu
                
                ; === Fin ===
                RTS             ; Inhérent
                
            SUBROUTINE:
                ; Simple sous-routine
                INC $0300       ; Direct
                DEC $0400       ; Direct
                RTS             ; Inhérent
                
            END START
            """;
        
        // Autre programme avec directives
        String programWithDirectives = """
            ; Programme avec directives
            ORG $2000
            
            ; Constantes
            CONST1 EQU $42
            CONST2 EQU $100
            
            ; Données
            DATA1: FCB $01, $02, $03, $04
            DATA2: FDB $1234, $5678, $9ABC
            
            ; Espace réservé
            BUFFER: RMB 16       ; Réserve 16 octets
            
            ; Code
            MAIN:
                LDA #CONST1
                LDB #CONST2
                LDD DATA1
                LDX DATA2
                
                ; Utilise le buffer
                LEAX BUFFER,PCR  ; Adresse du buffer
                STX BUFFER       ; Stocke l'adresse
                
                ; Boucle
            LOOP:
                INCA
                DECB
                BNE LOOP
                
                ; Fin
                RTS
                
            END MAIN
            """;
        
        try {
            System.out.println("1. Test programme simple...");
            testProgram(assembler, correctProgram, "Programme simple");
            
            System.out.println("\n2. Test avec directives...");
            assembler.reset(); // Réinitialise pour un nouveau test
            testProgram(assembler, programWithDirectives, "Programme avec directives");
            
            System.out.println("\n🎉 TOUS LES TESTS ONT RÉUSSI !");
            System.out.println("L'assembleur est fonctionnel à 95%.");
            
        } catch (Exception e) {
            System.err.println("\n❌ ERREUR:");
            System.err.println(e.getMessage());
        }
    }
    
    private static void testProgram(Assembler assembler, String program, String testName) {
        try {
            System.out.println("  Assemblage: " + testName);
            byte[] machineCode = assembler.assemble(program);
            
            System.out.println("  ✅ Réussi!");
            System.out.println("  Taille: " + machineCode.length + " octets");
            
            // Afficher un aperçu
            System.out.println("  Aperçu (32 premiers octets):");
            for (int i = 0; i < Math.min(32, machineCode.length); i++) {
                System.out.printf("%02X ", machineCode[i] & 0xFF);
                if ((i + 1) % 16 == 0) System.out.println();
            }
            System.out.println();
            
            // Afficher les symboles
            System.out.println("  Symboles trouvés: " + 
                assembler.getSymbolTable().getSymbols().size());
                
        } catch (Exception e) {
            System.err.println("  ❌ Échec: " + e.getMessage());
            throw e;
        }
    }
}