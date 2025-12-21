package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.config.Constants;


public class SBCA extends Instruction {
    
    public SBCA(int opcode, String mnemonic, int cycles) {
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
        
        int a = cpu.getRegisters().getA();
        int carry = cpu.getFlags().getCarry() ? 1 : 0;
        int result = a - value - carry;
        
        cpu.getRegisters().setA(result);
        cpu.getFlags().updateFlagsSub8(a, value + carry, result);
        
        return this.baseCycles;
    }
}
