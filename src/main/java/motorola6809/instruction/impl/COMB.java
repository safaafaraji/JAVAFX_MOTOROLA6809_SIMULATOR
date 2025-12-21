package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class COMB extends Instruction {
    
    public COMB() {
        super("COMB", 0x53, 2);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = cpu.getRegisters().getB();
        int result = (~value) & 0xFF;
        
        cpu.getRegisters().setB(result);
        
        cpu.getFlags().updateNZ8(result);
        cpu.getFlags().setOverflow(false);
        cpu.getFlags().setCarry(true);
        
        return baseCycles;
    }
}