package motorola6809.test;

import motorola6809.assembler.Assembler;

public class TestSansEtiquettes {
    
    public static void main(String[] args) {
        System.out.println("=== TEST SANS ÉTIQUETTES ===\n");
        
        Assembler assembler = new Assembler();
        
        // Programme SANS étiquettes - juste des adresses absolues
        String simpleProgram = 
        	    "; Programme sans étiquettes\n" +
        	    "        ORG $1400\n" +
        	    "\n" +
        	    "        ; Instructions avec adresses absolues\n" +
        	    "        LDA #$01            ; Immédiat\n" +
        	    "        LDB #$02            ; Immédiat\n" +
        	    "        ADDA #$03           ; Immédiat\n" +
        	    "        SUBB #$01           ; Immédiat\n" +
        	    "        NOP                 ; Inhérent\n" +
        	    "        JMP $1400           ; Étendu (adresse absolue)\n" +
        	    "\n" +
        	    "        END\n";
        
        try {
            System.out.println("Assemblage...");
            byte[] machineCode = assembler.assemble(simpleProgram);
            
            System.out.println("\n✅ SUCCÈS !");
            System.out.println("Code machine (" + machineCode.length + " octets):");
            
            for (int i = 0; i < machineCode.length; i++) {
                System.out.printf("%02X ", machineCode[i] & 0xFF);
                if ((i + 1) % 8 == 0) System.out.print(" ");
                if ((i + 1) % 16 == 0) System.out.println();
            }
            
            // Vérification manuelle
            System.out.println("\n\nVérification:");
            System.out.println("LDA #$01  = 86 01");
            System.out.println("LDB #$02  = C6 02");
            System.out.println("ADDA #$03 = 8B 03");
            System.out.println("SUBB #$01 = C0 01");
            System.out.println("NOP       = 12");
            System.out.println("JMP $1400 = 7E 14 00");
            
        } catch (Exception e) {
            System.err.println("\n❌ ÉCHEC:");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}