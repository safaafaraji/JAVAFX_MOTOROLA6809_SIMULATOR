package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class COMA extends Instruction {
    
    public COMA() {
        super("COMA", 0x43, 2);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = cpu.getRegisters().getA();
        int result = (~value) & 0xFF;
        
        cpu.getRegisters().setA(result);
        
        cpu.getFlags().updateNZ8(result);
        cpu.getFlags().setOverflow(false);
        cpu.getFlags().setCarry(true); // COM met toujours C à 1
        
        return baseCycles;
    }
}