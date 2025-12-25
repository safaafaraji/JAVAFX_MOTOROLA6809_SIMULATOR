package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;
import motorola6809.addressing.AddressingMode;
import motorola6809.addressing.AddressingModeResolver;

public class LDA extends Instruction {
    
    private String operand;
    
    public LDA(int opcode, String mnemonic, int cycles) {
        super(mnemonic, opcode, cycles);
    }
    
    public LDA(int opcode, String mnemonic, int cycles, String operand) {
        super(mnemonic, opcode, cycles);
        this.operand = operand;
    }
    
    @Override
    public int execute(CPU cpu) {
        int value = 0;
        
        // Utiliser le mode d'adressage résolu
        if (operand != null) {
            AddressingMode mode = AddressingModeResolver.resolve(operand, 0);
            mode.resolve(cpu);
            value = mode.getValue(cpu);
        } else {
            // Basé sur l'opcode
            switch (opcode) {
                case 0x86: // LDA #imm
                    value = cpu.fetchByte();
                    break;
                case 0x96: // LDA dir
                    int offset = cpu.fetchByte();
                    int address = (cpu.getRegisters().getDP() << 8) | offset;
                    value = cpu.getMemory().readByte(address);
                    break;
                case 0xB6: // LDA ext
                    value = cpu.getMemory().readByte(cpu.fetchWord());
                    break;
                case 0xA6: // LDA idx
                    int postbyte = cpu.fetchByte();
                    value = cpu.getMemory().readByte(resolveIndexed(cpu, postbyte));
                    break;
            }
        }
        
        cpu.getRegisters().setA(value & 0xFF);
        cpu.getFlags().updateNZ8(value);
        cpu.getFlags().setOverflow(false);
        
        return baseCycles;
    }
    
    private int resolveIndexed(CPU cpu, int postbyte) {
        // TODO: Implémenter correctement le mode indexé
        int x = cpu.getRegisters().getX();
        
        // Mode simple: ,X
        if ((postbyte & 0x9F) == 0x80) { // ,X
            return x;
        }
        
        // Mode simple: ,X+
        if ((postbyte & 0x9F) == 0x80) { // ,X+
            cpu.getRegisters().setX(x + 1);
            return x;
        }
        
        return x; // Par défaut
    }
}