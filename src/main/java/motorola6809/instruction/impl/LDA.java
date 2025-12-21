package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class LDA extends Instruction {
    
    public LDA(int opcode, String mnemonic, int cycles) {
        super(mnemonic, opcode, cycles);
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = 0;
        
        // Détermine le mode d'adressage et lit la valeur
        if ((opcode & 0xF0) == 0x80) {
            // Immediate
            value = cpu.fetchByte();
        } else if ((opcode & 0xF0) == 0x90) {
            // Direct
            int offset = cpu.fetchByte();
            int address = (cpu.getRegisters().getDP() << 8) | offset;
            value = cpu.getMemory().readByte(address);
        } else if ((opcode & 0xF0) == 0xB0) {
            // Extended
            int address = cpu.fetchWord();
            value = cpu.getMemory().readByte(address);
        } else if ((opcode & 0xF0) == 0xA0) {
            // Indexed - simplifié
            int postbyte = cpu.fetchByte();
            int address = resolveIndexed(cpu, postbyte);
            value = cpu.getMemory().readByte(address);
        }
        
        // Charge dans A
        cpu.getRegisters().setA(value);
        
        // Met à jour les flags
        cpu.getFlags().updateNZ8(value);
        cpu.getFlags().setOverflow(false);
        
        return baseCycles;
    }
    
    private int resolveIndexed(CPU cpu, int postbyte) {
        // Simplifié - juste ,X pour l'exemple
        return cpu.getRegisters().getX();
    }
}
