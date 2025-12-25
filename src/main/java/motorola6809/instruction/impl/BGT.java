package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.utils.BitOperations;

public class BGT extends Instruction {
    
    public BGT() {
        super("BGT", 0x2E, 3);
    }
    
    @Override
    public int execute(CPU cpu) {
        // Z OR (N XOR V) = 0
        if (!cpu.getFlags().getZero() && 
            (cpu.getFlags().getNegative() == cpu.getFlags().getOverflow())) {
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