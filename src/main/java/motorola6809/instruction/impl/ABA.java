// ABA.java
package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class ABA extends Instruction {
    
    public ABA() {
        super("ABA", 0x1B, 2);
    }
    
    @Override
    public int execute(CPU cpu) {
        int a = cpu.getRegisters().getA() & 0xFF;
        int b = cpu.getRegisters().getB() & 0xFF;
        int result = a + b;
        
        cpu.getRegisters().setA(result & 0xFF);
        
        // Mettre à jour les flags
        cpu.getFlags().updateFlagsAdd8(a, b, result);
        
        return baseCycles;
    }
}