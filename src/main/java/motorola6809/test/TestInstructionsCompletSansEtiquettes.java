package motorola6809.test;

import motorola6809.assembler.Assembler;

public class TestInstructionsCompletSansEtiquettes {
    
    public static void main(String[] args) {
        System.out.println("████████████████████████████████████████████████");
        System.out.println("██ TEST COMPLET INSTRUCTIONS SANS ÉTIQUETTES ██");
        System.out.println("████████████████████████████████████████████████\n");
        
        Assembler assembler = new Assembler();
        
        // Programme COMPLET sans AUCUNE étiquette - uniquement des adresses absolues
        String program = """
            ; ============================================
            ; TEST COMPLET MOTOROLA 6809 - SANS ÉTIQUETTES
            ; Toutes les instructions avec adresses absolues
            ; ============================================
            ORG $2000
            
            ; ========== SECTION 1: CHARGEMENT (LOAD) ==========
            ; LDA - Load Accumulator A
            LDA #$42        ; Immédiat
            LDA $50         ; Direct Page
            LDA $1000       ; Étendu
            
            ; LDB - Load Accumulator B  
            LDB #$13        ; Immédiat
            LDB $60         ; Direct
            LDB $1100       ; Étendu
            
            ; LDD - Load Double Accumulator D (A:B)
            LDD #$1234      ; Immédiat 16-bit
            LDD $70         ; Direct
            LDD $1200       ; Étendu
            
            ; LDX - Load Index Register X
            LDX #$5678      ; Immédiat
            LDX $80         ; Direct
            LDX $1300       ; Étendu
            
            ; LDY - Load Index Register Y
            LDY #$9ABC      ; Immédiat
            LDY $90         ; Direct
            LDY $1400       ; Étendu
            
            ; LDU - Load User Stack Pointer
            LDU #$DEF0      ; Immédiat
            LDU $A0         ; Direct
            LDU $1500       ; Étendu
            
            ; ========== SECTION 2: STOCKAGE (STORE) ==========
            ; STA - Store Accumulator A
            STA $B0         ; Direct
            STA $1600       ; Étendu
            
            ; STB - Store Accumulator B
            STB $C0         ; Direct  
            STB $1700       ; Étendu
            
            ; STD - Store Double Accumulator D
            STD $D0         ; Direct
            STD $1800       ; Étendu
            
            ; STX - Store Index Register X
            STX $E0         ; Direct
            STX $1900       ; Étendu
            
            ; STY - Store Index Register Y
            STY $F0         ; Direct
            STY $1A00       ; Étendu
            
            ; ========== SECTION 3: ARITHMÉTIQUE ==========
            ; ADDA - Add to A
            ADDA #$01       ; Immédiat
            ADDA $10        ; Direct
            ADDA $1B00      ; Étendu
            
            ; ADDB - Add to B
            ADDB #$02       ; Immédiat
            ADDB $20        ; Direct
            ADDB $1C00      ; Étendu
            
            ; ADDD - Add to D
            ADDD #$0304     ; Immédiat
            ADDD $30        ; Direct
            ADDD $1D00      ; Étendu
            
            ; SUBA - Subtract from A
            SUBA #$05       ; Immédiat
            SUBA $40        ; Direct
            SUBA $1E00      ; Étendu
            
            ; SUBB - Subtract from B
            SUBB #$06       ; Immédiat
            SUBB $50        ; Direct
            SUBB $1F00      ; Étendu
            
            ; SUBD - Subtract from D
            SUBD #$0708     ; Immédiat
            SUBD $60        ; Direct
            SUBD $2000      ; Étendu
            
            ; INCA - Increment A
            INCA            ; Inhérent
            
            ; INCB - Increment B
            INCB            ; Inhérent
            
            ; INC - Increment Memory
            INC $70         ; Direct
            INC $2100       ; Étendu
            
            ; DECA - Decrement A
            DECA            ; Inhérent
            
            ; DECB - Decrement B
            DECB            ; Inhérent
            
            ; DEC - Decrement Memory
            DEC $80         ; Direct
            DEC $2200       ; Étendu
            
            ; MUL - Multiply
            MUL             ; Inhérent (A × B → D)
            
            ; DAA - Decimal Adjust A
            DAA             ; Inhérent
            
            ; ========== SECTION 4: LOGIQUE ==========
            ; ANDA - Logical AND A
            ANDA #$0F       ; Immédiat
            ANDA $90        ; Direct
            ANDA $2300      ; Étendu
            
            ; ANDB - Logical AND B
            ANDB #$F0       ; Immédiat
            ANDB $A0        ; Direct
            ANDB $2400      ; Étendu
            
            ; ANDCC - AND Condition Codes
            ANDCC #$F0      ; Immédiat
            
            ; ORA - Logical OR A
            ORA #$FF        ; Immédiat
            ORA $B0         ; Direct
            ORA $2500       ; Étendu
            
            ; ORB - Logical OR B
            ORB #$00        ; Immédiat
            ORB $C0         ; Direct
            ORB $2600       ; Étendu
            
            ; ORCC - OR Condition Codes
            ORCC #$0F       ; Immédiat
            
            ; EORA - Exclusive OR A
            EORA #$AA       ; Immédiat
            EORA $D0        ; Direct
            EORA $2700      ; Étendu
            
            ; EORB - Exclusive OR B
            EORB #$55       ; Immédiat
            EORB $E0        ; Direct
            EORB $2800      ; Étendu
            
            ; COMA - Complement A
            COMA            ; Inhérent
            
            ; COMB - Complement B
            COMB            ; Inhérent
            
            ; COM - Complement Memory
            COM $F0         ; Direct
            COM $2900       ; Étendu
            
            ; NEGA - Negate A
            NEGA            ; Inhérent
            
            ; NEGB - Negate B
            NEGB            ; Inhérent
            
            ; NEG - Negate Memory
            NEG $00         ; Direct
            NEG $2A00       ; Étendu
            
            ; ========== SECTION 5: COMPARAISON ==========
            ; CMPA - Compare A
            CMPA #$10       ; Immédiat
            CMPA $01        ; Direct
            CMPA $2B00      ; Étendu
            
            ; CMPB - Compare B
            CMPB #$20       ; Immédiat
            CMPB $02        ; Direct
            CMPB $2C00      ; Étendu
            
            ; CMPD - Compare D
            CMPD #$3040     ; Immédiat
            CMPD $03        ; Direct
            CMPD $2D00      ; Étendu
            
            ; CMPX - Compare X
            CMPX #$5060     ; Immédiat
            CMPX $04        ; Direct
            CMPX $2E00      ; Étendu
            
            ; CMPY - Compare Y
            CMPY #$7080     ; Immédiat
            CMPY $05        ; Direct
            CMPY $2F00      ; Étendu
            
            ; TSTA - Test A
            TSTA            ; Inhérent
            
            ; TSTB - Test B
            TSTB            ; Inhérent
            
            ; TST - Test Memory
            TST $06         ; Direct
            TST $3000       ; Étendu
            
            ; ========== SECTION 6: BRANCHEMENT ==========
            ; NOP - No Operation
            NOP             ; Inhérent
            
            ; JMP - Jump
            JMP $3100       ; Étendu
            
            ; JSR - Jump to Subroutine
            JSR $3200       ; Étendu
            
            ; RTS - Return from Subroutine
            RTS             ; Inhérent
            
            ; RTI - Return from Interrupt
            RTI             ; Inhérent
            
            ; ========== SECTION 7: AUTRES INSTRUCTIONS ==========
            ; EXG - Exchange Registers
            EXG A,B         ; Inhérent (temporaire)
            
            ; CLRA - Clear A
            CLRA            ; Inhérent
            
            ; CLRB - Clear B
            CLRB            ; Inhérent
            
            ; CLR - Clear Memory
            CLR $07         ; Direct
            CLR $3300       ; Étendu
            
            ; ABX - Add B to X
            ABX             ; Inhérent
            
            ; SEX - Sign Extend
            SEX             ; Inhérent (B → A)
            
            ; CWAI - Wait for Interrupt
            CWAI #$FF       ; Immédiat
            
            ; SWI - Software Interrupt
            SWI             ; Inhérent
            
            ; SWI2 - Software Interrupt 2
            SWI2            ; Inhérent
            
            ; SWI3 - Software Interrupt 3
            SWI3            ; Inhérent
            
            ; SYNC - Synchronize
            SYNC            ; Inhérent
            
            ; ========== SECTION 8: TRANSFERT PILE ==========
            ; PSHS - Push onto Hardware Stack
            PSHS A,B        ; Inhérent
            
            ; PULS - Pull from Hardware Stack
            PULS X,Y        ; Inhérent
            
            ; PSHU - Push onto User Stack
            PSHU S,U        ; Inhérent
            
            ; PULU - Pull from User Stack
            PULU D,PC       ; Inhérent
            
            ; ========== FIN ==========
            ; Boucle infinie sur l'adresse de début
            JMP $2000       ; Étendu
            
            END
            """;
        
        try {
            System.out.println("🔧 Assemblage en cours...");
            long startTime = System.currentTimeMillis();
            byte[] machineCode = assembler.assemble(program);
            long endTime = System.currentTimeMillis();
            
            System.out.println("\n" + "=".repeat(60));
            System.out.println("✅ ASSEMBLAGE RÉUSSI !");
            System.out.println("=".repeat(60));
            
            System.out.println("\n📊 STATISTIQUES:");
            System.out.println("- Temps d'assemblage: " + (endTime - startTime) + " ms");
            System.out.println("- Taille du code: " + machineCode.length + " octets");
            System.out.println("- Adresse de départ: $2000");
            System.out.println("- Adresse de fin: $" + 
                Integer.toHexString(0x2000 + machineCode.length).toUpperCase());
            
            System.out.println("\n🧪 INSTRUCTIONS TESTÉES:");
            System.out.println("✓ CHARGEMENT: LDA, LDB, LDD, LDX, LDY, LDU");
            System.out.println("✓ STOCKAGE: STA, STB, STD, STX, STY");
            System.out.println("✓ ARITHMÉTIQUE: ADDA, ADDB, ADDD, SUBA, SUBB, SUBD");
            System.out.println("✓ INCRÉMENT/DÉCRÉMENT: INCA, INCB, INC, DECA, DECB, DEC");
            System.out.println("✓ LOGIQUE: ANDA, ANDB, ANDCC, ORA, ORB, ORCC, EORA, EORB");
            System.out.println("✓ COMPLÉMENT/NÉGATION: COMA, COMB, COM, NEGA, NEGB, NEG");
            System.out.println("✓ COMPARAISON: CMPA, CMPB, CMPD, CMPX, CMPY, TSTA, TSTB, TST");
            System.out.println("✓ BRANCHEMENT: NOP, JMP, JSR, RTS, RTI");
            System.out.println("✓ AUTRES: MUL, DAA, EXG, CLRA, CLRB, CLR, ABX, SEX");
            System.out.println("✓ INTERRUPTIONS: CWAI, SWI, SWI2, SWI3, SYNC");
            System.out.println("✓ PILE: PSHS, PULS, PSHU, PULU");
            
            System.out.println("\n🎛️  MODES D'ADRESSAGE TESTÉS:");
            System.out.println("✓ IMMEDIATE (8-bit et 16-bit)");
            System.out.println("✓ DIRECT (Direct Page $00-$FF)");
            System.out.println("✓ EXTENDED (16-bit address)");
            System.out.println("✓ INHERENT (pas d'opérande)");
            
            System.out.println("\n🔍 APERÇU DU CODE MACHINE:");
            System.out.println("(Premiers 64 octets)");
            for (int i = 0; i < Math.min(64, machineCode.length); i++) {
                System.out.printf("%02X ", machineCode[i] & 0xFF);
                if ((i + 1) % 16 == 0) System.out.println();
                else if ((i + 1) % 8 == 0) System.out.print(" ");
            }
            
            if (machineCode.length > 64) {
                System.out.println("\n... (" + (machineCode.length - 64) + " octets supplémentaires)");
            }
            
            System.out.println("\n" + "=".repeat(60));
            System.out.println("🎉 TEST COMPLET RÉUSSI !");
            System.out.println("L'assembleur supporte toutes les instructions de base.");
            System.out.println("=".repeat(60));
            
            // Vérification rapide
            System.out.println("\n💡 CONSEIL:");
            System.out.println("Pour tester les étiquettes, commencez par:");
            System.out.println("1. Programme avec UNE étiquette simple");
            System.out.println("2. Vérifiez que firstPass() l'ajoute à symbolTable");
            System.out.println("3. Vérifiez que parseValue() la trouve");
            
        } catch (Exception e) {
            System.err.println("\n" + "=".repeat(60));
            System.err.println("❌ ÉCHEC DE L'ASSEMBLAGE");
            System.err.println("=".repeat(60));
            System.err.println("\nMessage d'erreur: " + e.getMessage());
            
            // Analyse de l'erreur
            String error = e.getMessage();
            if (error.contains("Instruction inconnue")) {
                System.err.println("\n🔧 SOLUTION:");
                System.err.println("Ajoutez l'instruction manquante dans OpcodeGenerator.java");
            } else if (error.contains("Mode d'adressage")) {
                System.err.println("\n🔧 SOLUTION:");
                System.err.println("Vérifiez que l'instruction a ce mode dans OpcodeGenerator");
            } else if (error.contains("Valeur invalide")) {
                System.err.println("\n🔧 SOLUTION:");
                System.err.println("Problème dans parseValue() - vérifiez la gestion des nombres");
            }
            
            // Debug supplémentaire
            System.err.println("\n📋 DÉTAILS DE L'ERREUR:");
            e.printStackTrace();
        }
    }
}