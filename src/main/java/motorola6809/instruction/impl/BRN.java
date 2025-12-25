package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class BRN extends Instruction {
    
    public BRN() {
        super("BRN", 0x21, 3);
    }
    
    @Override
    public int execute(CPU cpu) {
        // Ne branche jamais, juste consomme l'offset
        cpu.fetchByte();
        return baseCycles;
    }
}