package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.config.Constants;

public class RTI extends Instruction {
    
    public RTI() {
        super("RTI", 0x3B, 6);
    }
    
    @Override
    public int execute(CPU cpu) {
        cpu.getFlags().setCC(cpu.popS());
        
        if (cpu.getFlags().getEntire()) {
            cpu.getRegisters().setA(cpu.popS());
            cpu.getRegisters().setB(cpu.popS());
            cpu.getRegisters().setDP(cpu.popS());
            cpu.getRegisters().setX(cpu.popWordS());
            cpu.getRegisters().setY(cpu.popWordS());
            cpu.getRegisters().setU(cpu.popWordS());
        }
        
        cpu.getRegisters().setPC(cpu.popWordS());
        
        return this.baseCycles;
    }
}