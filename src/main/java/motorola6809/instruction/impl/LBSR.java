package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class LBSR extends Instruction {
    
    public LBSR() {
        super("LBSR", 0x17, 9);
    }
    
    @Override
    public int execute(CPU cpu) {
        int offset = cpu.fetchWord();
        if ((offset & 0x8000) != 0) {
            offset |= 0xFFFF0000;
        }
        int pc = cpu.getRegisters().getPC();
        
        // Pousser l'adresse de retour
        cpu.pushWordS(pc);
        
        // Sauter
        cpu.getRegisters().setPC(pc + offset);
        
        return baseCycles;
    }
}