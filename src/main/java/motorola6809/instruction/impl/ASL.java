package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.utils.BitOperations;

public class ASL extends Instruction {
    
    public ASL(int opcode, String mnemonic, int cycles) {
        super(mnemonic, opcode, cycles);
    }
    
    @Override
    public int execute(CPU cpu) {
        int address = 0;
        
        if (this.opcode == 0x08) {
            int offset = cpu.fetchByte();
            address = (cpu.getRegisters().getDP() << 8) | offset;
        } else if (this.opcode == 0x78) {
            address = cpu.fetchWord();
        } else if (this.opcode == 0x68) {
            int postbyte = cpu.fetchByte();
            address = cpu.getRegisters().getX();
        }
        
        int value = cpu.getMemory().readByte(address);
        int result = (value << 1) & 0xFF;
        cpu.getMemory().writeByte(address, result);
        
        cpu.getFlags().updateNZ8(result);
        cpu.getFlags().setCarry((value & 0x80) != 0);
        cpu.getFlags().setOverflow(((value ^ result) & 0x80) != 0);
        
        return this.baseCycles;
    }
}