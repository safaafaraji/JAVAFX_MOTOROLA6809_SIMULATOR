// TestMemory.java
package motorola6809.test;

import motorola6809.core.SimulatorBackend;

public class TestMemory {
    
    public static void main(String[] args) {
        System.out.println("=== TEST MÉMOIRE MOTOROLA 6809 ===");
        
        SimulatorBackend backend = SimulatorBackend.getInstance();
        
        // Test 1: Écriture directe
        System.out.println("\n1. Test écriture directe:");
        backend.writeMemory((short)0x2000, (byte)0xAA);
        backend.writeMemory((short)0x2001, (byte)0xBB);
        
        byte val1 = backend.readMemory(0x2000);
        byte val2 = backend.readMemory(0x2001);
        
        System.out.println("   Adresse 0x2000 = $" + String.format("%02X", val1 & 0xFF));
        System.out.println("   Adresse 0x2001 = $" + String.format("%02X", val2 & 0xFF));
        
        // Test 2: Programme assembleur simple
        System.out.println("\n2. Test avec programme assembleur:");
        String program = 
            "        ORG $1000\n" +
            "        LDA #$55\n" +
            "        STA $2000\n" +
            "        LDB #$AA\n" +
            "        STB $2001\n" +
            "        LDD #$1234\n" +
            "        STD $2002\n" +
            "        END";
        
        backend.assemble(program);
        
        // Exécuter quelques pas
        for (int i = 0; i < 6; i++) {
            backend.step();
            System.out.println("   Pas " + (i+1) + " - PC: $" + 
                SimulatorBackend.formatHex16(backend.getPC()));
        }
        
        // Vérifier la mémoire
        System.out.println("\n3. Vérification mémoire:");
        System.out.println("   $2000 = $" + String.format("%02X", backend.readMemory(0x2000) & 0xFF) + 
                         " (devrait être $55)");
        System.out.println("   $2001 = $" + String.format("%02X", backend.readMemory(0x2001) & 0xFF) + 
                         " (devrait être $AA)");
        System.out.println("   $2002 = $" + String.format("%02X", backend.readMemory(0x2002) & 0xFF) + 
                         " (devrait être $12)");
        System.out.println("   $2003 = $" + String.format("%02X", backend.readMemory(0x2003) & 0xFF) + 
                         " (devrait être $34)");
        
        // Test 3: Dump mémoire
        System.out.println("\n4. Dump mémoire $2000-$200F:");
        for (int i = 0; i < 16; i++) {
            if (i % 8 == 0) {
                System.out.print("\n   $" + String.format("%04X", 0x2000 + i) + ": ");
            }
            System.out.print(String.format("%02X ", backend.readMemory(0x2000 + i) & 0xFF));
        }
        System.out.println();
        
        System.out.println("\n=== TEST TERMINÉ ===");
    }
}