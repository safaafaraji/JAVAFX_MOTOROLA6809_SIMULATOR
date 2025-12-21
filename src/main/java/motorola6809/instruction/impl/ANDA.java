package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.utils.BitOperations;

public class ANDA extends Instruction {
    
    public ANDA(int opcode, String mnemonic, int cycles) {
        super(mnemonic, opcode, cycles);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = 0;
        
        if ((this.opcode & 0xF0) == 0x80) {
            value = cpu.fetchByte();
        } else if ((this.opcode & 0xF0) == 0x90) {
            int offset = cpu.fetchByte();
            int address = (cpu.getRegisters().getDP() << 8) | offset;
            value = cpu.getMemory().readByte(address);
        } else if ((this.opcode & 0xF0) == 0xB0) {
            int address = cpu.fetchWord();
            value = cpu.getMemory().readByte(address);
        } else if ((this.opcode & 0xF0) == 0xA0) {
            int postbyte = cpu.fetchByte();
            int address = cpu.getRegisters().getX();
            value = cpu.getMemory().readByte(address);
        }
        
        int result = cpu.getRegisters().getA() & value;
        cpu.getRegisters().setA(result);
        
        cpu.getFlags().updateNZ8(result);
        cpu.getFlags().setOverflow(false);
        
        return this.baseCycles;
    }
}
