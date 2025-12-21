package motorola6809.addressing.modes;

import motorola6809.addressing.AddressingMode;
import motorola6809.core.CPU;
import motorola6809.utils.BitOperations;

public class RelativeMode extends AddressingMode {
    
    private boolean longBranch; // true pour 16 bits, false pour 8 bits
    
    public RelativeMode(String operand, boolean longBranch) {
        super(operand);
        this.longBranch = longBranch;
    }
    
    @Override
    public void resolve(CPU cpu) {
        int pc = cpu.getRegisters().getPC();
        int offset;
        
        if (longBranch) {
            // Offset 16 bits (signé)
            offset = cpu.fetchWord();
            if ((offset & 0x8000) != 0) {
                offset |= 0xFFFF0000; // Extension de signe
            }
        } else {
            // Offset 8 bits (signé)
            offset = cpu.fetchByte();
            offset = BitOperations.signExtend8to16(offset);
        }
        
        this.effectiveAddress = (pc + offset) & 0xFFFF;
    }
    
    @Override
    public int getValue(CPU cpu) {
        return effectiveAddress;
    }
    
    @Override
    public void setValue(CPU cpu, int value) {
        throw new UnsupportedOperationException("Cannot write to relative mode");
    }
    
    @Override
    public int getAdditionalCycles() {
        return longBranch ? 1 : 0;
    }
    
    @Override
    public int getOperandSize() {
        return longBranch ? 2 : 1;
    }
}