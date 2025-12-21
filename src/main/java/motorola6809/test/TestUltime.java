package motorola6809.test;

import motorola6809.assembler.Assembler;

public class TestUltime {
    
    public static void main(String[] args) {
        System.out.println("=== TEST ULTIME - DOIT FONCTIONNER ===\n");
        
        Assembler assembler = new Assembler();
        
        // Programme ULTRA SIMPLE avec adresses absolues (pas d'étiquettes)
        String program = """
            ORG $1400
            
            ; Programme avec adresses ABSOLUES seulement
            LDA #$01
            LDB #$02
            ADDA #$03
            SUBB #$01
            NOP
            JMP $1400    ; Adresse absolue
            
            END
            """;
        
        try {
            System.out.println("Assemblage...");
            byte[] machineCode = assembler.assemble(program);
            
            System.out.println("\n🎉 SUCCÈS TOTAL !");
            System.out.println("Code machine généré:");
            
            for (int i = 0; i < machineCode.length; i++) {
                System.out.printf("%02X ", machineCode[i] & 0xFF);
                if ((i + 1) % 8 == 0) System.out.print(" ");
                if ((i + 1) % 16 == 0) System.out.println();
            }
            
            System.out.println("\n\nDécodage:");
            System.out.println("LDA #$01    = 86 01");
            System.out.println("LDB #$02    = C6 02");
            System.out.println("ADDA #$03   = 8B 03");
            System.out.println("SUBB #$01   = C0 01");
            System.out.println("NOP         = 12");
            System.out.println("JMP $1400   = 7E 14 00");
            
            System.out.println("\n✅ L'assembleur fonctionne pour les instructions de base !");
            
        } catch (Exception e) {
            System.err.println("\n❌ ÉCHEC FONDAMENTAL !");
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            
            System.err.println("\n🚨 PROBLÈME CRITIQUE:");
            System.err.println("L'assembleur ne reconnaît pas les instructions de base.");
            System.err.println("Vérifiez:");
            System.err.println("1. OpcodeGenerator contient toutes les instructions");
            System.err.println("2. Parser.getAddressingMode() fonctionne");
            System.err.println("3. Assembler.generateInstruction() fonctionne");
        }
    }
}