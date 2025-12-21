package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class INCA extends Instruction {
    
    public INCA() {
        super("INCA", 0x4C, 2);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = cpu.getRegisters().getA();
        int result = (value + 1) & 0xFF;
        
        cpu.getRegisters().setA(result);
        
        // Flags
        cpu.getFlags().updateNZ8(result);
        cpu.getFlags().setOverflow(value == 0x7F); // Overflow si passage de +127 à -128
        
        return baseCycles;
    }
}
