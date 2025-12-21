package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class PSHS extends Instruction {
    
    public PSHS() {
        super("PSHS", 0x34, 5);
    }
    
    @Override
    public int execute(CPU cpu) {
        int postbyte = cpu.fetchByte();
        
        if ((postbyte & 0x80) != 0) { cpu.pushWordS(cpu.getRegisters().getPC()); }
        if ((postbyte & 0x40) != 0) { cpu.pushWordS(cpu.getRegisters().getU()); }
        if ((postbyte & 0x20) != 0) { cpu.pushWordS(cpu.getRegisters().getY()); }
        if ((postbyte & 0x10) != 0) { cpu.pushWordS(cpu.getRegisters().getX()); }
        if ((postbyte & 0x08) != 0) { cpu.pushS(cpu.getRegisters().getDP()); }
        if ((postbyte & 0x04) != 0) { cpu.pushS(cpu.getRegisters().getB()); }
        if ((postbyte & 0x02) != 0) { cpu.pushS(cpu.getRegisters().getA()); }
        if ((postbyte & 0x01) != 0) { cpu.pushS(cpu.getFlags().getCC()); }
        
        return this.baseCycles;
    }
}