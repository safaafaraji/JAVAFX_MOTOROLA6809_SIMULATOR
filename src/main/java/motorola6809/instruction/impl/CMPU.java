package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class CMPU extends Instruction {
    
    public CMPU(int opcode, String mnemonic, int cycles) {
        super(mnemonic, opcode, cycles);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = cpu.fetchWord();
        int u = cpu.getRegisters().getU();
        int result = u - value;
        
        cpu.getFlags().updateFlagsSub16(u, value, result);
        
        return this.baseCycles;
    }
}
