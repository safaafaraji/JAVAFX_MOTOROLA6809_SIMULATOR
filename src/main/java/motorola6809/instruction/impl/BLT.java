package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.utils.BitOperations;

public class BLT extends Instruction {
    
    public BLT() {
        super("BLT", 0x2D, 3);
    }
    
    @Override
    public int execute(CPU cpu) {
        // N XOR V = 1
        if (cpu.getFlags().getNegative() != cpu.getFlags().getOverflow()) {
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