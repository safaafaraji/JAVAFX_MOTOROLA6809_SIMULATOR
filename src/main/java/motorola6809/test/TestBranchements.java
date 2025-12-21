package motorola6809.test;

import motorola6809.assembler.Assembler;

public class TestBranchements {
    
    public static void main(String[] args) {
        System.out.println("=== TEST BRANCHEMENTS ===\n");
        
        Assembler assembler = new Assembler();
        
        // Programme avec branchements mais adresses numériques
        String program = """
            ORG $1400
            
            ; Programme avec adresses NUMÉRIQUES pour les branchements
            LDA #$01
            LDB #$05
            
            ; Adresse de BOUCLE calculée: $1404
            ; Après LDA (#2 octets) + LDB (#2 octets) = $1404
            BOUCLE:
                ADDA #$01
                DECB
                BNE $1404    ; Adresse numérique au lieu d'étiquette
                
                NOP
                END
            """;
        
        try {
            System.out.println("Assemblage...");
            byte[] machineCode = assembler.assemble(program);
            
            System.out.println("\n✅ SUCCÈS !");
            System.out.println("Taille: " + machineCode.length + " octets");
            
        } catch (Exception e) {
            System.err.println("\n❌ ÉCHEC:");
            System.err.println(e.getMessage());
        }
    }
}