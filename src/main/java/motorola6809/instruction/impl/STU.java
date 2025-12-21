package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.config.Constants;

public class STU extends Instruction {
    
    public STU(int opcode, String mnemonic, int cycles) {
        super(mnemonic, opcode, cycles);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = cpu.getRegisters().getU();
        int address = 0;
        
        if ((this.opcode & 0xF0) == 0xD0) {
            int offset = cpu.fetchByte();
            address = (cpu.getRegisters().getDP() << 8) | offset;
        } else if ((this.opcode & 0xF0) == 0xF0) {
            address = cpu.fetchWord();
        } else if ((this.opcode & 0xF0) == 0xE0) {
            int postbyte = cpu.fetchByte();
            address = cpu.getRegisters().getX();
        }
        
        cpu.getMemory().writeWord(address, value);
        cpu.getFlags().updateNZ16(value);
        cpu.getFlags().setOverflow(false);
        
        return this.baseCycles;
    }
}
