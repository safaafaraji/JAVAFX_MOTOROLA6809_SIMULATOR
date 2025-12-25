package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.utils.BitOperations;

public class BNE extends Instruction {
    
    private String operand; // Pour le mode relatif
    
    public BNE() {
        super("BNE", 0x26, 3); // BNE relatif 8-bit
    }
    
    public BNE(String operand) {
        super("BNE", 0x26, 3);
        this.operand = operand;
    }
    
    @Override
    public int execute(CPU cpu) {
        if (!cpu.getFlags().getZero()) {
            // Z=0, donc branchement
            int offset = getOffset(cpu);
            int pc = cpu.getRegisters().getPC();
            cpu.getRegisters().setPC(pc + offset);
            return baseCycles + 1; // +1 cycle si le branchement est pris
        } else {
            // Z=1, pas de branchement
            skipOffset(cpu); // Avancer PC pour sauter l'offset
            return baseCycles;
        }
    }
    
    private int getOffset(CPU cpu) {
        if (operand != null) {
            // Mode étendu - calculer l'offset depuis l'étiquette
            // À implémenter avec le symbol table
            return 0;
        } else {
            // Mode relatif - lire l'offset depuis le flux d'instructions
            int offset = cpu.fetchByte();
            return BitOperations.signExtend8to16(offset);
        }
    }
    
    private void skipOffset(CPU cpu) {
        // Avancer PC pour sauter l'octet d'offset
        cpu.fetchByte();
    }
}