package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class NOP extends Instruction {
    
    public NOP() {
        super("NOP", 0x12, 2);
    }
    
    @Override
    public int execute(CPU cpu) {
        // Ne fait rien
        return baseCycles;
    }
}