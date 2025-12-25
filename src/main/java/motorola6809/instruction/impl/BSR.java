package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.utils.BitOperations;

public class BSR extends Instruction {
    
    public BSR() {
        super("BSR", 0x8D, 7);
    }
    
    @Override
    public int execute(CPU cpu) {
        int offset = cpu.fetchByte();
        offset = BitOperations.signExtend8to16(offset);
        int pc = cpu.getRegisters().getPC();
        
        // Pousser l'adresse de retour (PC actuel) sur la pile
        cpu.pushWordS(pc);
        
        // Sauter à la subroutine
        cpu.getRegisters().setPC(pc + offset);
        
        return baseCycles;
    }
}