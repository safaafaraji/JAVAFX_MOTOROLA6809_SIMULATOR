package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class INC extends Instruction {
    
    public INC(int opcode, String mnemonic, int cycles) {
        super(mnemonic, opcode, cycles);
    }
    
    @Override
    public int execute(CPU cpu) {
        int address = 0;
        
        if (opcode == 0x0C) {
            // Direct
            int offset = cpu.fetchByte();
            address = (cpu.getRegisters().getDP() << 8) | offset;
        } else if (opcode == 0x7C) {
            // Extended
            address = cpu.fetchWord();
        } else if (opcode == 0x6C) {
            // Indexed
            int postbyte = cpu.fetchByte();
            address = resolveIndexed(cpu, postbyte);
        }
        
        int value = cpu.getMemory().readByte(address);
        int result = (value + 1) & 0xFF;
        cpu.getMemory().writeByte(address, result);
        
        cpu.getFlags().updateNZ8(result);
        cpu.getFlags().setOverflow(value == 0x7F);
        
        return baseCycles;
    }
    
    private int resolveIndexed(CPU cpu, int postbyte) {
        return cpu.getRegisters().getX();
    }
}