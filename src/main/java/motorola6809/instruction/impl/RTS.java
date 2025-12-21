package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.config.Constants;

public class RTS extends Instruction {
    
    public RTS() {
        super("RTS", 0x39, 5);
    }
    
    @Override
    public int execute(CPU cpu) {
        int returnAddress = cpu.popWordS();
        cpu.getRegisters().setPC(returnAddress);
        
        return this.baseCycles;
    }
}