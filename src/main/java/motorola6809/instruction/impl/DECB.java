package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class DECB extends Instruction {
    
    public DECB() {
        super("DECB", 0x5A, 2);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = cpu.getRegisters().getB();
        int result = (value - 1) & 0xFF;
        
        cpu.getRegisters().setB(result);
        
        cpu.getFlags().updateNZ8(result);
        cpu.getFlags().setOverflow(value == 0x80);
        
        return baseCycles;
    }
}