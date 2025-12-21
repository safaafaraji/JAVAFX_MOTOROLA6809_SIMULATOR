package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class DECA extends Instruction {
    
    public DECA() {
        super("DECA", 0x4A, 2);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = cpu.getRegisters().getA();
        int result = (value - 1) & 0xFF;
        
        cpu.getRegisters().setA(result);
        
        cpu.getFlags().updateNZ8(result);
        cpu.getFlags().setOverflow(value == 0x80); // Overflow si passage de -128 à +127
        
        return baseCycles;
    }
}