package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.config.Constants;

public class RORB extends Instruction {
    
    public RORB() {
        super("RORB", 0x56, 2);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = cpu.getRegisters().getB();
        int carry = cpu.getFlags().getCarry() ? 0x80 : 0;
        int result = ((value >> 1) | carry) & 0xFF;
        
        cpu.getRegisters().setB(result);
        
        cpu.getFlags().updateNZ8(result);
        cpu.getFlags().setCarry((value & 0x01) != 0);
        
        return this.baseCycles;
    }
}