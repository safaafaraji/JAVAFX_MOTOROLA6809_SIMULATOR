package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.utils.BitOperations;

public class ASR extends Instruction {
    
    public ASR(int opcode, String mnemonic, int cycles) {
        super(mnemonic, opcode, cycles);
    }
    
    @Override
    public int execute(CPU cpu) {
        int address = 0;
        
        if (this.opcode == 0x07) {
            int offset = cpu.fetchByte();
            address = (cpu.getRegisters().getDP() << 8) | offset;
        } else if (this.opcode == 0x77) {
            address = cpu.fetchWord();
        } else if (this.opcode == 0x67) {
            int postbyte = cpu.fetchByte();
            address = cpu.getRegisters().getX();
        }
        
        int value = cpu.getMemory().readByte(address);
        int msb = value & 0x80;
        int result = (value >> 1) | msb;
        cpu.getMemory().writeByte(address, result);
        
        cpu.getFlags().updateNZ8(result);
        cpu.getFlags().setCarry((value & 0x01) != 0);
        
        return this.baseCycles;
    }
}
