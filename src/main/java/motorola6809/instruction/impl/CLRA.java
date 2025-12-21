package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.utils.BitOperations;

public class CLRA extends Instruction {
    
    public CLRA() {
        super("CLRA", 0x4F, 2);
    }
    
    @Override
    public int execute(CPU cpu) {
        cpu.getRegisters().setA(0);
        
        cpu.getFlags().setNegative(false);
        cpu.getFlags().setZero(true);
        cpu.getFlags().setOverflow(false);
        cpu.getFlags().setCarry(false);
        
        return this.baseCycles;
    }
}
