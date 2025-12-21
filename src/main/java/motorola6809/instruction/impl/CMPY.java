package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class CMPY extends Instruction {
    
    public CMPY(int opcode, String mnemonic, int cycles) {
        super(mnemonic, opcode, cycles);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = cpu.fetchWord();
        int y = cpu.getRegisters().getY();
        int result = y - value;
        
        cpu.getFlags().updateFlagsSub16(y, value, result);
        
        return this.baseCycles;
    }
}
