package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;


public class INCB extends Instruction {
    
    public INCB() {
        super("INCB", 0x5C, 2);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = cpu.getRegisters().getB();
        int result = (value + 1) & 0xFF;
        
        cpu.getRegisters().setB(result);
        
        cpu.getFlags().updateNZ8(result);
        cpu.getFlags().setOverflow(value == 0x7F);
        
        return baseCycles;
    }
}