package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.utils.BitOperations;

public class ASLB extends Instruction {
    
    public ASLB() {
        super("ASLB", 0x58, 2);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = cpu.getRegisters().getB();
        int result = (value << 1) & 0xFF;
        
        cpu.getRegisters().setB(result);
        
        cpu.getFlags().updateNZ8(result);
        cpu.getFlags().setCarry((value & 0x80) != 0);
        cpu.getFlags().setOverflow(((value ^ result) & 0x80) != 0);
        
        return this.baseCycles;
    }
}
