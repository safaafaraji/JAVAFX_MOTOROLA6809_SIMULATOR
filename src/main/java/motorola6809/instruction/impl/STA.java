package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class STA extends Instruction {
    
    public STA(int opcode, String mnemonic, int cycles) {
        super(mnemonic, opcode, cycles);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = cpu.getRegisters().getA();
        int address = 0;
        
        if ((opcode & 0xF0) == 0x90) {
            // Direct
            int offset = cpu.fetchByte();
            address = (cpu.getRegisters().getDP() << 8) | offset;
        } else if ((opcode & 0xF0) == 0xB0) {
            // Extended
            address = cpu.fetchWord();
        } else if ((opcode & 0xF0) == 0xA0) {
            // Indexed
            int postbyte = cpu.fetchByte();
            address = resolveIndexed(cpu, postbyte);
        }
        
        cpu.getMemory().writeByte(address, value);
        cpu.getFlags().updateNZ8(value);
        cpu.getFlags().setOverflow(false);
        
        return baseCycles;
    }
    
    private int resolveIndexed(CPU cpu, int postbyte) {
        return cpu.getRegisters().getX();
    }
}