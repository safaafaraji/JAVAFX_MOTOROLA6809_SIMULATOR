package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class CMPS extends Instruction {
    
    public CMPS(int opcode, String mnemonic, int cycles) {
        super(mnemonic, opcode, cycles);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = cpu.fetchWord();
        int s = cpu.getRegisters().getS();
        int result = s - value;
        
        cpu.getFlags().updateFlagsSub16(s, value, result);
        
        return this.baseCycles;
    }
}
