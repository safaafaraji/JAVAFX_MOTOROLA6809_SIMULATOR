package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.utils.BitOperations;

public class BHI extends Instruction {
    
    public BHI() {
        super("BHI", 0x22, 3);
    }
    
    @Override
    public int execute(CPU cpu) {
        if (!cpu.getFlags().getCarry() && !cpu.getFlags().getZero()) {
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