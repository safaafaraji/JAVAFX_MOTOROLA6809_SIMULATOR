package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class PULU extends Instruction {
    
    public PULU() {
        super("PULU", 0x37, 5);
    }
    
    @Override
    public int execute(CPU cpu) {
        int postbyte = cpu.fetchByte();
        
        if ((postbyte & 0x01) != 0) { cpu.getFlags().setCC(cpu.popU()); }
        if ((postbyte & 0x02) != 0) { cpu.getRegisters().setA(cpu.popU()); }
        if ((postbyte & 0x04) != 0) { cpu.getRegisters().setB(cpu.popU()); }
        if ((postbyte & 0x08) != 0) { cpu.getRegisters().setDP(cpu.popU()); }
        if ((postbyte & 0x10) != 0) { cpu.getRegisters().setX(cpu.popWordU()); }
        if ((postbyte & 0x20) != 0) { cpu.getRegisters().setY(cpu.popWordU()); }
        if ((postbyte & 0x40) != 0) { cpu.getRegisters().setS(cpu.popWordU()); }
        if ((postbyte & 0x80) != 0) { cpu.getRegisters().setPC(cpu.popWordU()); }
        
        return this.baseCycles;
    }
}
