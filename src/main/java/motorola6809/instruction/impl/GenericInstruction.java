package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class GenericInstruction extends Instruction {
    
    private String description;
    
    public GenericInstruction(String mnemonic, int opcode, int cycles, String description) {
        super(mnemonic, opcode, cycles);
        this.description = description;
    }
    
    @Override
    public int execute(CPU cpu) {
        // Log pour debug
        System.out.println("Exécution de " + mnemonic + " (" + description + ") - Opcode: 0x" + 
                          Integer.toHexString(opcode));
        
        // Pour les instructions non encore implémentées, retourner les cycles de base
        return baseCycles;
    }
    
    public String getDescription() {
        return description;
    }
}