package motorola6809.test;

import motorola6809.core.CPU;

public class TestSimple {
    public static void main(String[] args) {
        CPU cpu = new CPU();
        
        // Programme simple: NOP, LDA #$FF, NOP, HALT
        byte[] program = {
            0x12,       // NOP
            (byte)0x86, // LDA #
            (byte)0xFF, // $FF
            0x12,       // NOP
            0x3F        // SWI (pour arrêter)
        };
        
        cpu.loadProgram(program, 0x1000);
        cpu.start();
        
        // Exécuter quelques instructions
        for (int i = 0; i < 10; i++) {
            cpu.step();
            
            // Afficher l'état
            System.out.println(cpu);
            System.out.println("---");
            
            if (cpu.isHalted()) {
                System.out.println("CPU arrêté après " + i + " instructions");
                break;
            }
        }
    }
}