package motorola6809.test;

import motorola6809.core.SimulatorBackend;
import motorola6809.core.CPU;
import motorola6809.core.Memory;

public class TestStepByStep {
    
    public static void main(String[] args) throws Exception {
        System.out.println("=== TEST DÉTAILLÉ ÉTAPE PAR ÉTAPE ===");
        
        SimulatorBackend backend = SimulatorBackend.getInstance();
        CPU cpu = backend.getCPU();
        Memory memory = cpu.getMemory();
        
        // Programme minimal - juste STA
        String program = 
            "        ORG $1000\n" +
            "        LDA #$42\n" +
            "        STA $2000\n" +
            "        END";
        
        System.out.println("1. Assemblage...");
        backend.assemble(program);
        
        System.out.println("\n2. Avant exécution:");
        System.out.println("   PC: $" + String.format("%04X", cpu.getRegisters().getPC()));
        System.out.println("   A: $" + String.format("%02X", cpu.getRegisters().getA()));
        System.out.println("   $2000: $" + String.format("%02X", memory.readByte(0x2000)));
        
        System.out.println("\n3. Exécution instruction LDA...");
        
        // Lire manuellement l'opcode LDA #$42 (0x86)
        int opcode = memory.readByte(0x1000);
        System.out.println("   Opcode à $1000: $" + String.format("%02X", opcode) + 
                         " (devrait être 0x86 = LDA)");
        
        // Exécuter LDA
        backend.step();
        
        System.out.println("\n   Après LDA:");
        System.out.println("   PC: $" + String.format("%04X", cpu.getRegisters().getPC()));
        System.out.println("   A: $" + String.format("%02X", cpu.getRegisters().getA()) + 
                         " (devrait être $42)");
        
        System.out.println("\n4. Vérification de l'instruction suivante...");
        int nextOpcode = memory.readByte(cpu.getRegisters().getPC());
        System.out.println("   Opcode à $" + String.format("%04X", cpu.getRegisters().getPC()) + 
                         ": $" + String.format("%02X", nextOpcode) + 
                         " (devrait être 0xB7 = STA extended)");
        
        System.out.println("\n5. Exécution instruction STA...");
        backend.step();
        
        System.out.println("\n   Après STA:");
        System.out.println("   PC: $" + String.format("%04X", cpu.getRegisters().getPC()));
        System.out.println("   A: $" + String.format("%02X", cpu.getRegisters().getA()));
        System.out.println("   $2000: $" + String.format("%02X", memory.readByte(0x2000)) + 
                         " (devrait être $42)");
        
        System.out.println("\n6. Dump mémoire autour de $2000:");
        for (int i = 0; i < 8; i++) {
            System.out.println("   $" + String.format("%04X", 0x2000 + i) + ": $" + 
                             String.format("%02X", memory.readByte(0x2000 + i)));
        }
        
        System.out.println("\n=== TEST TERMINÉ ===");
    }
}