package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class EORA extends Instruction {
    
    public EORA(int opcode, String mnemonic, int cycles) {
        super(mnemonic, opcode, cycles);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = 0;
        
        if ((opcode & 0xF0) == 0x80) {
            // Immediate
            value = cpu.fetchByte();
        } else if ((opcode & 0xF0) == 0x90) {
            // Direct
            int offset = cpu.fetchByte();
            int address = (cpu.getRegisters().getDP() << 8) | offset;
            value = cpu.getMemory().readByte(address);
        } else if ((opcode & 0xF0) == 0xB0) {
            // Extended
            int address = cpu.fetchWord();
            value = cpu.getMemory().readByte(address);
        } else if ((opcode & 0xF0) == 0xA0) {
            // Indexed
            int postbyte = cpu.fetchByte();
            int address = resolveIndexed(cpu, postbyte);
            value = cpu.getMemory().readByte(address);
        }
        
        int result = cpu.getRegisters().getA() ^ value;
        cpu.getRegisters().setA(result);
        
        cpu.getFlags().updateNZ8(result);
        cpu.getFlags().setOverflow(false);
        
        return baseCycles;
    }
    
    private int resolveIndexed(CPU cpu, int postbyte) {
        return cpu.getRegisters().getX();
    }
}