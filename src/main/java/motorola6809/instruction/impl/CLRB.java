package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.utils.BitOperations;

public class CLRB extends Instruction {
    
    public CLRB() {
        super("CLRB", 0x5F, 2);
    }
    
    @Override
    public int execute(CPU cpu) {
        cpu.getRegisters().setB(0);
        
        cpu.getFlags().setNegative(false);
        cpu.getFlags().setZero(true);
        cpu.getFlags().setOverflow(false);
        cpu.getFlags().setCarry(false);
        
        return this.baseCycles;
    }
}
