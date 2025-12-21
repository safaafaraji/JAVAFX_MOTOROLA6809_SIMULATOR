package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class JSR extends Instruction {
    
    public JSR(int opcode, String mnemonic, int cycles) {
        super(mnemonic, opcode, cycles);
    }
    
    @Override
    public int execute(CPU cpu) {
        int address = 0;
        
        if (opcode == 0x9D) {
            // Direct
            int offset = cpu.fetchByte();
            address = (cpu.getRegisters().getDP() << 8) | offset;
        } else if (opcode == 0xBD) {
            // Extended
            address = cpu.fetchWord();
        } else if (opcode == 0xAD) {
            // Indexed
            int postbyte = cpu.fetchByte();
            address = resolveIndexed(cpu, postbyte);
        }
        
        // Empile l'adresse de retour
        cpu.pushWordS(cpu.getRegisters().getPC());
        
        // Saute à l'adresse
        cpu.getRegisters().setPC(address);
        
        return baseCycles;
    }
    
    private int resolveIndexed(CPU cpu, int postbyte) {
        return cpu.getRegisters().getX();
    }
}