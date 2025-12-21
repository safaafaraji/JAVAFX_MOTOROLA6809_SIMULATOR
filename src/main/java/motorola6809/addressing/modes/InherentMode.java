package motorola6809.addressing.modes;

import motorola6809.addressing.AddressingMode;
import motorola6809.core.CPU;

public class InherentMode extends AddressingMode {
    
    public InherentMode() {
        super("");
    }
    
    @Override
    public void resolve(CPU cpu) {
        // Pas d'opérande
    }
    
    @Override
    public int getValue(CPU cpu) {
        throw new UnsupportedOperationException("Inherent mode has no value");
    }
    
    @Override
    public void setValue(CPU cpu, int value) {
        throw new UnsupportedOperationException("Inherent mode has no value");
    }
    
    @Override
    public int getAdditionalCycles() {
        return 0;
    }
    
    @Override
    public int getOperandSize() {
        return 0;
    }
}