package motorola6809.test;

import motorola6809.assembler.Assembler;

public class TestAvecEtiquettes {
    
    public static void main(String[] args) {
        System.out.println("=== TEST AVEC ÉTIQUETTES ===\n");
        
        Assembler assembler = new Assembler();
        
        // Programme AVEC étiquettes
        String program = """
            ORG $1400
            
            ; Définir une constante
            MAX EQU 10
            
            ; Point d'entrée
            MAIN:
                LDA #MAX        ; Utilise la constante
                LDB #$05
                
            BOUCLE:
                ADDA #$01
                DECB
                BNE BOUCLE      ; Branchement vers étiquette
                
                JMP FIN         ; Branchement vers étiquette
                
            FIN:
                NOP
                END MAIN
            """;
        
        try {
            System.out.println("Assemblage...");
            byte[] machineCode = assembler.assemble(program);
            
            System.out.println("\n✅ SUCCÈS !");
            System.out.println("Taille: " + machineCode.length + " octets");
            
            // Afficher la table des symboles
            System.out.println("\nTable des symboles:");
            assembler.getSymbolTable().getSymbols().forEach((label, address) -> {
                System.out.printf("  %-10s = $%04X\n", label, address);
            });
            
        } catch (Exception e) {
            System.err.println("\n❌ ÉCHEC:");
            System.err.println(e.getMessage());
            
            // Debug avancé
            System.err.println("\nStack trace:");
            e.printStackTrace();
        }
    }
}