package motorola6809.instruction.impl;

import motorola6809.instruction.Instruction;
import motorola6809.core.CPU;

public class CWAI extends Instruction {
    
    public CWAI() {
        super("CWAI", 0x3C, 20);
    }
    
    @Override
    public int execute(CPU cpu) {
        int mask = cpu.fetchByte();
        
        // Met E à 1 (entire state will be saved)
        cpu.getFlags().setEntire(true);
        
        // Empile tout l'état du processeur
        cpu.pushWordS(cpu.getRegisters().getPC());
        cpu.pushWordS(cpu.getRegisters().getU());
        cpu.pushWordS(cpu.getRegisters().getY());
        cpu.pushWordS(cpu.getRegisters().getX());
        cpu.pushS(cpu.getRegisters().getDP());
        cpu.pushS(cpu.getRegisters().getB());
        cpu.pushS(cpu.getRegisters().getA());
        cpu.pushS(cpu.getFlags().getCC());
        
        // Applique le masque aux flags
        int cc = cpu.getFlags().getCC() & mask;
        cpu.getFlags().setCC(cc);
        
        // Le CPU attend une interruption (simulation simplifiée)
        // Dans une vraie implémentation, on entrerait en mode wait
        
        return baseCycles;
    }
}