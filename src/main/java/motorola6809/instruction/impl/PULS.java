package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class PULS extends Instruction {
    
    public PULS() {
        super("PULS", 0x35, 5);
    }
    
    @Override
    public int execute(CPU cpu) {
        int postbyte = cpu.fetchByte();
        
        if ((postbyte & 0x01) != 0) { cpu.getFlags().setCC(cpu.popS()); }
        if ((postbyte & 0x02) != 0) { cpu.getRegisters().setA(cpu.popS()); }
        if ((postbyte & 0x04) != 0) { cpu.getRegisters().setB(cpu.popS()); }
        if ((postbyte & 0x08) != 0) { cpu.getRegisters().setDP(cpu.popS()); }
        if ((postbyte & 0x10) != 0) { cpu.getRegisters().setX(cpu.popWordS()); }
        if ((postbyte & 0x20) != 0) { cpu.getRegisters().setY(cpu.popWordS()); }
        if ((postbyte & 0x40) != 0) { cpu.getRegisters().setU(cpu.popWordS()); }
        if ((postbyte & 0x80) != 0) { cpu.getRegisters().setPC(cpu.popWordS()); }
        
        return this.baseCycles;
    }
}