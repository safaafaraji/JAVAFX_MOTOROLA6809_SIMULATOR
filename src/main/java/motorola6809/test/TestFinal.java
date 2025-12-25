package motorola6809.test;

import motorola6809.core.CPU;

public class TestFinal {
    public static void main(String[] args) {
        CPU cpu = new CPU();
        cpu.setMaxInstructions(50); // Limite à 50 instructions
        
        // Programme: NOP, LDA #$FF, BRA -2 (boucle infinie contrôlée)
        byte[] program = {
            0x12,       // NOP
            (byte)0x86, // LDA #
            (byte)0xFF, // $FF
            0x20,       // BRA
            (byte)0xFC  // -4 (retour au début)
        };
        
        cpu.loadProgram(program, 0x1000);
        cpu.start();
        
        // Exécuter
        for (int i = 0; i < 60; i++) {
            cpu.step();
            if (cpu.isHalted()) {
                System.out.println("CPU arrêté après " + cpu.getInstructionsExecuted() + " instructions");
                break;
            }
        }
        
        System.out.println("Test terminé");
    }
}
