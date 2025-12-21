package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;


public class DAA extends Instruction {
    
    public DAA() {
        super("DAA", 0x19, 2);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = cpu.getRegisters().getA();
        int correction = 0;
        
        // Correction du nibble bas
        if ((value & 0x0F) > 9 || cpu.getFlags().getHalfCarry()) {
            correction += 0x06;
        }
        
        // Correction du nibble haut
        if (((value & 0xF0) >> 4) > 9 || cpu.getFlags().getCarry() || 
            ((value & 0xF0) > 0x80 && (value & 0x0F) > 9)) {
            correction += 0x60;
        }
        
        int result = (value + correction) & 0xFF;
        cpu.getRegisters().setA(result);
        
        cpu.getFlags().updateNZ8(result);
        if (correction >= 0x60) {
            cpu.getFlags().setCarry(true);
        }
        
        return baseCycles;
    }
}
