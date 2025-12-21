package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class LDD extends Instruction {
    
    public LDD(int opcode, String mnemonic, int cycles) {
        super(mnemonic, opcode, cycles);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = 0;
        
        if ((opcode & 0xF0) == 0xC0) {
            // Immediate
            value = cpu.fetchWord();
        } else if ((opcode & 0xF0) == 0xD0) {
            // Direct
            int offset = cpu.fetchByte();
            int address = (cpu.getRegisters().getDP() << 8) | offset;
            value = cpu.getMemory().readWord(address);
        } else if ((opcode & 0xF0) == 0xF0) {
            // Extended
            int address = cpu.fetchWord();
            value = cpu.getMemory().readWord(address);
        } else if ((opcode & 0xF0) == 0xE0) {
            // Indexed
            int postbyte = cpu.fetchByte();
            int address = resolveIndexed(cpu, postbyte);
            value = cpu.getMemory().readWord(address);
        }
        
        cpu.getRegisters().setD(value);
        cpu.getFlags().updateNZ16(value);
        cpu.getFlags().setOverflow(false);
        
        return baseCycles;
    }
    
    private int resolveIndexed(CPU cpu, int postbyte) {
        return cpu.getRegisters().getX();
    }
}
