package motorola6809.test;

import motorola6809.core.SimulatorBackend;

public class TestInstructionsFixed {
    
    public static void main(String[] args) {
        System.out.println("=== TEST INSTRUCTIONS CORRIGÉ ===");
        
        SimulatorBackend backend = SimulatorBackend.getInstance();
        
        // Programme SANS ADDA B pour l'instant
        String program = 
            "        ORG $1000\n" +
            "        LDA #$05\n" +
            "        LDB #$03\n" +
            "        STA $2000     ; Stocke A\n" +
            "        STB $2001     ; Stocke B\n" +
            "        MUL           ; D = A * B\n" +
            "        STD $2002     ; Stocke D (16-bit)\n" +
            "        END";
        
        System.out.println("1. Assemblage...");
        try {
            boolean success = backend.assemble(program);
            System.out.println("   Résultat: " + (success ? "SUCCÈS" : "ÉCHEC"));
            
            if (success) {
                System.out.println("\n2. Exécution...");
                
                // Exécuter toutes les instructions
                for (int i = 0; i < 6; i++) {
                    backend.step();
                    System.out.println("   Pas " + (i+1) + " - PC: $" + 
                        SimulatorBackend.formatHex16(backend.getPC()));
                }
                
                System.out.println("\n3. Résultats:");
                System.out.println("   A = $" + SimulatorBackend.formatHex8(backend.getA()));
                System.out.println("   B = $" + SimulatorBackend.formatHex8(backend.getB()));
                System.out.println("   D = $" + SimulatorBackend.formatHex16(backend.getD()));
                System.out.println("   $2000 = $" + SimulatorBackend.formatHex8(backend.readMemory(0x2000)));
                System.out.println("   $2001 = $" + SimulatorBackend.formatHex8(backend.readMemory(0x2001)));
                System.out.println("   $2002 = $" + SimulatorBackend.formatHex8(backend.readMemory(0x2002)));
                System.out.println("   $2003 = $" + SimulatorBackend.formatHex8(backend.readMemory(0x2003)));
                
                // MUL devrait donner 5 * 3 = 15 (0x000F)
                // D = $000F, donc $2002 = $00, $2003 = $0F
            }
            
        } catch (Exception e) {
            System.out.println("   ERREUR: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== TEST TERMINÉ ===");
    }
}