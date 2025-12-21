package motorola6809.test;

import motorola6809.assembler.Assembler;

public class TestSansRelative {
    
    public static void main(String[] args) {
        System.out.println("=== TEST SANS MODE RELATIVE ===\n");
        
        // Programme avec branchements en EXTENDED (adresses absolues)
        String program = """
            ORG $1400
            
            ; Tous les branchements en EXTENDED maintenant
            START:
                LDA #$01
                LDB #$05
                
            BOUCLE:
                ADDA #$01
                DECB
                BNE BOUCLE   ; EXTENDED (2 octets d'adresse)
                
                ; JMP aussi en EXTENDED
                JMP START
                
                NOP
                END START
            """;
        
        Assembler assembler = new Assembler();
        
        try {
            System.out.println("Assemblage...");
            byte[] machineCode = assembler.assemble(program);
            
            System.out.println("\n✅ SUCCÈS !");
            System.out.println("Toutes les instructions en EXTENDED mode.");
            System.out.println("Taille: " + machineCode.length + " octets");
            
            // Calcul attendu :
            // LDA #$01 = 86 01 (2)
            // LDB #$05 = C6 05 (2)
            // ADDA #$01 = 8B 01 (2)
            // DECB = 5A (1)
            // BNE BOUCLE = 26 14 06? (3) ← 2 octets d'adresse
            // JMP START = 7E 14 00 (3)
            // NOP = 12 (1)
            // Total: ~14 octets
            
            System.out.println("\nCode généré:");
            for (int i = 0; i < machineCode.length; i++) {
                System.out.printf("%02X ", machineCode[i] & 0xFF);
                if ((i + 1) % 8 == 0) System.out.print(" ");
                if ((i + 1) % 16 == 0) System.out.println();
            }
            
        } catch (Exception e) {
            System.err.println("\n❌ ÉCHEC:");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}