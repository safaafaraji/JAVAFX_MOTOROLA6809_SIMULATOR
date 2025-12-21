package motorola6809.test;

import motorola6809.assembler.Assembler;

public class TestAvecEtiquettesCorrige {
    
    public static void main(String[] args) {
        System.out.println("=== TEST CORRIGÉ AVEC ÉTIQUETTES ===\n");
        
        Assembler assembler = new Assembler();
        
        // Programme CORRIGÉ - pas de MAX comme instruction
        String program = """
            ORG $1400
            
            ; Définir une constante avec EQU
            MAX EQU 10
            
            ; Point d'entrée
            MAIN:
                LDA #MAX        ; Utilise la constante (immédiat avec #)
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
            
            // Afficher le code
            System.out.println("\nCode machine:");
            for (int i = 0; i < machineCode.length; i++) {
                System.out.printf("%02X ", machineCode[i] & 0xFF);
                if ((i + 1) % 8 == 0) System.out.print(" ");
                if ((i + 1) % 16 == 0) System.out.println();
            }
            
            // Afficher la table des symboles
            System.out.println("\n\nTable des symboles:");
            assembler.getSymbolTable().getSymbols().forEach((label, address) -> {
                System.out.printf("  %-10s = $%04X\n", label, address);
            });
            
            // Afficher les constantes
            System.out.println("\nConstantes (via EQU):");
            // Vous devrez peut-être ajouter une méthode getConstants() à SymbolTable
            
        } catch (Exception e) {
            System.err.println("\n❌ ÉCHEC:");
            System.err.println(e.getMessage());
            
            // Aide au débogage
            System.err.println("\n💡 CONSEIL:");
            System.err.println("Le problème est que 'MAX EQU 10' est traité comme une instruction.");
            System.err.println("Assurez-vous que EQU est dans la liste des directives dans isDirective().");
            System.err.println("Et qu'il est géré dans processDirective() ou ignoré dans secondPass().");
        }
    }
}