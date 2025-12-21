package motorola6809.addressing.modes;

import motorola6809.addressing.AddressingMode;
import motorola6809.core.CPU;
import motorola6809.utils.HexConverter;

public class ExtendedMode extends AddressingMode {
    
    public ExtendedMode(String operand) {
        super(operand);
    }
    
    @Override
    public void resolve(CPU cpu) {
        // Retire $ du début si présent
        String hexValue = operand.startsWith("$") ? operand.substring(1) : operand;
        this.effectiveAddress = HexConverter.hexToInt(hexValue);
    }
    
    @Override
    public int getValue(CPU cpu) {
        return cpu.getMemory().readByte(effectiveAddress);
    }
    
    @Override
    public void setValue(CPU cpu, int value) {
        cpu.getMemory().writeByte(effectiveAddress, value);
    }
    
    @Override
    public int getAdditionalCycles() {
        return 2;
    }
    
    @Override
    public int getOperandSize() {
        return 2; // 2 octets pour l'adresse complète
    }
}