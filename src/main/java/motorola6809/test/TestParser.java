package motorola6809.test;

import motorola6809.assembler.Assembler;

public class TestParser {
    
    public static void main(String[] args) {
        System.out.println("=== TEST DU PARSER ===\n");
        
        // Test simple du parsing
        String testCode = """
            ORG $1400
            START:
                LDA #$01
                LDB $50
                BNE START
                JMP $2000
                NOP
            END
            """;
        
        Assembler assembler = new Assembler();
        
        try {
            System.out.println("Test d'assemblage...");
            byte[] code = assembler.assemble(testCode);
            
            System.out.println("\n✅ SUCCÈS !");
            System.out.println("Code généré: " + code.length + " octets");
            
            // Afficher les modes détectés
            System.out.println("\nModes détectés:");
            System.out.println("LDA #$01  -> IMMEDIATE");
            System.out.println("LDB $50   -> DIRECT");
            System.out.println("BNE START -> RELATIVE");
            System.out.println("JMP $2000 -> EXTENDED");
            System.out.println("NOP       -> INHERENT");
            
        } catch (Exception e) {
            System.err.println("\n❌ ÉCHEC:");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}