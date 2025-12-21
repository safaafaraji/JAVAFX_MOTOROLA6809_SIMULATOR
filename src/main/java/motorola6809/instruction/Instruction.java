package motorola6809.instruction;

import motorola6809.core.CPU;
import motorola6809.addressing.AddressingMode;

public abstract class Instruction {
    
    protected String mnemonic;
    protected int opcode;
    protected AddressingMode addressingMode;
    protected int baseCycles;
    
    public Instruction(String mnemonic, int opcode, int baseCycles) {
        this.mnemonic = mnemonic;
        this.opcode = opcode;
        this.baseCycles = baseCycles;
    }
    
    public abstract int execute(CPU cpu);
    
    public void setAddressingMode(AddressingMode mode) {
        this.addressingMode = mode;
    }
    
    public String getMnemonic() {
        return mnemonic;
    }
    
    public int getOpcode() {
        return opcode;
    }
    
    public int getBaseCycles() {
        return baseCycles;
    }
    
    @Override
    public String toString() {
        return String.format("%s (%02X)", mnemonic, opcode);
    }
}