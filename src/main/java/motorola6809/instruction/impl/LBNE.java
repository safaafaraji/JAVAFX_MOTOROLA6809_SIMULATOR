package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class LBNE extends Instruction {
    
    private String operand;
    
    public LBNE() {
        super("LBNE", 0x1026, 5); // Préfixe 0x10 + BNE
    }
    
    public LBNE(String operand) {
        super("LBNE", 0x1026, 5);
        this.operand = operand;
    }
    
    @Override
    public int execute(CPU cpu) {
        if (!cpu.getFlags().getZero()) {
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
            // Lire offset 16-bit
            int offset = cpu.fetchWord();
            // Extension de signe pour 16 bits
            if ((offset & 0x8000) != 0) {
                offset |= 0xFFFF0000;
            }
            return offset;
        }
    }
    
    private void skipOffset(CPU cpu) {
        cpu.fetchWord(); // Sauter 2 octets
    }
}