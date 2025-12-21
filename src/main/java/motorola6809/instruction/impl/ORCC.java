package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.config.Constants;



public class ORCC extends Instruction {
    
    public ORCC() {
        super("ORCC", 0x1A, 3);
    }
    
    @Override
    public int execute(CPU cpu) {
        int mask = cpu.fetchByte();
        int cc = cpu.getFlags().getCC();
        cpu.getFlags().setCC(cc | mask);
        
        return this.baseCycles;
    }
}