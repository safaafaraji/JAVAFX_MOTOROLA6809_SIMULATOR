package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.utils.BitOperations;

public class BGE extends Instruction {
    
    public BGE() {
        super("BGE", 0x2C, 3);
    }
    
    @Override
    public int execute(CPU cpu) {
        // N XOR V = 0
        if (cpu.getFlags().getNegative() == cpu.getFlags().getOverflow()) {
            int offset = cpu.fetchByte();
            offset = BitOperations.signExtend8to16(offset);
            cpu.getRegisters().setPC(cpu.getRegisters().getPC() + offset);
            return baseCycles + 1;
        } else {
            cpu.fetchByte();
            return baseCycles;
        }
    }
}