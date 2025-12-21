package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class STY extends Instruction {
    
    public STY(int opcode, String mnemonic, int cycles) {
        super(mnemonic, opcode, cycles);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = cpu.getRegisters().getY();
        int address = 0;
        int actualOpcode = opcode & 0xFF;
        
        if ((actualOpcode & 0xF0) == 0x90) {
            int offset = cpu.fetchByte();
            address = (cpu.getRegisters().getDP() << 8) | offset;
        } else if ((actualOpcode & 0xF0) == 0xB0) {
            address = cpu.fetchWord();
        } else if ((actualOpcode & 0xF0) == 0xA0) {
            int postbyte = cpu.fetchByte();
            address = resolveIndexed(cpu, postbyte);
        }
        
        cpu.getMemory().writeWord(address, value);
        cpu.getFlags().updateNZ16(value);
        cpu.getFlags().setOverflow(false);
        
        return baseCycles;
    }
    
    private int resolveIndexed(CPU cpu, int postbyte) {
        return cpu.getRegisters().getX();
    }
}