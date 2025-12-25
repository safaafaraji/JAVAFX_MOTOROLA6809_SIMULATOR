package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class LBEQ extends Instruction {
    
    private String operand;
    
    public LBEQ() {
        super("LBEQ", 0x1027, 5);
    }
    
    public LBEQ(String operand) {
        super("LBEQ", 0x1027, 5);
        this.operand = operand;
    }
    
    @Override
    public int execute(CPU cpu) {
        if (cpu.getFlags().getZero()) {
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
            return 0;
        } else {
            int offset = cpu.fetchWord();
            if ((offset & 0x8000) != 0) {
                offset |= 0xFFFF0000;
            }
            return offset;
        }
    }
    
    private void skipOffset(CPU cpu) {
        cpu.fetchWord();
    }
}