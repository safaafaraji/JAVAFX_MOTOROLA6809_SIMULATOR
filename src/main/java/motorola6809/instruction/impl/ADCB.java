package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.utils.BitOperations;


public class ADCB extends Instruction {
    
    public ADCB(int opcode, String mnemonic, int cycles) {
        super(mnemonic, opcode, cycles);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = 0;
        
        if ((this.opcode & 0xF0) == 0xC0) {
            value = cpu.fetchByte();
        } else if ((this.opcode & 0xF0) == 0xD0) {
            int offset = cpu.fetchByte();
            int address = (cpu.getRegisters().getDP() << 8) | offset;
            value = cpu.getMemory().readByte(address);
        } else if ((this.opcode & 0xF0) == 0xF0) {
            int address = cpu.fetchWord();
            value = cpu.getMemory().readByte(address);
        } else if ((this.opcode & 0xF0) == 0xE0) {
            int postbyte = cpu.fetchByte();
            int address = cpu.getRegisters().getX();
            value = cpu.getMemory().readByte(address);
        }
        
        int b = cpu.getRegisters().getB();
        int carry = cpu.getFlags().getCarry() ? 1 : 0;
        int result = b + value + carry;
        
        cpu.getRegisters().setB(result);
        cpu.getFlags().updateFlagsAdd8(b, value + carry, result);
        
        return this.baseCycles;
    }
}