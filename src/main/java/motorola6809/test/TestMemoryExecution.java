package motorola6809.test;

import motorola6809.core.SimulatorBackend;

public class TestMemoryExecution {
    
    public static void main(String[] args) {
        System.out.println("=== TEST EXÉCUTION COMPLÈTE ===");
        
        SimulatorBackend backend = SimulatorBackend.getInstance();
        
        // Programme qui écrit en mémoire
        String program = 
            "        ORG $1000\n" +
            "        LDA #$55     ; A = $55\n" +
            "        STA $2000    ; Écrire A à $2000\n" +
            "        LDB #$AA     ; B = $AA\n" +
            "        STB $2001    ; Écrire B à $2001\n" +
            "        LDA #$11     ; A = $11\n" +
            "        STA $2002    ; Écrire A à $2002\n" +
            "        END";
        
        System.out.println("1. Assemblage du programme...");
        boolean assembled = backend.assemble(program);
        System.out.println("   Résultat: " + (assembled ? "SUCCÈS" : "ÉCHEC"));
        
        if (!assembled) {
            return;
        }
        
        System.out.println("\n2. État initial avant exécution:");
        printState(backend);
        
        System.out.println("\n3. Exécution pas à pas...");
        
        // Exécuter 4 instructions (LDA, STA, LDB, STB, LDA, STA)
        for (int i = 1; i <= 5; i++) {
            System.out.println("\n   --- Pas " + i + " ---");
            backend.step();
            printState(backend);
        }
        
        System.out.println("\n4. Vérification finale:");
        System.out.println("   $2000 = $" + String.format("%02X", backend.readMemory(0x2000)) + 
                         " (devrait être $55)");
        System.out.println("   $2001 = $" + String.format("%02X", backend.readMemory(0x2001)) + 
                         " (devrait être $AA)");
        System.out.println("   $2002 = $" + String.format("%02X", backend.readMemory(0x2002)) + 
                         " (devrait être $11)");
        
        System.out.println("\n5. Dump mémoire $2000-$200F:");
        dumpMemory(backend, 0x2000, 16);
        
        System.out.println("\n=== TEST TERMINÉ ===");
    }
    
    private static void printState(SimulatorBackend backend) {
        System.out.println("   PC = $" + String.format("%04X", backend.getPC()));
        System.out.println("   A  = $" + String.format("%02X", backend.getA()));
        System.out.println("   B  = $" + String.format("%02X", backend.getB()));
        System.out.println("   D  = $" + String.format("%04X", backend.getD()));
    }
    
    private static void dumpMemory(SimulatorBackend backend, int start, int length) {
        for (int i = 0; i < length; i += 8) {
            System.out.print("   $" + String.format("%04X", start + i) + ": ");
            for (int j = 0; j < 8 && (i + j) < length; j++) {
                System.out.print(String.format("%02X ", backend.readMemory(start + i + j)));
            }
            System.out.println();
        }
    }
}