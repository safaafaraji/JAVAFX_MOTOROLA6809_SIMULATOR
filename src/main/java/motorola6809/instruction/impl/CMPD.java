package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class CMPD extends Instruction {
    
    public CMPD(int opcode, String mnemonic, int cycles) {
        super(mnemonic, opcode, cycles);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = 0;
        
        if ((this.opcode & 0xFF) == 0x83) {
            value = cpu.fetchWord();
        } else if ((this.opcode & 0xFF) == 0x93) {
            int offset = cpu.fetchByte();
            int address = (cpu.getRegisters().getDP() << 8) | offset;
            value = cpu.getMemory().readWord(address);
        } else if ((this.opcode & 0xFF) == 0xB3) {
            int address = cpu.fetchWord();
            value = cpu.getMemory().readWord(address);
        } else if ((this.opcode & 0xFF) == 0xA3) {
            int postbyte = cpu.fetchByte();
            int address = cpu.getRegisters().getX();
            value = cpu.getMemory().readWord(address);
        }
        
        int d = cpu.getRegisters().getD();
        int result = d - value;
        
        cpu.getFlags().updateFlagsSub16(d, value, result);
        
        return this.baseCycles;
    }
}