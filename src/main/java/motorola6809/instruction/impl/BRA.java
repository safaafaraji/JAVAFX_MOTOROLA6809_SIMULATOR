package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.utils.BitOperations;

public class BRA extends Instruction {
    
    public BRA() {
        super("BRA", 0x20, 3);
    }
    
    @Override
    public int execute(CPU cpu) {
        int offset = cpu.fetchByte();
        offset = BitOperations.signExtend8to16(offset);
        cpu.getRegisters().setPC(cpu.getRegisters().getPC() + offset);
        return baseCycles;
    }
}