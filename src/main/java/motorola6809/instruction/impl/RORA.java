package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.config.Constants;

public class RORA extends Instruction {
    
    public RORA() {
        super("RORA", 0x46, 2);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = cpu.getRegisters().getA();
        int carry = cpu.getFlags().getCarry() ? 0x80 : 0;
        int result = ((value >> 1) | carry) & 0xFF;
        
        cpu.getRegisters().setA(result);
        
        cpu.getFlags().updateNZ8(result);
        cpu.getFlags().setCarry((value & 0x01) != 0);
        
        return this.baseCycles;
    }
}