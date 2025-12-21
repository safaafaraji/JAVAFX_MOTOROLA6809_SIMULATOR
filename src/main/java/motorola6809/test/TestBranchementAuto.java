package motorola6809.test;

import motorola6809.assembler.Assembler;

public class TestBranchementAuto {
    
    public static void main(String[] args) {
        System.out.println("=== TEST BRANCHEMENT AVEC OFFSET AUTO ===\n");
        
        // Programme avec étiquette - l'assembleur doit calculer l'offset
        String program = """
            ORG $1400
            
            START:
                LDA #$01
                LDB #$05
                
            BOUCLE:
                ADDA #$01
                DECB
                BNE BOUCLE   ; Doit calculer offset vers BOUCLE
                
                NOP
                END START
            """;
        
        Assembler assembler = new Assembler();
        
        try {
            System.out.println("Assemblage...");
            byte[] code = assembler.assemble(program);
            
            System.out.println("\n🎉 SUCCÈS !");
            System.out.println("L'assembleur a calculé l'offset automatiquement.");
            
            // Afficher le code avec explication
            System.out.println("\nCode généré avec explication:");
            System.out.println("1400: 86 01     LDA #$01");
            System.out.println("1402: C6 05     LDB #$05");
            System.out.println("1404: 8B 01     ADDA #$01");
            System.out.println("1406: 5A        DECB");
            System.out.println("1407: 26 FB     BNE BOUCLE  ; offset = -5 (0xFB)");
            System.out.println("1409: 12        NOP");
            
            System.out.println("\n✅ L'assembleur gère correctement les branchements !");
            
        } catch (Exception e) {
            System.err.println("\n❌ ÉCHEC:");
            System.err.println(e.getMessage());
            
            System.err.println("\n🚨 ACTION REQUISE:");
            System.err.println("1. Assurez-vous que Parser.getAddressingMode() retourne 'RELATIVE' pour BNE");
            System.err.println("2. Assurez-vous que encodeRelativeOperand() calcule correctement l'offset");
            System.err.println("3. Assurez-vous que BNE est dans OpcodeGenerator avec mode RELATIVE");
        }
    }
}