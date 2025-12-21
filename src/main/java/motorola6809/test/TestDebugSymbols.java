package motorola6809.test;

import motorola6809.assembler.Assembler;
import motorola6809.assembler.Parser;
import java.util.List;

public class TestDebugSymbols {
    
    public static void main(String[] args) {
        System.out.println("=== DEBUG SYMBOLES ===\n");
        
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
        
        // 1. Parse seulement
        System.out.println("1. Parsing du code:");
        List<Parser.ParsedLine> lines = Parser.parseProgram(testCode);
        for (Parser.ParsedLine line : lines) {
            if (!line.isEmpty) {
                System.out.printf("Ligne %d: label='%s', mnemonic='%s', operand='%s'\n",
                    line.lineNumber, line.label, line.mnemonic, line.operand);
            }
        }
        
        // 2. Assembler avec debug
        System.out.println("\n2. Assemblage avec debug:");
        Assembler assembler = new Assembler();
        
        try {
            byte[] code = assembler.assemble(testCode);
            System.out.println("✅ Assemblage réussi!");
        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            
            // Debug manuel de la table des symboles
            System.err.println("\n🔍 État de la table des symboles lors de l'erreur:");
            // Malheureusement, on ne peut pas y accéder après l'erreur
            // Mais on peut modifier Assembler pour afficher plus d'info
        }
    }
}