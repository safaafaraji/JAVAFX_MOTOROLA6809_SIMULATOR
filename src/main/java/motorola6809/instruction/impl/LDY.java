package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class LDY extends Instruction {
    
    public LDY(int opcode, String mnemonic, int cycles) {
        super(mnemonic, opcode, cycles);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = 0;
        int actualOpcode = opcode & 0xFF;
        
        if ((actualOpcode & 0xF0) == 0x80) {
            value = cpu.fetchWord();
        } else if ((actualOpcode & 0xF0) == 0x90) {
            int offset = cpu.fetchByte();
            int address = (cpu.getRegisters().getDP() << 8) | offset;
            value = cpu.getMemory().readWord(address);
        } else if ((actualOpcode & 0xF0) == 0xB0) {
            int address = cpu.fetchWord();
            value = cpu.getMemory().readWord(address);
        } else if ((actualOpcode & 0xF0) == 0xA0) {
            int postbyte = cpu.fetchByte();
            int address = resolveIndexed(cpu, postbyte);
            value = cpu.getMemory().readWord(address);
        }
        
        cpu.getRegisters().setY(value);
        cpu.getFlags().updateNZ16(value);
        cpu.getFlags().setOverflow(false);
        
        return baseCycles;
    }
    
    private int resolveIndexed(CPU cpu, int postbyte) {
        return cpu.getRegisters().getX();
    }
}