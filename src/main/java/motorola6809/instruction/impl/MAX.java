package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class MAX extends Instruction {
    
    public MAX() {
        super("MAX", 0xCD, 4); // Opcode personnalisé (0xCD n'est pas utilisé en standard)
    }
    
    @Override
    public int execute(CPU cpu) {
        int a = cpu.getRegisters().getA() & 0xFF;
        int b = cpu.getRegisters().getB() & 0xFF;
        int max = Math.max(a, b);
        
        cpu.getRegisters().setA(max);
        
        // Mettre à jour les flags
        cpu.getFlags().updateNZ8(max);
        cpu.getFlags().setCarry(a >= b); // Carry=1 si A >= B
        cpu.getFlags().setOverflow(false);
        
        return baseCycles;
    }
}