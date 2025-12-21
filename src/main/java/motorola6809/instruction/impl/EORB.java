package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class EORB extends Instruction {
    
    public EORB(int opcode, String mnemonic, int cycles) {
        super(mnemonic, opcode, cycles);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = 0;
        
        if ((opcode & 0xF0) == 0xC0) {
            value = cpu.fetchByte();
        } else if ((opcode & 0xF0) == 0xD0) {
            int offset = cpu.fetchByte();
            int address = (cpu.getRegisters().getDP() << 8) | offset;
            value = cpu.getMemory().readByte(address);
        } else if ((opcode & 0xF0) == 0xF0) {
            int address = cpu.fetchWord();
            value = cpu.getMemory().readByte(address);
        } else if ((opcode & 0xF0) == 0xE0) {
            int postbyte = cpu.fetchByte();
            int address = resolveIndexed(cpu, postbyte);
            value = cpu.getMemory().readByte(address);
        }
        
        int result = cpu.getRegisters().getB() ^ value;
        cpu.getRegisters().setB(result);
        
        cpu.getFlags().updateNZ8(result);
        cpu.getFlags().setOverflow(false);
        
        return baseCycles;
    }
    
    private int resolveIndexed(CPU cpu, int postbyte) {
        return cpu.getRegisters().getX();
    }
}