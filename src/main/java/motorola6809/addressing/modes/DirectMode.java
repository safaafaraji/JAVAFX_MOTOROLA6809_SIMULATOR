package motorola6809.addressing.modes;

import motorola6809.addressing.AddressingMode;
import motorola6809.core.CPU;
import motorola6809.utils.HexConverter;
import motorola6809.utils.BitOperations;

public class DirectMode extends AddressingMode {
    
    public DirectMode(String operand) {
        super(operand);
    }
    
    @Override
    public void resolve(CPU cpu) {
        // Retire $ du début si présent
        String hexValue = operand.startsWith("$") ? operand.substring(1) : operand;
        
        int offset = HexConverter.hexToInt(hexValue);
        int dp = cpu.getRegisters().getDP();
        
        // Adresse = DP:offset
        this.effectiveAddress = BitOperations.makeWord(dp, offset);
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
        return 1;
    }
    
    @Override
    public int getOperandSize() {
        return 1; // 1 octet pour l'offset
    }
}