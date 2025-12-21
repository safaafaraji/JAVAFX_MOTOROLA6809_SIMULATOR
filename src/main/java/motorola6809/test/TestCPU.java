package motorola6809.test;

import motorola6809.core.CPU;

public class TestCPU {
    public static void main(String[] args) {
        System.out.println("=== TEST CPU DIRECT ===");
        
        CPU cpu = new CPU();
        cpu.reset();
        
        // Programme test hardcodé
        byte[] testProgram = {
            (byte)0x86, (byte)0x05,  // LDA #$05
            (byte)0xC6, (byte)0x04,  // LDB #$04
            (byte)0x3D,               // MUL
            (byte)0x12,               // NOP
            (byte)0x3F                // SWI
        };
        
        // Charge le programme
        cpu.loadProgram(testProgram, 0x1400);
        cpu.getRegisters().setPC(0x1400);
        
        System.out.println("État initial:");
        System.out.println("PC = " + String.format("%04X", cpu.getRegisters().getPC()));
        System.out.println("A = " + String.format("%02X", cpu.getRegisters().getA()));
        System.out.println("B = " + String.format("%02X", cpu.getRegisters().getB()));
        
        // Exécute étape par étape
        System.out.println("\n=== Exécution ===");
        
        try {
            // Instruction 1: LDA #$05
            System.out.println("\n1. LDA #$05");
            cpu.step();
            System.out.println("  PC = " + String.format("%04X", cpu.getRegisters().getPC()));
            System.out.println("  A = " + String.format("%02X", cpu.getRegisters().getA()));
            
            // Instruction 2: LDB #$04
            System.out.println("\n2. LDB #$04");
            cpu.step();
            System.out.println("  PC = " + String.format("%04X", cpu.getRegisters().getPC()));
            System.out.println("  B = " + String.format("%02X", cpu.getRegisters().getB()));
            
            // Instruction 3: MUL
            System.out.println("\n3. MUL");
            cpu.step();
            System.out.println("  PC = " + String.format("%04X", cpu.getRegisters().getPC()));
            System.out.println("  D = A:B = " + 
                String.format("%02X", cpu.getRegisters().getA()) + 
                String.format("%02X", cpu.getRegisters().getB()) +
                " (décimal: " + (cpu.getRegisters().getA() * 256 + cpu.getRegisters().getB()) + ")");
            
            // Instruction 4: NOP
            System.out.println("\n4. NOP");
            cpu.step();
            System.out.println("  PC = " + String.format("%04X", cpu.getRegisters().getPC()));
            
            // Instruction 5: SWI
            System.out.println("\n5. SWI");
            cpu.step();
            System.out.println("  PC = " + String.format("%04X", cpu.getRegisters().getPC()));
            
        } catch (Exception e) {
            System.out.println("ERREUR: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== FIN DU TEST ===");
    }
}