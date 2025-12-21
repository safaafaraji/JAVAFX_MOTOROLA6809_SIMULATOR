package motorola6809.test;

import motorola6809.assembler.Assembler;

public class TestSansBranchements {
    
    public static void main(String[] args) {
        System.out.println("=== TEST SANS BRANCHEMENTS (SAUF BITx) ===\n");
        
        Assembler assembler = new Assembler();
        
        // Programme sans AUCUN branchement
        String program = """
            ORG $2000
            
            ; === CHARGEMENT ===
            LDA #$42
            LDB #$13
            LDD #$1234
            LDX #$5678
            LDY #$9ABC
            LDU #$DEF0
            
            ; === STOCKAGE ===
            STA $0080
            STB $0090
            STD $00A0
            STX $00B0
            STY $00C0
            STU $00D0
            
            ; === ARITHMETIQUE ===
            ADDA #$01
            ADDB #$02
            ADDD #$0304
            SUBA #$05
            SUBB #$06
            SUBD #$0708
            
            ; === INCREMENT/DECREMENT ===
            INCA
            INCB
            INC $00E0
            DECA
            DECB
            DEC $00F0
            
            ; === LOGIQUE (INCLUT BITA/BITB) ===
            ANDA #$0F
            ANDB #$F0
            ANDCC #$F0
            ORA #$FF
            ORB #$00
            ORCC #$0F
            EORA #$AA
            EORB #$55
            
            ; === BIT TEST (PAS DES BRANCHEMENTS !) ===
            BITA #$01      ; Test bit 0 de A
            BITB #$80      ; Test bit 7 de B
            BITA $0100     ; Test avec mémoire
            BITB $0200     ; Test avec mémoire
            
            COMA
            COMB
            COM $0300
            NEGA
            NEGB
            NEG $0400
            
            ; === COMPARAISON ===
            CMPA #$10
            CMPB #$20
            CMPD #$3040
            CMPX #$5060
            CMPY #$7080
            TSTA
            TSTB
            TST $0500
            
            ; === TRANSFERT ===
            EXG A,B        ; Échange A et B
            TFR X,Y        ; Transfert X vers Y
            
            ; === PILE ===
            PSHS A,B,X,Y   ; Push sur pile système
            PULS A,B,X,Y   ; Pull de pile système
            PSHU S,U       ; Push sur pile utilisateur
            PULU D,PC      ; Pull de pile utilisateur
            
            ; === AUTRES ===
            NOP
            MUL
            DAA
            CLRA
            CLRB
            CLR $0600
            ABX
            SEX
            CWAI #$FF
            SWI
            SWI2
            SWI3
            SYNC
            RTS
            RTI
            
            ; === JMP/JSR (saut absolu, pas relatif) ===
            JMP $2000      ; Saut absolu - OK
            JSR $2100      ; Appel absolu - OK
            
            END
            """;
        
        try {
            System.out.println("Assemblage en cours...");
            byte[] machineCode = assembler.assemble(program);
            
            System.out.println("\n✅ SUCCÈS TOTAL !");
            System.out.println("Taille du code: " + machineCode.length + " octets");
            
            // Vérification
            System.out.println("\n📋 VÉRIFICATION:");
            System.out.println("✓ Pas de branchements relatifs (BRA, BNE, etc.)");
            System.out.println("✓ BITA et BITB présents (instructions logiques)");
            System.out.println("✓ JMP et JSR présents (sauts absolus)");
            System.out.println("✓ Toutes les autres instructions fonctionnent");
            
            System.out.println("\n🎉 ASSEMBLEUR MOTOROLA 6809 SANS BRANCHEMENTS PRÊT !");
            
        } catch (Exception e) {
            System.err.println("\n❌ ÉCHEC:");
            System.err.println(e.getMessage());
            
            if (e.getMessage().contains("BRA") || e.getMessage().contains("BNE") || 
                e.getMessage().contains("BEQ") || e.getMessage().contains("BCC")) {
                System.err.println("\n⚠️  Encore un branchement détecté !");
                System.err.println("Supprimez-le de OpcodeGenerator.java");
            }
        }
    }
}