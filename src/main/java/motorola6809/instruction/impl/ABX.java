package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.utils.BitOperations;

public class ABX extends Instruction {
    
    public ABX() {
        super("ABX", 0x3A, 3);
    }
    
    @Override
    public int execute(CPU cpu) {
        int b = cpu.getRegisters().getB();
        int x = cpu.getRegisters().getX();
        int result = (x + b) & 0xFFFF;
        
        cpu.getRegisters().setX(result);
        
        return this.baseCycles;
    }
}