package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class PSHU extends Instruction {
    
    public PSHU() {
        super("PSHU", 0x36, 5);
    }
    
    @Override
    public int execute(CPU cpu) {
        int postbyte = cpu.fetchByte();
        
        if ((postbyte & 0x80) != 0) { cpu.pushWordU(cpu.getRegisters().getPC()); }
        if ((postbyte & 0x40) != 0) { cpu.pushWordU(cpu.getRegisters().getS()); }
        if ((postbyte & 0x20) != 0) { cpu.pushWordU(cpu.getRegisters().getY()); }
        if ((postbyte & 0x10) != 0) { cpu.pushWordU(cpu.getRegisters().getX()); }
        if ((postbyte & 0x08) != 0) { cpu.pushU(cpu.getRegisters().getDP()); }
        if ((postbyte & 0x04) != 0) { cpu.pushU(cpu.getRegisters().getB()); }
        if ((postbyte & 0x02) != 0) { cpu.pushU(cpu.getRegisters().getA()); }
        if ((postbyte & 0x01) != 0) { cpu.pushU(cpu.getFlags().getCC()); }
        
        return this.baseCycles;
    }
}