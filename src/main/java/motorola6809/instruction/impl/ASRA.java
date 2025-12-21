package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.utils.BitOperations;

public class ASRA extends Instruction {
    
    public ASRA() {
        super("ASRA", 0x47, 2);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = cpu.getRegisters().getA();
        int msb = value & 0x80;
        int result = (value >> 1) | msb;
        
        cpu.getRegisters().setA(result);
        
        cpu.getFlags().updateNZ8(result);
        cpu.getFlags().setCarry((value & 0x01) != 0);
        
        return this.baseCycles;
    }
}