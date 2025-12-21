package motorola6809.test;

import motorola6809.assembler.Assembler;

public class TestAssemblerComplet {
    
    public static void main(String[] args) {
        System.out.println("=== TEST ASSEMBLEUR COMPLET 6809 ===\n");
        
        Assembler assembler = new Assembler();
        
        // Test avec TOUTES les instructions du 6809
        String allInstructions = """
            ; Test de toutes les instructions Motorola 6809
            ORG $1400
            
            START:
                ; === CHARGEMENT ===
                LDA #$42        ; IMMEDIATE
                LDB $50         ; DIRECT
                LDD $0100       ; EXTENDED
                LDX ,X          ; INDEXED
                LDY $0200       ; EXTENDED
                LDS $0300       ; EXTENDED
                LDU $0400       ; EXTENDED
                
                ; === STOCKAGE ===
                STA $60         ; DIRECT
                STB $70         ; DIRECT
                STD $0800       ; EXTENDED
                STX $0900       ; EXTENDED
                STY $0A00       ; EXTENDED
                STS $0B00       ; EXTENDED
                STU $0C00       ; EXTENDED
                
                ; === ARITHMETIQUE ===
                ADDA #$10       ; IMMEDIATE
                ADDB $80        ; DIRECT
                ADDD $0D00      ; EXTENDED
                SUBA #$05       ; IMMEDIATE
                SUBB $90        ; DIRECT
                SUBD $0E00      ; EXTENDED
                
                ; === INCREMENT/DECREMENT ===
                INCA            ; INHERENT
                INCB            ; INHERENT
                INC $A0         ; DIRECT
                DECA            ; INHERENT
                DECB            ; INHERENT
                DEC $B0         ; DIRECT
                
                ; === LOGIQUE ===
                ANDA #$0F       ; IMMEDIATE
                ANDB $C0        ; DIRECT
                ANDCC #$F0      ; IMMEDIATE
                ORA #$F0        ; IMMEDIATE (CORRIGÉ)
                ORB $D0         ; DIRECT
                ORCC #$0F       ; IMMEDIATE
                EORA #$FF       ; IMMEDIATE
                EORB $E0        ; DIRECT
                COMA            ; INHERENT
                COMB            ; INHERENT
                COM $F0         ; DIRECT
                
                ; === COMPARAISON ===
                CMPA #$20       ; IMMEDIATE
                CMPB $00        ; DIRECT
                CMPD $1000      ; EXTENDED
                CMPX $1100      ; EXTENDED
                CMPY $1200      ; EXTENDED
                TSTA            ; INHERENT
                TSTB            ; INHERENT
                TST $1300       ; EXTENDED
                
                ; === BRANCHEMENT ===
                BEQ START       ; RELATIVE
                BNE LOOP        ; RELATIVE
                JMP START       ; EXTENDED
                JSR SUBR        ; EXTENDED
                BSR SUBR2       ; RELATIVE
                
            LOOP:
                ; === TRANSFERT ===
                EXG A,B         ; INHERENT
                TFR A,B         ; INHERENT
                
                ; === AUTRES INSTRUCTIONS ===
                NOP             ; INHERENT
                MUL             ; INHERENT
                DAA             ; INHERENT
                CLRA            ; INHERENT
                CLRB            ; INHERENT
                CLR $1400       ; EXTENDED
                NEGA            ; INHERENT
                NEGB            ; INHERENT
                NEG $1500       ; EXTENDED
                ABX             ; INHERENT
                SEX             ; INHERENT
                
                ; === INSTRUCTIONS PILE ===
                PSHS A,B        ; INHERENT
                PULS X,Y        ; INHERENT
                PSHU S,U        ; INHERENT
                PULU D,PC       ; INHERENT
                
                ; === LEA ===
                LEAX ,X         ; INDEXED
                LEAY ,Y         ; INDEXED
                LEAS ,S         ; INDEXED
                LEAU ,U         ; INDEXED
                
                ; Fin
                CWAI #$FF       ; IMMEDIATE
                RTS             ; INHERENT
                
            SUBR:
                ; Sous-routine 1
                SWI             ; INHERENT
                SWI2            ; INHERENT
                SWI3            ; INHERENT
                RTI             ; INHERENT
                RTS             ; INHERENT
                
            SUBR2:
                ; Sous-routine 2
                SYNC            ; INHERENT
                BRA SUBR2       ; RELATIVE
                
            END START
            """;
        
        try {
            System.out.println("Assemblage du programme complet...");
            byte[] machineCode = assembler.assemble(allInstructions);
            
            System.out.println("\n✅ ASSEMBLAGE RÉUSSI !");
            System.out.println("Taille du code: " + machineCode.length + " octets");
            
            // Afficher les premiers 64 octets
            System.out.println("\n📊 Début du code machine (64 premiers octets):");
            for (int i = 0; i < Math.min(64, machineCode.length); i++) {
                System.out.printf("%02X ", machineCode[i] & 0xFF);
                if ((i + 1) % 16 == 0) System.out.println();
            }
            
            // Afficher la table des symboles
            System.out.println("\n📋 Table des symboles:");
            assembler.getSymbolTable().getSymbols().forEach((label, address) -> {
                System.out.printf("  %-10s = $%04X\n", label, address);
            });
            
            System.out.println("\n🎉 Toutes les instructions sont supportées !");
            
        } catch (Exception e) {
            System.err.println("\n❌ ERREUR D'ASSEMBLAGE:");
            System.err.println(e.getMessage());
            
            // Afficher plus de détails sur l'erreur
            if (e instanceof motorola6809.assembler.AssemblerException) {
                motorola6809.assembler.AssemblerException ae = (motorola6809.assembler.AssemblerException) e;
                System.err.println("Ligne: " + ae.getLineNumber());
            }
        }
    }
}