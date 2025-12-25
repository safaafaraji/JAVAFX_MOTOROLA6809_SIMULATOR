// TestInstructions.java
package motorola6809.test;

import motorola6809.core.SimulatorBackend;

public class TestInstructions {
    
    public static void main(String[] args) {
        System.out.println("=== TEST INSTRUCTIONS COMPLÈTES ===");
        
        SimulatorBackend backend = SimulatorBackend.getInstance();
        
        // Programme testant plusieurs instructions
        String program = 
            "        ORG $1000\n" +
            "        LDA #$55     ; A = $55\n" +
            "        LDB #$AA     ; B = $AA\n" +
            "        ADDA #$01    ; A = $56\n" +
            "        ADDB #$01    ; B = $AB\n" +
            "        STA $2000    ; Stocker A\n" +
            "        STB $2001    ; Stocker B\n" +
            "        ADDA B       ; A = A + B\n" +
            "        STA $2002    ; Stocker résultat\n" +
            "        MUL          ; D = A * B\n" +
            "        STD $2003    ; Stocker D\n" +
            "        END";
        
        System.out.println("1. Assemblage...");
        backend.assemble(program);
        
        System.out.println("\n2. Exécution complète...");
        
        // Exécuter toutes les instructions
        while (backend.getPC() < 0x1000 + 50) { // Limite de sécurité
            backend.step();
            
            // Arrêter si SWI ou fin
            if (backend.getCPU().isHalted()) {
                break;
            }
        }
        
        System.out.println("\n3. Résultats mémoire:");
        System.out.println("   $2000 (A) = $" + String.format("%02X", backend.readMemory(0x2000)));
        System.out.println("   $2001 (B) = $" + String.format("%02X", backend.readMemory(0x2001)));
        System.out.println("   $2002 (A+B) = $" + String.format("%02X", backend.readMemory(0x2002)));
        System.out.println("   $2003-4 (D) = $" + 
            String.format("%02X", backend.readMemory(0x2003)) + 
            String.format("%02X", backend.readMemory(0x2004)));
        
        System.out.println("\n4. Registres finaux:");
        System.out.println("   A = $" + String.format("%02X", backend.getA()));
        System.out.println("   B = $" + String.format("%02X", backend.getB()));
        System.out.println("   D = $" + String.format("%04X", backend.getD()));
        
        System.out.println("\n=== TEST TERMINÉ ===");
    }
}