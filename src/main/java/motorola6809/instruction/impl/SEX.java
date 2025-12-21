package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.config.Constants;


public class SEX extends Instruction {
    
    public SEX() {
        super("SEX", 0x1D, 2);
    }
    
    @Override
    public int execute(CPU cpu) {
        int b = cpu.getRegisters().getB();
        
        if ((b & 0x80) != 0) {
            cpu.getRegisters().setA(0xFF);
        } else {
            cpu.getRegisters().setA(0x00);
        }
        
        cpu.getFlags().updateNZ16(cpu.getRegisters().getD());
        
        return this.baseCycles;
    }
}
