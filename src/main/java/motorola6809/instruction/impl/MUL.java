package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class MUL extends Instruction {
    
    public MUL() {
        super("MUL", 0x3D, 11);
    }
    
    @Override
    public int execute(CPU cpu) {
        int a = cpu.getRegisters().getA() & 0xFF;
        int b = cpu.getRegisters().getB() & 0xFF;
        int result = a * b;
        
        // Stocker dans D (A:B)
        cpu.getRegisters().setD(result);
        
        // MUL affecte seulement C et Z
        // Zero flag: vrai si D = 0
        cpu.getFlags().setZero((result & 0xFFFF) == 0);
        
        // Carry flag: bit 7 du résultat haute partie (bit 7 de A)
        cpu.getFlags().setCarry((result & 0x80) != 0);
        
        // N et V sont toujours 0 pour MUL
        cpu.getFlags().setNegative(false);
        cpu.getFlags().setOverflow(false);
        
        return baseCycles;
    }
}