package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class CMPX extends Instruction {
    
    public CMPX(int opcode, String mnemonic, int cycles) {
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
            int address = cpu.getRegisters().getY();
            value = cpu.getMemory().readWord(address);
        }
        
        int x = cpu.getRegisters().getX();
        int result = x - value;
        
        cpu.getFlags().updateFlagsSub16(x, value, result);
        
        return this.baseCycles;
    }
}
