package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.utils.BitOperations;

public class ADDD extends Instruction {
    
    public ADDD(int opcode, String mnemonic, int cycles) {
        super(mnemonic, opcode, cycles);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = 0;
        
        if ((this.opcode & 0xF0) == 0xC0) {
            value = cpu.fetchWord();
        } else if ((this.opcode & 0xF0) == 0xD0) {
            int offset = cpu.fetchByte();
            int address = (cpu.getRegisters().getDP() << 8) | offset;
            value = cpu.getMemory().readWord(address);
        } else if ((this.opcode & 0xF0) == 0xF0) {
            int address = cpu.fetchWord();
            value = cpu.getMemory().readWord(address);
        } else if ((this.opcode & 0xF0) == 0xE0) {
            int postbyte = cpu.fetchByte();
            int address = cpu.getRegisters().getX();
            value = cpu.getMemory().readWord(address);
        }
        
        int d = cpu.getRegisters().getD();
        int result = d + value;
        
        cpu.getRegisters().setD(result);
        cpu.getFlags().updateFlagsAdd16(d, value, result);
        
        return this.baseCycles;
    }
}