package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.config.Constants;


public class SUBD extends Instruction {
    
    public SUBD(int opcode, String mnemonic, int cycles) {
        super(mnemonic, opcode, cycles);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = 0;
        
        if ((this.opcode & 0xF0) == 0x80) {
            value = cpu.fetchWord();
        } else if ((this.opcode & 0xF0) == 0x90) {
            int offset = cpu.fetchByte();
            int address = (cpu.getRegisters().getDP() << 8) | offset;
            value = cpu.getMemory().readWord(address);
        } else if ((this.opcode & 0xF0) == 0xB0) {
            int address = cpu.fetchWord();
            value = cpu.getMemory().readWord(address);
        } else if ((this.opcode & 0xF0) == 0xA0) {
            int postbyte = cpu.fetchByte();
            int address = cpu.getRegisters().getX();
            value = cpu.getMemory().readWord(address);
        }
        
        int d = cpu.getRegisters().getD();
        int result = d - value;
        
        cpu.getRegisters().setD(result);
        cpu.getFlags().updateFlagsSub16(d, value, result);
        
        return this.baseCycles;
    }
}
