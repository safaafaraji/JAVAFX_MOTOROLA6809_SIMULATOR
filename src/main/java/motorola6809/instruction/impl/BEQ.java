package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.utils.BitOperations;

public class BEQ extends Instruction {
    
    private String operand;
    
    public BEQ() {
        super("BEQ", 0x27, 3); // BEQ relatif 8-bit
    }
    
    public BEQ(String operand) {
        super("BEQ", 0x27, 3);
        this.operand = operand;
    }
    
    @Override
    public int execute(CPU cpu) {
        if (cpu.getFlags().getZero()) {
            // Z=1, donc branchement
            int offset = getOffset(cpu);
            int pc = cpu.getRegisters().getPC();
            cpu.getRegisters().setPC(pc + offset);
            return baseCycles + 1;
        } else {
            skipOffset(cpu);
            return baseCycles;
        }
    }
    
    private int getOffset(CPU cpu) {
        if (operand != null) {
            // Calcul depuis étiquette
            return 0;
        } else {
            int offset = cpu.fetchByte();
            return BitOperations.signExtend8to16(offset);
        }
    }
    
    private void skipOffset(CPU cpu) {
        cpu.fetchByte();
    }
}