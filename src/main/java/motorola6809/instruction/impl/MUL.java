package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class MUL extends Instruction {
    
    public MUL() {
        super("MUL", 0x3D, 11);
    }
    
    @Override
    public int execute(CPU cpu) {
        int a = cpu.getRegisters().getA();
        int b = cpu.getRegisters().getB();
        int result = a * b;
        
        cpu.getRegisters().setD(result);
        
        // MUL affecte seulement C et Z
        cpu.getFlags().setZero(result == 0);
        cpu.getFlags().setCarry((result & 0x80) != 0); // Bit 7 du résultat
        
        return baseCycles;
    }
}