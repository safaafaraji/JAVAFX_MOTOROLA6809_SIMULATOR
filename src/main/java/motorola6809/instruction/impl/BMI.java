package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.utils.BitOperations;

public class BMI extends Instruction {
    
    public BMI() {
        super("BMI", 0x2B, 3);
    }
    
    @Override
    public int execute(CPU cpu) {
        if (cpu.getFlags().getNegative()) {
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