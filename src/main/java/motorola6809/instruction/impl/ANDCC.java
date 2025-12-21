package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.utils.BitOperations;

public class ANDCC extends Instruction {
    
    public ANDCC() {
        super("ANDCC", 0x1C, 3);
    }
    
    @Override
    public int execute(CPU cpu) {
        int mask = cpu.fetchByte();
        int cc = cpu.getFlags().getCC();
        cpu.getFlags().setCC(cc & mask);
        
        return this.baseCycles;
    }
}