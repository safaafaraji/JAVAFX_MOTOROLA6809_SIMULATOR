package motorola6809.addressing.modes;

import motorola6809.addressing.AddressingMode;
import motorola6809.core.CPU;
import motorola6809.utils.HexConverter;

public class ImmediateMode extends AddressingMode {
    
    private int size; // 1 pour 8 bits, 2 pour 16 bits
    
    public ImmediateMode(String operand, int size) {
        super(operand);
        this.size = size;
        this.isValue = true;
    }
    
    @Override
    public void resolve(CPU cpu) {
        // Retire #$ ou # du début
        String hexValue = operand;
        if (hexValue.startsWith("#$")) {
            hexValue = hexValue.substring(2);
        } else if (hexValue.startsWith("#")) {
            hexValue = hexValue.substring(1);
        }
        
        this.value = HexConverter.hexToInt(hexValue);
    }
    
    @Override
    public int getValue(CPU cpu) {
        return value;
    }
    
    @Override
    public void setValue(CPU cpu, int value) {
        throw new UnsupportedOperationException("Cannot write to immediate mode");
    }
    
    @Override
    public int getAdditionalCycles() {
        return 0; // Mode immédiat est le plus rapide
    }
    
    @Override
    public int getOperandSize() {
        return size;
    }
}