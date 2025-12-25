package motorola6809.test;

import motorola6809.assembler.Parser;
import motorola6809.assembler.Assembler;
import java.util.List;

public class TestParser {
    
    public static void main(String[] args) {
        System.out.println("=== TEST PARSER ET ASSEMBLEUR ===");
        
        // Test 1: Programme sans étiquette
        String program1 = 
            "        ORG $1000\n" +
            "        LDA #$55\n" +
            "        STA $2000\n" +
            "        END";
        
        System.out.println("\n1. Programme sans étiquette:");
        testAssemble(program1);
        
        // Test 2: Programme avec étiquette avec :
        String program2 = 
            "        ORG $1000\n" +
            "START:  LDA #$55\n" +
            "        STA $2000\n" +
            "        END";
        
        System.out.println("\n2. Programme avec étiquette avec ':':");
        testAssemble(program2);
        
        // Test 3: Programme avec étiquette sans :
        String program3 = 
            "        ORG $1000\n" +
            "START   LDA #$55\n" +
            "        STA $2000\n" +
            "        END";
        
        System.out.println("\n3. Programme avec étiquette sans ':':");
        testAssemble(program3);
    }
    
    private static void testAssemble(String program) {
        try {
            Assembler assembler = new Assembler();
            byte[] code = assembler.assemble(program);
            System.out.println("   Assemblage réussi ! Taille: " + code.length + " octets");
            
            // Afficher le code machine
            System.out.print("   Code machine: ");
            for (int i = 0; i < Math.min(10, code.length); i++) {
                System.out.print(String.format("%02X ", code[i] & 0xFF));
            }
            System.out.println();
            
        } catch (Exception e) {
            System.out.println("   ERREUR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}