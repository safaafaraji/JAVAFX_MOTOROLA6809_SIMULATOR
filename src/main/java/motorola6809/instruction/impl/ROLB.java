package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.config.Constants;


public class ROLB extends Instruction {
    
    public ROLB() {
        super("ROLB", 0x59, 2);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = cpu.getRegisters().getB();
        int carry = cpu.getFlags().getCarry() ? 1 : 0;
        int result = ((value << 1) | carry) & 0xFF;
        
        cpu.getRegisters().setB(result);
        
        cpu.getFlags().updateNZ8(result);
        cpu.getFlags().setCarry((value & 0x80) != 0);
        cpu.getFlags().setOverflow(((value ^ result) & 0x80) != 0);
        
        return this.baseCycles;
    }
}
