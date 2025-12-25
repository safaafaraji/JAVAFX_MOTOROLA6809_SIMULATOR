package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class LBRA extends Instruction {
    
    public LBRA() {
        super("LBRA", 0x16, 5);
    }
    
    @Override
    public int execute(CPU cpu) {
        int offset = cpu.fetchWord();
        // Extension de signe pour 16 bits
        if ((offset & 0x8000) != 0) {
            offset |= 0xFFFF0000;
        }
        cpu.getRegisters().setPC(cpu.getRegisters().getPC() + offset);
        return baseCycles;
    }
}